package chatty.gui;

public interface ChatInstance {
	public void updateNetStatus();
	public void appendText(String text);
	public void appendError(String text);
	public void clearText();
}
