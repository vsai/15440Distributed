package testSandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClientServerStub implements MyRmiServerIntf{

	String hostname;
	int portnum;
	PrintStream out;
	BufferedReader in;
	
	MyClientServerStub(String serverHostname, int portnum){
		this.hostname = serverHostname;
		this.portnum = portnum;
	}
	
	@Override
	public String getMessage() {
		Socket socket;
		String selfIPAddr = "localhost";
		int selfPortnum = 1233;
		String serverKey = "service1";
		try {
			socket = new Socket(hostname, portnum);
			out = new PrintStream(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(selfIPAddr + selfPortnum + serverKey + "getMessage()");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//create socket connection
		return null;
	}

}
