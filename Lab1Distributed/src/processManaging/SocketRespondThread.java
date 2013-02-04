package processManaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketRespondThread extends SocketMessage{

	Socket conn;
    //PrintWriter out; //what the socket writes to the client --from server
	PrintStream out;
    BufferedReader in; //what the socket (client) reads from the client --from server
    SlaveInfo slaveInfo;

	SocketRespondThread(Socket conn, SlaveInfo slaveInfo){
		this.conn = conn;
		this.slaveInfo = slaveInfo;
		try {
			out = new PrintStream(conn.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}        
	}
	
	// FOR LOAD BALANCER, MAKE SURE TO PUT IN RESUME
	
	public void run(){
		while (true){
			try {
				String clientMessage = in.readLine();
				System.out.println("In Master SocketResponder: " + clientMessage);
				String mess[] = clientMessage.split(" ", 2);
				String i;
				while (!((i = in.readLine()).equals(messageTerminator))){
					//should only go in here for ALIVE processes
					System.out.println("THE MESSAGE I GOT WAS NOT A MESSAGE TERMINATOR");
					slaveInfo.removeProcess(i);
				}
				System.out.println("In Master: GOT AN END MESSAGE");
				
				if (mess[0].equals(alive)){
					//check if workload sent matches the workload in slave info???
					//or not necessary?
					System.out.println("SLAVE is alive");
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
				} else if (!mess[0].equals(messageTerminator)){
					//this should be a newProcess
					//TO SEND TO ANOTHER SOCKET POSSIBLY:
					System.out.println("IN MASTER: RECEIVED A NEW PROCESS FROM CLIENT");
					out.println(sendMessage(receivedProcess + " " + clientMessage));
					out.println(sendMessage(startProcess + " " + clientMessage));
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
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
