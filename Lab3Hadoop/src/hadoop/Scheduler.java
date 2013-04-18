package hadoop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import fileIO.ConfigReader;
import fileIO.DirectoryHandler;
import fileIO.RecordReader;

import messageProtocol.Job;
import messageProtocol.MapMessage;
import messageProtocol.ReduceMessage;

public class Scheduler extends Thread {
	
	ConcurrentHashMap<Job, List<MapMessage>> jobs;
	ConcurrentHashMap<SlaveWrapper, List<MapMessage>> slaves;
	
	public Scheduler (ConcurrentHashMap<Job, List<MapMessage>> jobs, 
			ConcurrentHashMap<SlaveWrapper, List<MapMessage>> slaves) {
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
			perSlave = (int) Math.ceil(numLines / numSlaves);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.format("NumLines: %d, NumSlaves: %d, PerSlave: %d, StartSeek: %d\n", 
				numLines, numSlaves, perSlave, startSeek);
		
		String jobDir = "./" + ConfigReader.getTempmapfiles() + job.getJobName();
		boolean success = (new File(jobDir)).mkdirs();
		System.out.println("In scheduler, creating folder");
//		System.out.println(jobDir);
//		System.out.println(success);
		int partitionNumber = 0;
		
		for (SlaveWrapper sw : slaves.keySet()) {
//			System.out.println(sw);
			if (numLines > 0) {
				int partitionLines = ((numLines>perSlave) ? perSlave : numLines);
				System.out.format("Partition Number: %d\n", partitionNumber);
				System.out.format("Partition Lines: %d\n", partitionLines);
				System.out.format("StartSeek: %d\n", startSeek);
				MapMessage m = new MapMessage(startSeek, partitionLines, partitionNumber,
						job.getMapClass(), job.getMapURL(), job.getJobName(),
						job.getInputformat(), job.getInputFilename(), jobDir);
				jobs.get(job).add(m);
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
//			System.out.format("Numlines: %d\t", numLines);
//			System.out.format("StartSeek: %d\n", startSeek);
		}
		job.setState(Job.State.STARTED);
	}

	public void dispatchReduce(Job job) {

		DirectoryHandler dh = new DirectoryHandler();
		String destDir = ConfigReader.getResultfiles() + job.getJobName();
		String sourceDir = ConfigReader.getTempmapfiles() + job.getJobName();
		System.out.println("Destination Directory: " + destDir);
		System.out.println("Source Directory: " + sourceDir);
		dh.collectAllFiles(destDir, sourceDir, ".txt");
		
		// now for every file in the destDir, send a reduce job
		
		
		
		String pathfile = destDir;
		String classdirectory = job.getReduceURL();
		String classname = job.getReduceClass();
		String jobname = job.getJobName();
		
		for (SlaveWrapper sw : slaves.keySet()) {
			ArrayList<String> filenames = DirectoryHandler.getAllFiles(destDir, ".txt");
			ReduceMessage rm = new ReduceMessage(filenames, pathfile, classdirectory, classname, jobname);
			try {
				sw.writeToSlave(rm);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
		
		
	}


	public void run() {
		while (true) {
//			System.out.println("in sched");
			BufferedReader br = null;
			// clean away completed jobs
			int numJobsStarted = 0;
			for (Job j : jobs.keySet()) {
				//System.out.println("JobName: " + j.getJobName() + ", Number of partitions: " + jobs.get(j).size());
				if (j.getState().equals(Job.State.ENDED)) {
					jobs.remove(j);
				} else if (j.getState().equals(Job.State.INIT)) {
					System.out.println("DIDPATCHING MAP JOB");
					dispatchMap(j);
				} else if (j.getState().equals(Job.State.STARTED) && (jobs.get(j).size() == 0)) {
					System.out.println("DISPATCHING REDUCE JOB");
					dispatchReduce(j);
					j.setState(Job.State.REDUCING);
				}
			}
//			if (numJobsStarted == 1) {
//				for (Job j : jobs.keySet()) {
//					if (j.getState().equals(Job.State.INIT)){
//						
//						/* 
//						 * BREAK APART THE JOB AND DISPATCH TO EACH OF THE SLAVES
//						 */
//						String lines = null;
////						Iterator<SlaveWrapper> iter = slaves2.iterator();
//						SlaveWrapper nextSlave;
//						try {
//							br = new BufferedReader(new FileReader(j.getInputFilename()));
//							int count;
//							while((lines = br.readLine()) != null){
//								count =0;
//								while((lines +=br.readLine())!= null && count <4){
//									count++;
//								}
//								//line now has the break up
//								//send it to the slave
////								nextSlave= iter.next();
////								sendToSlave(lines, nextSlave, j.getInputformat());
////								nextSlave.setStatus(Status.MAPPING);
//							
//							}
//						}
//						catch (FileNotFoundException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
			
			// for 
			//check if there is a job to run
			//if there is, then dispatch it - i.e. write it to the slave
//		}


		}
	}
}