package processManaging;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import Transactional_IO.TransactionalFileInputStream;
import Transactional_IO.TransactionalFileOutputStream;
import processMigration.MigratableProcess;

public class SlaveReadMaster extends SocketMessage {

	BufferedReader in;
	PrintStream out;
	ExecutorService executor = Executors.newCachedThreadPool();
	Map<String, ProcessInfo> hashOfProcesses;
	
	SlaveReadMaster(BufferedReader in, PrintStream out, Map<String,ProcessInfo> hashOfProcesses) {
		this.in = in;
		this.out = out;
		this.hashOfProcesses = hashOfProcesses;
	}
	
	public void run(){
		/*
		 * messages to read:
		 * "STARTProcess <processName> <<processArgs>>" | "STARTED <processNameKey>
		 * "SUSPENDProcess <processName> <<processArgs>>" | "SUSPENDED <filename> <processName> <<processArgs>>"
		 * "RESUMEProcess <filename> <processName> <<processArgs>>"
		 * "RECEIVEDNewProcess <processName> <<processArgs>>"
		 */
		
		String inputLine;
		String filePath = null;
		
		while (true) {
			try {
				synchronized(in){
					while(!((inputLine = in.readLine()).equals(messageTerminator))) {
						//System.out.println("IN SLAVE: LISTENING TO MESSAGES FROM MASTER");
						//System.out.println("IN SLAVE: GOT THE MESSAGE: " + inputLine);
						String[] input = inputLine.split(" ", 2);
						if (input[0].equals(startProcess)) {
							try {
								out.println(sendMessage(started + " " + start(input[1])));
							} catch (IllegalArgumentException e) {
								System.err.println("Bad arguemnts for that process");
							} catch (ClassNotFoundException e) {
								System.err.println("No Process by that name");
							} catch (InstantiationException e) {
								System.err.println("Could not initialize the class");
							} catch (IllegalAccessException e) {
								System.err.println("Did not have access to that class");
							} catch (InvocationTargetException e) {
								System.err.println("Could not invoke the target");
							}
						} else if (input[0].equals(suspendProcess)) {
							filePath = suspend(input[1]);
							synchronized(out) {
								out.println(sendMessage(suspended + " " + filePath + " " + input[1]));
							}
						} else if (input[0].equals(resumeProcess)) {
							
						} else if (input[0].equals(receivedProcess)) {
							//System.out.println("IN CLIENT: The master received a process from me");
						}
					}
				}
			} catch (IOException e) {
				System.out.println("Could not read from buffer");
				e.printStackTrace();
			}
		}
	}
	
	private String getRandomString(int len){
		StringBuffer sb = new StringBuffer();  
	    for (int x = 0; x <len; x++)  
	    {  
	      sb.append((char)((int)(Math.random()*26)+97));  
	    } 
	    return sb.toString();
	}
	/*
	 * input: str = <processName> <<processArgs>>
	 * do: put into hash (key for process, input string)
	 * return: key for the process
	 */
	public String start(String str) 
			throws ClassNotFoundException, IllegalArgumentException, 
			InstantiationException, IllegalAccessException, InvocationTargetException {		
		String [] p=str.split(" ", 2);
		Class<?> t = Class.forName(p[0]);
		String [] pArgs = p[1].split(" ");

		Constructor<?>[] listOfConstructors = t.getConstructors();
		int correctConstructor=0;
		for(int i =0;i<listOfConstructors.length;i++) {
			if (listOfConstructors[i].getGenericParameterTypes().length == 1) {
				correctConstructor = i;
			}
		}
		
		Object arg = pArgs;//new String[0];
		MigratableProcess mp = (MigratableProcess) listOfConstructors[correctConstructor].newInstance(arg);
		Future<?> future = executor.submit(mp);
		ProcessInfo pi= new ProcessInfo(future,mp, p[0], p[1]);
		
		String key = getRandomString(30);
		hashOfProcesses.put(key, pi);
		return key;
		
	}
	public String suspend(String str) throws IOException {
		//Error message if the master sends a process to suspend that does not exists
		if(!hashOfProcesses.containsKey(str)) {
			return "No Process in the list";
		}
		ProcessInfo process = hashOfProcesses.get(str);
		//ProcessInfo p = processes.get(0);
		//processes.remove(p);
		hashOfProcesses.remove(str);
		//hashOfProcesses.put(str,processes);
		MigratableProcess mp=process.getProcess();
		mp.suspend();
		Future<?> f = process.getFuture();
		boolean b=false;
		f.cancel(b);
		String currentDir = System.getProperty("user.dir");
		String name=process.getProcessName();
		TransactionalFileOutputStream fos = new TransactionalFileOutputStream(name); 
		ObjectOutputStream oos = new ObjectOutputStream(fos); 
		oos.writeObject(mp); 
		oos.flush(); 
		oos.close(); 
		
		return currentDir+"/"+name;
	}
	public String resume(String procAndArgs, String filename) throws IOException, ClassNotFoundException {
		TransactionalFileInputStream fis = new TransactionalFileInputStream(filename); 
		ObjectInputStream ois = new ObjectInputStream(fis); 
		MigratableProcess mp = (MigratableProcess)ois.readObject(); 
		ois.close(); 
		Future<?> future = executor.submit(mp);
		String[] in = procAndArgs.split(" ", 2);
		ProcessInfo pi= new ProcessInfo(future,mp, in[0], in[1]);
		/*List<ProcessInfo> processes = Collections.synchronizedList(new ArrayList<ProcessInfo>());
		
		if(hashOfProcesses.containsKey(procAndArgs)) {
			processes=hashOfProcesses.get(procAndArgs);
		}
		processes.add(pi);
		hashOfProcesses.put(procAndArgs, processes);
		*/
		String key=getRandomString(30);
		hashOfProcesses.put(key, pi);
		return "Resumed "+ procAndArgs;
	}

	

}
