//package rmi_440.client;
//
//import rmi_440.RemoteObjectReference;
//
//public abstract class ClientStub {
//
//	RemoteObjectReference ror;
//	RMIMessageHandler rmiMessageHandler;
//	
//	public ClientStub(RemoteObjectReference ror){
//		this.ror = ror;
//		this.rmiMessageHandler = new RMIMessageHandler();
//	}
//	
//	public RemoteObjectReference getRemoteObjectReference() {
//		return ror;
//	}
//	
//	public String getCurrentMethodName(){
//		return Thread.currentThread().getStackTrace()[2].getMethodName();
//	}	
//}
