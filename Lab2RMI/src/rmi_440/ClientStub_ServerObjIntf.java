package rmi_440;

import messageProtocol.RMIMessageReturn;

public class ClientStub_ServerObjIntf extends ClientStub implements ServerObjIntf{
	
	public ClientStub_ServerObjIntf(RemoteObjectReference ror) {
		super(ror);
	}
	
	public String getCurrentMethodName(){
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
	
	@Override
	public String getMessage() {
		RMIMessageReturn returnObject= rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), null);
		
		boolean c = returnObject.getIsCompleted();
		Object o = returnObject.getReturnObject();
		Exception e = returnObject.getExceptionThrown();
		
		if (c) {
			return (String)o;
		} else if (e != null) {
//			throw e;			
		}
		return null;
	}

	@Override
	public int getScore() {
		RMIMessageReturn returnObject= rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), null);
		
		boolean c = returnObject.getIsCompleted();
		Object o = returnObject.getReturnObject();
		Exception e = returnObject.getExceptionThrown();

		if (c) {
			return (Integer)o;
		} else if (e != null) {
//			throw e;			
		}
		return -1;
	}

	@Override
	public void increment() {
		RMIMessageReturn returnObject = rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), null);
		
		boolean c = returnObject.getIsCompleted();
		Object o = returnObject.getReturnObject();
		Exception e = returnObject.getExceptionThrown();
		
		if (c){
			return;
		} else if (e != null) {
//			throw e;
		}
		return;
	}

	@Override
	public String returnSameString(String str, String secondStr, int num) {
		Object [] args={str,secondStr,num};
		RMIMessageReturn returnObject = rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), args);
		
		boolean c = returnObject.getIsCompleted();
		Object o = returnObject.getReturnObject();
		Exception e = returnObject.getExceptionThrown();
		if (c){
			return (String)o;
		} else if (e != null) {
//			throw e;
		}
		return "";
	}
	
	
}