package rmi_440.registry;

import java.net.ServerSocket;
import java.net.Socket;

public abstract class RegistryListener extends Thread{
	ServerSocket ss;
	Socket listener;
	int listenPortnum;
	public RegistryListener(int listenPortnum) {
		this.listenPortnum = listenPortnum;
	}
}
