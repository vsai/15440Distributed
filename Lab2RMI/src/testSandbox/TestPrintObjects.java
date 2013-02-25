package testSandbox;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TestPrintObjects {
	
	public boolean printObjectToFile(Object obj, String filename){
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			fos = new FileOutputStream(filename);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj); 
			oos.flush();
			oos.close(); 
		} catch (FileNotFoundException e) {
			System.err.println("Could not find filename to write to");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			System.err.println("Could not write to file");
			e.printStackTrace();
			return false;
		}
		return true;		
	}
	
	public static void main(String args[]){
		Object a = Integer.valueOf(1);
		TestPrintObjects printer = new TestPrintObjects();
		boolean result = printer.printObjectToFile(a, "writeObj2.txt");
		boolean result2 = printer.printObjectToFile(1, "writeObj3.txt");
		System.out.println("Writing object to file: " + result + result2);
	}

}
