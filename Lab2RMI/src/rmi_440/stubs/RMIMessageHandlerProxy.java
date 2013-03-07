package rmi_440.stubs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import rmi_440.RemoteObjectReference;

import messageProtocol.RMIMessage;
import messageProtocol.RMIMessageReturn;

/*
 * Creates a RMI Message, and receives back and RMIMessageReturn
 */
public class RMIMessageHandlerProxy { //extends message?
		public RMIMessageReturn sendInvocation(RemoteObjectReference ror, String m, Object[] args, Class<?>[] classArgs) {
			if (ror == null) {
				System.err.println("In sendInvocation: ror is NULL");
			}
			
			//Converts stubs into their RemoteObjectReferences when sending the request
			if(args != null){
				for (int i = 0; i < args.length; i++) {
					Object argument = args[i];
					if (argument instanceof Stub) { //TODO or is it instanceof Remote440
						args[i] = ((Stub)argument).getRemoteObjectReference();
					}
				}
			}
			
			Socket toServer;
			ObjectOutputStream out;
			ObjectInputStream in;

			RMIMessage message = new RMIMessage(ror.getObjectName(), m, args,classArgs);
			try {
				toServer = new Socket(ror.getServerIp(), ror.getPortnum());
				out = new ObjectOutputStream(toServer.getOutputStream());
				in = new ObjectInputStream(toServer.getInputStream());
				out.writeObject(message);
				out.flush();

				Object ret;
				try {
					ret = in.readObject();
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