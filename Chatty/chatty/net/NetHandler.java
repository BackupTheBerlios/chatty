package chatty.net;

import chatty.gui.ChatInstance;

public class NetHandler {
	
	private ChatInstance window;
	private Server server;
	private Client client;
	
	public NetHandler(ChatInstance window) {
		this.window = window;
		server = new Server(window);
		client = new Client(window);
	}
	
	
	//Sever Handling
	public void startServer() {
		server.startServer();
		client.connect("localhost");
	}
	
	public void stopServer() {
		server.stopServer();
	}
	
	//Client Handling
	public void connect(String address) {
		client.connect(address);
	}
	
	public void disconnect() {
		client.disconnect();
	}
	
	public void send(String txt) {
		client.send(txt);
	}
	
	//Handling von Server und Client
	public void exit() {
		stopServer();
		disconnect();
	}
	
	

}
