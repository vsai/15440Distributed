package processManaging;

public abstract class SocketMessage extends Thread {

	final static String messageTerminator = "ENDMESSAGE";
	//final String messageTerminator = "";
	
	final static String startProcess = "STARTProcess";
	final static String suspendProcess = "SUSPENDProcess";
	final static String resumeProcess = "RESUMEProcess";
	final static String receivedProcess = "RECEIVEDNewProcess";
	
	final static String suspendALL = "SUSPENDALL";
	
	final static String messageFrom = "MESSAGEFROM";
	
	final String alive = "ALIVE";
	final String suspended = "SUSPENDED";
	final String started = "STARTED";
	final String quit = "quit";
	
	final String error = "ERROR:";
	
	public static String sendMessage(String inputStr){
		return inputStr + "\n" + messageTerminator;
	}
	
}
