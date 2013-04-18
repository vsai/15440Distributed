package messageProtocol;

public class InitiateConnection implements java.io.Serializable{
	
	String selfIp;
	int selfPortnum;

	public InitiateConnection (String selfIp, int selfPortnum) {
		this.selfIp = selfIp;
		this.selfPortnum = selfPortnum;
	}
	
	public String getSelfIp() {
		return selfIp;
	}

	public int getSelfPortnum() {
		return selfPortnum;
	}
	
}
