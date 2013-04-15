package userRequest;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import hadoop.Writable;

public class MyWritable implements Writable {
	
	private String text;
	
	 public void write(DataOutput out) throws IOException {
		 out.writeBytes(text);
	 }
	 
	 public void readFields(DataInput in) throws IOException {
		 text=in.readLine();
	 }
	 
	 public static MyWritable read(DataInput in) throws IOException {
		 MyWritable w = new MyWritable();
		 w.readFields(in);
		 return w;
	 }
	 
	 
}

