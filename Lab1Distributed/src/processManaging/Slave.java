package processManaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import processMigration.MigratableProcess;

/*
 * USE JOINS AROUND THE SUSPEND STUFF
 */

public class Slave extends SocketMessage{

	Socket socketToMaster;
	String hostname;
	final int hostPortnum;
	String selfIp;
	PrintStream out;
	BufferedReader in;
	List<String> heartbeatLastDeadProcesses;
	List<String> psLastDeadProcesses;
	ConcurrentHashMap<String, ProcessInfo> hashOfProcesses;
	
	Thread heartbeat;
	
	public Slave(String hostname, final int hostPortnum, String selfIp) {
		this.hostname = hostname;
		this.hostPortnum = hostPortnum;
		this.selfIp = selfIp;
		this.hashOfProcesses = new ConcurrentHashMap<String, ProcessInfo>(); //filePath -> processInfo (future, obj, pname, pargs)
		
		this.out = null; //write to the master
		this.in = null; //read from master
		
		this.heartbeatLastDeadProcesses = Collections.synchronizedList(new ArrayList<String>());
		this.psLastDeadProcesses = Collections.synchronizedList(new ArrayList<String>());
		
		this.heartbeat = new Thread("Heartbeat") {
			Long sleepTime = (long) 5000;
			public void run() {
				while (true) {
					String toSend = alive;
					
					/*
					 * find everything that has died since last check and add it to
					 * heartbeatLastDeadProcesses
					 * psLastDeadProcesses
					 */
					ProcessInfo pInfo;
					for (String processName : hashOfProcesses.keySet()){
						pInfo=hashOfProcesses.get(processName);
						if (pInfo.getFuture().isDone()){
								hashOfProcesses.remove(processName);
								putHeartbeatLastDeadProcesses(pInfo.getFilePath());
								//putHeartbeatLastDeadProcesses(pInfo.getProcessName()+" "+pInfo.getProcessArgs());
								putPSLastDeadProcesses(pInfo.getProcessName()+" "+pInfo.getProcessArgs());
							}
					}
					
						
					StringBuilder builder1 = new StringBuilder();
					for (String deadProcess : getHeartbeatLastDeadProcesses()) {
						builder1.append(deadProcess + "\n");
					}
					String dProcesses = builder1.toString();
					if (dProcesses.length() > 0) {
						dProcesses = dProcesses.substring(0, dProcesses.length() -1);
						toSend += "\n" + dProcesses;
					}
					
					synchronized(out){
						out.println(sendMessage(toSend));
					}
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						System.err.println("Heartbeat sleeptime interrupted");
						e.printStackTrace();
					}
					heartbeatLastDeadProcesses = Collections.synchronizedList(new ArrayList<String>());
				}
			}
		};
	}
	
	public List<String> getHeartbeatLastDeadProcesses() {
		return heartbeatLastDeadProcesses;
	}
	public synchronized boolean putHeartbeatLastDeadProcesses(String deadProcess){
		return heartbeatLastDeadProcesses.add(deadProcess);
	}
	
	public List<String> getPSLastDeadProcesses() {
		return psLastDeadProcesses;
	}
	
	public synchronized boolean putPSLastDeadProcesses(String deadProcess){
		return psLastDeadProcesses.add(deadProcess);
	}
	
	public String cleanUserInput(String str){
		//trim trailing space characters from str
		return str;
	}
	public void run() {
        try {
        	socketToMaster = new Socket(hostname, hostPortnum);
            out = new PrintStream(socketToMaster.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socketToMaster.getInputStream()));
            System.out.println("In Slave: Connected to master");
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostname);
            System.exit(1);
        }
        
        heartbeat.start();
        SlaveReadMaster readHandler = new SlaveReadMaster(in, out, hashOfProcesses);
        readHandler.start();
        
        Scanner sc = new Scanner(System.in);
        while(true){
    		System.out.print("Prompt for input ==>");
    		String input = sc.nextLine(); //only 1-line inputs from user terminal
    		input = cleanUserInput(input);
    		if (input.equals("ps")) {
    			System.out.println("Currently Running:");
    			for (String processName : hashOfProcesses.keySet()){
    				ProcessInfo a = hashOfProcesses.get(processName);
    				System.out.println(a.getProcessName() + " " + 	a.getProcessArgs());
				}
    			for (String termProcess : getPSLastDeadProcesses()){
    				System.out.println("Terminated: " + termProcess);
    			}
    			psLastDeadProcesses = Collections.synchronizedList(new ArrayList<String>());
    			
    		} else if (input.equals(quit)){
    			try {
    				synchronized(out){
    					out.println(sendMessage(input));
    					out.close();
    				}
    				synchronized(in){
    					in.close();
    				}
	    			socketToMaster.close();
				} catch (IOException e) {
					System.out.println("Failed to close buffers/socket before exiting");
					e.printStackTrace();
				}
    			System.exit(0);
    			
    		} else { //Process input with commands - only for master
    			System.out.println("In Slave: In new process input terminal");
    			synchronized(out) {
    				out.println(sendMessage(startProcess + " " + input));
    			}
    		} 
        }        
    }	
}
