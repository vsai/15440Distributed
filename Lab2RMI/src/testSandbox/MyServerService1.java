package testSandbox;

public class MyServerService1 implements MyService1{

	private final String message = "HELLO World";
	
	@Override
	public String getMessage() {
		return message;
	}

}
