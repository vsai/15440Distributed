package rmi_440;


public class Client {

	
	public static void main(String[] args) {
		String registry_ip=Settings.registry_ipAddress;
		int registry_port=Settings.registry_listeningToClientPortnum;
		RegistryLookup registry = new RegistryLookup(registry_ip,registry_port);
		
		ServerObjIntf soi = (ServerObjIntf) registry.lookup("obj1");
		
		int score = soi.getScore();
		String mess = soi.getMessage();
		soi.increment();
		int finalScore = soi.getScore();
		
		System.out.printf("Init score: %d, Final score: %d, Message: %s\n", score, finalScore, mess);
		
	}

}
