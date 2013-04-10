package hadoop;

import java.util.List;
import messageProtocol.Job;

public class Scheduler extends Thread {
	
	List<Job> jobs;
	List<SlaveMessageHandler> slaves;
	
	public Scheduler (List<Job> jobs, List<SlaveMessageHandler> slaves) {
		this.jobs = jobs;
		this.slaves = slaves;
	}

	public void run() {
		while (true) {
			//check if there is a job to run
			//if there is, then dispatch it - i.e. write it to the slave
		}
	}
}
