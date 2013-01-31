package processManaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.ObjectOutputStream;
import java.io.PrintStream;
//import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;

import processMigration.MigratableProcess;

public class Slave {

	Socket socketToMaster;
	String hostname;
	final int hostPortnum;
	InetAddress selfIp;
	
	HashMap<MigratableProcess, String[]> processes;
	
	public Slave(String hostname, final int hostPortnum, InetAddress selfIp) {
		
		this.hostname = hostname;
		this.hostPortnum = hostPortnum;
		this.selfIp = selfIp;
		this.processes = new HashMap<MigratableProcess, String[]>();
	}
	
	public String messageToMaster(String message){
		String ipAddr = selfIp.getHostAddress();
		return ipAddr + " " + message;
	}
	
	public void startUp() {
        PrintStream out = null; //what you write to the master
        BufferedReader in = null; //what you read from master
        
        try {
        	socketToMaster = new Socket(hostname, hostPortnum);
            out = new PrintStream(socketToMaster.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socketToMaster.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostname);
            System.exit(1);
        }

        Scanner sc = new Scanner(System.in);
        
        while(true){
    		System.out.println("Prompt for input ==>");
    		String input = sc.nextLine();
    		
    		if (input.equals("ps")) {
    			try {
					out.println(messageToMaster("ps"));
					String inputLine;
					while ((inputLine = in.readLine()) != null) {   
						System.out.println(inputLine);					    
					}
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
    			//String in1[] = input.split(" ", 2);
    			//String processArgs[] = in1[1].split(" ");
    			
    			/*
    			try {
    				Class<?> myClass = Class.forName(in1[0]);
    				Constructor myCtor = myClass.getConstructor();
    				myCtor.getClass().getConstructor();
    				//Thread t = new Thread((Runnable) myCtor.newInstance(processArgs));
    			} catch (ClassNotFoundException e) {
    				System.out.println("You have inputted an invalid process. Try again");
    				e.printStackTrace();
    			} catch (SecurityException e) {
    				System.out.println("You don't have access to the constructor of this class");
    				e.printStackTrace();
    			} catch (NoSuchMethodException e) {
    				System.out.println("This process does not have a constructor");
    				e.printStackTrace();
    			}
    			*/
    		} 
    		/*else {
    			System.out.println("Unsupported input");
    			System.out.println("Master: ps, quit, and can start new processes");
    			System.out.println("Slave: ps, quit");
    		}
    		*/
    	}

        /*
        
        try {
        	out.close();
        	in.close();
  //      	stdIn.close();
        	socketToMaster.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        */	
    }
	
	
	/*
	public void sendMessageToServer(String message) {
		PrintWriter out = null;
        BufferedReader in = null;
        
        try {
        	socketToHost = new Socket(hostname, hostPortnum);
            out = new PrintWriter(socketToHost.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socketToHost.getInputStream()));
            //socketToHost.connect(listenSocket.getLocalSocketAddress());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostname);
            System.exit(1);
        }
        
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;

        out.println(message);
        

        out.close();
        try {
        	in.close();
        	stdIn.close();
        	socketToHost.close();
        } catch (IOException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }	
        
	}
	*/
	
}