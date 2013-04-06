package hadoop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import messageProtocol.InitiateConnection;
import messageProtocol.InitiateConnection.Source;

import fileIO.ConfigReader;

public class Slave extends Thread {
	
	String ipAddress;
	int portnum;
	
	public Slave (String ipAddress, int portnum) {
		this.ipAddress = ipAddress;
		this.portnum = portnum;
	}

	public void run() {
		ConfigReader cread = new ConfigReader();
		MasterWrapper m = cread.readMaster();
		Socket toMaster;
		InitiateConnection initConn = new InitiateConnection(ipAddress, portnum, Source.SLAVE);
		ObjectInputStream in;
		ObjectOutputStream out;
		try {
			toMaster = new Socket(m.ipAddress, m.portnum);
			in = new ObjectInputStream(toMaster.getInputStream());
			out = new ObjectOutputStream(toMaster.getOutputStream());
			out.writeObject(initConn);
			
			
			
//			in.close();
//			out.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
