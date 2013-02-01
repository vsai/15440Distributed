package Transactional_IO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TransactionalFileInputStream extends java.io.InputStream implements java.io.Serializable{

	private static final long serialVersionUID = 2407972277266614549L;
	private String myFile;
	private int seekLocation;
	//private ;
	
	public TransactionalFileInputStream(String file) throws FileNotFoundException
	{
		myFile=file;
		seekLocation=0;
		
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
