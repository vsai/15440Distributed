package rmi_440.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import rmi_440.RemoteObjectReference;
import rmi_440.stubs.Stub;
import rmi_440.stubs.StubGenerator;

import messageProtocol.Message;

public class RegistryLookup { //extends Message {
	
	String registryIp;
	int registryPortnum;
	String messageType;
	StubGenerator stubGenerator;
		
	public RegistryLookup(String registryIp, int registryPortnum) {
		this.messageType = Message.messageTypeRegLookup;
		this.registryIp = registryIp;
		this.registryPortnum = registryPortnum;
		this.stubGenerator = new StubGenerator();
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
	
	public Stub lookup(String objName) {
		RemoteObjectReference ror = sendLookupMessage(objName);
		if (ror == null) {
			System.err.println("Registry Lookup from client returned null");
			return null;
		} else {
			return stubGenerator.createStub(ror);
		}
	}
	
//	public ClientStub lookup(String objName) {
//		RemoteObjectReference ror = sendLookupMessage(objName);
//		if (ror == null) {
//			System.err.println("Registry Lookup from client returned null");
//			return null;
//		} else if (ror.getInterfaceName().equals("ServerObj1Intf")){
//			//TODO: EXTEND FOR MORE OBJECT INTERFACES
//			return new ClientStub_ServerObj1Intf(ror);
//		} else if (ror.getInterfaceName().equals("ServerObj2Intf")){
//			return new ClientStub_ServerObj2Intf(ror);
//		}
//		return null;
//	}

}