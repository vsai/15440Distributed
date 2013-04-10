package messageProtocol;

public class InitiateConnection {
	
//	public enum Source {
//		USER, SLAVE, MASTER
//	}
	
	String selfIp;
	int selfPortnum;
//	Source startpos;

	public InitiateConnection (String selfIp, int selfPortnum) { //, Source startpos) {
		this.selfIp = selfIp;
		this.selfPortnum = selfPortnum;
//		this.startpos = startpos;
	}
	
	public String getSelfIp() {
		return selfIp;
	}

	public int getSelfPortnum() {
		return selfPortnum;
	}

//	public Source getStartpos() {
//		return startpos;
//	}
	
	
}
