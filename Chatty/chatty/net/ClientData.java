package chatty.net;

import java.awt.Color;
import chatty.tools.*;

/**
 * Beeinhaltet alle m�glichen Daten �ber Clienten
 */

public class ClientData {
    
    private String userName;
    private String address;
    private int ID;
    private String comment;
    private Color color;
    
    ClientData(){
        ID=-1;
        comment = "";
        address ="local";
        color = ListTools.getNextColor();
    }
    
    String convertToString(){
        return userName + "/" + address + "/" + ID + "/" + comment;
    }
    
    public String toString(){
        return userName;
    }
    
    void setFromString(String data){
        String[] temp = data.split("/",4);
        userName = temp[0];
        address  = temp[1];
        ID		 = Integer.valueOf(temp[2]).intValue();
        comment  = temp[3];     
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
	public Color getColor() {
		return color;
	}
	
    public String getName() {
        return userName;
    }
    
    
    public void setName(String userName) {
        this.userName = userName;
    }
    
    public int getID() {
        return ID;
    }
    
    public void setID(int id) {
        ID = id;
    }
}
