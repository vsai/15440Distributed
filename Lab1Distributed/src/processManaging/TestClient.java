package processManaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Socket socketToMaster;
		String hostname = "localhost";
		int hostPortnum = 12123;
		//PrintWriter out = null;
		PrintStream out = null;
		BufferedReader in = null;
		try {
        	socketToMaster = new Socket(hostname, hostPortnum);
            out = new PrintStream(socketToMaster.getOutputStream(), false);
            in = new BufferedReader(new InputStreamReader(socketToMaster.getInputStream()));
            System.out.println("In Client: Connected to server");
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostname);
            System.exit(1);
        }
		for (int i=0; i<5; i++) {
			out.println("SENDING MESSAGE FROM CLIENT");
			System.out.println("SENT MESSAGE TO SERVER FROM CLIENT");
			String s = null;
			try {
				s = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("RECEIVING MESSAGE FROM SERVER: " + s);
		}
		
	}
}