package rmi_440;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import messageProtocol.RMIMessage;
import messageProtocol.RMIMessageReturn;

public class RMIMessageHandler { //extends message?
		public RMIMessageReturn sendInvocation(RemoteObjectReference ror, String m, Object[] args, Class[] classArgs) {
			if (ror == null) {
				System.err.println("In sendInvocation: ror is NULL");
			}
			
			Socket toServer;
			ObjectOutputStream out;
			ObjectInputStream in;
			//check if any args are a ServerObjIntf
			
			RMIMessage message = new RMIMessage(ror.getObjectName(), m, args,classArgs);
			try {
				toServer = new Socket(ror.getServerIp(), ror.getPortnum());
				out = new ObjectOutputStream(toServer.getOutputStream());
				in = new ObjectInputStream(toServer.getInputStream());
				out.writeObject(message);
				out.flush();
//				System.out.println("In RMIMessageHandler");
//				System.out.println(toServer);
//				System.out.println(out);
//				System.out.println(in);
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
