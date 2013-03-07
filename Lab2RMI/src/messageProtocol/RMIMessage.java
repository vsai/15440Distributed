package messageProtocol;

public class RMIMessage extends Message{

	String objectName;
	String method;
	Object[] args;
	Class<?> [] classArgs;
	public RMIMessage(String objectName, String method, Object[] args, Class<?>[] classArgs) {
		messageType = "RMIMessage";
		
		this.objectName = objectName;
		this.method = method;
		this.args = args;
		this.classArgs= classArgs;
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
	
	public Class[] getClassArguments()
	{
		return classArgs;
	}
}
