package hadoop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
	SlaveWrapper sw;
	ConcurrentHashMap<Job, List<MapMessage>> jobs;
	ConcurrentHashMap<SlaveWrapper, List<MapMessage>> slaves;
	
	public SlaveMessageHandler (ObjectInputStream in, SlaveWrapper sw,
			ConcurrentHashMap<Job, List<MapMessage>> jobs,
			ConcurrentHashMap<SlaveWrapper, List<MapMessage>> slaves) {
		this.in = in;
		this.sw = sw;
		this.jobs = jobs;
		this.slaves = slaves;
	}
	
	public void run() {
		Object inobj;
		while (true) {
			try {
				inobj = in.readObject();
				if (inobj instanceof MapResult) {
					System.out.println("received mapresult");
					MapResult mr = (MapResult) inobj;
					
					List<MapMessage> inslave = slaves.get(sw);
					for (MapMessage mm : inslave) {
						if (mm.getPartitionNum() == mr.getPartitionNum()) {
							inslave.remove(mm);
						}
					}
					
					for (Job j : jobs.keySet()) {
						if (j.getJobName().equals(mr.getJobName())) {
							List<MapMessage> injob = jobs.get(j);
							synchronized(injob) {
								for (MapMessage mm : injob) {
									if (mm.getPartitionNum() == mr.getPartitionNum()) {
										injob.remove(mm);
//										jobs.get(j).remove(mm);
										System.out.println("AFter remove:"+jobs.get(j).size());
										break;
									}
								}
							}
						}
					}
					//if failed MapResult
				} else if (inobj instanceof ReduceResult) {
					ReduceResult rr = (ReduceResult) inobj;
					
					for (Job j : jobs.keySet()) {
						if (j.getJobName().equals(rr.getJobName())) {
							j.setState(Job.State.ENDED);
						}
					}
					
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