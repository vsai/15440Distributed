package hadoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import messageProtocol.InitiateConnection;

public class Master extends Thread {

	String ipAddress;
	int portnum;
	
	ArrayList<Slave> slaves;
	
	public Master (String ipAddress, int portnum) {
		this.ipAddress = ipAddress;
		this.portnum = portnum;
	}
	
	public void run () {
		ServerSocket ss;
		Socket s;
		ObjectInputStream in;
		ObjectOutputStream out;
		InitiateConnection initConn;
		try {
			ss = new ServerSocket(portnum);
			while (true) {
				try {
					s = ss.accept();
					in = new ObjectInputStream(s.getInputStream());
					out = new ObjectOutputStream(s.getOutputStream());
					try {
						initConn = (InitiateConnection) in.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					in.close();
					out.close();
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		//start a server socket running on the portnum
		//listen to incoming connections from slaves and create a list of slaves
		//listen to incoming connections from users
		
		//scheduler + dispatcher
	}
}
