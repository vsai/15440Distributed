package messageProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import rmi_440.RemoteObjectReference;

/*
 * NOT NEEDED ANYMORE - it has been simplified into a 1-line message:
 * 			RegistryLookup objName
 */

public class RegistryLookup extends Message {
	
	String registryIp;
	int registryPortnum;
		
	public RegistryLookup(String registryIp, int registryPortnum) {
		messageType = Message.messageTypeRegLookup;
		this.registryIp = registryIp;
		this.registryPortnum = registryPortnum;
	}
	
	public String lookupMessage(String objName) {
		return messageType + " " + objName;
	}
	
	public RemoteObjectReference sendLookupMessage(String objName) {
		Socket toReg;
		PrintStream out;
		ObjectInputStream in;
		
		try {
			toReg = new Socket(registryIp, registryPortnum);
			out = new PrintStream(toReg.getOutputStream());
		    in = new ObjectInputStream(toReg.getInputStream());
			out.println(lookupMessage(objName));
			out.flush();
			return (RemoteObjectReference) in.readObject();			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String lookup(String objName){
		/*
		 * Create socket connection with registry. Send message via sockets
		 * Receive serialized object
		 */
		String[] messageArgs = objName.split("\n");
		return createMessage(messageArgs);
	}
}