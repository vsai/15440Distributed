package userRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import messageProtocol.Job;
import messageProtocol.Job.InputType;

import hadoop.MasterWrapper;
import fileIO.ConfigReader;

public class RequestMapReduce {
	
	public RequestMapReduce () {
		
	}
	
	//get master details - ip address, 
	
	public static void main(String args[]) throws UnknownHostException {
		ConfigReader cread = new ConfigReader();
		MasterWrapper master = cread.readMaster();
		Socket toMaster;
		ObjectOutputStream out;
		ObjectInputStream in;
		boolean result = false;
		Job jobRequest = new Job(null, null, null);
		
		String jobName = "Job2";
		
		jobName = randomString(20);
		System.out.println("JOBNAME TO BE CREATED: " + jobName);
		
		InputType inType = InputType.TEXT;
		String myIp = InetAddress.getLocalHost().getHostAddress();
//		String inputFilename = "/Users/apple/Desktop/15-440/15440Distributed/Lab3Hadoop/src/inputfile.txt";
		String inputFilename = "/Users/apple/Desktop/15-440/15440Distributed/Lab3Hadoop/inputTest.txt";
		String outputFilename = "testOutputFile.txt";
		String mapClass = "userRequest.UserMapper";
		String mapURL = "file:///Users/apple/Desktop/15-440/15440Distributed/Lab3Hadoop/bin/userRequest/";
		String reduceClass = "userRequest.UserMapper";
		String reduceURL = "file:///Users/apple/Desktop/15-440/15440Distributed/Lab3Hadoop/bin/userRequest/";
		
		
		jobRequest.setInputFilename(inputFilename);
		jobRequest.setInputformat(inType);
		jobRequest.setJobName(jobName);
		jobRequest.setMapClass(mapClass);
		jobRequest.setMapURL(mapURL);
		jobRequest.setOutputFilename(outputFilename);
		jobRequest.setReduceClass(reduceClass);
		jobRequest.setReduceURL(reduceURL);
		jobRequest.setRequesterIp(myIp);
		
//		bunch of setters for other things of job
//		jobRequest.set
//		System.out.println("1");
		try {
			toMaster = new Socket(master.getIpAddress(), master.getPortnum());
//			System.out.println("2");
			out = new ObjectOutputStream(toMaster.getOutputStream());
//			System.out.println("3");
			//in = new ObjectInputStream(toMaster.getInputStream());
//			System.out.println("adfadsf");
			out.writeObject(jobRequest);
//			System.out.println("adfadsf12312312");
			out.flush();
			System.out.println("sent jobrequest");
			out.close();
			//result = (Boolean) in.readObject();
			//in.close();
//			System.out.println("Received " + result);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
		}	
	}
	
	
	public static String randomString(int len) {
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		int strLen = str.length();
		Random random = new Random();
		String rand = "";
		for (int i = 0; i < len; i++) {
			rand += str.charAt(random.nextInt(strLen));
		}
		return rand;
	}
}
