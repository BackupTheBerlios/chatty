package chatty.net;

import chatty.gui.ChatInstance;
import java.io.*;
import java.net.*;

/**
 * TODO Parseteil optimaler gestalten (STEFAN)<br>
 * 		Username Anmeldung (Martin)
 */

public class Client implements Runnable {
	
	private ChatInstance window;
	private boolean isConnected;
	private Socket socket;
	private BufferedReader in;
	private BufferedWriter out;
	private int ID;
	
	public Client(ChatInstance window) {
		this.window = window;
		isConnected = false;
		socket = null;
		in = null;
		out = null;
		ID = 0;
	}
	
	public void connect(String address) {
		if (address==null)
			return;
		//alte verbindung trennen
		disconnect();
	    //neue verbindungsdaten extrahieren
		int pos = address.indexOf(':');
	    String ip;
	    int port = 0;
	    if (pos==-1) {
	    	ip = address;
	    	port = 1111;
	    } else {
	    	ip = address.substring(0,pos);
	    	port = Integer.valueOf(address.substring(pos+1)).intValue();
	    }
	    //verbindung herstellen
	    try {
	    	socket = new Socket(ip,port);
	    	socket.setSoTimeout(1000);
	    	in = new BufferedReader(
	    			new InputStreamReader(socket.getInputStream()));
	    	out = new BufferedWriter(
	    			new OutputStreamWriter(socket.getOutputStream()));
	    	isConnected = true;
	    	window.clearText();
	    	window.appendText("Verbindung mit Server hergestellt");
	    } catch (Exception e) {
	    	socket = null;
	    	in = null;
	    	out = null;
	    	window.appendText("Verbindung mit Server nicht möglich");
	    }
	    window.setConnected(isConnected);
	    if (isConnected) {
	    	//thread starten
	    	Thread t = new Thread(this);
	    	t.start();
	    }
	}
	  
	public void disconnect() {
		if (!isConnected)
			return;
		window.appendText("Verbindung wird getrennt");
		isConnected = false;
		window.setConnected(false);
		try {
			out.flush();
			out.close();
			socket.close();
		} catch (Exception e) {
			window.appendText("Fehler beim Trennen der Verbindung");
		}
		in = null;
		out = null;
		socket = null;
	}
		
	public void send(String txt) {
		if (isConnected);
		try {
			out.write(txt);
			out.newLine();
			out.flush();
			window.appendText("client: " + txt);
		} catch (Exception e) {
			window.appendText("Senden nicht möglich");
		}
	}
		
	public void run() {
		while (isConnected) {
			String txt = null;
			try {
				txt = in.readLine();
				if (txt==null) {
					disconnect();
					continue;
				}
			} catch (Exception e) {
				continue;
			}
			protocolIncoming(txt);
        }
    }
	
	private void protocolIncoming(String txt) {
		if(txt.substring(0,7).equals("YOURID ")){
        	ID = Integer.valueOf(txt.substring(7)).intValue();
        } else if(txt.substring(0,4).equals("OUT ") || true){
        	window.appendText("server: " + txt.substring(4));
        }
	}

}