package chatty.tools;

import java.util.ArrayList;
import chatty.net.ClientData;


public class ClientList extends ArrayList {
	
	public ClientData getClientByID(int ID) {
		for (int i=0; i<size();i++) {
			ClientData c = (ClientData)get(i);
			if (c.getID()==ID)
				return c;
		}
		return null;
	}
	
}
