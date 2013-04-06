package lab3tests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ReadJavaFiles {

	
	public static void main (String args[]) {
		try {
			RandomAccessFile f = new RandomAccessFile("/Users/apple/Desktop/yoloswag.txt", "r");
			System.out.println(f.getFilePointer());
			f.readChar();
			System.out.println(f.getFilePointer());
			
			RandomAccessFile fo = new RandomAccessFile("/Users/apple/Desktop/yoloswag.txt", "r");
			System.out.println(fo.getFilePointer());
			fo.readChar();
			System.out.println(f.getFilePointer());
			System.out.println(fo.getFilePointer());
			
			f.close();
			fo.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
