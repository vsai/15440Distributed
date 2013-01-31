package processManaging;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class Systems {
	
	private InetAddress ip;
	
	public Systems() {
		Astartup();
	}

	public void Astartup() {
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
	}
	

}