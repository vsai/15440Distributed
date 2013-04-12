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
	List<SlaveWrapper> slaves;
	List<Job> jobs;
	Scheduler scheduler;
	
	public Master (String ipAddress, int portnum) {
		this.ipAddress = ipAddress;
		this.portnum = portnum;
		this.slaves = Collections.synchronizedList(new ArrayList<SlaveWrapper>());
		this.jobs = Collections.synchronizedList(new ArrayList<Job>());
		this.scheduler = new Scheduler(jobs, slaves);
	}
	
	public void run () {
		ServerSocket ss;
		Socket s;
		ObjectInputStream in;
		ObjectOutputStream out;
		InitiateConnection initConn;
		Job jobRequest;
		Object inobj;
		SlaveWrapper slave;
		
		scheduler.start();
		
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
							slave = new SlaveWrapper(initConn.getSelfIp(), initConn.getSelfPortnum(), s, in, out);
							slaves.add(slave);
							slave.getSmh().start(); /* to listen to inputs from Slave */
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
	}
}