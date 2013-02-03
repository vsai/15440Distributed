package processManaging;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestingCode {

	/**
	 * @param args
	 */
	private static InetAddress ip;
	
	public static void main(String[] args) {
		//Find out your own IP details
		
		String filePath = "t.txt";
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
			e.printStackTrace();
		}
		String n;
		try {
			while ((n = in.readLine())!= null){
				System.out.println(n);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("DONE");
		
		/*
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    System.out.println(strDate);
	    System.out.println(now);
	    Date now1 = new Date();
	    System.out.println(now1.compareTo(now));
		
		for (int i = 0; i<3; i++) {
			try {
				ip = InetAddress.getLocalHost();
				System.out.println("Current IP address : " + ip.getHostAddress());
				break;
			} catch (UnknownHostException e) {
				if (i <3) {
					System.out.println("Could not resolve ip address. Trying again.");
				} else {
					System.out.println("Could not resolve ip address. Exiting system.");
					System.exit(0);
				}
				e.printStackTrace();
			}
		}
		String[] ab = {"ABC", "def", "odojs"};
		System.out.println(ab);
		*/
	}
}
