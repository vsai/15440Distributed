package processManaging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.lang.Thread;

public class Master extends Thread{

	//Thread listen;
	ServerSocket listenSocket;
	Map<Long, SlaveInfo> allProcess;
	final int hostPortnum;
	
	public Master(final int hostPortnum) {
		allProcess = Collections.synchronizedMap(new HashMap<Long, SlaveInfo>());
		this.hostPortnum = hostPortnum;
		/*
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
		*/
	}

	public void run() {
		//listen.run();
		try {
			listenSocket = new ServerSocket(hostPortnum);
		} catch (IOException e1) {
			System.out.println("Couldn't listen on: " + hostPortnum);
			e1.printStackTrace();
		}
		while (true) {
			try {
				Socket clientConn = listenSocket.accept();				
				System.out.println("In Master: socket connection established");
				SlaveInfo p = new SlaveInfo();
				SocketRespondThread srt = new SocketRespondThread(clientConn, p);
				allProcess.put(srt.getId(), p);
				System.out.println(allProcess);
				srt.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
