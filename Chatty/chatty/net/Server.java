package chatty.net;

import chatty.gui.ChatInstance;
import java.net.*;
import java.util.*;

class Server implements Runnable {
	
	private String name;
	private ChatInstance window;
	private ServerSocket serverSocket;
	private ArrayList list;
	private int freeID;
	
	Server(ChatInstance window) {
		this.window = window;
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	String getName() {
		return name;
	}
	
	void startServer() {
		//Alten Server runterfahren
		stopServer();
		//Neuen Server hochfahren
		try {
			serverSocket = new ServerSocket(1111);
			window.appendText("Hochgefahren");
		} catch (Exception e) {
			window.appendError("Hochfahren nicht möglich");
			return;
		}
		list = new ArrayList();
		Thread thread = new Thread(this);
		thread.start();	
	}
	
	boolean isRunning() {
		return serverSocket!=null;
	}
	
	void stopServer() {
		if (!isRunning())
			return;
		//Runterfahren des Servers
		window.appendText("Entfernen des Servers");
		try {
			serverSocket.close();
		} catch (Exception e) {}
		serverSocket = null;
		//Entfernen der vorhandenen ServerThreads
		if (list==null) 
			return;
		while (!list.isEmpty()) {
			ServerThread t = (ServerThread)(list.get(0));
			window.appendText("Entfernen des Serverthreads #"+t.getID());
			t.disconnect();
		}
		window.appendText("Server ordnungsgemäß heruntergefahren");
	}
	
	public void run() {
		while (isRunning()) {
			try {
				Socket socket = serverSocket.accept();
				ServerThread thread = new ServerThread(this,freeID,socket,window);				
				list.add(thread);
				freeID++;
			} catch (Exception e) {}
		}
	}
	
	void removeServerThread(ServerThread t){
	    list.remove(t);
	}
	
	void sendToAll(String txt){
		for (int i=0; i<list.size(); i++) {
			ServerThread t = (ServerThread)(list.get(i));
			t.send(txt);
		}
	}
	
	/**
	 * Sendet die Liste mit den schon verbundenen Clients
	 * @param ServerThread der Liste erhalten soll
	 */
	void sendClientList(ServerThread thread){
	    for (int i=0; i<list.size(); i++) {
			ServerThread t = (ServerThread)(list.get(i));
			if(t!=thread)
			    thread.send("ADDLI "+t.myClientData().convertToString());
		}
	}
		
}