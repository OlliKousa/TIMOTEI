/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Kousa
 */
public class Reclamation {
    
    String reclamationID; 
    String packageID; 
    Date time; 
    String info; 
    
    public Reclamation(String rID, String pID, String i){
        
        reclamationID = rID;
        packageID = pID;
        info = i;
        time = new java.util.Date();
        
    }

    public String getReclamationID() {
        return reclamationID;
    }

    public String getPackageID() {
        return packageID;
    }

    public Date getTime() {
        return time;
    }

    public String getInfo() {
        return info;
    }
    
    
    
    
}
