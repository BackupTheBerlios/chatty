package chatty.tools;

import java.util.Comparator;

public class ClientComparator implements Comparator {
    
    final public int compare(Object o1, Object o2) {
        int ergebnis = o1.toString().compareToIgnoreCase(o2.toString());
        if (ergebnis == 0)
            return -1;
        return ergebnis;
    }
    
}