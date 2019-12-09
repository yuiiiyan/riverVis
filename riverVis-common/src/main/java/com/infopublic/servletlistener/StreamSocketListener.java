package com.infopublic.servletlistener;

import com.streamsocket.server.StreamServer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StreamSocketListener  implements ServletContextListener {
	
	private StreamSocketThread socketThread;
	
	@Override
	public void contextDestroyed(ServletContextEvent e){
		if (socketThread != null && !socketThread.isInterrupted()){
			socketThread.interrupt();
		}
	}
	
	@Override
	public void contextInitialized(ServletContextEvent e){
		System.out.println("Server contextInitialized over");
		if (socketThread == null){
			socketThread = new StreamSocketThread(null);
			socketThread.start(); 
		}
	}


}

class StreamSocketThread extends Thread{
	Integer count = 0;
	private StreamServer serverSocket;

	public StreamSocketThread(StreamServer serverSocket){
		if (serverSocket == null){
			serverSocket = new StreamServer();
		}
		this.serverSocket=serverSocket;
	}
	public void run(){
		serverSocket.bind();
	}
}