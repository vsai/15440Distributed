package fileIO;

import java.io.FileInputStream;
import java.io.IOException;

public class TransactionalFileInputStream extends java.io.InputStream implements java.io.Serializable{

	private String myFile;
	private int seekLocation;
	
	public TransactionalFileInputStream(String myFile) {
		this.myFile = myFile;
		seekLocation = 0;
	}
	
	@Override
	public int read() throws IOException {
		FileInputStream f= new FileInputStream(myFile);
		f.skip(seekLocation);
		int n= f.read();
		seekLocation+=1;
		f.close();
		return n;
	}
	

}
