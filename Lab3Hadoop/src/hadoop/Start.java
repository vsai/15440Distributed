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
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Could not get ipAddress");
			System.exit(-1);
		}
		ConfigReader cread = new ConfigReader();
		MasterWrapper m = cread.readMaster();
		
		ArrayList<SlaveWrapper> sw = cread.readSlaves();
		System.out.println(sw);
		System.out.println(sw.size());
		System.out.println(ipAddress);
		System.out.println(m);
		if (m.getIpAddress().equals(ipAddress)) {
			System.out.println("ABC");
			Master master = new Master(m.getIpAddress(), m.getPortnum());
			master.start();
		}
		System.out.println("Setup master if needed");
		for (SlaveWrapper s : sw) {
			System.out.println("parsing slavewrappers");
			if (s.getIpAddress().equals(ipAddress)) {
				Slave slave = new Slave(s.getIpAddress(), s.getPortnum());
				System.out.println("Setting up slave");
				slave.start();
			}
		}
		
	}
}