package rmi_440;

public interface ServerObjIntf extends Remote440{

	String getMessage();
	
	int getScore();
	
	void increment();
	
	String returnSameString(String str, int [] ar,int num);
	
}
