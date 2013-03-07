package rmi_440.server;

//import rmi_440.Remote440_Exception;

public interface ServerObj1Intf extends Remote440 {

	String getMessage();// throws Remote440_Exception;
	int getScore();// throws Remote440_Exception;
	void increment();// throws Remote440_Exception;
	String returnSameString(String str, int [] ar,int num);// throws Remote440_Exception;
	
}
