package chatty.net;

import chatty.gui.ChatInstance;
import java.net.*;
import java.util.*;

class Server implements Runnable {
	
	private ChatInstance window;
	private ServerSocket serverSocket;
	private ArrayList list;
	private int freeID;
	
	Server(ChatInstance window) {
		this.window = window;
	}
	
	void startServer() {
		//Alten Server runterfahren
		stopServer();
		//Neuen Server hochfahren
		System.out.println("Chattyserver - Hochfahren");
		try {
			serverSocket = new ServerSocket(1111);
			System.out.println("Chattyserver - Hochgefahren");
		} catch (Exception e) {
			System.err.println("Chattyserver - Hochfahren des Servers nicht möglich");
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
		System.out.println("Chattyserver - Entfernen des Servers");
		try {
			serverSocket.close();
		} catch (Exception e) {}
		serverSocket = null;
		//Entfernen der vorhandenen ServerThreads
		if (list==null) 
			return;
		while (!list.isEmpty()) {
			ServerThread t = (ServerThread)(list.get(0));
			System.out.println("Chattyserver - Entfernen des Serverthreads #"+t.getID());
			t.disconnect();
		}
	}
	
	public void run() {
		System.out.println("Chattyserver - Warten begonnen");
		while (isRunning()) {
			try {
				Socket socket = serverSocket.accept();
				ServerThread thread = new ServerThread(socket,freeID,this,window);
				sendToAll("hat den Chat betreten",freeID);				
				list.add(thread);
				freeID++;
			} catch (Exception e) {}
		}
		System.out.println("Chattyserver - Warten beendet");
	}
	
	void removeServerThread(ServerThread t){
	    list.remove(t);
	    sendToAll("hat den Chat verlassen",t.getID());
	}
	
	void sendToAll(String txt,int ID){
		for (int i=0; i<list.size(); i++) {
			ServerThread t = (ServerThread)(list.get(i));
			try{
			    if(ID==t.getID()){
			        t.send("OUT "+"ME: "+txt);
			    }
			    else{
			      t.send("OUT "+"Client"+ID+": "+txt);
			    }
			} catch(Exception e){
			    System.err.println("Chattyserver - Send error!");
			}
		}
	}
		
}