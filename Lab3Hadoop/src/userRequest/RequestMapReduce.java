package userRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import messageProtocol.Job;
import messageProtocol.Job.InputType;

import hadoop.MasterWrapper;
import fileIO.ConfigReader;

public class RequestMapReduce {
	
	public RequestMapReduce () {
		
	}
	
	//get master details - ip address, 
	
	public static void main(String args[]) {
		ConfigReader cread = new ConfigReader();
		MasterWrapper master = cread.readMaster();
		Socket toMaster;
		ObjectOutputStream out;
		ObjectInputStream in;
		boolean result = false;
		Job jobRequest = new Job(null, null, null);
		String inputFilename = "/Users/apple/Desktop/15-440/15440Distributed/Lab3Hadoop/src/inputfile.txt";
		jobRequest.setInputFilename(inputFilename);
		jobRequest.setInputformat(InputType.TEXT);
		jobRequest.setJobName("Job2");
		jobRequest.setMapClass("userRequest.UserMapper");
		jobRequest.setMapURL("file:///Users/apple/Desktop/15-440/15440Distributed/Lab3Hadoop/bin/userRequest/");
		jobRequest.setOutputFilename("testOutputFile.txt");
		jobRequest.setReduceClass("userRequest.UserMapper");
		jobRequest.setReduceURL("file:///Users/apple/Desktop/15-440/15440Distributed/Lab3Hadoop/bin/userRequest/");
		jobRequest.setRequesterIp("128.237.251.114");
		
//		bunch of setters for other things of job
//		jobRequest.set
		System.out.println("1");
		try {
			toMaster = new Socket(master.getIpAddress(), master.getPortnum());
			System.out.println("2");
			out = new ObjectOutputStream(toMaster.getOutputStream());
			System.out.println("3");
			//in = new ObjectInputStream(toMaster.getInputStream());
			System.out.println("adfadsf");
			out.writeObject(jobRequest);
			System.out.println("adfadsf12312312");
			out.flush();
			System.out.println("sent jobrequest");
			out.close();
			//result = (Boolean) in.readObject();
			//in.close();
			System.out.println("Received " + result);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
		}
		
		if (result) {
			System.out.println("Successfully sent job");
		} else {
			System.out.println("Failed to send job");
		}	
	}
}
