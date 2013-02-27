package rmi_440;

import messageProtocol.*;

public class Client {

	
	public static void main(String[] args) {
		String registry_ip=Settings.registry_ipAddress;
		int registry_port=Settings.registry_listeningToClientPortnum;
		RegistryLookup registry = new RegistryLookup(registry_ip,registry_port);
		
		RemoteObjectReference testObj= registry.sendLookupMessage("obj1");
		RMIMessageHandler rmiMessageHandler = new RMIMessageHandler();
		RMIMessageReturn returnObject= rmiMessageHandler.sendInvocation(testObj, "getScore", null);
		
	}

}
