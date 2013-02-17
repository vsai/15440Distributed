package testSandbox;

public class MyClient {
	
	MyRmiServerIntf obj = null;
	
	String serverName = "localhost";
	int portnum = 1234;
	
	public MyClient(){
	}
	
	public String getMessage(){
		obj = new MyClientServerStub(serverName, portnum);
		return obj.getMessage();
	}
	
	public static void main(String args[]){
		MyClient cli = new MyClient();
		System.out.println(cli.getMessage());
	}
}
