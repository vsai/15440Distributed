package rmi_440;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import messageProtocol.Message;


public class RegistryLookup { //extends Message {
	
	String registryIp;
	int registryPortnum;
	String messageType;
		
	public RegistryLookup(String registryIp, int registryPortnum) {
		this.messageType = Message.messageTypeRegLookup;
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
	
	public ClientStub lookup(String objName) {
		RemoteObjectReference ror = sendLookupMessage(objName);
		if (ror == null) {
			return null;
		} else if (ror.getInterfaceName().equals("ServerObjIntf")){
			return new ClientStub_ServerObjIntf(ror);
		}
		return null;
	}
	
}