package processManaging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
						reDistribute();
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
	public int getAvgProcessNumber()
	{
		int total=0,count=0;
		for(SlaveInfo s: allProcess.values()){
			total+=s.getProcesses().size();
			count++;
		}
		return (int)Math.floor(total/count);
	}
	public void reDistribute(){
		SlaveInfo s;
		int avgNum=getAvgProcessNumber();
		//ArrayList<SocketRespondThread> underAvgList=new ArrayList<SocketRespondThread>();
		ArrayList<SocketRespondThread> overAvgList=new ArrayList<SocketRespondThread>();
		for(SocketRespondThread socket: allProcess.keySet()){
			s= allProcess.get(socket);
			//if(s.getProcesses().size()< avgNum)
				//underAvgList.add(socket);
			if(s.getProcesses().size() > avgNum)
				overAvgList.add(socket);
		}
		SlaveInfo slaveOver;
		String keyForProcess;
		for(SocketRespondThread socketOfSlaveOver : overAvgList){
			 slaveOver = allProcess.get(socketOfSlaveOver);
			while(slaveOver.getProcesses().size()>avgNum){
				keyForProcess=slaveOver.getProcesses().remove(0);
				//suspend this key in this slave
				socketOfSlaveOver.out.println(sendMessage(suspendProcess + " " + keyForProcess));
				//
				}
			}
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
