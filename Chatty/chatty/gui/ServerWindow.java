package chatty.gui;

/**
 * Konsolenfenster für Serverapplikation
 */

public class ServerWindow implements ChatInstance {

	public void updateNetStatus() {
	}

	public void appendText(String text) {
		System.out.println(text);
	}

	public void appendError(String text) {
		System.err.println(text);
	}

	public void clearText() {
	}

}
