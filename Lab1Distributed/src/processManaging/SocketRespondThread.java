package processManaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.ObjectOutputStream;
//import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
//import java.util.HashMap;
//import java.util.Map;

public class SocketRespondThread extends SocketMessage{

	Socket conn;
    PrintWriter out; //what the socket writes to the client --from server
    BufferedReader in; //what the socket (client) reads from the client --from server
    SlaveInfo slaveInfo;

	SocketRespondThread(Socket conn, SlaveInfo slaveInfo){
		this.conn = conn;
		this.slaveInfo = slaveInfo;
		try {
			out = new PrintWriter(conn.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}        
	}
	
	public void run(){
		System.out.println("Currently connected to slave");
		while (true){
			try {
				String clientMessage = in.readLine();
				String mess[] = clientMessage.split(" ", 2);
				String i;
				while ((i = in.readLine()) != messageTerminator){
					//should only go in here for ALIVE processes
					slaveInfo.removeProcess(i);
				}
				if (mess[0].equals(alive)){
					//check if workload sent matches the workload in slave info???
					//or not necessary?
				} else if (mess[0].equals(suspended)) {
					String suspendDetails[] = mess[1].split(" ", 2);
					String filePath = suspendDetails[0];
					String processSuspended = suspendDetails[1];
				} else if (mess[0].equals(quit)) {
					try {
						this.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					//this should be a newProcess
					//TO SEND TO ANOTHER SOCKET POSSIBLY:
					//out.println(startProcess + " " + clientMessage);
					
					out.println(receivedProcess + " " + mess[1]);
				}
				
				//String mess[] = clientMessage.split(" ", 2);
				//String ipAddr = mess[0];
				//String clientCommand = mess[1];
				//System.out.println("In Master: Client sent: " + clientMessage);
				//out.println("In Master: Master received message");
				
				/*
				 * ALIVE <workload> \n deadProcesses list
				 * SUSPENDED <filePath> <processName> <<processArgs>> 
				 * quit
				 * <newProcessName> <<processArgs>>
				 */
				
				
				/*
				
				if (clientCommand.equals("ps")) {
					//find the processes for that client (ipAddr) and return back
					System.out.println("Printing running processes;;;");
					out.println("rpwork " + slaveInfo.getWorkload());
					for (String s : slaveInfo.getProcesses()){
						out.println("rp " + s);
						//System.out.println("rp " + s);
					}
					
				} else if (clientCommand.equals("quit")) {
					try {
						this.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (clientCommand.equals("heartbeat")) {
					//IS THIS NECESSARY?
				} else {
					//clientCommand is a string of a new process with its arguments			
					//slaveInfo.putProcess(clientCommand);
				}
				*/
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
