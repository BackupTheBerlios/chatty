package chatty.net;

import chatty.gui.ChatInstance;
import chatty.tools.ClientList;

/*
 * DID parseteil "revolutioniert"
 */

public class Client extends Connection {
	
	private ClientList clientList; 
    
    Client(ChatInstance window) {
		super(window);
		clientList = new ClientList();
	}
	
	void sendMessage(String txt) {
		send("SENDTOALL * "+txt);
	}
	
	void sendMessage(ClientData toClient,String msg){
		send("SEND "+toClient.getID()+" "+msg);
	}
	
	protected void onDisconnection() {
		clientList.clear();
	}
	
	protected void initProtocol() {
		send("LOGIN * "+getClientData().convertToString());
	}
	
	protected void runProtocol(String txt) {
	    String[] msg = txt.split(" ",3);//Aufsplitten in Befehl und Nachricht
	    if(msg[0].equals("YOURID")){
        	getClientData().setID(Integer.valueOf(msg[2]).intValue());
        } else if(msg[0].equals("OUT")){
        	//Text is angekommen
        	ClientData source = null;
        	if (!msg[1].equals("*")) {
        		int ID = Integer.parseInt(msg[1]);
        		source = clientList.getClientByID(ID);
        	}
        	window.appendText(msg[2],source);
        } else if(msg[0].equals("ADDCL")){
            //Client soll in Liste hinzugefügt werden
            ClientData neu = new ClientData();
            neu.setFromString(msg[2]);
            clientList.add(neu);
            window.addToList(neu);
        } else if(msg[0].equals("REMCL")){
            ClientData clientToRemove = new ClientData();
            clientToRemove.setFromString(msg[2]);
            removeFromClientList(clientToRemove);
            window.removeFromList(clientToRemove);
            window.appendText(clientToRemove.getName()+" hat den Chat verlassen",this);
        }
	}
        
        private void removeFromClientList(ClientData clientToRemove){
            for(int i=0;i<clientList.size();i++){
                ClientData c = (ClientData)clientList.get(i);
                if(clientToRemove.getID()==c.getID()){
                    clientList.remove(i);
                    return;
                }
            }
        }

}