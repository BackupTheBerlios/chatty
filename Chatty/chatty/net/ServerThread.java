package chatty.net;

import chatty.gui.ChatInstance;
import java.net.Socket;

class ServerThread extends Connection {
	private Server server;
		
	ServerThread(Server server, int ID, Socket socket, 
			ChatInstance window) throws Exception {
		super(window);
		this.server = server;
		myClientData().setID(ID);
		if(!connect(socket))
			throw new Exception();
	}
	
	protected void onDisconnection(){
		server.removeServerThread(this);
		server.sendToAll("REMCL "+myClientData().convertToString());
	    window.appendText(getName()+" ausgeloggt");
	}
	
	protected void initProtocol() {
		try {
		    send("YOURID "+getID());
		    send("OUT Hallo auf Server "+server.getName());
		} catch (Exception e) {
			window.appendError("Verbindungsfehler mit Client #"+getID());
			disconnect();
		}		
	}
	
	private void loginClient(String daten){
	    //Daten des Clients auslesen - ID noch fehlerhaft
	    int IDtemp = getID();
	    myClientData().setFromString(daten);
	    myClientData().setID(IDtemp);
		//Gibt jedem Client die Anweisung, den neuen in seine Liste aufzunehmen
		server.sendToAll("ADDCL "+myClientData().convertToString());
		server.sendToAll("OUT "+getName()+" hat den Chat betreten");
		server.sendClientList(this);
		window.appendText(getName()+" eingeloggt");
	}
	
	protected void runProtocol(String txt) {
		String[] msg = txt.split(" ",2);
	    if (msg[0].equals("LOGIN")) {
			loginClient(msg[1]);
		} else if (msg[0].equals("SENDTOALL")) {
			server.sendToAll("OUT "+getName()+": "+msg[1]);
		}
	}
}
