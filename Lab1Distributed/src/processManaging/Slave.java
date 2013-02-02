package processManaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
//import java.io.ObjectOutputStream;
import java.io.PrintStream;
//import java.lang.reflect.Constructor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;

import processMigration.MigratableProcess;

/*
 * USE JOINS AROUND THE SUSPEND STUFF
 */


public class Slave extends Thread{

	Socket socketToMaster;
	String hostname;
	final int hostPortnum;
	String selfIp;
	
	HashMap<MigratableProcess, String[]> processes;
	
	public Slave(String hostname, final int hostPortnum, String selfIp) {
		this.hostname = hostname;
		this.hostPortnum = hostPortnum;
		this.selfIp = selfIp;
		this.processes = new HashMap<MigratableProcess, String[]>();
	}
	
	public String messageToMaster(String message){
		String ipAddr = selfIp;
		return ipAddr + " " + message;
	}
	
	public void run() {
        PrintWriter out = null; //what you write to the master
        //ObjectOutputStream out = null;
        BufferedReader in = null; //what you read from master
        
        try {
        	socketToMaster = new Socket(hostname, hostPortnum);
            out = new PrintWriter(socketToMaster.getOutputStream(), true);
            //out = new ObjectOutputStream(socketToMaster.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socketToMaster.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostname);
            System.exit(1);
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("In Slave: Connected to master");
        while(true){
    		System.out.println("In Slave: Prompt for input ==>");
    		String input = sc.nextLine();
    		
    		if (input.equals("ps")) {
    			try {
					out.println(messageToMaster("ps"));
					//out.writeChars(messageToMaster("ps"));
					String inputLine = in.readLine();
					System.out.println(inputLine);
					
					
					//while ((inputLine = in.readLine()) != null) {   
					//	System.out.println(inputLine);					    
					//}
					System.out.println("In Slave: Read everything back");
				} catch (UnknownHostException e) {
					System.err.println("Could not resolve selfIp host");
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("Could not read response from master");
					e.printStackTrace();
				}
    			
    		} else if (input.equals("quit")){
    			try {
    				out.println(messageToMaster("quit"));
    				//out.writeChars(messageToMaster("quit"));
					out.close();
					in.close();
	    			socketToMaster.close();
				} catch (IOException e) {
					System.out.println("Failed to close buffers/socket before exiting");
					e.printStackTrace();
				}
    			System.exit(0);
    			
    		} else { //Process input with commands - only for master
    			out.println(messageToMaster(input));
    			/*
    			try {
					out.writeChars(messageToMaster(input));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				*/
    			String inputLine = null;
    			try {
					inputLine = in.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
    			String[] inputLine2 = inputLine.split(" ", 2);
    			String[] processDetails = inputLine2[1].split(" ",2);
    			String[] processArgs = processDetails[1].split(" ");
    			try {
					Class<?> myClass = Class.forName(processDetails[0]);
					Constructor<?> myCtor = myClass.getConstructor();
    				myCtor.getClass().getConstructor();
    				Thread t = new Thread((Runnable) myCtor.newInstance(processArgs));
				} catch (ClassNotFoundException e) {
					System.out.println("You have inputted an invalid process. Try again");
					e.printStackTrace();
				} catch (SecurityException e) {
					System.out.println("You don't have access to the constructor of this class");
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					System.out.println("This process does not have a constructor");
					e.printStackTrace();
				}catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

    		} 
    	}	
    }	
}
