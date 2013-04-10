package hadoop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import messageProtocol.InitiateConnection;
import messageProtocol.Job;

public class Master extends Thread {

	String ipAddress;
	int portnum;
//	ArrayList<SlaveWrapper> slaves;
//	ArrayList<SlaveMessageHandler> slaves;
	List<SlaveMessageHandler> slaves;// = Collections.synchronizedList(new ArrayList<SlaveMessageHandler>());
	List<Job> jobs;
	
	public Master (String ipAddress, int portnum) {
		this.ipAddress = ipAddress;
		this.portnum = portnum;
		this.slaves = Collections.synchronizedList(new ArrayList<SlaveMessageHandler>());
		this.jobs = Collections.synchronizedList(new ArrayList<Job>());
	}
	
	public void run () {
		ServerSocket ss;
		Socket s;
		ObjectInputStream in;
		ObjectOutputStream out;
		InitiateConnection initConn;
		Job jobRequest;
		Object inobj;
		SlaveMessageHandler slave;
		try {
			ss = new ServerSocket(portnum);
			while (true) {
				try {
					s = ss.accept();
					in = new ObjectInputStream(s.getInputStream());
					out = new ObjectOutputStream(s.getOutputStream());
					try {
						inobj = in.readObject();
						if (inobj instanceof InitiateConnection) {
							initConn = (InitiateConnection) inobj;
							slave = new SlaveMessageHandler(initConn, s, in, out);
							slaves.add(slave);
							slave.start();
//							newSlave = new SlaveWrapper(initConn.getSelfIp(), initConn.getSelfPortnum());
//							newSlave.setConnToSlave(s);
//							newSlave.setIn(in);
//							newSlave.setOut(out);
//							slaves.add(newSlave);
						} else if (inobj instanceof Job) {
							jobRequest = (Job) inobj;
							jobs.add(jobRequest);
							out.writeBoolean(true);
							in.close();
							out.close();
							s.close();
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//scheduler + dispatcher
	}
}
