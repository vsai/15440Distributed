package testSandbox;

import java.rmi.RemoteException;

public class TGetIntfs implements RmiServerIntf, MyRmiServerIntf{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TGetIntfs a = new TGetIntfs();
		Class<?>[] b = a.getClass().getInterfaces();
		System.out.println(b[0].getName());

	}

	@Override
	public String getMessage() {
		return null;
	}

	@Override
	public int getAge() {
		return 0;
	}
}
