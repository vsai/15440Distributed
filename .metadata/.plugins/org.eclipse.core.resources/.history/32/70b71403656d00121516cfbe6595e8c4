package processManaging;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestingCode {

	/**
	 * @param args
	 */
	private static InetAddress ip;
	
	public static void main(String[] args) {
		//Find out your own IP details
		for (int i = 0; i<3; i++) {
			try {
				ip = InetAddress.getLocalHost();
				System.out.println("Current IP address : " + ip.getHostAddress());
				break;
			} catch (UnknownHostException e) {
				if (i <3) {
					System.out.println("Could not resolve ip address. Trying again.");
				} else {
					System.out.println("Could not resolve ip address. Exiting system.");
					System.exit(0);
				}
				e.printStackTrace();
			}
		}
		String[] ab = {"ABC", "def", "odojs"};
		System.out.println(ab);
	}
}
