package chatty.net;

import chatty.gui.ChatInstance;
import java.net.Socket;

class ServerThread extends Connection {
	private Server server;
		
	ServerThread(Server server, int ID, Socket socket, 
			ChatInstance window) throws Exception {
		super(window);
		this.server = server;
		getClientData().setID(ID);
		if(!connect(socket))
			throw new Exception();
	}
	
	protected void onDisconnection(){
		server.removeServerThread(this);
		server.sendToAll("REMCL "+getClientData().getID());
	    window.appendText(getClientData().getName()+" ausgeloggt",this);
	}
	
	protected void initProtocol() {
		try {
		    send("YOURID "+getClientData().getID());
		    send("OUT * Hallo auf Server "+server.getName());
		} catch (Exception e) {
			window.appendError("Verbindungsfehler mit Client #"+getClientData().getID());
			disconnect();
		}		
	}
	
	private void loginClient(String daten){
	    //Daten des Clients auslesen - ID noch fehlerhaft
	    int IDtemp = getClientData().getID();
	    getClientData().setFromString(daten);
	    getClientData().setID(IDtemp);
		//Gibt jedem Client die Anweisung, den neuen in seine Liste aufzunehmen
		server.sendToAllOther("ADDCL * "+getClientData().convertToString(),this);
		server.sendClientList("ADDCL * ",this);
		server.sendToAll("OUT * "+getClientData().getName()+" hat den Chat betreten");
		window.appendText(getClientData().getName()+" eingeloggt",this);
	}
	
	protected void runProtocol(String txt) {
		String[] msg = txt.split(" ",3);
	    if (msg[0].equals("LOGIN")) {
			loginClient(msg[2]);
		} else if (msg[0].equals("SENDTOALL")) {
			server.sendToAll("OUT "+getClientData().getID()+" "+msg[2]);
		} else if (msg[0].equals("SEND")){
			int ID = Integer.parseInt(msg[1]);
			server.sendTo(ID,"OUT "+getClientData().getID()+" "+msg[2]);
			//und nochmal an mich selbst
			if (ID!=getClientData().getID())
				send("OUT "+getClientData().getID()+" "+msg[2]);
		}
	}
}
