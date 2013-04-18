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
		
//		System.out.println(sw);
//		System.out.println(sw.size());
//		System.out.println(ipAddress);
//		System.out.println(m);
		if (m.getIpAddress().equals(ipAddress)) {
			System.out.println("this is a master");
			Master master = new Master(m.getIpAddress(), m.getPortnum());
			master.start();
		}

		ArrayList<SlaveWrapper> sw = cread.readSlaves();
		System.out.println("starting");
		System.out.println("arraylist size: " + sw.size());
		
		System.out.println("Going through all slave wrappers");
		for (SlaveWrapper s : sw) {
			if (s.getIpAddress().equals(ipAddress)) {
				System.out.println("this is a slave");
				Slave slave = new Slave(s.getIpAddress(), s.getPortnum());
//				System.out.println("Setting up slave");
				slave.start();
			}
		}
		
	}
}