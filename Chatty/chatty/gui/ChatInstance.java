package chatty.gui;

public interface ChatInstance {
	public void setConnected(boolean connected);
	public void appendText(String text);
	public void clearText();
}
