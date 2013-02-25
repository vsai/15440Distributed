package rmi_440;

import java.io.Serializable;

public class RemoteObjectReference implements Serializable {

	String serverIp;
	int portnum;
	String interfaceName;
	String objectName;
	
	public RemoteObjectReference(String serverIp, int portnum, String interfaceName, String objectName) {
		this.serverIp = serverIp;
		this.portnum = portnum;
		this.interfaceName = interfaceName;
		this.objectName = objectName;
	}
	
	public String getServerIp() {
		return serverIp;
	}
	
	public int getPortnum() {
		return portnum;
	}
	
	public String getInterfaceName() {
		return interfaceName;
	}
	
	public String getObjectName() {
		return objectName;
	}
}
