package rmi_440;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryServerListener extends RegistryListener{

	ConcurrentHashMap<String, RemoteObjectReference> regStore;
	ObjectInputStream in;
	
	public RegistryServerListener(int listenPortnum, ConcurrentHashMap<String, RemoteObjectReference> regStore) {
		super(listenPortnum);
		this.regStore = regStore;
	}

	public void run() {
		RemoteObjectReference ror;
		
		try {
			ss = new ServerSocket(listenPortnum);
		} catch (IOException e1) {
			System.err.println("In RegistryClientListener: Could not initialize ServerSocket");
			e1.printStackTrace();
		}
		
		while (true) {
			try {
				listener = ss.accept();
				in = new ObjectInputStream(listener.getInputStream());
				try {
					ror = (RemoteObjectReference)in.readObject();
					System.out.println(ror);
					System.out.println(ror.getObjectName());
					System.out.println(regStore);
					regStore.put(ror.getObjectName(), ror);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
		 	}
		}
	}
	
}
