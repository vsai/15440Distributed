package processManaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class SlaveInfo {

	//the key to the process which is a randomly generated string of length 30
	List<String> processes;
	boolean alive;
	Date lastHeard;
	
	public SlaveInfo() {
		this.processes = Collections.synchronizedList(new ArrayList<String>());
		this.lastHeard = new Date();
	}
	
	public List<String> getProcesses(){
		return processes;
	}
	
	public boolean putProcess(String p) {
		return processes.add(p);
	}
	
	public int getWorkload(){
		return processes.size();
	}
	
	public boolean removeProcess(String p){
		//removes first occurrence of that process
		return processes.remove(p);
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public void setAlive(boolean alive){
		this.alive = alive;
	}

}
