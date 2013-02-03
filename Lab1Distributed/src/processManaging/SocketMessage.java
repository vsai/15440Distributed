package processManaging;

public abstract class SocketMessage extends Thread {

	String messageTerminator = "\nENDMESSAGE";
	
	String startProcess = "STARTProcess";
	String suspendProcess = "SUSPENDProcess";
	String resumeProcess = "RESUMEProcess";
	String receivedProcess = "RECEIVEDNewProcess";
	
	String alive = "ALIVE";
	String suspended = "SUSPENDED";
	String quit = "quit";
	
	public String sendMessage(String inputStr){
		return inputStr + messageTerminator;
	}
	
}
