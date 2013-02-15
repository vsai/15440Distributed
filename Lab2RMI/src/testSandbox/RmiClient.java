package testSandbox;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
 
public class RmiClient { 
    // "obj" is the reference of the remote object
    RmiServerIntf obj = null; 
 
    public String getMessage() { 
        try { 
            obj = (RmiServerIntf)Naming.lookup("//localhost/RmiServer");
            return obj.getMessage(); 
        } catch (Exception e) { 
            System.err.println("RmiClient exception: " + e); 
            e.printStackTrace(); 
 
            return e.getMessage();
        } 
    } 
 
    public static void main(String args[]) {
        // Create and install a security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
 
        RmiClient cli = new RmiClient();
 
        System.out.println(cli.getMessage());
    }
}