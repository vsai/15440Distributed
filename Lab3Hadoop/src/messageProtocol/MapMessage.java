package messageProtocol;

import messageProtocol.Job.InputType;

public class MapMessage {

	
	int startSeek;
	int numLines;
	int partitionNum;
	String classDirectory;
	String className;
	String jobName;
	String jobDirectory;
	InputType type;
	String inputFile;
	
	public MapMessage(int startSeek, int endSeek, int partitionNum, String className, String classDirectory, String jobName, InputType type, String inputFile, String jobDirectory){
		this.startSeek = startSeek;
		this.numLines = endSeek;
		this.partitionNum = partitionNum;
		this.className = className;
		this.classDirectory = classDirectory;
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
	
	public String getclassDirectory(){
		return classDirectory;
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
