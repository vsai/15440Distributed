package processManaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import processMigration.MigratableProcess;

/*
 * USE JOINS AROUND THE SUSPEND STUFF
 */

public class Slave extends SocketMessage{

	Socket socketToMaster;
	String hostname;
	final int hostPortnum;
	String selfIp;
	Map<MigratableProcess, String[]> processes;
	PrintWriter out;
	BufferedReader in;
	ArrayList<String> lastDeadProcesses;
	
	Thread heartbeat;
	
	public Slave(String hostname, final int hostPortnum, String selfIp) {
		this.hostname = hostname;
		this.hostPortnum = hostPortnum;
		this.selfIp = selfIp;
		this.processes = Collections.synchronizedMap(new HashMap<MigratableProcess, String[]>());
		this.out = null; //write to the master
		this.in = null; //read from master
		this.lastDeadProcesses = new ArrayList<String>();
		
		this.heartbeat = new Thread("Heartbeat") {
			Long sleepTime = (long) 5000;
			public void run() {
				while (true) {
					String toSend = alive;
					toSend += " " + getSlaveWorkload() + "\n";
					StringBuilder builder1 = new StringBuilder();
					for (String deadProcess : getLastDeadProcesses()) {
						builder1.append(deadProcess + "\n");
					}
					String dProcesses = builder1.toString();
					dProcesses = dProcesses.substring(0, dProcesses.length() -1);
					toSend += dProcesses;
					synchronized(out){
						out.println(sendMessage(toSend));
					}
					try {
						this.sleep(sleepTime);
					} catch (InterruptedException e) {
						System.err.println("Heartbeat sleeptime interrupted");
						e.printStackTrace();
					}
					lastDeadProcesses = new ArrayList<String>();
				}
			}
		};
	}
	
	public int getSlaveWorkload(){
		return 0;
	}
	
	public ArrayList<String> getLastDeadProcesses() {
		return lastDeadProcesses;
	}
	
	public String cleanUserInput(String str){
		//trim trailing space characters from str
		return str;
	}
	public void run() {
        try {
        	socketToMaster = new Socket(hostname, hostPortnum);
            out = new PrintWriter(socketToMaster.getOutputStream(), true);
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
        SlaveReadMaster readHandler = new SlaveReadMaster(in, out);
        readHandler.start();
        
        Scanner sc = new Scanner(System.in);
        while(true){
    		System.out.println("Prompt for input ==>");
    		String input = sc.nextLine(); //only 1-line inputs from user terminal
    		input = cleanUserInput(input);
    		if (input.equals("ps")) {
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
    			synchronized(out) {    				
    				out.println(sendMessage(input));
    			}
    		} 
        }        
    }	
}
