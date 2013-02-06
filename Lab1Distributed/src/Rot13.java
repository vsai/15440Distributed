
import java.io.PrintStream;
import java.io.EOFException;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.Thread;
import java.lang.InterruptedException;

import processMigration.MigratableProcess;
import Transactional_IO.TransactionalFileInputStream;
import Transactional_IO.TransactionalFileOutputStream;

public class Rot13 implements MigratableProcess
{
	private TransactionalFileInputStream  inFile;
	private TransactionalFileOutputStream outFile;

	private volatile boolean suspending;

	public Rot13(String args[]) throws Exception
	{
		System.out.println("DEFAULT PACKAGE Rot13");
		if (args.length != 2) {
			System.out.println("usage: Rot13 <inputFile> <outputFile>");
			throw new Exception("Invalid Arguments");
		}
		
		inFile = new TransactionalFileInputStream(args[0]);
		outFile = new TransactionalFileOutputStream(args[1]);
	}

	public void run() {
		PrintStream out = new PrintStream(outFile);
		DataInputStream in = new DataInputStream(inFile);

		try {
			while (!suspending) {
				String line = in.readLine();

				if (line == null) break;
								
				StringBuffer sb = new StringBuffer(line);
				
				assert(line.length() == sb.length());
				for (int index = 0; index<sb.length(); index++){
					char c = line.charAt(index);
					if       (c >= 'a' && c <= 'm') c += 13;
		            else if  (c >= 'A' && c <= 'M') c += 13;
		            else if  (c >= 'n' && c <= 'z') c -= 13;
		            else if  (c >= 'N' && c <= 'Z') c -= 13;
					sb.setCharAt(index, c);
				}
				//String newLine = sb.toString();
				
				out.println(sb);
				
				
				// Make grep take longer so that we don't require extremely large files for interesting results
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// ignore it
				}
			}
		} catch (EOFException e) {
			//End of File
		} catch (IOException e) {
			System.out.println ("GrepProcess: Error: " + e);
		}


		suspending = false;
	}

	public void suspend() {
		suspending = true;
		while (suspending);
	}
}