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
		
		
		
		
		
		String a = "abcdefghijklmn";
		StringBuffer sb = new StringBuffer(a);
		
		assert(a.length() == sb.length());
		for (int index = 0; index<sb.length(); index++){
			char c = a.charAt(index);
			if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'N' && c <= 'Z') c -= 13;
			sb.setCharAt(index, c);
		}
		System.out.println(sb);
		System.out.println(a);
		
		/*for (int index = 0; index<a.length(); index++) {
			char aChar = a.charAt(index);
			System.out.println(aChar);
			a.charAt(index) = '1';
		}
		*/
		
		System.out.println("DONE WITH 1");
		
		
		
		
		char[] charArr = a.toCharArray();
		
		for (char b : charArr){
			//System.out.println(b);
			b = '1';
		}
		System.out.println(a);
		System.out.println(charArr);
		/*
		String a = "abcdef\nab\nac\n";
		System.out.println(a);
		System.out.println("ABC");
		System.out.println(a.substring(0, a.length() -1));
		System.out.println("ABC");
		*/
		//Find out your own IP details
		/*
		Master m = new Master(1);
		System.out.println(m.getClass().getName());
		System.out.println(m.getClass().getSimpleName());
		*/
		/*
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
		*/
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
		*/
		/*
		String[] ab = {"ABC", "def", "odojs"};
		System.out.println(ab);
		System.out.println(ab.toString());
		StringBuilder builder = new StringBuilder();
		for(String s : ab) {
		    builder.append(s + " ");
		}
		String a = builder.toString();
		System.out.println(a);
		System.out.println(a.substring(0, a.length() -1));
		*/
	}
}
