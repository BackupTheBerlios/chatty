package chatty.gui;

/**
 * Hauptfenster für Chattyclient TODO Benutzername darf kein "/" enthalten
 * entweder neue Eingabe oder herausschneiden
 */

import chatty.net.ClientData;
import chatty.net.NetHandler;
import chatty.tools.ListTools;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class MainWindow extends JFrame implements ChatInstance, ActionListener,
		WindowListener {

	private NetHandler nethandler;

	private JMenuItem miConnect, miDisconnect, miExit, miAbout, miStartServer,
			miStopServer;

	private JButton bConnect, bSend;

	private JTextPane taOut;

	private JTextField tfIn;

	private JList clientJList;
	private DefaultListModel clientList;

	//Initialisierung des Fensters und Starten des NetHandlers
	public MainWindow() {
		//Fenster
		super("Chatty");
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
		}
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);

		//Menu Start
		JMenuBar menubar = new JMenuBar();

		//Menu - Chatty
		JMenu mChatty = new JMenu("Chatty");
		miConnect = new JMenuItem("Verbinden..");
		miConnect.addActionListener(this);
		mChatty.add(miConnect);
		miDisconnect = new JMenuItem("Verbindung trennen");
		miDisconnect.addActionListener(this);
		mChatty.add(miDisconnect);
		mChatty.addSeparator();
		miExit = new JMenuItem("Beenden");
		miExit.addActionListener(this);
		mChatty.add(miExit);
		menubar.add(mChatty);

		//Menu - ChattyServer
		JMenu mServer = new JMenu("ChattyServer");
		miStartServer = new JMenuItem("Server starten");
		miStartServer.addActionListener(this);
		mServer.add(miStartServer);
		miStopServer = new JMenuItem("Server beenden");
		miStopServer.addActionListener(this);
		mServer.add(miStopServer);
		menubar.add(mServer);

		//Menu - Hilfe
		JMenu mHelp = new JMenu("Hilfe");
		miAbout = new JMenuItem("Über..");
		miAbout.addActionListener(this);
		mHelp.add(miAbout);
		menubar.add(mHelp);

		//Menu Ende
		setJMenuBar(menubar);

		//Content Pane Start
		JPanel p = new JPanel(new BorderLayout());

		//Content Pane - Toolbar
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		bConnect = new JButton("Verbinden..");
		bConnect.addActionListener(this);
		toolbar.add(bConnect);
		p.add(toolbar, BorderLayout.NORTH);

		//Content Pane - Center
		JPanel pc = new JPanel(new BorderLayout());
		taOut = new JTextPane();
		taOut.setFont(new Font("Serif", Font.PLAIN, 14));
		taOut.setEditable(false);
		//taOut.setLineWrap(true);
		JScrollPane scrollLog = new JScrollPane(taOut);
		scrollLog.setPreferredSize(new Dimension(520, 480));
		scrollLog.setBorder(BorderFactory.createEtchedBorder());
		pc.add(scrollLog, BorderLayout.WEST);
		clientList = new DefaultListModel();

		clientJList = new JList(clientList);
		clientJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		clientJList.setFont(new Font("SansSerif", Font.ITALIC, 15));
		JScrollPane scrollList = new JScrollPane(clientJList);
		scrollList.setPreferredSize(new Dimension(120, 480));
		scrollList.setBorder(BorderFactory.createEtchedBorder());
		pc.add(scrollList, BorderLayout.EAST);
		p.add(pc, BorderLayout.CENTER);

		//Content Pane - Süd
		JPanel ps = new JPanel(new FlowLayout());
		tfIn = new JTextField(20);
		tfIn.addActionListener(this);
		ps.add(tfIn);
		bSend = new JButton("Senden");
		bSend.addActionListener(this);
		ps.add(bSend);
		ps.setBorder(BorderFactory.createEtchedBorder());
		p.add(ps, BorderLayout.SOUTH);

		//Content Pane Ende
		setContentPane(p);

		//Fenster
		pack();
		setResizable(false);
		setVisible(true);

		//NetHandler erstellen
		nethandler = new NetHandler(this);
		updateNetStatus();

		//Username erfragen
		String name = null;
		while (name == null)
			name = JOptionPane.showInputDialog("Username wählen", System
					.getProperty("user.name"));
		nethandler.setName(name);
	}

	//Methoden der ChatInstance

	public void updateNetStatus() {
		String t = "Chatty";
		if (nethandler.isConnected()) {
			t += " - verbunden als " + nethandler.getClientData().getName();
			String alle = "Alle";
			clientList.addElement(alle);
			clientJList.setSelectedValue(alle,true);
			tfIn.requestFocus();
		} else {
			t += " - nicht verbunden";
			clientList.removeAllElements();
		}
		if (nethandler.isServer())
			t += " - Servermodus";
		bSend.setEnabled(nethandler.isConnected());
		setTitle(t);
	}

	public void appendText(String txt,Object source) {
		Document doc = taOut.getDocument();
		String name = "";
		SimpleAttributeSet attribute = new SimpleAttributeSet();
		if(source instanceof ClientData){
			ClientData user = (ClientData)source;
			name = user.getName()+": ";
			StyleConstants.setForeground(attribute,user.getColor());
			if (user.getID()==nethandler.getClientData().getID())
				StyleConstants.setItalic(attribute,true);				
		} else if (source==null) {
			StyleConstants.setBold(attribute,true);
		}
		try {
			doc.insertString(doc.getLength(),name+txt+'\n',attribute);
		} catch (Exception e) {}
		taOut.setCaretPosition(doc.getLength());
	}

	public void appendError(String txt) {
		JOptionPane.showMessageDialog(this, txt, "Fehler",
				JOptionPane.ERROR_MESSAGE);
	}

	public void clearText() {
		taOut.setText("");
	}

	public void addToList(ClientData newClient) {
	    ListTools.nextColor();
	    clientList.addElement(newClient);
		Object o = clientList.get(clientJList.getSelectedIndex());
		ListTools.sortClientList(clientList);
		clientJList.setSelectedIndex(clientList.indexOf(o));
	}

	public void removeFromList(ClientData clientToRemove) {
	    Object o = clientList.get(clientJList.getSelectedIndex());
	    ListTools.removeFromClientList(clientList,clientToRemove);
	    int i = clientList.indexOf(o);
	    //wenn selektierter client gelöscht, selektiere alle
	    if (i<0)
	        i=0;
	    clientJList.setSelectedIndex(i);
	}

	//EventHandling - ActionListener
	public void actionPerformed(ActionEvent event) {
		Object src = event.getSource();

		//Menu - Chatty - Verbinden
		if (src == bConnect || src == miConnect) {
			String s = JOptionPane
					.showInputDialog(
							"Bitte Adresse nach dem Schema 'address:port' oder 'address' eigeben",
							"localhost");
			if (s == null)
				return;
			nethandler.connect(s);

			//Menu - Chatty Verbindung trennen
		} else if (src == miDisconnect) {
			nethandler.disconnect();

			//Menu - Chatty - Beenden
		} else if (src == miExit) {
			nethandler.exit();
			System.exit(0);

			//Menu - ChattyServer - Server starten
		} else if (src == miStartServer) {
			nethandler.startServer();

			//Menu - ChattyServer - Server beenden
		} else if (src == miStopServer) {
			nethandler.stopServer();

			//Menu - Hilfe - Über
		} else if (src == miAbout) {
			JOptionPane.showMessageDialog(this,
					"Chatty\nby Martin Siggel und Stefan Schell\n"
							+ "© 2005 - alle Rechte vorbehalten", "Chatty",
					JOptionPane.INFORMATION_MESSAGE);

			//Senden
		} else if (src == bSend || (src == tfIn && bSend.isEnabled())) {
			String txt = tfIn.getText();
			Object selected = clientJList.getSelectedValue();
			if (selected==null || selected instanceof String)
				nethandler.sendMessage(txt);
			else
				nethandler.sendMessage((ClientData)selected,txt);
			tfIn.requestFocus();
			tfIn.selectAll();
		}
	}

	public void test() {
	}

	//Event Handling - WindowListener
	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		nethandler.exit();
		System.exit(0);
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

}