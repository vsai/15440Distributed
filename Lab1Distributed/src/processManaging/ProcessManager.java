package processManaging;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ProcessManager {

	/**
	 * @param args
	 */
	private static boolean isSlave;
	private static String hostname;
	private static int hostPortnum = 12121;
	private static String ip;

	public static String getIp() {
		return ip;
	}

	public static void setIp() {
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("Could not resolve ip address. Exiting system.");
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {	
		if (args.length == 0) {
			hostname = null;
			System.out.println("I'm a MASTER");
			isSlave = false;	
		} else if (args.length == 2 && args[0].equals("-c")) {
			hostname = args[1];
			System.out.println("I'm a SLAVE. Master is :" + hostname);
			isSlave = true;
		} else {
			System.out.println("Invalid input to startup ProcessManager");
			System.exit(0);
		}
		setIp();
		Master m = null;
		Slave s = null;
		
		if (!isSlave){
			m = new Master(hostPortnum);
			hostname = getIp();
			m.start();
		}
		s = new Slave(hostname, hostPortnum, getIp());
		s.start();
	}
}