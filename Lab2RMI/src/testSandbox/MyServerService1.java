package testSandbox;

public class MyServerService1 implements MyRmiServerIntf{

	private final String message = "HELLO World";
	
	@Override
	public String getMessage() {
		return message;
	}

}
