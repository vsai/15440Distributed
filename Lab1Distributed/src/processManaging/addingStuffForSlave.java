package processManaging;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import processMigration.MigratableProcess;
import java.lang.reflect.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.*; 

//import TestShitzzz.Test1;
public class addingStuffForSlave {

	static ExecutorService executor = Executors.newCachedThreadPool();
	static  HashMap<String,ArrayList<ProccessInfo>> hashOfProcesses = new HashMap<String, ArrayList<ProccessInfo>>();
	public static String Start(String str) throws ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		String [] p=str.split(" ");
		Class<?> t = Class.forName(p[0]);
		String [] pArgs = new String [p.length-1];
		for(int i=1;i<p.length;i++)
			pArgs[i-1]=p[i];
		Constructor<?>[] listOfConstructors = t.getConstructors();
		int correctConstructor=0;
		for(int i =0;i<listOfConstructors.length;i++)
		{
			if(listOfConstructors[i].getParameterTypes().length==pArgs.length)
				correctConstructor=i;
		}
		MigratableProcess mp=(MigratableProcess) listOfConstructors[correctConstructor].newInstance(pArgs);
		Future<?> future = executor.submit(mp);
		
		ProccessInfo pi= new ProccessInfo(future,mp);
		ArrayList<ProccessInfo> processes = new ArrayList<ProccessInfo>();
		
		if(hashOfProcesses.containsKey(str))
		{
			processes=hashOfProcesses.get(str);
		}

		processes.add(pi);
		hashOfProcesses.put(str, processes);
		return str;
		
	}
	public static String Suspend(String str) throws IOException
	{
		//Error message if the master sends a process to suspend that does not exists
		if(!hashOfProcesses.containsKey(str))
		{
			return "No Process in the list";
		}
		ArrayList<ProccessInfo> processes = hashOfProcesses.get(str);
		ProccessInfo p = processes.get(0);
		processes.remove(p);
		hashOfProcesses.put(str,processes);
		MigratableProcess mp=p.getProcess();
		mp.suspend();
		Future<?> f = p.getFuture();
		boolean b=false;
		f.cancel(b);
		String currentDir = System.getProperty("user.dir");
		String name=str.split(" ")[0];
		FileOutputStream fos = new FileOutputStream(name); 
		ObjectOutputStream oos = new ObjectOutputStream(fos); 
		oos.writeObject(mp); 
		oos.flush(); 
		oos.close(); 
		
		return "Suspended "+ str +" "+ currentDir+"/"+name;
	}
	public static String Resume(String str) throws IOException, ClassNotFoundException
	{
		String [] p = str.split(" ");
		String location = p[p.length-1];
		String procAndArgs ="";
		for(int i=0;i<p.length-1;i++)
			procAndArgs+=p[i]+" ";
		
		//delete the extra space
		procAndArgs = procAndArgs.substring(0, procAndArgs.length() - 1);
		
		FileInputStream fis = new FileInputStream(location); 
		ObjectInputStream ois = new ObjectInputStream(fis); 
		MigratableProcess mp = (MigratableProcess)ois.readObject(); 
		ois.close(); 
		Future<?> future = executor.submit(mp);
		
		ProccessInfo pi= new ProccessInfo(future,mp);
		ArrayList<ProccessInfo> processes = new ArrayList<ProccessInfo>();
		
		if(hashOfProcesses.containsKey(str))
		{
			processes=hashOfProcesses.get(str);
		}

		processes.add(pi);
		hashOfProcesses.put(procAndArgs, processes);
		return "Resumed "+ procAndArgs;
		
	}
	public static void ps()
	{
		
	}
	public static void main(String[] args) {
		System.out.println("a");
	}
}
