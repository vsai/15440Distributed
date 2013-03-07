package rmi_440.stubs;

import messageProtocol.RMIMessageReturn;
import rmi_440.RemoteObjectReference;
import rmi_440.server.ServerObj1Intf;
import rmi_440.server.ServerObj2Intf;

public class Stub_ServerObj2Intf extends Stub implements ServerObj2Intf{

	public Stub_ServerObj2Intf(RemoteObjectReference ror) {
		super(ror);
	}

	@Override
	public int getStubScore(ServerObj1Intf a) {
		return a.getScore();
//		return 0;
	}

	@Override
	public int getValue2() {
		RMIMessageReturn returnObject= rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), null,null);
		
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
	public void incrementValue2() {
		RMIMessageReturn returnObject = rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), null,null);
		
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
}
