package chatty.net;

import chatty.gui.ChatInstance;
import java.net.Socket;

class ServerThread extends Connection {
	private Server server;
		
	ServerThread(Socket socket, int ID, Server server, ChatInstance window) throws Exception {
		super(window);
		this.server = server;
		this.ID = ID;
		if(!connect(socket))
			throw new Exception();
	}
	
	protected void onDisconnection(){
		server.removeServerThread(this);
	}
	
	protected void initProtocol() {
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
	
	protected void runProtocol(String txt) {
		server.sendToAll(txt,ID);
	}
}
