package chatty.net;

import java.util.ArrayList;
import chatty.gui.ChatInstance;

/**
 * TODO Parseteil optimaler gestalten (STEFAN)<br>
 */

public class Client extends Connection {
	
	private ArrayList ClientList;
    
    Client(ChatInstance window) {
		super(window);
		ClientList = new ArrayList();
	}
	
	void sendMessage(String txt) {
		send("SENDTOALL "+txt);
	}
	
	protected void onDisconnection() {
		
	}
	
	protected void initProtocol() {
		send("LOGIN "+myClientData().convertToString());
	}
	
	protected void runProtocol(String txt) {
		if(txt.startsWith("YOURID ")){
        	myClientData().setID(Integer.valueOf(txt.substring(7)).intValue());
        } else if(txt.startsWith("OUT ")){
        	//Text is angekommen
            window.appendText(txt.substring(4));
        } else if(txt.startsWith("ADDLI ")){
            //Client soll in Liste hinzugefügt werden
            ClientData neu = new ClientData();
            neu.setFromString(txt.substring(6));
            ClientList.add(neu);
        }
	}

}