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
						redistribute();
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
			srt.out.println(sendMessage(messageType + " " + clientMessage));
		} else {
			System.out.println("Not a valid message");
		}
	}
	
	public static SocketRespondThread getExtremeWorkloadSlave(boolean mostWorkload) {
		SlaveInfo s;
		SocketRespondThread bestSocket = null;
		int work;
		
		for(SocketRespondThread socket : allProcess.keySet()){
			s=allProcess.get(socket);
			work = s.getWorkload();
			if (!mostWorkload) {
				int count = 100000;
				if (work<count) {
					count = work;
					bestSocket = socket;
				}
			} else {
				int count = 0;
				if (work > count) {
					count = work;
					bestSocket = socket;
				}
			}
		}
		return bestSocket;
		
	}
		
	public static SocketRespondThread getMinWorkloadSlave(){
		return getExtremeWorkloadSlave(false);
	}
	
	public static SocketRespondThread getMaxWorkloadSlave() {
		return getExtremeWorkloadSlave(true);
	}
	
	public static void startProcessWithBestSlave(String clientMessage){
		SocketRespondThread bestSocket = getMinWorkloadSlave();
		messageToSlave(startProcess, clientMessage, bestSocket);
	}

	public void redistribute(){
		SocketRespondThread worst = getMaxWorkloadSlave();
		SocketRespondThread best = getMinWorkloadSlave();
		List<String> a = worst.slaveInfo.getProcesses();
		String key = a.remove(0);
		messageToSlave(suspendProcess, key, worst);
		//messageToSlave(resumeProcess, key, best);
	}
	
	/*
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
	*/
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
				System.out.println(allProcess);
				srt.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
