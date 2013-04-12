package messageProtocol;

import java.io.Serializable;
import java.net.URL;

public class Job implements Serializable {
	
	public enum State {
		INIT, STARTED, ENDED
	}
	
	public enum InputType {
		TEXT, KEYVALUE
	}
	
	String jobName;
	String inputFilename;
	String outputFilename;
	URL mapURL;
	URL reduceURL;
	String mapClass;
	String reduceClass;
	String requesterIp;
	InputType inputformat;
	State state;
	
	public Job (String jobName, String inputFilename, String outputFilename) {
		this.jobName = jobName;
		this.inputFilename = inputFilename;
		this.outputFilename = outputFilename;
		this.state = State.INIT;
	}
	
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}	
	
	public String getInputFilename() {
		return inputFilename;
	}

	public void setInputFilename(String inputFilename) {
		this.inputFilename = inputFilename;
	}

	public String getOutputFilename() {
		return outputFilename;
	}

	public void setOutputFilename(String outputFilename) {
		this.outputFilename = outputFilename;
	}

	public URL getMapURL() {
		return mapURL;
	}

	public void setMapURL(URL mapURL) {
		this.mapURL = mapURL;
	}

	public URL getReduceURL() {
		return reduceURL;
	}

	public void setReduceURL(URL reduceURL) {
		this.reduceURL = reduceURL;
	}

	public String getMapClass() {
		return mapClass;
	}

	public void setMapClass(String mapClass) {
		this.mapClass = mapClass;
	}

	public String getReduceClass() {
		return reduceClass;
	}

	public void setReduceClass(String reduceClass) {
		this.reduceClass = reduceClass;
	}

	public String getRequesterIp() {
		return requesterIp;
	}

	public void setRequesterIp(String requesterIp) {
		this.requesterIp = requesterIp;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public InputType getInputformat() {
		return inputformat;
	}

	public void setInputformat(InputType inputformat) {
		this.inputformat = inputformat;
	}
	
	
}