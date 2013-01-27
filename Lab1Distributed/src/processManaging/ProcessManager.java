package processManaging;

import java.util.ArrayList;
import java.util.Scanner;

import processMigration.MigratableProcesses;

public class ProcessManager {

	/**
	 * @param args
	 */
	static ArrayList<MigratableProcesses> processes;
	
	public static void main(String[] args) {
		
		if (args.length == 0) {
			//run process manager as master
			System.out.println("I'm a MASTER");
		} else if (args.length == 0 && args[0].equals("-c")) {
			String hostname = args[1];
			System.out.println("I'm a SLAVE. Master is :" + hostname);
		} else {
			System.out.println("Invalid input to startup ProcessManager");
			System.exit(0);
		}
		
		processes = new ArrayList<MigratableProcesses>();
		
		Scanner sc = new Scanner(System.in);
		while(true){
			System.out.println("Prompt for input ==>");
			String input = sc.nextLine();
			if (input.equals("ps")) {
				for (MigratableProcesses p : processes){
					
				}
			} else if (input.equals("quit")){
				
			} else {
				String in[] = input.split(" ");
				//in[0] = process name
				//in[1] = arg0
				//in[2] = arg1
				//etc.
			}
			
		}
	}

}
