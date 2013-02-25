package testSandbox;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringBufferInputStream;
import java.util.HashMap;

public class TestServer implements TestServerInterface{

	/**
	 * @param args
	 */
	int score;
	String name;
//	HashMap<String, Serializable> registryStore;
	
	public TestServer(){
		score = 0;
		name = "Vishalsai";
//		registryStore = new HashMap<String, Serializable>();
	}
	
	@Override
	public int getScore() {
		return score;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void incrementScore() {
		score++;
	}

	@Override
	public void setScore(int score) {
		this.score = score;
	}

	
	
	public boolean printObjectToFile(Object obj, String filename){
		FileOutputStream fos;
		ByteArrayOutputStream baos;
		String a;
		ObjectOutputStream oos;
		try {
//			fos = new FileOutputStream(filename);
//			oos = new ObjectOutputStream(fos);
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
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
	
	
	
	public static void main(String[] args) {
		HashMap<String, Serializable> registryStore = new HashMap<String, Serializable>();
		TestServer a = new TestServer();
		
//		ObjectOutputStream oos = new ObjectOutputStream();
		
		
//		registryStore.put("1", a);

	}
	
}
