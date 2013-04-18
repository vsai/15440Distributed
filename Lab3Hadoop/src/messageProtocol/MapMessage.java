package messageProtocol;

import messageProtocol.Job.InputType;

public class MapMessage implements java.io.Serializable{
	
	int startSeek;
	int numLines;
	int partitionNum;
	String classURL;
	String className;
	String jobName;
	String jobDirectory;
	InputType type;
	String inputFile;
	
	public MapMessage(int startSeek, int numLines, int partitionNum, 
			String className, String classURL, String jobName, 
			InputType type, String inputFile, String jobDirectory){
		this.startSeek = startSeek;
		this.numLines = numLines;
		this.partitionNum = partitionNum;
		this.className = className;
		this.classURL = classURL;
		this.jobName = jobName;
		this.type = type;
		this.inputFile = inputFile;
		this.jobDirectory= jobDirectory;
	}
	
	public int getStartSeek(){
		return startSeek;
	}
	
	public int getNumLines(){
		return numLines;
	}
	
	public int getPartitionNum(){
		return partitionNum;
	}
	
	public String getclassURL(){
		return classURL;
	}
	
	public String getClassName(){
		return className;
	}
	
	public InputType getInputType(){
		return type;
	}
	
	public String getInputFile(){
		return inputFile;
	}
	
	public String getJobName(){
		return jobName;
	}
	
	public String getJobDirectory(){
		return jobDirectory;
	}
}
