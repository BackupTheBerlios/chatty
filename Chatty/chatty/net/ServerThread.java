package chatty.net;

import chatty.gui.ChatInstance;
import java.io.*;
import java.net.*;


class ServerThread implements Runnable {
	private Server server;
	private ChatInstance window;
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
		socket.setSoTimeout(1000);
		
		in = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream()));
		
		Thread thread = new Thread(this);
		thread.start();
		System.out.println("Chattyserver - Verbindung mit Client #"+ID+" hergestellt");
	}
	
	void disconnect() {
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
		while (socket!=null) {
			String txt;
			try {
				System.out.println("vorreadl");
			    txt = in.readLine();
			    System.out.println("nachread");
				if (txt==null) {
					System.err.println("client weg");
				    disconnect();
					continue;
				}
			} catch (Exception e) {
				System.out.println("except");
				//TODO Hängeproblem beim brutalen Trennen
				//disconnect();
				continue;
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
