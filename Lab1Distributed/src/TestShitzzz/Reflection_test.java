package TestShitzzz;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import Transactional_IO.GrepProcess;


public class Reflection_test {

	public static void main(String [] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException
	{
	
			Class<?> t = Class.forName("GrepProcess");
			Constructor<?>[] listOfConstructors = t.getConstructors();
			String [] inputArgs = {"li","t.txt","q.txt"};
			 GrepProcess o =  (GrepProcess) t.newInstance();
			//Object o = (flower.getConstructor(new Class[0])).newInstance(new Object[0]);
			 /*ExecutorService executor = Executors.newCachedThreadPool();
			Future<?> future = executor.submit(o);
			Thread.sleep(1000L);
			o.suspend();
			System.out.println(o);
			System.out.println("after");
			/FileOutputStream fos = new FileOutputStream("seria"); 
			ObjectOutputStream oos = new ObjectOutputStream(fos); 
			oos.writeObject(o); 
			oos.flush(); 
			oos.close(); 
			
			boolean mayInterruptIfRunning = false;
			future.cancel(mayInterruptIfRunning);
			
			Test1 object2; 
			FileInputStream fis = new FileInputStream("seria"); 
			ObjectInputStream ois = new ObjectInputStream(fis); 
			object2 = (Test1)ois.readObject(); 
			ois.close(); 
			
			future = executor.submit(object2);
			Thread.sleep(1000L);
			object2.suspend();
			System.out.println(object2);
			
			
			/*try {
				future.get();
			} catch (InterruptedException e) {
				// This won't happen in this case.
			} catch (ExecutionException e) {
				// An exception happened in the run() method above!
				e.getCause().printStackTrace();
			}*/


			
	
	}
	
}



class Test1 implements Runnable, java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3165224171753677808L;
	private volatile boolean suspending;
	public int x=0;;
	public void run()
	{
		
		System.out.println("in run");
		//suspending = false;
		while(!suspending)
		{
			x++;
			//System.out.println("Im running");
		}
		
		suspending = false;
	}
	public void suspend()
	{
		suspending = true;
		while (suspending);
	}
	@Override
	public String toString()
	{
		return "x:"+x;
	}

}
