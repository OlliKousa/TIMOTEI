/*
 * Varasto vastaa uusien pakettien luomisesta ja hallinnoi varastossa olevia paketteja. 
 */
package timotei;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Kousa
 */
public class Storage {
    
    // Singleton-konstruktori
    private static Storage instance = null;

    protected Storage() {
        // Exists only to defeat instantiation.
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }
    // ------- Singleton ENDS -------
    
    ArrayList<Package> packageList = new ArrayList();
    
    // Lisää paketin annetulla tyypillä ja omistajan ID:llä
    public void addPackage(int iType, String oID){
        
        String pID;
        
        String tmp;
        
        if(packageList.isEmpty())
            pID = "I00001";
        else{
            tmp = packageList.get(packageList.size()).getPacketID();
            tmp = tmp.substring(1);
            tmp = String.valueOf(Integer.sum(Integer.valueOf(tmp), 1));
            pID = "I" + tmp;
            
        }
        
        packageList.add(new Package(iType, oID, pID));
        
    }
    
    
    // Etsii annetun pakettiID:n perusteella paketin ja palauttaa paketti-olion.
    public Package findPackage(String pID){
        Package p = null;
        
        Iterator itr = packageList.iterator();

        // Haetaan reklamaatioID:n perusteella.
        if (pID.length() == 6) {
            while (itr.hasNext()) {
                p = (Package) itr.next();
                if (p.getPacketID().contains(pID)) {
                    return p;
                }
            }
        }else{
            System.out.println("ID väärässä muodossa");
            return null;
        }
        
        return p;
    }
    
    
}
