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

    int lastHeard = 0;
    
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
				
				//String mess[] = clientMessage.split(" ", 2);
				String i;
				while (!((i = in.readLine()).equals(messageTerminator))){
					lastHeard = 0;
					System.out.println("In Master SocketResponder: " + i);
					String mess[] = i.split(" ", 2);
					if (mess[0].equals(alive)){
						System.out.println("SLAVE is alive");
					} else if (mess[0].equals(died)) {
						if (mess.length >1) {
							System.out.println("Removing: " + mess[1]);
							slaveInfo.removeProcess(mess[1]);
						}
					} else if (mess[0].equals(suspended)) {
						String suspendDetails[] = mess[1].split(" ", 2);
						String filePath = suspendDetails[0];
						String processSuspended = suspendDetails[1];
						//SUSPENDED <filePath> <processName> <<processArgs>>
						
					} else if (mess[0].equals(quit)) {
						try {
							this.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						//this should be a newProcess
						//TO SEND TO ANOTHER SOCKET POSSIBLY:
						System.out.println("IN MASTER: RECEIVED A NEW PROCESS FROM CLIENT");
						out.println(sendMessage(receivedProcess + " " + i));
						out.println(sendMessage(startProcess + " " + i));
					}

					//should only go in here for ALIVE -- these should have died
					//System.out.println("THE MESSAGE I GOT WAS NOT A MESSAGE TERMINATOR");
					
				}
				System.out.println("In Master: GOT AN END MESSAGE");
				
								
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
