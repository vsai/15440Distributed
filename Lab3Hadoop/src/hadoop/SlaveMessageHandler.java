package hadoop;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messageProtocol.InitiateConnection;

/***
 * Created on the master, and is basically used for handling messages between
 * the master and the slave.
 * Overall: Create a connection, send a map job, read back a result success, send result job, read back success 
 *
 */

public class SlaveMessageHandler extends Thread{

	Socket s;
	ObjectInputStream in;
	ObjectOutputStream out;
	SlaveWrapper slave;
	
	public SlaveMessageHandler (InitiateConnection initConn, Socket s, ObjectInputStream in, ObjectOutputStream out) {
		this.s = s;
		this.in = in;
		this.out = out;
		this.slave = new SlaveWrapper(initConn.getSelfIp(), initConn.getSelfPortnum());
	}
	
	public void run() {
		
	}
	
}
