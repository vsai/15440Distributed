package messageProtocol;

import java.util.ArrayList;

public class ReduceMessage implements java.io.Serializable {

	ArrayList<String> fileNames;
	String pathFile;
	String classDirectory;
	String className;
	String jobName;
	public ReduceMessage(ArrayList<String> fileNames, String pathFile, 
			String classDirectory, String className, String jobName){
		this.fileNames = fileNames;
		this.pathFile = pathFile;
		this.classDirectory = classDirectory;
		this.className = className;
		this.jobName = jobName;
	}
	
	public ArrayList<String> getFileNames(){
		return fileNames;
	}
	
	public String getPathFile(){
		return pathFile;
	}
	
	public String getClassDirectory(){
		return classDirectory;
	}
	
	public String getClassName(){
		return className;
	}
	
	public String getJobName(){
		return jobName;
	}
}
