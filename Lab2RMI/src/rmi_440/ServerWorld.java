package rmi_440;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import messageProtocol.Message;

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
		BufferedReader in;
		PrintStream out;
		
		while (true) {
			try {
				clientConn = listen.accept();
				System.out.println("In Master: socket connection established");
				out = new PrintStream(clientConn.getOutputStream(), true);
	            in = new BufferedReader(new InputStreamReader(clientConn.getInputStream()));
	            String readline;
	            while (!(readline = in.readLine()).equals(Message.TERMINATOR)) {
	            	
	            }
			} catch (IOException e) {
				e.printStackTrace();
			} 		
		}
	}
}