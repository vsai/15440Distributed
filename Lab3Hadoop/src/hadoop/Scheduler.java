package hadoop;

import hadoop.SlaveWrapper.Status;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import messageProtocol.Job;
import messageProtocol.Job.InputType;

public class Scheduler extends Thread {
	
	List<Job> jobs;
	List<SlaveWrapper> slaves;
	SOList slaves2;
	boolean [] salvesBusy;
	
	public Scheduler (List<Job> jobs, List<SlaveWrapper> slaves) {
		this.jobs = jobs;
		this.slaves = slaves;
		this.slaves2= new SOList(slaves);
	}
	
	public void dispatch(Job job) {
		/* Create a MapMessage partition and write it to each of the slaves*/
		
	}

	private void sendToSlave(String lines, SlaveWrapper sw,
			InputType inputformat) {
		// TODO Auto-generated method stub
		
	}
	public void run() {
		while (true) {
			BufferedReader br = null;
			// clean away completed jobs
			int numJobsStarted = 0;
			for (Job j : jobs) {
				if (j.getState().equals(Job.State.ENDED)) {
					jobs.remove(j);
				} else if (j.getState().equals(Job.State.STARTED)) {
					System.out.println("Jobs running: " + j.getJobName());
					numJobsStarted+=1;
				}
			}
			if (numJobsStarted == 1) {
				for (Job j : jobs) {
					if (j.getState().equals(Job.State.INIT)){
						
						/* 
						 * BREAK APART THE JOB AND DISPATCH TO EACH OF THE SLAVES
						 */
						String lines = null;
						Iterator<SlaveWrapper> iter = slaves2.iterator();
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
								nextSlave= iter.next();
								sendToSlave(lines, nextSlave, j.getInputformat());
								nextSlave.setStatus(Status.MAPPING);
							
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