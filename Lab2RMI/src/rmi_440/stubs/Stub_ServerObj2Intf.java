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
	public int getStubScore(ServerObj1Intf a) throws Exception {
		Object [] args={a};
		Class<?>[] classArgs= {ServerObj1Intf.class};
		RMIMessageReturn returnObject= rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), args, classArgs);
		
		boolean c = returnObject.getIsCompleted();
		Object o = returnObject.getReturnObject();
		Exception e = returnObject.getExceptionThrown();

		if (c && e==null) {
			return (Integer)o;
		} else if (e != null) {
			throw e;
		}
		return -1;
	}

	@Override
	public int getValue2() throws Exception{
		RMIMessageReturn returnObject= rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), null,null);
		
		boolean c = returnObject.getIsCompleted();
		Object o = returnObject.getReturnObject();
		Exception e = returnObject.getExceptionThrown();

		if (c && e==null) {
			return (Integer)o;
		} else if (e != null) {
			throw e;
		}
		return -1;
	}

	@Override
	public void incrementValue2() throws Exception{
		RMIMessageReturn returnObject = rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), null,null);
		
		boolean c = returnObject.getIsCompleted();
		Object o = returnObject.getReturnObject();
		Exception e = returnObject.getExceptionThrown();
		
		if (c && o==null && e==null){
			assert(o== null);
			return;
		} else if (e != null) {
			throw e;
		}
		return;
	}

	@Override
	public void setStubScore(ServerObj1Intf a, int newScore) throws Exception {
		Object [] args={a, newScore};
		Class<?>[] classArgs= {ServerObj1Intf.class, int.class};
		RMIMessageReturn returnObject = rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), args,classArgs);
		
		boolean c = returnObject.getIsCompleted();
		Object o = returnObject.getReturnObject();
		Exception e = returnObject.getExceptionThrown();
		
		if (c && o!=null && e==null){
			return;
		} else if (e != null) {
			throw e;
		}
		return;
	}
}