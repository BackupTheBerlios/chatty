package chatty.net;

import chatty.gui.ChatInstance;
import chatty.gui.ServerWindow;

public class NetHandler {
	
	private ChatInstance window;
	private Server server;
	private Client client;
	
	public NetHandler(ChatInstance window) {
		this.window = window;
		server = new Server(new ServerWindow());
		client = new Client(window);
	}
	
	//Sever Handling
	public void startServer() {
		//Hier vielleicht nen Fehlerdialog ausgeben
	    if(server.startServer())
		    client.connect("localhost");
	    else
	        window.appendError("Server konnte nicht erstellt werden");
	}
	
	public void stopServer() {
		server.stopServer();
	}
	
	public boolean isServer() {
		return server.isRunning();
	}
	
	//Client Handling
	public void connect(String address) {
		client.connect(address);
	}
	
	public void disconnect() {
		client.disconnect();
	}
	
	public void sendMessage(String txt) {
		client.sendMessage(txt);
	}
	
	public boolean isConnected() {
		return client.isConnected();
	}
	
	//Handling von Server und Client
	public void setName(String name) {
		client.setName(name);
		server.setName(name);
	}
	
	public String getClientName() {
		return client.getName();
	}
	
	public void exit() {
		stopServer();
		disconnect();
	}
	
}
