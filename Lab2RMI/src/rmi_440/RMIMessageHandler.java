package rmi_440;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;

import messageProtocol.Message;
import messageProtocol.RMIMessage;
import messageProtocol.RMIMessageReturn;

public class RMIMessageHandler extends Message {

	public RMIMessageHandler() {
		messageType = "RMIMessage";
	}
		
		public RMIMessageReturn sendInvocation(RemoteObjectReference ror, String m, Object[] args) {
		Socket toServer;
		ObjectOutputStream out;
		ObjectInputStream in;
		RMIMessage message = new RMIMessage(ror.getObjectName(), m, args);
		try {
			toServer = new Socket(ror.getServerIp(), ror.getPortnum());
			out = new ObjectOutputStream(toServer.getOutputStream());
			in = new ObjectInputStream(toServer.getInputStream());
			out.writeObject(message);
			out.flush();
			try {
				Object ret = in.readObject();
				return (RMIMessageReturn) ret;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
