package processManaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
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
	Map<String, ProcessInfo> hashOfProcesses; //FilePath -> ProcessInfo
	
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
//		String filePath = null;
		
		while (true) {
			try {
				synchronized(in){
					while(!((inputLine = in.readLine()).equals(messageTerminator))) {
						System.out.println("SLAVE input:"+inputLine);
						String[] input = inputLine.split(" ", 2);
						if (input[0].equals(resumeProcess)){
							String [] processInfo = input[1].split(" ", 2);
							System.out.println("IN SLAVEREADMASTER: processInfo[0]: " + processInfo[0]);
							System.out.println("IN SLAVEREADMASTER: processInfo[1]: " + processInfo[1]);
							try {
								resume(processInfo[0], processInfo[1]);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							
						} else if (input[0].equals(suspendProcess)){
							
						} else if (input[0].equals(suspendALL)){
							System.out.println("SUSPENDING ALL");
							suspendAll();
						}
					}
				}
			} catch (IOException e) {
				System.out.println("Could not read from buffer");
				e.printStackTrace();
			}
		}
	}
	
	public void suspendAll() throws IOException{
		System.out.println("RUNNING SUSPEND ALL METHOD");
		for (String fileName : hashOfProcesses.keySet()){
			suspend(fileName);
			hashOfProcesses.remove(fileName);
		}
	}
	
	/*
	 * Input: key (filepath)
	 */
	public void suspend(String fileName) throws IOException {
		System.out.println("SUSPENDING IN SUPEND: " + fileName);
		for (String fname : hashOfProcesses.keySet()){
			System.out.println("HAVE " + fname);
		}
		if(!hashOfProcesses.containsKey(fileName)) {
			System.out.println("NOT GOING TO DELETE IN SUSPEND");			
			return;
		}	
		System.out.println("Hash is contained in hashOfProcesses");
		
		ProcessInfo process = hashOfProcesses.get(fileName);
		MigratableProcess mp = process.getProcess();
		System.out.println("SUCCESSFULLY REMOVED SLAVE READ MASTER: " + hashOfProcesses.remove(fileName));
		mp.suspend();
		System.out.println("SLAVE READ MASTER: JUST SUSPENDED");
		boolean b = true;
		process.getFuture().cancel(b);
		System.out.println("Did it cancel?" +process.getFuture().isCancelled());
		
		//String currentDir = System.getProperty("user.dir");
//		String filePath = process.getFilePath();
		System.out.println("About to serialize");
		TransactionalFileOutputStream fos = new TransactionalFileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fos); 
		oos.writeObject(mp); 
		oos.flush();
		oos.close(); 
		System.out.println("SUCCESSFULLY SUSPENDED");
	}

	public void resume(String filename, String procAndArgs) throws IOException, ClassNotFoundException {
		System.out.println("IN SLAVEREADMASTER: IN RESUME");
		System.out.println("filename: " + filename);
		System.out.println("procAndArgs: " + procAndArgs);
		System.out.println("I am resuming: " + filename);
		TransactionalFileInputStream fis = new TransactionalFileInputStream(filename); 
		ObjectInputStream ois = new ObjectInputStream(fis); 
		MigratableProcess mp = (MigratableProcess)ois.readObject(); 
		ois.close(); 
		Future<?> future = executor.submit(mp);
		String[] in = procAndArgs.split(" ", 2);
		ProcessInfo pi= new ProcessInfo(future,mp, in[0], in[1],filename);
		hashOfProcesses.put(filename, pi);
	}
}
