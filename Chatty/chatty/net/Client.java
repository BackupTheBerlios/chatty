package chatty.net;

import java.util.ArrayList;

import chatty.gui.ChatInstance;

/*
 * DID parseteil "revolutioniert"
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
		ClientList.clear();
	}
	
	protected void initProtocol() {
		send("LOGIN "+myClientData().convertToString());
	}
	
	protected void runProtocol(String txt) {
	    String[] msg = txt.split(" ",2);//Aufsplitten in Befehl und Nachricht
	    if(msg[0].equals("YOURID")){
        	myClientData().setID(Integer.valueOf(msg[1]).intValue());
        } else if(msg[0].equals("OUT")){
        	//Text is angekommen
            window.appendText(msg[1]);
            
        } else if(msg[0].equals("ADDCL")){
            //Client soll in Liste hinzugefügt werden
            ClientData neu = new ClientData();
            neu.setFromString(msg[1]);
            ClientList.add(neu);
            window.addToList(neu);
        } else if(msg[0].equals("REMCL")){
            ClientData clientToRemove = new ClientData();
            clientToRemove.setFromString(msg[1]);
            removeFromClientList(clientToRemove);
            window.removeFromList(clientToRemove);
            window.appendText(clientToRemove.getName()+" hat den Chat verlassen");
        }
	}
        
        private void removeFromClientList(ClientData clientToRemove){
            for(int i=0;i<ClientList.size();i++){
                ClientData c = (ClientData)ClientList.get(i);
                if(clientToRemove.getID()==c.getID()){
                    ClientList.remove(i);
                    return;
                }
            }
        }

}