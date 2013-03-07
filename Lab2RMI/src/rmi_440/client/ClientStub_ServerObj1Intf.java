//package rmi_440.client;
//
//import rmi_440.RemoteObjectReference;
////import rmi_440.Remote440_Exception;
//import rmi_440.server.ServerObj1Intf;
//import messageProtocol.RMIMessageReturn;
//
//public class ClientStub_ServerObj1Intf extends ClientStub implements ServerObj1Intf{
//	
//	public ClientStub_ServerObj1Intf(RemoteObjectReference ror) {
//		super(ror);
//	}
//	
//	@Override
//	public String getMessage() {
//		RMIMessageReturn returnObject= rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), null,null);
//		
//		boolean c = returnObject.getIsCompleted();
//		Object o = returnObject.getReturnObject();
//		Exception e = returnObject.getExceptionThrown();
//		
//		if (c) {
//			return (String)o;
//		} else if (e != null) {
////			throw e;			
//		}
//		return null;
//	}
//
//	@Override
//	public int getScore() {
//		RMIMessageReturn returnObject= rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), null,null);
//		
//		boolean c = returnObject.getIsCompleted();
//		Object o = returnObject.getReturnObject();
//		Exception e = returnObject.getExceptionThrown();
//
//		if (c) {
//			return (Integer)o;
//		} else if (e != null) {
////			throw e;			
//		}
//		return -1;
//	}
//
//	@Override
//	public void increment() {
//		RMIMessageReturn returnObject = rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), null,null);
//		
//		boolean c = returnObject.getIsCompleted();
//		Object o = returnObject.getReturnObject();
//		Exception e = returnObject.getExceptionThrown();
//		
//		if (c){
//			return;
//		} else if (e != null) {
////			throw e;
//		}
//		return;
//	}
//
//	@Override
//	public String returnSameString(String str, int [] ar, int num) {
//		Object [] args={str,ar,num};
//		
//		Class [] classArgs= {String.class,int[].class,int.class};
//		RMIMessageReturn returnObject = rmiMessageHandler.sendInvocation(ror, getCurrentMethodName(), args,classArgs);
//		
//		boolean c = returnObject.getIsCompleted();
//		Object o = returnObject.getReturnObject();
//		Exception e = returnObject.getExceptionThrown();
//		System.out.println(c);
//		if (c){
//			return (String)o;
//		} else if (e != null) {
////			throw e;
//		}
//		return "";
//	}
//}