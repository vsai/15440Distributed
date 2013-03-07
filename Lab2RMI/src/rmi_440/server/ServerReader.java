package rmi_440.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

import messageProtocol.RMIMessage;
import messageProtocol.RMIMessageReturn;
import rmi_440.RemoteObjectReference;
import rmi_440.stubs.StubGenerator;

public class ServerReader extends Thread{

	Socket clientConn;
	
	public ServerReader(Socket clientConn) {
		this.clientConn = clientConn;
	}
	
	public void run() {
		ObjectInputStream in;
		ObjectOutputStream out;
		RMIMessage rmiMess;
		RMIMessageReturn rmiRet;
		Object objInvoke;
		StubGenerator stubGenerator = new StubGenerator();
		
		try {
			in = new ObjectInputStream(clientConn.getInputStream());
			out = new ObjectOutputStream(clientConn.getOutputStream());
			rmiMess = (RMIMessage) in.readObject();
			System.out.println("In Server: Obj to invoke: " + rmiMess.getObjectName());
			objInvoke = Server.serverObjectStore.get(rmiMess.getObjectName());
			
			String m = rmiMess.getMethod();
			Object[] argus = rmiMess.getArguments();
			Class<?>[] classArgs = rmiMess.getClassArguments();
			
			/*
			 * Converts RemoteObjectReferences into their respective stubs
			 */
			if (argus!=null) {
				for (int i = 0; i < argus.length; i++) {
					if (argus[i] instanceof RemoteObjectReference) {
						argus[i] = stubGenerator.createStub((RemoteObjectReference)argus[i]);
					}							
				}
			}					
			
			boolean completed = false;
			Object result = null;
			Exception ex = null;
			
			Method meth = null;
			try {
				meth=objInvoke.getClass().getDeclaredMethod(m, classArgs);
				result = meth.invoke(objInvoke,argus);
				System.out.println("result is:"+result);
				completed = true;
				System.out.println("method is completed:"+completed);

			} catch (IllegalArgumentException e) {
				ex = e;
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				ex = e;
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				ex = e;
				e.printStackTrace();
			} catch (Exception e) {
				ex = e;
				e.printStackTrace();
			}
			rmiRet = new RMIMessageReturn(completed, result, ex);
			out.writeObject(rmiRet);
			out.flush();
			in.close();
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
				
	}
}
