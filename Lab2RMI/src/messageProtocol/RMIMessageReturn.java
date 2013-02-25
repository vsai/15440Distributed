package messageProtocol;

public class RMIMessageReturn extends Message {

	public enum ReturnType {
		INT, CHAR, BOOLEAN, OBJECT, BYTE, SHORT, LONG, FLOAT, DOUBLE 
	}
	
	/*
	 * TODO: What if the return object itself is not-serializable?
	 */
	
	boolean completed;
	ReturnType retType;
	Exception exceptionThrown;
	
	Object returnObject;
	int returnInt;
	boolean returnBool;
	char returnChar;
	byte returnByte;
	short returnShort;
	long returnLong;
	float returnFloat;
	double returnDouble;
	
	public RMIMessageReturn(boolean completed, ReturnType retType, Exception exceptionThrown) {
		messageType = "RMIMessageReturn";
		this.completed = completed;
		this.retType = retType;
		this.exceptionThrown = exceptionThrown;
	}
	
	public void setReturnVal(Object ret) {
		this.returnObject = ret;
	}
	
	public void setReturnVal(int ret) {
		this.returnInt = ret;
	}
	
	public void setReturnVal(boolean ret) {
		this.returnBool = ret;
	}
	
	public void setReturnVal(char ret) {
		this.returnChar = ret;
	}
	
	public void setReturnVal(byte ret) {
		this.returnByte = ret;
	}
	
	public void setReturnVal(short ret) {
		this.returnShort = ret;
	}
	
	public void setReturnVal(long ret) {
		this.returnLong = ret;
	}
	
	public void setReturnVal(float ret) {
		this.returnFloat = ret;
	}
	
	public void setReturnVal(double ret) {
		this.returnDouble = ret;
	}
	
}
