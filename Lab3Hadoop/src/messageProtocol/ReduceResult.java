package messageProtocol;

import java.io.Serializable;

public class ReduceResult implements Serializable {

	boolean success;
	String outputFile;
	
	public ReduceResult(boolean success, String outputFile){
		this.success = success;
		this.outputFile = outputFile;
	}
	
	public boolean getSuccess(){
		return success;
	}
	
	public String getOutputFile(){
		return outputFile;
	}
}
