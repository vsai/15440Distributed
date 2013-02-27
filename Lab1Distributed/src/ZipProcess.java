import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import Transactional_IO.TransactionalFileInputStream;
import Transactional_IO.TransactionalFileOutputStream;

public class ZipProcess{
	
	private String  file;
	private TransactionalFileInputStream in;
	private TransactionalFileOutputStream outFile;
	private volatile boolean suspending;

	public ZipProcess(String args[]) throws Exception {
		if (args.length != 2) {
			System.out.println("usage: ZipProcess  <inputFile> <outputFile>");
			throw new Exception("Invalid Arguments");
		}
		
		in= new TransactionalFileInputStream(args[0]);
		outFile = new TransactionalFileOutputStream(args[1]);

		ZipOutputStream zos = new ZipOutputStream(outFile);
		ZipEntry ze= new ZipEntry(args[0]);
		zos.putNextEntry(ze);			
	}
	
	public void run() {
		//FileInputStream in = new FileInputStream("F:/sometxt.txt");
        // out put file 
		try {
			while (!suspending) {
				ZipOutputStream zos = new ZipOutputStream(outFile);
				ZipEntry ze= new ZipEntry(file);
				zos.putNextEntry(ze);
				
				byte[] b = new byte[1024];
				int count;
				while ((count = in.read(b)) > 0) {
					zos.write(b, 0, count);
				}
				zos.close();
				in.close();
			}
		}
		catch (EOFException e) {
			//End of File
		} catch (IOException e) {
			System.out.println ("ZipProcess: Error: " + e);
		}
		suspending = false;
	}

	public void suspend() {
		suspending = true;
		while (suspending);
	}
}