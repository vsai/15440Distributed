package processManaging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.lang.Thread;

public class Master extends SocketMessage {

	
	Thread redistributeWorkload;
	ServerSocket listenSocket;
	static Map<SocketRespondThread, SlaveInfo> allProcess;
	final int hostPortnum;
	
	public Master(final int hostPortnum) {
		allProcess = Collections.synchronizedMap(new HashMap<SocketRespondThread, SlaveInfo>());
		this.hostPortnum = hostPortnum;
		
		redistributeWorkload = new Thread("WorkloadRedistributer") {
			Long sleepTime = (long) 20000;
			public void run(){
				while (true) {
					for (SocketRespondThread sl : allProcess.keySet()) {
						SlaveInfo sInfo = allProcess.get(sl);
						if (sInfo.isAlive()) {
							sInfo.setAlive(false);
						} else{
							//that process has died
							allProcess.remove(sl);
						}
					}
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		};
	}
	
	public static void messageToSlave(String messageType, String clientMessage, SocketRespondThread srt) {
		if (messageType.equals(startProcess) || messageType.equals(suspendProcess) || messageType.equals(resumeProcess)) {
			synchronized (srt.out) {
				srt.out.println(messageType + " " + clientMessage);
			}
		} else {
			System.out.println("Not a valid message");
		}
	}
	
	public static SocketRespondThread getLeastWorkloadSlave(){
		SlaveInfo s;
		SocketRespondThread bestSocket = null;
		int size;
		int count = 100000;
		
		for(SocketRespondThread socket : allProcess.keySet()){
			s=allProcess.get(socket);
			size = s.getWorkload();
			if(s.getProcesses().size()<count){
				count =size;
				bestSocket=socket;
			}		
		}
		return bestSocket;
	}
	
	public static void startProcessWithBestSlave(String clientMessage){
		SocketRespondThread bestSocket = getLeastWorkloadSlave();
		messageToSlave(startProcess, clientMessage, bestSocket);
	}
	
	public void run() {
		try {
			listenSocket = new ServerSocket(hostPortnum);
		} catch (IOException e1) {
			System.out.println("Couldn't listen on: " + hostPortnum);
			e1.printStackTrace();
		}
		redistributeWorkload.run();
		while (true) {
			try {
				Socket clientConn = listenSocket.accept();				
				System.out.println("In Master: socket connection established");
				SlaveInfo p = new SlaveInfo();
				SocketRespondThread srt = new SocketRespondThread(clientConn, p);
				allProcess.put(srt, p);
				System.out.println(allProcess);
				srt.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
