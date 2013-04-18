package hadoop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import fileIO.RecordReader;

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
	
	public void dispatchMap(Job job) {
		/* Create a MapMessage partition and write it to each of the slaves*/
		System.out.println("In dispatchMap");
		RecordReader jobInput = new RecordReader(job.getInputFilename());
		int numLines = 0;
		int numSlaves = slaves.keySet().size();
		int perSlave = 0;
		int startSeek = 0;
		try {
			numLines = jobInput.fileNumberOfLines();
			perSlave = (numLines / numSlaves) + 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String jobDir = "./" + job.getJobName();
		boolean success = (new File(jobDir)).mkdirs();
		System.out.println("In scheduler, creating folder");
		System.out.println(jobDir);
		System.out.println(success);
		int partitionNumber = 0;
		
		for (SlaveWrapper sw : slaves.keySet()) {
			System.out.println(sw);
			if (numLines > 0) {
				int partitionLines = ((numLines>perSlave) ? perSlave : numLines);
				MapMessage m = new MapMessage(startSeek, partitionLines, partitionNumber,
						job.getMapClass(), job.getMapURL(), job.getJobName(),
						job.getInputformat(), job.getInputFilename(), jobDir);
								
				try {
					sw.writeToSlave(m);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				numLines -= partitionLines;
				startSeek += partitionLines;
			}
			partitionNumber +=1;
		}
		job.setState(Job.State.STARTED);
	}

	public void dispatchReduce(Job job) {
		
	}


	public void run() {
		while (true) {
//			System.out.println("in sched");
			BufferedReader br = null;
			// clean away completed jobs
			int numJobsStarted = 0;
			for (Job j : jobs.keySet()) {

				if (j.getState().equals(Job.State.ENDED)) {
					jobs.remove(j);
				} else if (j.getState().equals(Job.State.INIT)) {
					dispatchMap(j);
				} else if (j.getState().equals(Job.State.STARTED) && (jobs.get(j).size() == 0)) {
					//dispatchReduce
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