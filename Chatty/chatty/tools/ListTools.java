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
    
    static int currentColor=0;
    
    public static void nextColor(){
        currentColor++;
    }
    
    static public Color getColor() {
    	if(currentColor>=colors.length)
    	    currentColor=0;
        Color color = colors[currentColor];
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
