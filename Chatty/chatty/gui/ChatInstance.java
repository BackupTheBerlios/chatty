package chatty.gui;

import chatty.net.ClientData;

/**
 * Oberklasse für Mainwindow und Konsole ...
 */

public interface ChatInstance {
	public void updateNetStatus();
	public void appendText(String text,Object source);
	public void appendError(String text);
	public void clearText();
	public void addToList(ClientData newClient);
	public void removeFromList(ClientData client);
}
