package chatty.net;

import java.io.*;
import java.net.*;
import chatty.gui.ChatInstance;

/**
 * Oberklasse für Client, Server und sonstige Verbindungen
 */

abstract class Connection implements Runnable{
	
	protected ChatInstance window;
	private boolean isConnected;
	private Socket socket;
	private BufferedReader in;
	private BufferedWriter out;
	private ClientData clientData;
	
	Connection(ChatInstance window){
		this.window=window;
		this.isConnected=false;
		this.socket=null;
		this.in=null;
		this.out=null;
		clientData = new ClientData();
		clientData.setID(-1);
	}
	
	final String getName() {
		return clientData.getName();
	}
	
	final void setName(String name) {
		clientData.setName(name);
	}
	
	final ClientData myClientData(){
	    return clientData;
	}
	
	final boolean connect(String address) {
		//alte verbindung trennen
		disconnect();
	    //neue verbindungsdaten extrahieren
		String ip;
		int port = 0;		
		try {
			int pos = address.indexOf(':');
			if (pos==-1) {
				ip = address;
				port = 1111;
			} else {
				ip = address.substring(0,pos);
				port = Integer.valueOf(address.substring(pos+1)).intValue();
			}
		} catch (Exception e) {
			window.appendError("Adresse fehlerhaft");
			return false;
		}
	    //verbindung herstellen
	    try {
	    	socket = new Socket(ip,port);
	    	connectionDetails();
	    } catch (Exception e){
	    	window.appendError("Adresse nicht erreichbar");
	    	return false;
	    }
	    return true;
	}
	
	final boolean connect(Socket socket) {
		//alte verbindung trennen
		disconnect();
		//verbindung herstellen
		this.socket = socket;
		if (socket==null)
			return false;
		try {
			connectionDetails();
		} catch (Exception e) {
			window.appendError("Adresse nicht erreichbar");
	    	return false;
		}
		return true;
	}
	
	/**
	 * Initialisiert IO Objekte
	 * @throws Fehler wenn Verbindung nicht hergestellt (Exception)
	 */
	final private void connectionDetails() throws Exception {
		try {
	    	in = new BufferedReader(
	    			new InputStreamReader(socket.getInputStream()));
	    	out = new BufferedWriter(
	    			new OutputStreamWriter(socket.getOutputStream()));
	    	window.clearText();
	    	window.appendText("Verbindung hergestellt");
	    } catch (Exception e) {
	    	socket = null;
	    	in = null;
	    	out = null;
	    	throw e;
	    }
	    isConnected=true;
	    new Thread(this).start();
	    window.updateNetStatus();
	}
	
	final boolean isConnected() {
		return isConnected;
	}
	
	/**
	 * Finale Methode die in Disconnect aufgerufen wird
	 *
	 */
	abstract protected void onDisconnection();
	
	final synchronized void disconnect(){
		if (isConnected) {
			isConnected=false;
			window.appendText("Verbindung wird getrennt");
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
			onDisconnection();
			window.updateNetStatus();
		}
	}
	
	public void run(){
		initProtocol();
		while (isConnected) {
			String txt = null;
			try {
			    txt = in.readLine();
			    if (txt==null) { // Verbindung wurde von Gegner aktiv beendet
			    	disconnect();
			    	continue;
			    }
			} catch (SocketException e) {
				//Thread wurde beendet (socket closed) 
				//oder Gegner abgebrochen (connection reset)
				disconnect();
				continue;
			} catch (Exception e) {
				window.appendError("Verbindungsproblem durch ID"+getID());
			}
			runProtocol(txt);
		}		    
	}
	
	final int getID() {
		return clientData.getID();
	}
	
	boolean send(String txt) {
		try {
		out.write(txt); out.newLine(); out.flush();
		} catch (Exception e) {
			window.appendError("Fehler beim Senden");
			return false;
		}
		return true;
	}
	
	abstract protected void initProtocol();
	
	abstract protected void runProtocol(String msg);
	
}
