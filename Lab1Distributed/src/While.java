
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

public class While implements MigratableProcess
{
	private TransactionalFileInputStream  inFile;
	private TransactionalFileOutputStream outFile;
	private String query;

	private volatile boolean suspending;

	public While(String args[]) throws Exception
	{
		System.out.println("Starting the proc");
	}

	public void run()
	{
		PrintStream out = new PrintStream(outFile);
		DataInputStream in = new DataInputStream(inFile);

			while (!suspending) {

				
				// Make grep take longer so that we don't require extremely large files for interesting results
				try {
					Thread.sleep(10000);
					System.out.println("IN WHILE PROCESS");
				} catch (InterruptedException e) {
					// ignore it
				}
			}


		suspending = false;
	}

	public void suspend()
	{
		suspending = true;
		while (suspending);
	}

}