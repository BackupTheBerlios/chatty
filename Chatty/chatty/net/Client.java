package chatty.net;

import chatty.gui.ChatInstance;

/**
 * TODO Parseteil optimaler gestalten (STEFAN)<br>
 * 		Username Anmeldung (Martin)
 */

public class Client extends Connection {
	
	Client(ChatInstance window) {
		super(window);
	}
	
	void sendMessage(String txt) {
		send("SENDTOALL "+txt);
	}
	
	protected void onDisconnection() {
		
	}
	
	protected void initProtocol() {
		send("LOGIN "+getName());
	}
	
	protected void runProtocol(String txt) {
		if(txt.substring(0,7).equals("YOURID ")){
        	ID = Integer.valueOf(txt.substring(7)).intValue();
        } else if(txt.substring(0,4).equals("OUT ") || true){
        	window.appendText(txt.substring(4));
        }
	}

}