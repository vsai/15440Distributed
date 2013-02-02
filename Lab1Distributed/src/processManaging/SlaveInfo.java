package processManaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlaveInfo {

	List<String> processes;
	
	public SlaveInfo() {
		this.processes = Collections.synchronizedList(new ArrayList<String>());
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
		return false;
	}

}
