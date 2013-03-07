package rmi_440.stubs;

import rmi_440.RemoteObjectReference;

public class StubGenerator {

	public StubGenerator() {		
	}
	
	public Stub createStub(RemoteObjectReference ror) {
		if (ror.getInterfaceName().equals("ServerObj1Intf")) {
			return new Stub_ServerObj1Intf(ror);
		} else if (ror.getInterfaceName().equals("ServerObj2Intf")) {
			return new Stub_ServerObj2Intf(ror);
		}
		
		return null;
	}
}