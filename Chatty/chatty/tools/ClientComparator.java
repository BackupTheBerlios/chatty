package chatty.tools;

import java.util.Comparator;

public class ClientComparator implements Comparator {
    
    final public int compare(Object o1, Object o2) {
        int ergebnis = o1.toString().compareToIgnoreCase(o2.toString());
        return ergebnis==0?-1:ergebnis;
    }
    
}