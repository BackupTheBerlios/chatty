package chatty.tools;

import java.awt.Color;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.DefaultListModel;

import chatty.net.ClientData;

public class ListTools {
    
    ListTools(){}
    
    private static Color[] colors = { 
    	Color.red, Color.green.darker(), 
    	Color.blue, Color.lightGray, Color.magenta };
   
    private static int currentColor = 0;
    
    static public Color getNextColor() {
    	Color color = colors[currentColor];
		if (currentColor!=colors.length-1)
			currentColor++;
		else
			currentColor = 0;
    	return color;
    }
    
    static public void sortClientList(DefaultListModel list){
        TreeSet s = new TreeSet(new ClientComparator());
        for(int i = 0;i<list.size();i++)
            s.add(list.elementAt(i));
        
        Iterator it = s.iterator();
        list.clear();
        for(int i=0;it.hasNext();i++)
            list.add(i,it.next());
    }
    
    static public void removeFromClientList(DefaultListModel clientList,ClientData clientToRemove){
        for (int i = 0; i < clientList.size(); i++) {
			Object c = clientList.get(i);
			if (!(c instanceof String))
				if (clientToRemove.getID() == ((ClientData) c).getID()) {
				    clientList.remove(i);
					return;
				}
		}
    }
    
}
