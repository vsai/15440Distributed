package hadoop;

import java.io.File;
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
			e.printStackTrace();
		}
		System.out.format("NumLines: %d, NumSlaves: %d, PerSlave: %d, StartSeek: %d\n", 
				numLines, numSlaves, perSlave, startSeek);
		
		String jobDir = "./" + ConfigReader.getTempmapfiles() + job.getJobName();
		boolean success = (new File(jobDir)).mkdirs();
		System.out.println("In scheduler, creating folder");
		int partitionNumber = 0;
		
		for (SlaveWrapper sw : slaves.keySet()) {
			if (numLines > 0) {
				int partitionLines = ((numLines>perSlave) ? perSlave : numLines);
//				System.out.format("Partition Number: %d\n", partitionNumber);
//				System.out.format("Partition Lines: %d\n", partitionLines);
//				System.out.format("StartSeek: %d\n", startSeek);
				MapMessage m = new MapMessage(startSeek, partitionLines, partitionNumber,
						job.getMapClass(), job.getMapURL(), job.getJobName(),
						job.getInputformat(), job.getInputFilename(), jobDir);
				jobs.get(job).add(m);
				try {
					sw.writeToSlave(m);
				} catch (IOException e) {
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
			//we sent all the files to be reduced to one slave in this case
			//we would further extend the map reduce facility to perform this
			//distribution in the same way as was done for map
			ArrayList<String> filenames = DirectoryHandler.getAllFiles(destDir, ".txt");
			ReduceMessage rm = new ReduceMessage(filenames, pathfile, classdirectory, classname, jobname);
			try {
				sw.writeToSlave(rm);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	public void run() {
		while (true) {
			// clean away completed jobs
			for (Job j : jobs.keySet()) {
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
		}
	}
}