package messageProtocol;

import messageProtocol.Job.InputType;

public class MapMessage {

	
	int startSeek;
	int numLines;
	int partitionNum;
	String classDirectory;
	String className;
	String jobName;
	InputType type;
	String inputFile;
	
	public MapMessage(int startSeek, int endSeek, int partitionNum, String className, String classDirectory, String jobName, InputType type, String inputFile){
		this.startSeek = startSeek;
		this.numLines = endSeek;
		this.partitionNum = partitionNum;
		this.className = className;
		this.classDirectory = classDirectory;
		this.jobName = jobName;
		this.type = type;
		this.inputFile = inputFile;
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
}
