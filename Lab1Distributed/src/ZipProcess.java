 import java.io.*;
import java.util.zip.*;

import Transactional_IO.TransactionalFileInputStream;
import Transactional_IO.TransactionalFileOutputStream;

public class ZipProcess{
	
	private TransactionalFileInputStream  in;
	private TransactionalFileOutputStream outFile;
	//private String query;

	private volatile boolean suspending;

	public ZipProcess(String args[]) throws Exception
	{
		if (args.length != 2) {
			System.out.println("usage: ZipProcess  <inputFile> <outputFile>");
			throw new Exception("Invalid Arguments");
		}
		
		in= new TransactionalFileInputStream(args[0]);
		outFile = new TransactionalFileOutputStream(args[1]);
	}
	
	public void run()
	{
		//FileInputStream in = new FileInputStream("F:/sometxt.txt");
        // out put file 
		try {
			while (!suspending) {
				ZipOutputStream out = new ZipOutputStream(outFile);
         // name the file inside the zip  file 

				out.putNextEntry(new ZipEntry("zippedjava.txt")); 

				byte[] b = new byte[1024];
            
				int count;

				while ((count = in.read(b)) > 0) {
					//System.out.println();

					out.write(b, 0, count);
				}
				out.close();
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

public static void main(String[] args)  throws Exception  {

// input file 

       FileInputStream in = new FileInputStream("F:/sometxt.txt");
        // out put file 

       ZipOutputStream out = new ZipOutputStream(new FileOutputStream("F:/tmp.zip"));
         // name the file inside the zip  file 

         out.putNextEntry(new ZipEntry("zippedjava.txt")); 

            byte[] b = new byte[1024];

        int count;

        while ((count = in.read(b)) > 0) {
            System.out.println();

         out.write(b, 0, count);
        }
        out.close();
        in.close();
        }
    }