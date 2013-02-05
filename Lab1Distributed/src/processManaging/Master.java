package processManaging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.lang.Thread;

public class Master extends SocketMessage {

	ServerSocket listenSocket;
	static Map<SocketRespondThread, SlaveInfo> allProcess;
	final int hostPortnum;
	
	public Master(final int hostPortnum) {
		allProcess = Collections.synchronizedMap(new HashMap<SocketRespondThread, SlaveInfo>());
		this.hostPortnum = hostPortnum;
	}

	public static void sendProcessToSlave(String clientMessage)
	{
		SlaveInfo s;
		SocketRespondThread bestSocket = null;
		
		int count = 100000;
		int size;
		
		for(SocketRespondThread socket : allProcess.keySet()){
			s=allProcess.get(socket);
			//size= s.getProcesses().size();
			size = s.getWorkload();
			if(s.getProcesses().size()<count){
				count =size;
				bestSocket=socket;
			}		
		}
		bestSocket.out.println(sendMessage(startProcess + " " + clientMessage));
	}
	public void run() {
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
				allProcess.put(srt, p);
				System.out.println(allProcess);
				srt.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
