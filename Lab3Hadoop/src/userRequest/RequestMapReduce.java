package userRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import messageProtocol.Job;

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
//		bunch of setters for other things of job
//		jobRequest.set
		
		try {
			toMaster = new Socket(master.getIpAddress(), master.getPortnum());
			out = new ObjectOutputStream(toMaster.getOutputStream());
			in = new ObjectInputStream(toMaster.getInputStream());
			out.writeObject(jobRequest);
			result = (Boolean) in.readObject();
			out.flush();
			out.close();
			in.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if (result) {
			System.out.println("Successfully sent job");
		} else {
			System.out.println("Failed to send job");
		}	
	}
}
