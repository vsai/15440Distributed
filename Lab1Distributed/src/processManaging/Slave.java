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
	//Map<MigratableProcess, String[]> processes;
	//PrintWriter out;
	PrintStream out;
	BufferedReader in;
	ArrayList<String> heartbeatLastDeadProcesses;
	ArrayList<String> psLastDeadProcesses;
	Map<String, List<ProcessInfo>> hashOfProcesses;
	//Map<String, ProcessInfo> hashOfProcesses;
	
	Thread heartbeat;
	
	public Slave(String hostname, final int hostPortnum, String selfIp) {
		this.hostname = hostname;
		this.hostPortnum = hostPortnum;
		this.selfIp = selfIp;
		this.hashOfProcesses = Collections.synchronizedMap(new HashMap<String, List<ProcessInfo>>());
		this.hashOfProcesses = new ConcurrentHashMap<String, List<ProcessInfo>>();
		this.out = null; //write to the master
		this.in = null; //read from master
		this.heartbeatLastDeadProcesses = new ArrayList<String>();
		this.psLastDeadProcesses = new ArrayList<String>();
		
		this.heartbeat = new Thread("Heartbeat") {
			Long sleepTime = (long) 5000;
			public void run() {
				while (true) {
					String toSend = alive;
					toSend += " " + getSlaveWorkload();
					
					/*
					 * find everything that has died since last check and add it to
					 * heartbeatLastDeadProcesses
					 * psLastDeadProcesses
					 */
					for (String processName : hashOfProcesses.keySet()){
						List<ProcessInfo> a = Collections.synchronizedList(hashOfProcesses.get(processName));
						synchronized(a) {
							for (ProcessInfo pInfo : a){
								if (pInfo.getFuture().isDone()){
									a.remove(pInfo);
									putHeartbeatLastDeadProcesses(processName);
									putPSLastDeadProcesses(processName);
								}
							}
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
					heartbeatLastDeadProcesses = new ArrayList<String>();
				}
			}
		};
	}
	
	public int getSlaveWorkload(){
		return 0;
	}
	
	public ArrayList<String> getHeartbeatLastDeadProcesses() {
		return heartbeatLastDeadProcesses;
	}
	public synchronized boolean putHeartbeatLastDeadProcesses(String deadProcess){
		return heartbeatLastDeadProcesses.add(deadProcess);
	}
	
	public ArrayList<String> getPSLastDeadProcesses() {
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
    		System.out.println("Prompt for input ==>");
    		String input = sc.nextLine(); //only 1-line inputs from user terminal
    		input = cleanUserInput(input);
    		if (input.equals("ps")) {
    			System.out.println("Shoould do ps now");
    			for (String processName : hashOfProcesses.keySet()){
    				System.out.println(processName);
    				List<ProcessInfo> a = hashOfProcesses.get(processName);
    				System.out.println(a);
    				System.out.println(a.isEmpty());
    				synchronized(a) {
    					System.out.println("In Slave Synchronized a");
    					for (int i=0; i<a.size(); i++) {
        					System.out.println("Currently running: " + processName);
        				}
    				}
				}
    			for (String termProcess : getHeartbeatLastDeadProcesses()){
    				System.out.println("Terminated: " + termProcess);
    			}
    			/*
    			for (MigratableProcess m : processes.keySet()) {
    				String className = m.getClass().getSimpleName();
    				String pArgs[] = processes.get(m);
    				StringBuilder builder = new StringBuilder();
    				for(String s : pArgs) {
    				    builder.append(s + " ");
    				}
    				String pArg = builder.toString();
    				pArg = pArg.substring(0, pArg.length() - 1);;
    				System.out.println(className + " " + pArg);
    			}
    			*/
    			
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
    			//out.println(sendMessage(startProcess + " " + input));
    			out.println(sendMessage(input));
    			System.out.println("In Slave: SENT Message to Master");
    			//synchronized(out) {    				
    			//	out.println(sendMessage(input));
    			//}
    		} 
        }        
    }	
}
