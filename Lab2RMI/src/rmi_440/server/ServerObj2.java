package rmi_440.server;


class ServerObj2 extends ServerObjects implements ServerObj2Intf{

	int value2;
	
	public ServerObj2(String objName) {
		super(objName);
		value2 = 5;
	}

	public int getStubScore(ServerObj1Intf a) throws Exception {
		return a.getScore();
	}
	
	public void setStubScore(ServerObj1Intf a, int newScore) throws Exception {
		a.setScore(newScore);
	}
	
	public int getValue2() {
		return value2;
	}
	
	public void incrementValue2() {
		value2+=1;
	}
}
