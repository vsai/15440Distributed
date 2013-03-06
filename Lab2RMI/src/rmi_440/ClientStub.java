package rmi_440;

public abstract class ClientStub {

	RemoteObjectReference ror;
	RMIMessageHandler rmiMessageHandler;
	
	public ClientStub(RemoteObjectReference ror){
		this.ror = ror;
		this.rmiMessageHandler = new RMIMessageHandler();
	}
	
	public RemoteObjectReference getRemoteObjectReference() {
		return ror;
	}
}
