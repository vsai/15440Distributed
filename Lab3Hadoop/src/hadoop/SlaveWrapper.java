package hadoop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import messageProtocol.Job;
import messageProtocol.MapMessage;

public class SlaveWrapper {

	public enum Status {
		CHILLIN, MAPPING, REDUCING
	}
	
	String ipAddress;
	int portnum;
	Status status;
	Socket s; 
	ObjectOutputStream out;
	SlaveMessageHandler smh;
	
	public SlaveWrapper (String ipAddress, int portnum) {
		this.ipAddress = ipAddress;
		this.portnum = portnum;
		this.status = Status.CHILLIN;

	}
	
	public SlaveWrapper (String ipAddress, int portnum, Socket s, ObjectInputStream in, 
			ObjectOutputStream out,
			ConcurrentHashMap<Job, List<MapMessage>> jobs,
			ConcurrentHashMap<SlaveWrapper, List<MapMessage>> slaves) {
		this.ipAddress = ipAddress;
		this.portnum = portnum;
		this.status = Status.CHILLIN;
		this.s = s;
		this.smh = new SlaveMessageHandler(in, this, jobs, slaves);
		this.out = out;
	}
		
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPortnum() {
		return portnum;
	}

	public void setPortnum(int portnum) {
		this.portnum = portnum;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public SlaveMessageHandler getSmh() {
		return smh;
	}

	public void setSmh(SlaveMessageHandler smh) {
		this.smh = smh;
	}

	public void writeToSlave(Object a) throws IOException {
		out.writeObject(a);
	}
}