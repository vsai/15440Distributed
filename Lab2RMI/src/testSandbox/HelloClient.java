package testSandbox;

import java.rmi.*;

public class HelloClient {
	// You'll want to change this to match your own host
	private static final String HelloServerURL = "rmi://gigo.sp.cs.cmu.edu/hello";

	// This takes one command line argument: A person's first name
	public static void main (String []args) {
		try {
			System.setSecurityManager (new RMISecurityManager());
			HelloInterface hello = (HelloInterface) Naming.lookup(HelloServerURL);

			String theGreeting = hello.sayHello (args[0]);

			System.out.println (theGreeting);
	    } catch (Exception e) {
	      // Bad things can happen to good people
	      e.printStackTrace();
	    }
	}
}
