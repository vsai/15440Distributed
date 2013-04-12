package hadoop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messageProtocol.InitiateConnection;
import messageProtocol.MapResult;
import messageProtocol.ReduceResult;

/***
 * Created on the master, and is basically used for handling messages between
 * the master and the slave.
 * Overall: Create a connection, send a map job, read back a result success, send result job, read back success 
 */

public class SlaveMessageHandler extends Thread{

	ObjectInputStream in;
	
	public SlaveMessageHandler (ObjectInputStream in) {
		this.in = in;
	}
	
	public void run() {
		Object inobj;
		while (true) {
			try {
				inobj = in.readObject();
				if (inobj instanceof MapResult) {
					
				} else if (inobj instanceof ReduceResult) {
					
				}
			} catch (IOException e) {
				/* 
				 * Usually throws this exception when a broken stream happens
				 * Recover this by re-writing the original partition sent to
				 * a different slave 
				 */
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}