package processMigration;

import java.util.ArrayList;

public class MigratableProcesses implements MigratableProcess {

	//@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running");
	}

	//@Override
	public void suspend() {
		// TODO Auto-generated method stub
		System.out.println("Suspended");
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("YOLO SWAG: ");
		System.out.println(args.length);
	}
	
}
