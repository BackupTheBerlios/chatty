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
	
	void send(String txt) {
		try {
			out.write(txt);
			out.newLine();
			out.flush();
			window.appendText("client: " + txt);
		} catch (Exception e) {
			window.appendText("Senden nicht möglich");
		}
	}
	
	protected void onDisconnection() {
		
	}
	
	protected void initProtocol() {
		
	}
	
	protected void runProtocol(String txt) {
		if(txt.substring(0,7).equals("YOURID ")){
        	ID = Integer.valueOf(txt.substring(7)).intValue();
        } else if(txt.substring(0,4).equals("OUT ") || true){
        	window.appendText("server: " + txt.substring(4));
        }
	}

}