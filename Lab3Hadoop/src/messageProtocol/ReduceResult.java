package messageProtocol;

import java.io.Serializable;

public class ReduceResult implements Serializable {

	boolean success;
	String outputFile;
	String jobName;
	
	public ReduceResult(boolean success, String outputFile, String jobName){
		this.success = success;
		this.outputFile = outputFile;
		this .jobName = jobName;
	}
	
	public boolean getSuccess(){
		return success;
	}
	
	public String getOutputFile(){
		return outputFile;
	}
	
	public String getJobName(){
		return jobName;
	}
}
