package rmi_440;

import java.util.concurrent.ConcurrentHashMap;

public class Registry {

	static ConcurrentHashMap<String, RemoteObjectReference> registryStore;
	static RegistryClientListener clientListen;
	static RegistryServerListener serverListen;
	
	public static void main(String args[]){
		int clientLPortnum = 12344;
		int serverLPortnum = 12345;
		
		clientListen = new RegistryClientListener(clientLPortnum, registryStore);
		serverListen = new RegistryServerListener(serverLPortnum, registryStore);
		
		clientListen.start();
		serverListen.start();
	}
	
}
