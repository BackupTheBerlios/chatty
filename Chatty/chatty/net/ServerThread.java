package chatty.net;

import chatty.gui.ChatInstance;
import java.io.*;
import java.net.*;


class ServerThread implements Runnable {
	private Server server;
	private ChatInstance window;
	private boolean isConnected;
	private Socket socket;
	private int ID;
	private BufferedReader in;
	private BufferedWriter out;
	
	int getID(){
	    return ID;
	}
	
	ServerThread(Socket socket, int ID, Server server, ChatInstance window) throws Exception {
		this.window = window;
		this.server = server;
		this.ID = ID;
		this.socket = socket;
		isConnected=true;
		in = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream()));
		
		Thread thread = new Thread(this);
		thread.start();
		System.out.println("Chattyserver - Verbindung mit Client #"+ID+" hergestellt");
	}
	
	void disconnect() {
		if (!isConnected)
			return;
		isConnected=false;
		try {
			out.flush();
			out.close();
			socket.close();
		} catch (Exception e) {}
		socket = null;
		in = null;
		out = null;
		server.removeServerThread(this);
		System.out.println("Chattyserver - Verbindung mit Client #"+ID+" beendet");
	}
	
	public void run() {
		protocolStart();
		System.out.println("Chattyserver - Beginne Warten auf Input von Client #"+ID);
		while (isConnected) {
			String txt = null;
			try {
			    txt = in.readLine();
			    if (txt==null) { // Verbindung wurde von Client aktiv beendet
			    	disconnect();
			    	continue;
			    }
			} catch (SocketException e) {
				//Server wurde heruntergefahren (socket closed) 
				//oder Client abgebrochen (connection reset)
				disconnect();
				continue;
			} catch (Exception e) {
				System.err.println("Chattyserver - Verbindungsfehler");
			}
			protocolIncoming(txt);
		}		    
		System.out.println("Chattyserver - Beende Warten auf Input von Client #"+ID);
	}
	
	void send(String txt) throws Exception {
		out.write(txt); out.newLine(); out.flush();
	}
	
	private void protocolStart() {
		try {
		    send("YOURID "+ID);
		    send("OUT HALLO AUF CHATTYSERVER.");
		    send("OUT DU BIST JETZT VERBUNDEN");
		    send("OUT DEINE ID IST "+ID);
		} catch (Exception e) {
			System.err.println("Chattyserver - Verbindungsfehler mit Client #"+ID);
			disconnect();
		}		
	}
	
	private void protocolIncoming(String txt) {
		server.sendToAll(txt,ID);
	}
}
