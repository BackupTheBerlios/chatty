package chatty.tools;

import java.awt.Color;
import javax.swing.DefaultListModel;

import chatty.net.ClientData;

public class ListTools {
    
    ListTools(){}
    
    private static Color[] colors = { 
    	Color.red, Color.green.darker(), 
    	Color.blue, Color.lightGray, Color.magenta };
    
    static int currentColor=0;
    
    public static void nextColor(){
        currentColor++;
        if(currentColor>=colors.length)
    	    currentColor = 0;
    }
    
    static public Color getColor() {
    	return colors[currentColor];
    }
    
    static private int compare(Object o1, Object o2) {
    	if (o1 instanceof String)
    		return -1;
    	if (o2 instanceof String)
    		return 1;
        int ergebnis = o1.toString().compareToIgnoreCase(o2.toString());
        return ergebnis==0?-1:ergebnis;
    }
    
    //TODO noch O(n) bald O(log n) mittels Devide and Conquer
    static public void insertInList(DefaultListModel list,ClientData newClient){
        int i=0;
        while(i < list.size() && compare(list.get(i),newClient)<0){
            i++;
        }
        list.add(i,newClient);
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
