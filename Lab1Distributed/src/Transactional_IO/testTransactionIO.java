package Transactional_IO;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
public class testTransactionIO {
	
	public static void main(String[] args) throws IOException {
		System.out.println(System.getProperty("user.dir"));
		File f= new File("t.txt");
		FileInputStream fi = new FileInputStream(f);
		
		
		
		TransactionalFileInputStream  inFile;
		
		try {
			inFile = new TransactionalFileInputStream("t.txt");
			DataInputStream in = new DataInputStream(inFile);
			/*System.out.println((char)in.read());
			System.out.println((char)in.read());
			System.out.println((char)in.read());
			System.out.println((char)in.read());*/

			String str = in.readLine();
			
			System.out.println(str);
			str = in.readLine();
			System.out.println(str);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
