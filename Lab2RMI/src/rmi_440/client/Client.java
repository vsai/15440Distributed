package rmi_440.client;

import rmi_440.Settings;
import rmi_440.server.ServerObj1Intf;
import rmi_440.server.ServerObj2Intf;


public class Client {

	
	public static void main(String[] args) {
		String registry_ip=Settings.registry_ipAddress;
		int registry_port=Settings.registry_listeningToClientPortnum;
		RegistryLookup registry = new RegistryLookup(registry_ip,registry_port);
		
		ServerObj1Intf soi = (ServerObj1Intf) registry.lookup("obj1");
		ServerObj1Intf soi1 = (ServerObj1Intf) registry.lookup("obj1");
		ServerObj1Intf soi11 = (ServerObj1Intf) registry.lookup("obj11");
		ServerObj2Intf soi2 = (ServerObj2Intf) registry.lookup("obj2");
		
		if (soi == null || soi1 == null || soi11 == null || soi2 == null) {
			System.err.println("Client soi is null");
		}
		
		int[] ar = {1,2,3};
		System.out.println("Expected: it worked ; 1 2 3 ; 5 ;, Actual: " + soi.returnSameString("it worked", ar, 5));
		int[] ars = {3,4,5,6,7};
		System.out.println("Expected: it worked ; 3 4 5 6 7 ; 6 ;, Actual: " + soi.returnSameString("it worked", ars, 6));
		
		System.out.println("Expected: 10, Actual: " + soi.getScore());
		System.out.println("Expected: Leggo, Actual: " + soi.getMessage());
		System.out.println("Expected: 10, Actual: " + soi11.getScore());
		System.out.println("Expected: 10, Actual: " + soi2.getStubScore(soi));
		System.out.println("Expected: 5, Actual: " + soi2.getValue2());
		
		soi.increment();

		System.out.println("Expected: 11, Actual: " + soi.getScore());
		System.out.println("Expected: 11, Actual: " + soi1.getScore());
		System.out.println("Expected: 10, Actual: " + soi11.getScore());
		System.out.println("Expected: 11, Actual: " + soi2.getStubScore(soi));
		System.out.println("Expected: 11, Actual: " + soi2.getStubScore(soi1));
		
		soi1.increment();
		
		System.out.println("Expected: 12, Actual: " + soi.getScore());
		System.out.println("Expected: 12, Actual: " + soi1.getScore());
		System.out.println("Expected: 10, Actual: " + soi11.getScore());
		System.out.println("Expected: 12, Actual: " + soi2.getStubScore(soi));
		System.out.println("Expected: 12, Actual: " + soi2.getStubScore(soi1));
		
//		int score = soi.getScore();
//		String mess = soi.getMessage();
//		soi.increment();
//		int finalScore = soi.getScore();
		
//		System.out.printf("Init score: %d, Final score: %d, Message: %s\n", score, finalScore, mess);
		
//		int [] ar = {1,2,3};
//		String str = soi.returnSameString("it worked",ar ,5);
//		System.out.println(str);
		
//		soi1.increment();
//		System.out.println(soi1.getScore());
//		System.out.println(soi11.getScore());
//		soi11.increment();
//		System.out.println(soi11.getScore());
//		System.out.println("2: " + soi2.getStubScore(soi));
	}

}
