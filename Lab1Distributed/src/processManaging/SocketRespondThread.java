package processManaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SocketRespondThread extends Thread{

	Socket conn;
    ObjectOutputStream out; //what the socket writes to the client --from server
    BufferedReader in; //what the socket (client) reads from the client --from server

	SocketRespondThread(Socket conn){
		this.conn = conn;
		try {
			out = new ObjectOutputStream(conn.getOutputStream());
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
	}
	
	public void run(){
		while (true){
			
		}
		//handle incoming messages from the client
		
		//new client coming in
		
		//heartbeat
		
		//request for 
	}
	
}