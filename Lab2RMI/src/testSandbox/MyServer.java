package testSandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;


public class MyServer {

//	public static final String message = "HELLO World";
	ConcurrentHashMap<String, MyRmiServerIntf> registry;
	int portnum = 1234;
	ServerSocket listen;
	PrintStream out;
    BufferedReader in;
	
	public MyServer(){
		registry = new ConcurrentHashMap<String, MyRmiServerIntf>();
	}
	
	public void addService(String serviceStr, MyRmiServerIntf service){
		registry.put(serviceStr, service);
	}
	
	public void main(String args[]){
		try {
			listen = new ServerSocket(portnum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (true) {
			try {
				Socket conn = listen.accept();
				System.out.println("");
				out = new PrintStream(conn.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				//read input, call appropriate method, and return respond
				//close clientConn
				
			} catch (IOException e) {
				e.printStackTrace();
			} 		
		}	
	}
}
