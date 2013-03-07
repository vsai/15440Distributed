package rmi_440.server;

public interface ServerObj1Intf extends Remote440 {

	String getMessage() throws Exception;
	int getScore() throws Exception;
	void increment() throws Exception;
	String returnSameString(String str, int [] ar,int num) throws Exception;
	void setScore(int newScore) throws Exception;
	
}
