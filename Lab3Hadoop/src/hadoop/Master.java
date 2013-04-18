package hadoop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import messageProtocol.InitiateConnection;
import messageProtocol.Job;
import messageProtocol.MapMessage;

public class Master extends Thread {

	/*
	 * Master is responsible for listening to incoming connections, and handles
	 * new job requests, and new slaves joining.
	 * 
	 * It spawns a Scheduler thread which is responsible for handling the messages
	 * to each of the slaves, and handles completion of jobs. 
	 */
	
	String ipAddress;
	int portnum;
	ConcurrentHashMap<SlaveWrapper, ArrayList<MapMessage>> slaves;
	ConcurrentHashMap<Job, ArrayList<MapMessage>> jobs;
	ConcurrentHashMap<SlaveWrapper, Thread> listeners;
	Scheduler scheduler;
	
	public Master (String ipAddress, int portnum) {
		this.ipAddress = ipAddress;
		this.portnum = portnum;
		this.slaves = new ConcurrentHashMap<SlaveWrapper, ArrayList<MapMessage>>();
		this.jobs = new ConcurrentHashMap<Job, ArrayList<MapMessage>>();
		this.listeners = new ConcurrentHashMap<SlaveWrapper, Thread>();
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
					System.out.println("go");
					s = ss.accept();
					System.out.println("go1");
					System.out.println(s);
					in = new ObjectInputStream(s.getInputStream());
					System.out.println("go2");
					out = new ObjectOutputStream(s.getOutputStream());
					System.out.println("go3");
					try {
						inobj = in.readObject();
						if (inobj instanceof InitiateConnection) {
							System.out.println("INITIATING CONECTION");
							initConn = (InitiateConnection) inobj;
							slave = new SlaveWrapper(initConn.getSelfIp(), initConn.getSelfPortnum(), 
													s, in, out, jobs, slaves);
							slaves.put(slave, new ArrayList<MapMessage>());
							slave.getSmh().start(); /* start master's listener from slave */
						} else if (inobj instanceof Job) {
							System.out.println("Received a job");
							jobRequest = (Job) inobj;
							jobs.put(jobRequest, new ArrayList<MapMessage>());
//							out.writeBoolean(true);
//							out.flush();
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