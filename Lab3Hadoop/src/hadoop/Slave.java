package hadoop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;

import messageProtocol.InitiateConnection;
import messageProtocol.Job.InputType;
import messageProtocol.MapMessage;
import messageProtocol.MapResult;
import messageProtocol.ReduceMessage;
import messageProtocol.ReduceResult;

import fileIO.ConfigReader;

public class Slave extends Thread {
	
	String ipAddress;
	int portnum;
	
	public Slave (String ipAddress, int portnum) {
		this.ipAddress = ipAddress;
		this.portnum = portnum;
	}

	public MapResult mapper(MapMessage mapMessage) throws MalformedURLException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		URL classUrl;
    	classUrl = new URL(mapMessage.getclassDirectory());
    	URL[] classUrls = { classUrl };
    	URLClassLoader ucl = new URLClassLoader(classUrls);
    	Class c = ucl.loadClass(mapMessage.getClassName());
    	Constructor cotr = c.getConstructors()[0];
    	Method [] methods = c.getMethods();
    	Method mapMethod=null;
    	for(Method m :methods){
    		if(m.getName().equals("map"))
    			mapMethod=m;
    	}
    	Object [] args = getArgsForMapMethod(mapMessage.getInputType(),mapMessage.getInputFile(),mapMessage.getStartSeek(),mapMessage.getEndSeek());
    	mapMethod.invoke(cotr.newInstance(),args);
    	return null;
	}
	
	private Object[] getArgsForMapMethod(InputType inputType, String inputFile,int startSeek, int endSeek) {
		Object [] objArr = new Object [3];
		
		
		return null;
	}

	public ReduceResult reducer(ReduceMessage reduceMessage) {
		//TODO
		return null;
	}
	public void run() {
		ConfigReader cread = new ConfigReader();
		MasterWrapper m = cread.readMaster();
		Socket toMaster;
		InitiateConnection initConn = new InitiateConnection(ipAddress, portnum);
		ObjectInputStream in;
		ObjectOutputStream out;
		Object inobj;
		try {
			toMaster = new Socket(m.ipAddress, m.portnum);
			in = new ObjectInputStream(toMaster.getInputStream());
			out = new ObjectOutputStream(toMaster.getOutputStream());
			out.writeObject(initConn);
			
			while (true) {
				try {
					inobj = in.readObject();
					if (inobj instanceof MapMessage) {
						MapResult mr = mapper((MapMessage) inobj); 
						out.writeObject(mr);
					} else if (inobj instanceof ReduceMessage) {
						ReduceResult rr = reducer((ReduceMessage) inobj);
						out.writeObject(rr);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
