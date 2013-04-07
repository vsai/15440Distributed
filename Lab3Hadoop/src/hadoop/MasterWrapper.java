package hadoop;

public class MasterWrapper {
	
	String ipAddress;
	int portnum;
	
	public MasterWrapper (String ipAddress, int portnum) {
		this.ipAddress = ipAddress;
		this.portnum = portnum;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public int getPortnum() {
		return portnum;
	}

}
