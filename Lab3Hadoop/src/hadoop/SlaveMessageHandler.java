package hadoop;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import fileIO.ConfigReader;
import fileIO.DirectoryHandler;

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
							writeDataToOutputFile(j);
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

	
	
	private void writeDataToOutputFile(Job j) throws IOException {
		// TODO Auto-generated method stub
		String filePath = ConfigReader.getResultfiles() + j.getJobName() + "/";
		ArrayList<String> allFiles = DirectoryHandler.getAllFiles(filePath, ".txt");
		String outputFile = j.getOutputFilename();
		String key;
		String value = null;
		FileWriter fileWritter = new FileWriter(outputFile);
		
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		for (String filename : allFiles){
			String[] fileparts = filename.split("/");
			key = fileparts[fileparts.length-1].replace(".txt", "");
			
			LineNumberReader lnr = new LineNumberReader(new FileReader(filename));
			String line;
			value="";
			while((line=lnr.readLine()) !=null) {
				value+=line;
			}
			
	        bufferWritter.write(key + "\t" + value+"\n");
	        
		}
		bufferWritter.close();
	}
}