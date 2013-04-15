package userRequest;

import hadoop.OutputCollector;
import hadoop.Writable;

public class UserMapper {

	public static void map(Writable key, Writable value, OutputCollector output){
		
		Object a= "a";
		Writable b = (Writable) a;
		String q=b.toString();
		System.out.println(q);
	}

	public static void main(String[] args) {
		Object a= "test";
		Writable s = (Writable)a;
		OutputCollector output = null;
		map(s,s,output);
	}
	
}
