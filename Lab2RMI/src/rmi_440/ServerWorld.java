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
	static int hostPortnum = Settings.server_listeningToClientPortnum;
	static String registryHostname = Settings.registry_ipAddress;
	static int registryPortnum = Settings.registry_listeningToServerPortnum;
	
	String message;
	int score;
	
	public ServerWorld(String objName) {
		super(objName);
		if (this.serverObjectStore == null){
			this.serverObjectStore = new ConcurrentHashMap<String, Remote440>();
		}

		this.message = "Leggo";
		this.score = 10;
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
	
//	@Override
//	public String returnSameString(String str) {
//		
//		return str;
//	}

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
			System.out.println("ServerRunning on: " + getIp() + " : " + hostPortnum);
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

					System.out.println("In ServerWorld");
					System.out.println("RMIMessage: " + rmiMess);
					System.out.println("ObjectToInvoke: " + objInvoke);
					
					String m = rmiMess.getMethod();
					Object[] argus = rmiMess.getArguments();
					System.out.println("RMIMessage objname:" + rmiMess.getObjectName());
					System.out.println("RMIMessage methodname:" + m);
					System.out.println("RMIMessage objargs:" + argus);
					
					Class[] arguments;
					if (argus == null) {
						arguments = null;
					} else {
						arguments = new Class[argus.length];
					}
					
					boolean completed = false;
					Object result = null;
					Exception ex = null;

//					Method meth = null;
					Class cl = objInvoke.getClass();
					Method meth = null;
					try {
						meth = cl.getMethod(m, arguments);
					} catch (NoSuchMethodException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					try {
						result = meth.invoke(objInvoke,argus);
						System.out.println("result is:"+result);
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
					System.out.println(rmiRet);
					out.writeObject(rmiRet);
					out.flush();
					in.close();
					out.close();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
				clientConn.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 		
		}
	}

	
}