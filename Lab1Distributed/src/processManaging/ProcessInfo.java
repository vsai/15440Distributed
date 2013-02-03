package processManaging;

import java.util.concurrent.Future;
import processMigration.MigratableProcess;

public class ProcessInfo {
	
	private Future<?> f;
	private MigratableProcess p;
	
	public ProcessInfo(Future<?> f, MigratableProcess p){
		this.f=f;
		this.p=p;
	}
	public Future<?> getFuture(){
		return f;
	}
	public MigratableProcess getProcess(){
		return p;
	}
}
