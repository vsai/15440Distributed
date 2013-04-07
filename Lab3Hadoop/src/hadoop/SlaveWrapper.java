package hadoop;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SlaveWrapper {

	public enum Status {
		CHILLIN, MAPPING, REDUCING
	}
	
	String ipAddress;
	int portnum;
	Socket connToSlave;
	ObjectOutputStream out;
	ObjectInputStream in;
	Status status;
	
	public SlaveWrapper (String ipAddress, int portnum) {
		this.ipAddress = ipAddress;
		this.portnum = portnum;
		this.status = Status.CHILLIN;
	}

	public Socket getConnToSlave() {
		return connToSlave;
	}

	public void setConnToSlave(Socket connToSlave) {
		this.connToSlave = connToSlave;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}