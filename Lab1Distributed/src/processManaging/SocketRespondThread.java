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
		try {
			while (true){
				String clientMessage = in.readLine();
				String mess[] = clientMessage.split(" ", 2);
				String i;
				while (!((i = in.readLine()).equals(messageTerminator))){
					//should only go in here for ALIVE, so these are the process that have died since then
					slaveInfo.removeProcess(i);
				}
				if (mess[0].equals(alive)){
					slaveInfo.setAlive(true);
					//System.out.println("SLAVE is alive");
//				} else if (mess[0].equals(suspended)) {
//					String suspendDetails[] = mess[1].split(" ", 2);
//					String filePath = suspendDetails[0];
//					String processSuspended = suspendDetails[1];
//					System.out.println("Process was suspended. Details:");
//					System.out.println("filePath: " + filePath);
//					System.out.println("processSuspended: " + processSuspended);

				} else if (mess[0].equals(quit)) {
					Master.allProcess.remove(this);
					break;
//				} else if (mess[0].equals(started)){
//					slaveInfo.putProcess(mess[1]);
				} else {
					System.out.println("IN MASTER: RECEIVED A NEW PROCESS FROM CLIENT");
//					out.println(sendMessage(receivedProcess + " " + clientMessage));
//					Master.startProcessWithBestSlave(clientMessage);
					
					/*
					 * START PROCESS IN MASTER
					 * SERIALIZE PROCESS
					 * STORE TO HASHMAP
					 */
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
