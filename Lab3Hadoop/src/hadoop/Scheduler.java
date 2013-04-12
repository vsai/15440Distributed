package hadoop;

import java.util.List;
import messageProtocol.Job;

public class Scheduler extends Thread {
	
	List<Job> jobs;
	List<SlaveWrapper> slaves;
	
	public Scheduler (List<Job> jobs, List<SlaveWrapper> slaves) {
		this.jobs = jobs;
		this.slaves = slaves;
	}
	
	public void dispatch(Job job) {
		/* Create a MapMessage partition and write it to each of the slaves*/
		
	}

	public void run() {
		while (true) {
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
						
						for (SlaveWrapper sw : slaves) {
							
						}
						
						break;
					}
				}
			}
			
			// for 
			//check if there is a job to run
			//if there is, then dispatch it - i.e. write it to the slave
		}
	}
}