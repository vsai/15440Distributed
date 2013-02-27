package lab2tests;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

public class ObjectSocketWriter implements Serializable{

	static String hostname = "localhost";
	static int portnum = 12344;
	
	int a;
	public ObjectSocketWriter(){
		a = 1;
	}
	
	public int getA(){
		return a;
	}
	
	public static void main(String args[]){
		Socket s;
		ObjectOutputStream oos;
		
		ObjectSocketWriter osw = new ObjectSocketWriter();
		
		try {
			s = new Socket(hostname, portnum);
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(osw);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
