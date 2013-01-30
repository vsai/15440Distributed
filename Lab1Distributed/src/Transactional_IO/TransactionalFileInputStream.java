package Transactional_IO;

import java.io.IOException;

public class TransactionalFileInputStream extends java.io.InputStream implements java.io.Serializable{

	private static final long serialVersionUID = 2407972277266614549L;
	private String myFile;
	private int seekLocation;
	
	public TransactionalFileInputStream(String file)
	{
		myFile=file;
		seekLocation=0;
	}
	
	@Override
	public int read() throws IOException {
		read();
		return 0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
