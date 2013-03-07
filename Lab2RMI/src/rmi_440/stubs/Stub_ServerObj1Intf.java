package rmi_440.stubs;

import rmi_440.RemoteObjectReference;
import rmi_440.server.ServerObj1Intf;
import messageProtocol.RMIMessageReturn;

public class Stub_ServerObj1Intf extends Stub implements ServerObj1Intf{
	
	public Stub_ServerObj1Intf(RemoteObjectReference ror) {
		super(ror);
	}
	
	@Override
	public String getMessage() throws Exception{
		RMIMessageReturn returnObject= rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), null,null);
		
		boolean c = returnObject.getIsCompleted();
		Object o = returnObject.getReturnObject();
		Exception e = returnObject.getExceptionThrown();
		
		if (c && e==null) {
			return (String)o;
		} else if (e != null) {
			throw e;			
		}
		return null;
	}

	@Override
	public int getScore() throws Exception{
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
	public void increment() throws Exception{
		RMIMessageReturn returnObject = rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), null,null);
		
		boolean c = returnObject.getIsCompleted();
		Object o = returnObject.getReturnObject();
		Exception e = returnObject.getExceptionThrown();
		
		if (c && o==null && e==null){
			return;
		} else if (e != null) { 
			throw e;
		}
		return;
	}

	@Override
	public String returnSameString(String str, int [] ar, int num) throws Exception{
		Object [] args={str,ar,num};
		Class<?>[] classArgs= {String.class,int[].class,int.class};
		RMIMessageReturn returnObject = rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), args,classArgs);
		
		boolean c = returnObject.getIsCompleted();
		Object o = returnObject.getReturnObject();
		Exception e = returnObject.getExceptionThrown();
		
		if (c && o!=null && e==null){
			return (String)o;
		} else if (e != null) {
			throw e;
		}
		return null;
	}

	@Override
	public void setScore(int newScore) throws Exception {
		Object [] args={newScore};
		Class<?>[] classArgs= {int.class};
		RMIMessageReturn returnObject = rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), args,classArgs);
		
		boolean c = returnObject.getIsCompleted();
		Object o = returnObject.getReturnObject();
		Exception e = returnObject.getExceptionThrown();

		if (c && e==null){
			return;
		} else if (e != null) {
			throw e;
		}
		return;
	}
}