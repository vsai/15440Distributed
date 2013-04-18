package userRequest;

import java.io.IOException;
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
	
	public static void main(String args[]) throws UnknownHostException {
		MasterWrapper master = ConfigReader.readMaster();
		Socket toMaster;
		ObjectOutputStream out;
		Job jobRequest = new Job(null, null, null);
		
		String jobName = randomString(20);
		System.out.println("JOBNAME TO BE CREATED: " + jobName);
		
		InputType inType = InputType.TEXT;
		String myIp = InetAddress.getLocalHost().getHostAddress();
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
		try {
			toMaster = new Socket(master.getIpAddress(), master.getPortnum());
			out = new ObjectOutputStream(toMaster.getOutputStream());
			out.writeObject(jobRequest);
			out.flush();
			System.out.println("sent jobrequest");
			out.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
