package lab2tests;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ObjectSocketReader {

	static int listeningPortnum = 12344;
	
	public static void main(String args[]){
		ServerSocket ss;
		Socket s;
		ObjectInputStream in;
		
		Object obj;
		
		try {
			ss = new ServerSocket(listeningPortnum);
			s = ss.accept();
			in = new ObjectInputStream(s.getInputStream());
			obj = in.readObject();
			
			System.out.println(obj);
			System.out.println(obj.getClass());
			
			System.out.println(((ObjectSocketWriter)obj).getA());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
