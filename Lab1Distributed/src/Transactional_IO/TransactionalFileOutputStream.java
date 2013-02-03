package Transactional_IO;

import java.io.IOException;
import java.io.*;
public class TransactionalFileOutputStream extends java.io.OutputStream implements java.io.Serializable {

	private static final long serialVersionUID = 6522677712988099866L;
	private String myFile;
	private int seekLocation;
	
	public TransactionalFileOutputStream(String f) {
		myFile=f;
		seekLocation=0;
	}
	
	@Override
	public void write(int arg0) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(myFile, "rw");
		raf.seek(seekLocation);
		raf.write(arg0);
		raf.close();
	}
	
}