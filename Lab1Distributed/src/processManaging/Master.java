package processManaging;

//import java.io.BufferedReader;
import java.io.IOException;
//import java.net.InetAddress;
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
	Map<Long, SlaveInfo> allProcess;
	
	public Master(final int hostPortnum) {
		allProcess = Collections.synchronizedMap(new HashMap<Long, SlaveInfo>());
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
				SlaveInfo p = new SlaveInfo();
				SocketRespondThread srt = new SocketRespondThread(clientConn, p);
				allProcess.put(srt.getId(), p);
				srt.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
