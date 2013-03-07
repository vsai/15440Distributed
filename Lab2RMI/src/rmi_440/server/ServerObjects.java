package rmi_440.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class ServerObjects {

	String objName;
	private static InetAddress ip;
	
	public ServerObjects(String objName) {
		this.objName = objName;
	}
	
	public String getObjectName() {
		return objName;
	}	
	
	public static String getIp(){
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip.getHostAddress().toString();
	}	
}