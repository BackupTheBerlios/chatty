/*
 * Created on 13.02.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package chatty.tools;

import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import chatty.tools.ClientComparator;

public class ListTools {
    
    ListTools(){}
    
    static public void sortClientList(DefaultListModel list){
        TreeSet s = new TreeSet(new ClientComparator());
        for(int i = 0;i<list.size();i++){
            s.add(list.elementAt(i));
        }
        Iterator it = s.iterator();
        for(int i=0;it.hasNext();i++){
            Object o = it.next();
            if(o!=list.get(i)){
                list.removeElementAt(i);
                list.add(i,o);
            }
        }
    }
    
}
