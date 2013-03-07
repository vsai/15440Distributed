package rmi_440.client;

import rmi_440.Settings;
import rmi_440.server.ServerObj1Intf;
import rmi_440.server.ServerObj2Intf;

public class Client {
	
	public static void clientDoStuff() throws Exception{
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
		
		System.out.println("THESE CHECKS WILL ONLY WORK WHEN INITIALIZING SERVER AND REGISTRY");
		
		int[] ar = {1,2,3};
		System.out.println("Expected: it worked ; 1 2 3 ; 5 ;, Actual: " + soi.returnSameString("it worked", ar, 5));
		int[] ars = {3,4,5,6,7};
		System.out.println("Expected: working ; 3 4 5 6 7 ; 6 ;, Actual: " + soi.returnSameString("working", ars, 6));
		
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
		
		soi2.incrementValue2();
		soi1.increment();
		
		System.out.println("Expected: 12, Actual: " + soi.getScore());
		System.out.println("Expected: 12, Actual: " + soi1.getScore());
		System.out.println("Expected: 10, Actual: " + soi11.getScore());
		System.out.println("Expected: 12, Actual: " + soi2.getStubScore(soi));
		System.out.println("Expected: 12, Actual: " + soi2.getStubScore(soi1));
		System.out.println("Expected: 6, Actual: " + soi2.getValue2());
		
		soi2.setStubScore(soi, 3);
		
		System.out.println("Expected: 3, Actual: " + soi.getScore());
		System.out.println("Expected: 3, Actual: " + soi1.getScore());
		System.out.println("Expected: 10, Actual: " + soi11.getScore());
		System.out.println("Expected: 3, Actual: " + soi2.getStubScore(soi));
		System.out.println("Expected: 3, Actual: " + soi2.getStubScore(soi1));
		System.out.println("Expected: 6, Actual: " + soi2.getValue2());
		
	}
	
	public static void main(String[] args) {
		try {
			clientDoStuff();
		} catch (Exception e) {
			System.err.println("Received an exception on remote objects");
			e.printStackTrace();
		}
		
	}
}