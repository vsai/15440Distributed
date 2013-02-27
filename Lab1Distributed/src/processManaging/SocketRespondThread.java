package processManaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class SocketRespondThread extends SocketMessage{

	Socket conn;
	PrintStream out;
    BufferedReader in;
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
				System.out.println("IN SocketRespondThread clientMessage: " + clientMessage);
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
				} else {
					System.out.println("SHOULDN'T GO THERE");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}