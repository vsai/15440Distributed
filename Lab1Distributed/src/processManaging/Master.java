package processManaging;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Thread;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import Transactional_IO.TransactionalFileOutputStream;

import processMigration.MigratableProcess;

public class Master extends SocketMessage {
	
	Thread redistributeWorkload;
	ServerSocket listenSocket;	
	static ConcurrentHashMap<SocketRespondThread, SlaveInfo> allProcess;
	final int hostPortnum;
	static Map<String,String> newProcessesToAdd;
	
	public Master(final int hostPortnum) {		
		allProcess = new ConcurrentHashMap<SocketRespondThread, SlaveInfo>();
		this.hostPortnum = hostPortnum;
		newProcessesToAdd = new HashMap<String,String>(); //key=filepath value = process and process args
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
	
	public void messageToSlave(String messageType, String clientMessage, SocketRespondThread srt) {
		if (messageType.equals(suspendProcess) || messageType.equals(resumeProcess)) {
			srt.out.println(sendMessage(messageType + " " + clientMessage));
		} else if (messageType.equals(suspendALL)){
			srt.out.println(sendMessage(messageType));
		} else {
			System.out.println("Not a valid message");
		}
	}
	
	public void redistribute(){
		//Key=filepath
		//value =Process and process args
		System.out.println("In Master:redistribute()");
		for (SocketRespondThread socket : allProcess.keySet()){
			socket.out.println(sendMessage(suspendALL));
		}
		
		//Code to resume processes
		Map<String,String> processes = new HashMap<String,String>();
		List<ProcessInfo> slavesProcess;
		for(SocketRespondThread socket : allProcess.keySet()){
			SlaveInfo s = allProcess.get(socket);
			slavesProcess = s.getProcesses();
			for(ProcessInfo p : slavesProcess){
				processes.put(p.getFilePath(), p.getProcessName()+p.getProcessArgs());
			}
			s.clearProcessInfoList();
		}
		//Add the new processes that we have received
		for(String filePath :newProcessesToAdd.keySet()){
			String pAndArgs=newProcessesToAdd.get(filePath);
			processes.put(filePath, pAndArgs);
		}
		newProcessesToAdd = new HashMap<String,String>(); 
		//processes = <String filepath, String pAndArgs> ------> SlaveInfo->ProcessInfo
		int numPr = processes.size();
		int avgNumPr = numPr / allProcess.size() +1;
		int count;
		ProcessInfo x;
		for(SocketRespondThread socket :allProcess.keySet()){
			SlaveInfo s=allProcess.get(socket);
			count =0;
			for(String fileName :processes.keySet()){
				if(count >avgNumPr)
					break;
				String pAndArgs=processes.get(fileName);
				System.out.println("In Master:redistribute: " + pAndArgs);
				String [] info = pAndArgs.split(" ", 2);
				System.out.println("INFO[0]: " + info[0]);
				System.out.println("INFO[1]: " + info[1]);
				x = new ProcessInfo(null, null, info[0],info[1],fileName);
				s.putProcess(x);
				count ++;
				processes.remove(fileName);
				System.out.println("In Master: redistribute: SENDING PROCESS " + fileName + " " + pAndArgs);
				socket.out.println(sendMessage(resumeProcess + " " + fileName + " " + pAndArgs));
			}
		}
	}
	
	private static String getRandomString(int len) {
		StringBuffer sb = new StringBuffer();  
	    for (int x = 0; x <len; x++)  
	    {  
	      sb.append((char)((int)(Math.random()*26)+97));  
	    } 
	    return sb.toString();
	}
	
	public static void addNewProcess(String str){
		System.out.println("Master going to add the new process");
		System.out.println("Input for Master:addNewProcess: " + str);
		String [] p=str.split(" ", 2);
		Class<?> t;
		try {
			t = Class.forName(p[0]);
			String [] pArgs = p[1].split(" ");

			Constructor<?>[] listOfConstructors = t.getConstructors();
			int correctConstructor=0;
			for(int i =0;i<listOfConstructors.length;i++) {
				if (listOfConstructors[i].getGenericParameterTypes().length == 1) {
					correctConstructor = i;
				}
			}
			Object arg = pArgs;//new String[0];
			MigratableProcess mp = (MigratableProcess) listOfConstructors[correctConstructor].newInstance(arg);
			System.out.println("Created the new instance");
			//serialize the object
			
			String currentDir = System.getProperty("user.dir");
			System.out.println("About to serialze");
			System.out.println("My current dir is: " + currentDir);
			String randomString = getRandomString(30);
			String key= currentDir + "/" + randomString;
			TransactionalFileOutputStream fos = new TransactionalFileOutputStream(randomString); 
			ObjectOutputStream oos;
			try {
				oos = new ObjectOutputStream(fos);
				oos.writeObject(mp); 
				oos.flush(); 
				oos.close(); 
				newProcessesToAdd.put(key,str);
			} catch (IOException e) {
				e.printStackTrace();
			} 
			System.out.println("Successfully serialized it with name: " + key);
			
		} catch (ClassNotFoundException e) {
			//we should remove this eventually
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			} catch (IOException e) {
				e.printStackTrace();
			} 		
		}
	}
}