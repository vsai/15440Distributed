package testSandbox;

public class Test {

	public Test(){
		
	}
	
	public String methName(){
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
	
	public int getAge(){
		System.out.println(methName());
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return 0;
	}
	
	public static void main(String args[]){
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		Test a = new Test();
		a.getAge();
	}
	
}
