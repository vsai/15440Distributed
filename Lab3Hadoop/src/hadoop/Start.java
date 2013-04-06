package hadoop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Start {

	private final static Logger LOGGER = Logger.getLogger(Start.class.getName());
	
	public static void initLogger () {
		try {
			LOGGER.addHandler(new FileHandler("Logging.txt"));
			LOGGER.setLevel(Level.INFO);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main (String args[]) throws UnknownHostException, FileNotFoundException {
		initLogger();
		String ip = InetAddress.getLocalHost().getHostAddress();	
		LOGGER.log(Level.INFO, "In user start. GotIp: " + ip);		
//		BufferedReader in = new BufferedReader(new FileReader("src/config.txt"));
//		String line;
//		String role;
//		int runport;
//		try {
//			while ((line = in.readLine()) != null) {
//				String[] linears = line.split("\t");
//				if (linears[0].equals(ip)){
//					role = linears[1];
//					runport = Integer.parseInt(linears[2]);
//					if (role.equals("MASTER")) {
//						//then spawn a master running on runport
//						LOGGER.log(Level.INFO, "spawning a master: " + ip + " " + runport);
//						Master m = new Master(ip, runport);
//						m.start();
//					} else if (role.equals("SLAVE")) {
//						//then spawn a slave on the port num
//						LOGGER.log(Level.INFO, "spawning a slave: " + ip + " " + runport);
//						Slave s = new Slave(ip, runport);
//						s.start();
//					}
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		//read config file, and spawn a new thread for every master and slave that matches this ip address
		//or
		//manually start up each slave, and then the master		
	}
}