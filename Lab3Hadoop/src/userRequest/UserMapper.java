package userRequest;

import hadoop.OutputCollector;
import hadoop.Writable;

public class UserMapper {

	
	//program written to determine how many times different words appear in a set of files.
	//This is an example of a TEXT Input map function
	//This map functions applies the function to make key value pair
	//The text is in the value and so we put
	//(word,"1") in our output collector as our map function
	//our reduce function will take this and add up all the same words
	public static void map(String key, String value, OutputCollector output){
		String [] ar = key.split(" ");
		for(String word: ar)
			output.collect(word, "1");
	}

	pubic static void reduce(String key, )
	public static void main(String[] args) {
	}
	
}
