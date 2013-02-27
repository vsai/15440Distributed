package messageProtocol;

//import java.lang.reflect.Method;

public class RMIMessage extends Message{

	String objectName;
	String method;
	Object[] args;
	
	public RMIMessage(String objectName, String method, Object[] args) {
		messageType = "RMIMessage";
		
		this.objectName = objectName;
		this.method = method;
		this.args = args;
	}
	
	public String getObjectName() {
		return objectName;
	}
	
	public String getMethod() {
		return method;
	}
	
	public Object[] getArguments() {
		return args;
	}
}
