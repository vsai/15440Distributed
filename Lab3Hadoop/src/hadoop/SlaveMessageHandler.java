package hadoop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import messageProtocol.InitiateConnection;
import messageProtocol.Job;
import messageProtocol.MapMessage;
import messageProtocol.MapResult;
import messageProtocol.ReduceResult;

/***
 * Created on the master, and is basically used for handling messages between
 * the master and the slave.
 * Overall: Create a connection, send a map job, read back a result success, send result job, read back success 
 */

public class SlaveMessageHandler extends Thread{

	ObjectInputStream in;
	ConcurrentHashMap<Job, ArrayList<MapMessage>> jobs;
	ConcurrentHashMap<SlaveWrapper, ArrayList<MapMessage>> slaves;
	
	public SlaveMessageHandler (ObjectInputStream in, 
			ConcurrentHashMap<Job, ArrayList<MapMessage>> jobs,
			ConcurrentHashMap<SlaveWrapper, ArrayList<MapMessage>> slaves) {
		this.in = in;
		this.jobs = jobs;
		this.slaves = slaves;
	}
	
	public void run() {
		Object inobj;
		while (true) {
			try {
				inobj = in.readObject();
				if (inobj instanceof MapResult) {
					//if successful MapResult
					//remove map message from job
					//remove map message from slave
					
					//if failed MapResult
				} else if (inobj instanceof ReduceResult) {
					//set job state to ended in jobs hashmap (scheduler will clean it out)
					
				}
			} catch (IOException e) {
				/* 
				 * Usually throws this exception when a broken stream happens
				 * Recover this by re-writing the original partition sent to
				 * a different slave 
				 */
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}