package hadoop;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import fileIO.ConfigReader;

public class Start {
	
	/*
	 * Starts the project for the current system.
	 */
	
	public static void main (String args[]) {
		String ipAddress = null;
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
			System.out.println(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Could not get ipAddress");
			System.exit(-1);
		}
		MasterWrapper m = ConfigReader.readMaster();
		
		if (m.getIpAddress().equals(ipAddress)) {
			Master master = new Master(m.getIpAddress(), m.getPortnum());
			System.out.println("STARTING MASTER");
			master.start();
		}

		ArrayList<SlaveWrapper> sw = ConfigReader.readSlaves();
		
		for (SlaveWrapper s : sw) {
			if (s.getIpAddress().equals(ipAddress)) {
				Slave slave = new Slave(s.getIpAddress(), s.getPortnum());
				slave.start();
			}
		}	
	}
}