
package chatty.gui;

import java.awt.Font;

import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import chatty.net.ClientData;
import chatty.net.NetHandler;

class ChatArea extends JTextPane{
    
    ChatArea(){
        super();
        setFont(new Font("Serif", Font.PLAIN, 14));
        setEditable(false);
    }

    void appendText(NetHandler nethandler,String txt,Object source) {
		Document doc = getDocument();
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
		setCaretPosition(doc.getLength());
	}
    
    void clear(){
        setText("");
    }
}
