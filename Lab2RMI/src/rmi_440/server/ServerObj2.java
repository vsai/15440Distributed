package rmi_440.server;


class ServerObj2 extends ServerObjects implements ServerObj2Intf{

	int value2;
	
	public ServerObj2(String objName) {
		super(objName);
		value2 = 5;
	}

	public int getStubScore(ServerObj1Intf a) {
		return a.getScore();
	}
	
	public int getValue2() {
		return value2;
	}
	
	public void incrementValue2() {
		value2+=1;
	}
	
}
