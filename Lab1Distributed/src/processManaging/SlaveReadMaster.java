package processManaging;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import processMigration.MigratableProcess;

public class SlaveReadMaster extends SocketMessage {

	BufferedReader in;
	//PrintWriter out;
	PrintStream out;
	static ExecutorService executor = Executors.newCachedThreadPool();
	static Map<String,ArrayList<ProcessInfo>> hashOfProcesses;
	
	SlaveReadMaster(BufferedReader in, PrintStream out, Map<String,ArrayList<ProcessInfo>> hashOfProcesses) {
		this.in = in;
		this.out = out;
		this.hashOfProcesses = hashOfProcesses;
		
	}
	
	public void run(){
		/*
		 * messages to read:
		 * "STARTProcess <processName> <<processArgs>>"
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
						System.out.println("IN SLAVE: LISTENING TO MESSAGES FROM MASTER");
						System.out.println("IN SLAVE: GOT THE MESSAGE: " + inputLine);
						String[] input = inputLine.split(" ", 2);
						System.out.println("YOLOSWAG");
						if (input[0].equals(startProcess)) {
							try {
								start(input[1]);
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						} else if (input[0].equals(suspendProcess)) {
							filePath = suspend(input[1]);
							synchronized(out) {
								out.println(sendMessage(suspended + filePath + " " + input[1]));
							}
						} else if (input[0].equals(resumeProcess)) {
							
						} else if (input[0].equals(receivedProcess)) {
							//coolstoryBro
							System.out.println("IN CLIENT: The master received a process from me");
						}
					}
				}
			} catch (IOException e) {
				System.out.println("Could not read from buffer");
				e.printStackTrace();
			}
			System.out.println("DID AN ITERATION");
		}
	}
	
	public String start(String str) 
			throws ClassNotFoundException, IllegalArgumentException, 
			InstantiationException, IllegalAccessException, InvocationTargetException
	{
		System.out.println("In Start func");
		String [] p=str.split(" ");
		Class<?> t = Class.forName(p[0]);
		String [] pArgs = new String [p.length-1];
		for(int i=1;i<p.length;i++) {
			pArgs[i-1]=p[i];
		}
		Constructor<?>[] listOfConstructors = t.getConstructors();
		int correctConstructor=0;
		for(int i =0;i<listOfConstructors.length;i++)
		{
			if(listOfConstructors[i].getParameterTypes().length==pArgs.length)
				correctConstructor=i;
		}
		MigratableProcess mp=(MigratableProcess) listOfConstructors[correctConstructor].newInstance(pArgs);
		Future<?> future = executor.submit(mp);
		System.out.println("added proccess to exectuor and running!");
		ProcessInfo pi= new ProcessInfo(future,mp);
		ArrayList<ProcessInfo> processes = new ArrayList<ProcessInfo>();
		
		if(hashOfProcesses.containsKey(str)) {
			processes=hashOfProcesses.get(str);
		}

		processes.add(pi);
		hashOfProcesses.put(str, processes);
		System.out.println(hashOfProcesses.size());
		System.out.println("Updated hash table");
		return str;
		
	}
	public String suspend(String str) throws IOException {
		//Error message if the master sends a process to suspend that does not exists
		if(!hashOfProcesses.containsKey(str)) {
			return "No Process in the list";
		}
		ArrayList<ProcessInfo> processes = hashOfProcesses.get(str);
		ProcessInfo p = processes.get(0);
		processes.remove(p);
		hashOfProcesses.put(str,processes);
		MigratableProcess mp=p.getProcess();
		mp.suspend();
		Future<?> f = p.getFuture();
		boolean b=false;
		f.cancel(b);
		String currentDir = System.getProperty("user.dir");
		String name=str.split(" ")[0];
		FileOutputStream fos = new FileOutputStream(name); 
		ObjectOutputStream oos = new ObjectOutputStream(fos); 
		oos.writeObject(mp); 
		oos.flush(); 
		oos.close(); 
		
		return currentDir+"/"+name;
	}
	public String resume(String procAndArgs, String filename) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(filename); 
		ObjectInputStream ois = new ObjectInputStream(fis); 
		MigratableProcess mp = (MigratableProcess)ois.readObject(); 
		ois.close(); 
		Future<?> future = executor.submit(mp);
		
		ProcessInfo pi= new ProcessInfo(future,mp);
		ArrayList<ProcessInfo> processes = new ArrayList<ProcessInfo>();
		
		if(hashOfProcesses.containsKey(procAndArgs)) {
			processes=hashOfProcesses.get(procAndArgs);
		}
		processes.add(pi);
		hashOfProcesses.put(procAndArgs, processes);
		return "Resumed "+ procAndArgs;
	}

	

}
