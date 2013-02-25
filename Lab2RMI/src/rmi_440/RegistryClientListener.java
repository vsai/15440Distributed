package rmi_440;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

import messageProtocol.Message;

public class RegistryClientListener extends RegistryListener{

	ConcurrentHashMap<String, RemoteObjectReference> regStore;
	BufferedReader in;
	ObjectOutputStream out;
	
	public RegistryClientListener(int listenPortnum, ConcurrentHashMap<String, RemoteObjectReference> regStore) {
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
				in = new BufferedReader(new InputStreamReader(listener.getInputStream()));
				out = new ObjectOutputStream(listener.getOutputStream());
				String request = in.readLine();
				String[] splitRequest = request.split(" ");
				
				if (splitRequest.length > 0) {
					/*
					 * Message Protocol:
					 * Lookup: messageTypeRegLookup objName
					 */
					if (splitRequest[0].equals(Message.messageTypeRegLookup) 
							&& splitRequest.length >1 ) {
						ror = regStore.get(splitRequest[1]);
						out.writeObject(ror);
						out.flush();
						System.out.println("In RegistryClientListener: Wrote RemoteReferenceObject for: " + splitRequest[1]);
					}
				}
				listener.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		 	}
			
			
			
		}
	}
	
}
