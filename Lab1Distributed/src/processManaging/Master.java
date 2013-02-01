package processManaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Master {

	Thread listen;
	Thread heartbeat;
	ServerSocket listenSocket;
	BufferedReader in;
	
	ArrayList<Socket> allSockets;
	ArrayList<String> socketIps;
	
	public Master(final int hostPortnum, InetAddress selfIp) {
		allSockets = new ArrayList<Socket>();
		socketIps = new ArrayList<String>();
		listen = new Thread("MasterListen") {
			public void run() {
				try {
					listenSocket = new ServerSocket(hostPortnum);
				} catch (IOException e) {
					System.out.println("Couldn't listen on: " + hostPortnum);
					e.printStackTrace();
				}
			}
		};
	}

	public void startUp() {
		listen.run();
		while (true) {
			try {
				Socket clientConn = listenSocket.accept();
				SocketRespondThread srt = new SocketRespondThread(clientConn);
				allSockets.add(clientConn);
				String hostAdd = clientConn.getLocalAddress().getHostAddress();
				socketIps.add(hostAdd);
				System.out.println("Connected to: " + hostAdd);
				srt.run();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}	
	
}
