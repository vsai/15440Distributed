package processManaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class SlaveInfo {

	//the key to the process which is a randomly generated string of length 30
	List<ProcessInfo> processes; //list of keys/procNameprocArgs
	boolean alive;
	
	public SlaveInfo() {
		processes = Collections.synchronizedList(new ArrayList<ProcessInfo>());
		alive=true;
	}
	
	public List<ProcessInfo> getProcesses(){
		return processes;
	}
	
	public boolean putProcess(ProcessInfo p) {
		return processes.add(p);
	}
	public void clearProcessInfoList(){
		processes = Collections.synchronizedList(new ArrayList<ProcessInfo>());
	}
	public int getWorkload(){
		return processes.size();
	}
	
	public boolean removeProcess(String p){
		//removes first occurrence of that process
		for (ProcessInfo x : processes){
			System.out.println(x.getFilePath());
			if (x.getFilePath().equals(p)){
				System.out.println("YESS!! SHOULD REMOVE THIS");
				return processes.remove(x);
			}
			
		}
		return false;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public void setAlive(boolean alive){
		this.alive = alive;
	}

}
