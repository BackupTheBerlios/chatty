package chatty.net;

import chatty.gui.ChatInstance;
import java.net.*;
import java.util.*;

public class Server implements Runnable {
	
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
	
	boolean startServer() {
		//Alten Server runterfahren
		stopServer();
		//Neuen Server hochfahren
		try {
			serverSocket = new ServerSocket(1111);
			window.appendText("Hochgefahren",this);
		} catch (Exception e) {
			window.appendError("Hochfahren nicht m�glich");
			return false;
		}
		list = new ArrayList();
		Thread thread = new Thread(this);
		thread.start();	
		return true;
	}
	
	boolean isRunning() {
		return serverSocket!=null;
	}
	
	void stopServer() {
		if (!isRunning())
			return;
		//Runterfahren des Servers
		window.appendText("Entfernen des Servers",this);
		try {
			serverSocket.close();
		} catch (Exception e) {}
		serverSocket = null;
		//Entfernen der vorhandenen ServerThreads
		if (list==null) 
			return;
		while (!list.isEmpty()) {
			ServerThread t = (ServerThread)(list.get(0));
			window.appendText("Entfernen des Serverthreads #"+t.getClientData().getID(),this);
			t.disconnect();
		}
		window.appendText("Server ordnungsgem�� heruntergefahren",this);
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
	
	void sendTo(ServerThread from, int toID,String msg){
		for (int i=0; i<list.size();i++) {
			ServerThread thread = (ServerThread)(list.get(i));
			if (thread.getClientData().getID()==toID) {
				thread.send(msg);
				return;
			}
		}
		window.appendError("Fehler beim Senden an ID "+toID);
	}
	
	void sendToAll(String txt){
		for (int i=0; i<list.size(); i++) {
			ServerThread t = (ServerThread)(list.get(i));
			t.send(txt);
		}
	}
	
	void sendToAllOther(String txt,ServerThread notMe){
		for (int i=0; i<list.size(); i++) {
			ServerThread t = (ServerThread)(list.get(i));
			if (t!=notMe)
				t.send(txt);
		}
	}
	
	/**
	 * Sendet die Liste mit den schon verbundenen Clients
	 * @param ServerThread der Liste erhalten soll
	 */
	void sendClientList(String command,ServerThread thread){
	    for (int i=0; i<list.size(); i++) {
			ServerThread t = (ServerThread)(list.get(i));
			thread.send(command+t.getClientData().convertToString());
		}
	}
		
}