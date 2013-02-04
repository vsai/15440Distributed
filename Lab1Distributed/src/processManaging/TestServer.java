package processManaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket listenSocket = null;
		int hostPortnum = 12123;
		
		try {
			listenSocket = new ServerSocket(hostPortnum);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Couldn't listen on: " + hostPortnum);
			e1.printStackTrace();
		}
		Socket s;
		PrintStream out = null;
		BufferedReader in = null;
		try {
			s = listenSocket.accept();
			out = new PrintStream(s.getOutputStream(), false);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			try {
				System.out.println(in.readLine());
				String i;
				while ((i=in.readLine()) !=null){
					System.out.println("RECEIVED FROM CLIENT: " + in.readLine());
					out.println("RECEVIED CLIENT MESSAGE. Sending Message to Server");
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
