package chatty.net;

import chatty.gui.ChatInstance;
import java.net.Socket;

class ServerThread extends Connection {
	private Server server;
		
	ServerThread(Server server, int ID, Socket socket, 
			ChatInstance window) throws Exception {
		super(window);
		this.server = server;
		this.ID = ID;
		if(!connect(socket))
			throw new Exception();
	}
	
	protected void onDisconnection(){
		server.removeServerThread(this);
		server.sendToAll("OUT "+getName()+" hat den Chat verlassen");
		window.appendText(getName()+" ausgeloggt");
	}
	
	protected void initProtocol() {
		try {
		    send("YOURID "+ID);
		    send("OUT Hallo auf Server "+server.getName());
		} catch (Exception e) {
			window.appendError("Verbindungsfehler mit Client #"+ID);
			disconnect();
		}		
	}
	
	protected void runProtocol(String txt) {
		if (txt.startsWith("LOGIN")) {
			//Name des Clients einlesen
			setName(txt.substring(6));
			server.sendToAll("OUT "+getName()+" hat den Chat betreten");
			window.appendText(getName()+" eingeloggt");
		} else if (txt.startsWith("SENDTOALL")) {
			server.sendToAll("OUT "+getName()+": "+txt.substring(10));
		}
	}
}
