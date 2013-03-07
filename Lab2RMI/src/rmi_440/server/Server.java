package rmi_440.server;

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

import rmi_440.RemoteObjectReference;
import rmi_440.Settings;
import rmi_440.stubs.StubGenerator;

public class Server {

	protected static ConcurrentHashMap<String, Remote440> serverObjectStore; //ConcurrentHashMap?
	private static ServerSocket listen;
	public static int hostPortnum = Settings.server_listeningToClientPortnum;
	public static String registryHostname = Settings.registry_ipAddress;
	public static int registryPortnum = Settings.registry_listeningToServerPortnum;
		
	private static void storeObjToRegistry(ServerObjects obj, String interfaceName) {
		RemoteObjectReference objRor = new RemoteObjectReference(ServerObjects.getIp(), hostPortnum, interfaceName, obj.getObjectName());
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
		System.out.println("Successfully stored object " + obj.getObjectName() + " to registry");
	}
	
	private static void storeObject(ServerObjects obj, String interfaceName) {
		serverObjectStore.put(obj.getObjectName(), (Remote440)obj);
		storeObjToRegistry(obj, interfaceName);
	}
	
	public static void main(String[] args) {
		/* Store all objects wanted on the server on startup */
		serverObjectStore = new ConcurrentHashMap<String, Remote440>();
		
		ServerObj1 obj1 = new ServerObj1("obj1");
		ServerObj1 obj11 = new ServerObj1("obj11");
		ServerObj1 obj12 = new ServerObj1("obj12");
		storeObject(obj1, "ServerObj1Intf");
		storeObject(obj11, "ServerObj1Intf");
		storeObject(obj12, "ServerObj1Intf");
		
		
		ServerObj2 obj2 = new ServerObj2("obj2");
		storeObject(obj2, "ServerObj2Intf");
		
		
		/* Prepare the server to listen to requests from clients */
		try {
			listen = new ServerSocket(hostPortnum);
		} catch (IOException e1) {
			System.out.println("Couldn't listen on: " + registryPortnum);
			e1.printStackTrace();
			System.exit(1);
		}

		/* Listen to requests from clients to perform actions on the server */
		Socket clientConn;
		ObjectInputStream in;
		ObjectOutputStream out;
		
		RMIMessage rmiMess;
		Remote440 objInvoke;
		RMIMessageReturn rmiRet;
		StubGenerator stubGenerator = new StubGenerator();
		while (true) {
			try {
				clientConn = listen.accept();
				System.out.println("In Server: socket connection established");
				in = new ObjectInputStream(clientConn.getInputStream());
				out = new ObjectOutputStream(clientConn.getOutputStream());
				
				try {
					rmiMess = (RMIMessage) in.readObject();
					objInvoke = serverObjectStore.get(rmiMess.getObjectName());
					
					String m = rmiMess.getMethod();
					Object[] argus = rmiMess.getArguments();
					Class<?>[] classArgs = rmiMess.getClassArguments();
					
					/*
					 * Converts RemoteObjectReferences into their respective stubs
					 */
					if (argus!=null) {
						for (int i = 0; i < argus.length; i++) {
							if (argus[i] instanceof RemoteObjectReference) {
								argus[i] = stubGenerator.createStub((RemoteObjectReference)argus[i]);
							}							
						}
					}					
					
					boolean completed = false;
					Object result = null;
					Exception ex = null;
					
					Method meth = null;
					try {
						meth=objInvoke.getClass().getDeclaredMethod(m, classArgs);
						result = meth.invoke(objInvoke,argus);
						System.out.println("result is:"+result);
						completed = true;
						System.out.println("method is completed:"+completed);

					} catch (IllegalArgumentException e) {
						ex = e;
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						ex = e;
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						ex = e;
						e.printStackTrace();
					} catch (Exception e) {
						ex = e;
						e.printStackTrace();
					}
					rmiRet = new RMIMessageReturn(completed, result, ex);
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
