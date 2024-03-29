package Transactional_IO;

import java.io.*;

public class SerializationDemo {

	public static void main(String args[]) { 
		// Object serialization 
		try { 
		MyClass object1 = new MyClass("Hell", -7, 2.7e10); 
		System.out.println("object1: " + object1); 
		FileOutputStream fos = new FileOutputStream("seria"); 
		ObjectOutputStream oos = new ObjectOutputStream(fos); 
		oos.writeObject(object1); 
		oos.flush(); 
		oos.close(); 
		} 
		catch(Exception e) { 
		System.out.println("Exception during serialization: " + e); 
		System.exit(0); 
		} 
		// Object deserialization 
		try { 
		MyClass object2; 
		FileInputStream fis = new FileInputStream("seria"); 
		ObjectInputStream ois = new ObjectInputStream(fis); 
		object2 = (MyClass)ois.readObject(); 
		ois.close(); 
		System.out.println("object2: " + object2); 
		} 
		catch(Exception e) { 
		System.out.println("Exception during deserialization: " + 
		e); 
		System.exit(0); 
		} 
		} 
		} 
		class MyClass implements java.io.Serializable { 
		/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		String s; 
		int i; 
		double d; 
		public MyClass(String s, int i, double d) { 
		this.s = s; 
		this.i = i; 
		this.d = d; 
		} 
		public String toString() { 
		return "s=" + s + "; i=" + i + "; d=" + d; 
		} 
	}