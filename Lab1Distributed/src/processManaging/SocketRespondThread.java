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
	
	public void run(){
		try {
			while (true){
				String clientMessage = in.readLine();
				String mess[] = clientMessage.split(" ", 2);
				String i;
				while (!((i = in.readLine()).equals(messageTerminator))){
					//should only go in here for ALIVE, so these are the process that have died since then
					System.out.println("MASTER SRT: I'm removing a process");
					System.out.println(i);
					System.out.println("RemoveSuccess:? --- " + slaveInfo.removeProcess(i));
				}
				if (mess[0].equals(alive)){
					slaveInfo.setAlive(true);
					System.out.println(clientMessage);
				} else if (mess[0].equals(quit)) {
					Master.allProcess.remove(this);
					break;
				} else if (mess[0].equals(startProcess)){
					System.out.println("IN MASTER: RECEIVED A NEW PROCESS FROM CLIENT");
					
					
					Master.addNewProcess(mess[1]);
					
					
					/*
					 * mess[1] will be a string of processname and process args
					 * START PROCESS IN MASTER
					 * SERIALIZE PROCESS
					 * STORE TO HASHMAP
					 */					
				} else {
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
