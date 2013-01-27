package processMigration;

import java.io.Serializable;

public interface MigratableProcess extends Serializable, Runnable {

	void suspend();
	
	String toString();
	
}
