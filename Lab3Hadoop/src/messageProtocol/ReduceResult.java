package messageProtocol;

import java.io.Serializable;

public class ReduceResult implements Serializable {

	boolean success;
	String pathFile;
	String jobName;
	
	public ReduceResult(boolean success, String pathFile, String jobName){
		this.success = success;
		this.pathFile = pathFile;
		this .jobName = jobName;
	}
	
	public boolean getSuccess(){
		return success;
	}
	
	public String getPathFile(){
		return pathFile;
	}
	
	public String getJobName(){
		return jobName;
	}
}
