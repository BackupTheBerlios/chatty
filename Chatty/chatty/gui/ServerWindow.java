package chatty.gui;

import java.util.*;

import chatty.net.ClientData;
/**
 * Konsolenfenster für Serverapplikation
 */

public class ServerWindow implements ChatInstance {

	private String getTime(){
	    GregorianCalendar cal = new GregorianCalendar();
	    String[] time = new String[3];
	    time[0] = cal.get(Calendar.HOUR_OF_DAY)+""; 
	    time[1] = cal.get(Calendar.MINUTE)+"";
	    time[2] = cal.get(Calendar.SECOND)+"";
	    for(int i=0;i<3;i++)
	        if(time[i].length()==1)
	            time[i]="0"+time[i];
	    return time[0]+":"+time[1]+":"+time[2]+" ";
	}
    
    public void updateNetStatus() {
	}

	public void appendText(String text,Object source) {
		System.out.println(getTime()+text);
	}

	public void appendError(String text) {
		System.err.println(getTime()+text);
	}

	public void clearText() {	    
	}
	
    public void addToList(ClientData newClient) {
    }

    public void removeFromList(ClientData client) {
    }


}
