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
		server.startServer();
		client.connect("localhost");
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
	
	public void send(String txt) {
		client.send(txt);
	}
	
	public boolean isConnected() {
		return client.isConnected();
	}
	
	//Handling von Server und Client
	public void exit() {
		stopServer();
		disconnect();
	}
	
	

}
