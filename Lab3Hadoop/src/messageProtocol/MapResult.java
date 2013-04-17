package messageProtocol;

import java.io.Serializable;

public class MapResult implements Serializable {

	String directoryName;
	String jobName;
	boolean success;
	int partitionNum;
	
	public MapResult(String directoryName, boolean success, int partitionNum,String jobName){
		this.directoryName = directoryName;
		this.success = success;
		this.partitionNum = partitionNum;
		this.jobName = jobName;
	}
	
	
	public String getDirectoryName() {
		return directoryName;
	}

	public String getJobName() {
		return jobName;
	}

	public boolean isSuccess() {
		return success;
	}

	public int getPartitionNum() {
		return partitionNum;
	}
	
}
