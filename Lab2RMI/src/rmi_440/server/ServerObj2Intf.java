package rmi_440.server;

public interface ServerObj2Intf extends Remote440{

	public int getStubScore(ServerObj1Intf a);
	
	public int getValue2();
	
	public void incrementValue2();
	
}
