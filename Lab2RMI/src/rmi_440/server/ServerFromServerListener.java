//package rmi_440.server;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//import rmi_440.RemoteObjectReference;
//import rmi_440.Settings;
//
//public class ServerFromServerListener extends Thread{
//
//	int listeningPortnum;
//	ServerSocket ss;
//	
//	
//	public ServerFromServerListener() {
//		listeningPortnum = Settings.server_listeningToServerPortnum;
//	}
//	
//	public void run() {
//		try {
//			ss = new ServerSocket(listeningPortnum);
//			Socket serverConn;
//			ObjectInputStream in;
//			ObjectOutputStream out;
//			RemoteObjectReference ror;
//			while (true) {
//				serverConn = ss.accept();
//				in = new ObjectInputStream(serverConn.getInputStream());
//				out = new ObjectOutputStream(serverConn.getOutputStream());
//				
//				try {
//					ror = (RemoteObjectReference)in.readObject();
//					Object a = Server.serverObjectStore.get(ror.getObjectName());
//					
//					//a is of type Remote440
//					//a should also be a "ServerObjects" type
//
//					//Should we encapsulate the object into a SERVER equivalent 
//					//of CLIENTSTUB and return that as an OBJECT?					
//					
//				} catch (ClassNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				in.close();
//				out.close();
//				serverConn.close();
//			}			
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}	
//}
