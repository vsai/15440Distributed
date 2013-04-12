package testShizz;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderTest {

	 public static void main(String[] args) throws ClassNotFoundException, MalformedURLException {
		 	URL classUrl;
	    	classUrl = new URL("file:///Users/AJ/Documents/15440/15440Distributed/Lab3Hadoop/bin/hadoop/");
	    	URL[] classUrls = { classUrl };
	    	URLClassLoader ucl = new URLClassLoader(classUrls);
	    	Class c = ucl.loadClass("hadoop.Slave");
	    	System.out.println(c.getMethods()[0].toString());
	    	
	 }
}
