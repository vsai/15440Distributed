package processManaging;

//import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.lang.Thread;

public class Master extends Thread{

	Thread listen;
	ServerSocket listenSocket;
	//BufferedReader in;

	Map<Long, Map<String, String[]>> allProcess;
	
	public Master(final int hostPortnum, InetAddress selfIp) {
		allProcess = Collections.synchronizedMap(new HashMap<Long, Map<String, String[]>>());
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

	public void run() {
		listen.run();
		while (true) {
			try {
				Socket clientConn = listenSocket.accept();
				Map<String, String[]> p = Collections.synchronizedMap(new HashMap<String, String[]>());
				SocketRespondThread srt = new SocketRespondThread(clientConn, p);
				allProcess.put(srt.getId(), p);
				String hostAdd = clientConn.getLocalAddress().getHostAddress();
				System.out.println("Connected to: " + hostAdd);
				srt.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
