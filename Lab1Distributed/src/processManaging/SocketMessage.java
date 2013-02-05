package processManaging;

public abstract class SocketMessage extends Thread {

	final String messageTerminator = "ENDMESSAGE";
	//final String messageTerminator = "";
	
	final String startProcess = "STARTProcess";
	final String suspendProcess = "SUSPENDProcess";
	final String resumeProcess = "RESUMEProcess";
	final String receivedProcess = "RECEIVEDNewProcess";
	
	final String alive = "ALIVE";
	final String suspended = "SUSPENDED";
	final String started = "STARTED";
	final String quit = "quit";
	
	public String sendMessage(String inputStr){
		return inputStr + "\n" + messageTerminator;
	}
	
}
