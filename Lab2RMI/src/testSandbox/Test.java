package testSandbox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test {

	public Test(){
		
	}
	
	public String getCurrentMethodName(){
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
	
	public int getA(){
		Integer x = Integer.valueOf(56);
		return x;
	}
	
	public int getHigher(int a){
		return a+1;
	}
	
	public int getAge(){
		System.out.println(getCurrentMethodName());
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return 0;
	}
	
	public static void main(String args[]){
//		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		Test a = new Test();
//		Object x = a.getAge();
		
		Object[] argus = {(Integer) 1, "APPLE", (Boolean) true};
		
		for (Object q : argus) {
			Class p = q.getClass();
			System.out.println(p);
			if (q instanceof Integer) {
				System.out.println("INTEGER WHOPPEEDEEDOODAA");
				System.out.println(q);
				q = "BOTTOM";
			} else if (q instanceof String) {
				System.out.println("STRING YOLOOOLOLO");
				System.out.println(q);
			} else {
				System.out.println("TOOBAD");
			}
		}
		System.out.println("NEW ITERATION");
		for (Object q : argus) {
			System.out.println(q);
		}
		
		
		
//		System.out.println("Exiting");
//		System.out.println(x);
//		System.out.println(x.toString());
//		System.out.println(x.getClass());
//		a.getA();
//		
//		System.out.println("Test dynamically invoking a method");
//		
//		
//		
//		try {
//			Class <?> cl = a.getClass();
//			Method meth = cl.getMethod("getHigher", new Class[] {int.class});
//			int d = (Integer) meth.invoke(a, 5);
//			System.out.println("d: " + d);
//			System.out.println("R1");
//			
//			Class <?> cl2 = a.getClass();
//			Method meth2 = cl.getMethod("getA", null);
//			int d2 = (Integer) meth2.invoke(a, null);
//			System.out.println("d2: " + d2);
//			System.out.println("R2");
//			
////			int c = (Integer) a.getClass().getMethod("getHigher").invoke(a, 5);
////			System.out.println("WOHOO " + c);
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		
	}
	
}
