package processManaging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Thread;
import java.util.concurrent.ConcurrentHashMap;

public class Master extends SocketMessage {
	
	Thread redistributeWorkload;
	ServerSocket listenSocket;
	//ConcurrentHashMap<SocketRespondThread, Boolean> srtHash;
	List<SocketRespondThread> srtList;
	//ConcurrentHashMap<String, ProcessInfo> processHashmap;
	
	static ConcurrentHashMap<SocketRespondThread, SlaveInfo> allProcess;
	final int hostPortnum;
	
	public Master(final int hostPortnum) {
		srtList = Collections.synchronizedList(new ArrayList<SocketRespondThread>());
		//processHashmap = new ConcurrentHashMap<String, ProcessInfo>();
		
		allProcess = new ConcurrentHashMap<SocketRespondThread, SlaveInfo>();
		this.hostPortnum = hostPortnum;
		
//		redistributeWorkload = new Thread("WorkloadRedistributer") {
//			Long sleepTime = (long) 20000;
//			public void run(){
//				while (true) {
//					for (SocketRespondThread sl : allProcess.keySet()) {
//						SlaveInfo sInfo = allProcess.get(sl);
//						if (sInfo.isAlive()) {
//							sInfo.setAlive(false);
//						} else{
//							//that process has died
//							allProcess.remove(sl);
//						}
//						redistribute();
//					}
//					try {
//						Thread.sleep(sleepTime);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		};
	}
	
	public static void messageToSlave(String messageType, String clientMessage, SocketRespondThread srt) {
		if (messageType.equals(startProcess) || messageType.equals(suspendProcess) || messageType.equals(resumeProcess)) {
			srt.out.println(sendMessage(messageType + " " + clientMessage));
		} else {
			System.out.println("Not a valid message");
		}
	}
	

	public void redistribute(){

	}
	
	public void run() {
		try {
			listenSocket = new ServerSocket(hostPortnum);
		} catch (IOException e1) {
			System.out.println("Couldn't listen on: " + hostPortnum);
			e1.printStackTrace();
		}
		redistributeWorkload.start();
		
		while (true) {
			try {
				Socket clientConn = listenSocket.accept();
				System.out.println("In Master: socket connection established");
				SlaveInfo p = new SlaveInfo();
				SocketRespondThread srt = new SocketRespondThread(clientConn, p);
				allProcess.put(srt, p);
				srt.start();
				srt.
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
	}
}
