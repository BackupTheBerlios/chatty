package chatty.gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

import chatty.net.ClientData;
import chatty.tools.ListTools;

/**
 *Liste mit aktiven Chattern <br>
 *<b>abgeleitet</b> von DefaultListModel<br>
 *<br>
 *neue Methoden:<br>
 *void initOnConnect(); Object getSelected(); void addPerson(ClientData); void remove(ClientData);
 *JScrollPane getPane();
 *
 *@author Martin
 *@param int Breite,@param int Höhe<br>
 *bei Aufruf des Konstruktors
 */
public class PersonList extends DefaultListModel {

    private JList clientJList;
    private JScrollPane scrollList;
    
    PersonList(int width, int height){
        clientJList = new JList(this);
        clientJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		clientJList.setFont(new Font("SansSerif", Font.ITALIC, 15));
		
		scrollList = new JScrollPane(clientJList);
		scrollList.setPreferredSize(new Dimension(width, height));
		scrollList.setBorder(BorderFactory.createEtchedBorder());
    }
    
    //beim Neuverbinden soll immer ALle ganz oben Stehen
    void initOnConnect(){
        String alle = "Alle";
		addElement(alle);
		clientJList.setSelectedValue(alle,true);
    }
    

    //Gibt in Liste ausgewähltes Objekt zurück
    Object getSelected(){
        return clientJList.getSelectedValue();
    }
    
    void addPerson(ClientData newClient){
        ListTools.nextColor();
        Object o = get(clientJList.getSelectedIndex());
        //einfügen in schon SORTIERTE Liste an richtiger Stelle
        ListTools.insertInList(this,newClient);
        clientJList.setSelectedIndex(indexOf(o));
    }
    
    /**
     *Löscht Client aus Liste. Wenn er selektiert war,
     *wird Auswahl auf Alle gesetzt
     */
    void remove(ClientData clientToRemove) {
	    Object o = get(clientJList.getSelectedIndex());
	    ListTools.removeFromClientList(this,clientToRemove);
	    int i = indexOf(o);
	    //wenn selektierter client gelöscht, selektiere alle
	    if (i<0) i=0;
	    clientJList.setSelectedIndex(i);
	}
    
    JScrollPane getPane(){
        return scrollList;
    }
    
    
}
