package rmi_440;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import messageProtocol.RMIMessage;
import messageProtocol.RMIMessageReturn;

public class ServerWorld extends ServerObjects implements ServerObjIntf{

	/**
	 * @param args
	 */
	static ConcurrentHashMap<String, Remote440> serverObjectStore; //ConcurrentHashMap?
	static ServerSocket listen;
	static int hostPortnum = 12321;
	static String registryHostname = "localhost";
	static int registryPortnum = 12322;
	
	String message;
	int score;
	
	public ServerWorld(String objName) {
		super(objName);
		if (this.serverObjectStore == null){
			this.serverObjectStore = new ConcurrentHashMap<String, Remote440>();
		}

		this.message = "Leggo";
		this.score = 0;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public void increment() {
		score++;
	}

	public static void storeObjToRegistry(ServerObjects obj) {
		//TODO: Generically get the interface name rather than hardcode it in
		RemoteObjectReference objRor = new RemoteObjectReference(getIp(), hostPortnum, "ServerObjIntf", obj.getObjectName());
		Socket toRegistry;
		ObjectOutputStream out;
		try {
			toRegistry = new Socket(registryHostname, registryPortnum);
			out = new ObjectOutputStream(toRegistry.getOutputStream());
			out.writeObject(objRor);
			out.flush();
			out.close();
			toRegistry.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void storeObject(ServerObjects obj) {
		serverObjectStore.put(obj.getObjectName(), (Remote440)obj);
		storeObjToRegistry(obj);
	}
	
	public static void main(String[] args) {
		/* Store all objects wanted on the server on startup */
		ServerWorld obj1 = new ServerWorld("obj1");
		storeObject(obj1);	
		
		/* Prepare the server to listen to requests from clients */
		try {
			listen = new ServerSocket(hostPortnum);
		} catch (IOException e1) {
			System.out.println("Couldn't listen on: " + registryPortnum);
			e1.printStackTrace();
		}

		/* Listen to requests from clients to perform actions on the server */
		Socket clientConn;
		ObjectInputStream in;
		ObjectOutputStream out;
		
		RMIMessage rmiMess;
		Remote440 objInvoke;
		RMIMessageReturn rmiRet;
		
		while (true) {
			try {
				clientConn = listen.accept();
				System.out.println("In Master: socket connection established");
				in = new ObjectInputStream(clientConn.getInputStream());
				out = new ObjectOutputStream(clientConn.getOutputStream());
				
				try {
					rmiMess = (RMIMessage) in.readObject();
					objInvoke = serverObjectStore.get(rmiMess.getObjectName());
					String m = rmiMess.getMethod();
					Object[] argus = rmiMess.getArguments();
					
					boolean completed = false;
					Object result = null;
					Exception ex = null;
					try {
						result = objInvoke.getClass().getMethod(m).invoke(argus);
//						result = method.invoke(objInvoke, argus);
						completed = true;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (Exception e) {
						ex = e;
					}
					rmiRet = new RMIMessageReturn(completed, result, ex);
					out.writeObject(rmiRet);
					out.flush();
					in.close();
					out.close();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				clientConn.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 		
		}
	}
}