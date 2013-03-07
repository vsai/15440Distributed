package rmi_440.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import rmi_440.RemoteObjectReference;
import rmi_440.Settings;

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
			System.err.println("Couldn't listen on: " + registryPortnum);
			System.err.println("Try restarting the server");
			System.exit(1);
		}

		/* Listen to requests from clients to perform actions on the server */
		Socket clientConn;
		ServerReader runner;
		while (true) {
			try {
				clientConn = listen.accept();
				System.out.println("In Server: socket connection established");
				runner = new ServerReader(clientConn);
				runner.start();
			} catch (IOException e) {
				System.err.println("Failed to accept socket connection");
			}	
		}
	}
}