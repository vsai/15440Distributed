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
	
	protected String getServerIp() {
		return serverIp;
	}
	
	protected int getPortnum() {
		return portnum;
	}
	
	protected String getInterfaceName() {
		return interfaceName;
	}
	
	protected String getObjectName() {
		return objectName;
	}
}
