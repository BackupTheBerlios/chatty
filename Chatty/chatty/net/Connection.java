package chatty.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

import chatty.gui.ChatInstance;

abstract class Connection implements Runnable{
	
	protected ChatInstance window;
	protected boolean isConnected;
	protected Socket socket;
	protected int ID;
	protected BufferedReader in;
	protected BufferedWriter out;
	
	Connection(ChatInstance window){
		this.window=window;
		this.isConnected=false;
		this.socket=null;
		this.ID=-1;
		this.in=null;
		this.out=null;
	}
	
	boolean connect(String address) {
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
	
	boolean connect(Socket socket) {
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
	
	private void connectionDetails() throws Exception {
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
	
	boolean isConnected() {
		return isConnected;
	}
	
	abstract protected void onDisconnection();
	
	synchronized void disconnect(){
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
				window.appendError("Verbindungsproblem durch ID"+ID);
			}
			runProtocol(txt);
		}		    
	}
	
	int getID() {
		return ID;
	}
	
	void send(String txt) throws Exception {
		out.write(txt); out.newLine(); out.flush();
	}
	
	abstract protected void initProtocol();
	
	abstract protected void runProtocol(String msg);
	
}
