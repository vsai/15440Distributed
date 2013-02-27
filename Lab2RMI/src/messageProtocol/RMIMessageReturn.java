package messageProtocol;

public class RMIMessageReturn extends Message {

	/*
	 * TODO: What if the return object itself is not-serializable?
	 */
	
	boolean completed;
	Object returnObject;
	Exception exceptionThrown;
	
	public RMIMessageReturn(boolean completed, Object returnObject, Exception exceptionThrown) {
		messageType = "RMIMessageReturn";
		this.completed = completed;
		this.returnObject = returnObject;
		this.exceptionThrown = exceptionThrown;
	}	
}
