package hadoop;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import messageProtocol.Job;
import messageProtocol.MapMessage;

public class Scheduler extends Thread {
	
	ConcurrentHashMap<Job, ArrayList<MapMessage>> jobs;
	ConcurrentHashMap<SlaveWrapper, ArrayList<MapMessage>> slaves;
	
	public Scheduler (ConcurrentHashMap<Job, ArrayList<MapMessage>> jobs, 
			ConcurrentHashMap<SlaveWrapper, ArrayList<MapMessage>> slaves) {
		this.jobs = jobs;
		this.slaves = slaves;
	}
	
	public void dispatch(Job job) {
		/* Create a MapMessage partition and write it to each of the slaves*/
	}


	public void run() {
		while (true) {
			BufferedReader br = null;
			// clean away completed jobs
			int numJobsStarted = 0;
			for (Job j : jobs.keySet()) {
				if (j.getState().equals(Job.State.ENDED)) {
					jobs.remove(j);
				} else if (j.getState().equals(Job.State.STARTED)) {
					System.out.println("Jobs running: " + j.getJobName());
					numJobsStarted+=1;
				}
			}
			if (numJobsStarted == 1) {
				for (Job j : jobs.keySet()) {
					if (j.getState().equals(Job.State.INIT)){
						
						/* 
						 * BREAK APART THE JOB AND DISPATCH TO EACH OF THE SLAVES
						 */
						String lines = null;
//						Iterator<SlaveWrapper> iter = slaves2.iterator();
						SlaveWrapper nextSlave;
						try {
							br = new BufferedReader(new FileReader(j.getInputFilename()));
							int count;
							while((lines = br.readLine()) != null){
								count =0;
								while((lines +=br.readLine())!= null && count <4){
									count++;
								}
								//line now has the break up
								//send it to the slave
//								nextSlave= iter.next();
//								sendToSlave(lines, nextSlave, j.getInputformat());
//								nextSlave.setStatus(Status.MAPPING);
							
							}
						}
						catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			
			// for 
			//check if there is a job to run
			//if there is, then dispatch it - i.e. write it to the slave
		}


		}
	}
}