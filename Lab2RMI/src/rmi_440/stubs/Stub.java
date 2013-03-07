package rmi_440.stubs;

import rmi_440.RemoteObjectReference;

public abstract class Stub {

	RemoteObjectReference ror;
	RMIMessageHandlerProxy rmiMessageHandler;
	
	public Stub(RemoteObjectReference ror){
		this.ror = ror;
		this.rmiMessageHandler = new RMIMessageHandlerProxy();
	}
	
	public RemoteObjectReference getRemoteObjectReference() {
		return ror;
	}
	
	public String getCurrentMethodName(){
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}	
}
