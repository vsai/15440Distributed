package processManaging;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Scanner;

import processMigration.MigratableProcess;

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ProcessManager {

	/**
	 * @param args
	 */
	private static boolean isSlave;
	private static String hostname;
	private static int hostPortnum = 12121;
	private static HashMap<MigratableProcess, String[]> processes;
	private static InetAddress ip;
	private static Socket clientSocket;
	
	//Master = start the new processes
	//Slave = does not start new processes
	//Master = accepts ps, quit, allows to start new processes
	//Slave = accepts ps, quit
	
	public static void main(String[] args) {	
		if (args.length == 0) {
			hostname = null;
			System.out.println("I'm a MASTER");
			isSlave = false;	
		} else if (args.length == 2 && args[0].equals("-c")) {
			hostname = args[1];
			System.out.println("I'm a SLAVE. Master is :" + hostname);
			isSlave = true;
		} else {
			System.out.println("Invalid input to startup ProcessManager");
			System.exit(0);
		}
		
		for (int i = 0; i<3; i++) {
			try {
				ip = InetAddress.getLocalHost();
				System.out.println("Current IP address : " + ip.getHostAddress());
				break;
			} catch (UnknownHostException e) {
				if (i <3) {
					System.out.println("Could not resolve ip address. Trying again.");
				} else {
					System.out.println("Could not resolve ip address. Exiting system.");
					System.exit(0);
				}
				e.printStackTrace();
			}
		}
		if (isSlave) {
			try {
				clientSocket = new Socket(hostname, hostPortnum);
			} catch (UnknownHostException e) {
				System.out.println("Could not connect to host: " + hostname);
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IO Exception occured");
				e.printStackTrace();
			}  
			System.out.println("Connected to " + hostname + " on port " + hostPortnum);    // for debugging purpose  
			//connect outgoing stream to client socket  
			DataOutputStream outToServer = null;
			try {
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
				String message = "Yo I'm connected. My ipaddress is (" + ip.getHostAddress() + ")";
				outToServer.writeBytes(message);
				clientSocket.close();
			} catch (IOException e) {
				System.out.println("Could not send message to client");
				e.printStackTrace();
			}  
		}
		
		processes = new HashMap<MigratableProcess, String[]>();
		Scanner sc = new Scanner(System.in);
		
		while(true){
			System.out.println("Prompt for input ==>");
			String input = sc.nextLine();
			
			if (input.equals("ps")) {
				for (MigratableProcess p : processes.keySet()){
					System.out.print(p.getClass().getName());
					String[] pArgs = processes.get(p);
					for (String arg : pArgs){
						System.out.print(arg);
					}
					System.out.println();
				}
			} else if (input.equals("quit")){
				System.exit(0);
			} else if (isSlave == false){//Process input with commands - only for master
				String in[] = input.split(" ", 2);
				String processArgs[] = in[1].split(" ");
				try {
					Class<?> myClass = Class.forName(in[0]);
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
			} else {
				System.out.println("Unsupported input");
				System.out.println("Master: ps, quit, and can start new processes");
				System.out.println("Slave: ps, quit");
			}
		}
	}
}