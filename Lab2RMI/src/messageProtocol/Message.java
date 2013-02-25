package messageProtocol;

import java.io.Serializable;

public abstract class Message implements Serializable {

	protected String messageType;
	public static String TERMINATOR = "ENDMESSAGE";
	public static String messageTypeRegLookup = "RegistryLookup";
	
	public String createMessage(String[] lines) {
		String message = messageType + "\n";
		for (String line : lines){
			message += line + "\n";
		}
		message += TERMINATOR;
		return message;
	}
		
}
