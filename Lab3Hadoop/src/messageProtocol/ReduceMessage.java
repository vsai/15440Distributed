package messageProtocol;

public class ReduceMessage {

	String jobTmpFilesDirecotry;
	String outputFile;
	String classDirectory;
	String className;
	
	public ReduceMessage(String jobTmpFilesDirectory, String outputFile, String classDirectory, String className){
		this.jobTmpFilesDirecotry = jobTmpFilesDirectory;
		this.outputFile = outputFile;
		this.classDirectory = classDirectory;
		this.className = className;
	}
	
	public String getJobTmpFileDirectory(){
		return jobTmpFilesDirecotry;
	}
	
	public String getOutputFile(){
		return outputFile;
	}
	
	public String getClassDirectory(){
		return classDirectory;
	}
	
	public String getClassName(){
		return className;
	}
}
