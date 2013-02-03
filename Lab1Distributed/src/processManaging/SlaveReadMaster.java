package processManaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class SlaveReadMaster extends SocketMessage {

	BufferedReader in;
	PrintWriter out;
	
	SlaveReadMaster(BufferedReader in, PrintWriter out) {
		this.in = in;
		this.out = out;
	}
	
	public void run(){
		/*
		 * messages to read:
		 * "STARTProcess <processName> <<processArgs>>"
		 * "SUSPENDProcess <processName> <<processArgs>>" | "SUSPENDED <processName> <<processArgs>>"
		 * "RESUMEProcess <processName> <<processArgs>>"
		 * "RECEIVEDNewProcess <processName> <<processArgs>>"
		 */
		
		String inputLine;
		String filePath = null;
		while (true) {
			try {
				synchronized(in){
					while((inputLine = in.readLine())!= messageTerminator) {
						String[] input = inputLine.split(" ", 2);
						if (input[0].equals(startProcess)) {
						} else if (input[0].equals(suspendProcess)) {
							synchronized(out) {
								out.println(sendMessage(suspended + filePath + " " + input[1]));
							}
						} else if (input[0].equals(resumeProcess)) {
							
						} else if (input[0].equals(receivedProcess)) {
							//coolstoryBro
						}
					}
				}
			} catch (IOException e) {
				System.out.println("Could not read from buffer");
				e.printStackTrace();
			}
		}
	}

}
