package rmi_440.server;

public interface ServerObj2Intf extends Remote440{

	public int getStubScore(ServerObj1Intf a) throws Exception;
	public void setStubScore(ServerObj1Intf a, int newScore) throws Exception;
	public int getValue2() throws Exception;
	public void incrementValue2() throws Exception;
	public void problemExceptionThrow() throws Exception;
	
}
