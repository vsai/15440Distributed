package rmi_440;

import messageProtocol.RMIMessageReturn;

public class ClientStub_ServerObjIntf extends ClientStub implements ServerObjIntf{

	RemoteObjectReference ror;
	RMIMessageHandler rmiMessageHandler;
	
	public ClientStub_ServerObjIntf(RemoteObjectReference ror) {
		this.ror = ror;
		this.rmiMessageHandler = new RMIMessageHandler();
	}
	
	public String getCurrentMethodName(){
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
	
	@Override
	public String getMessage() {
		RMIMessageReturn returnObject= rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), null);
		if (returnObject.getIsCompleted()) {
			return (String)returnObject.getReturnObject();
		}
		return null;
	}

	@Override
	public int getScore() {
		return 0;
	}

	@Override
	public void increment() {
		
	}

}
