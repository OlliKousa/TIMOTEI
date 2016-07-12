/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Kousa
 */
public class ReclamationHandler {
    
    // Singleton-konstruktori
    private static ReclamationHandler instance = null;
    protected ReclamationHandler() {
        // Exists only to defeat instantiation.
    }
    public static ReclamationHandler getInstance() {
        if (instance == null) {
            instance = new ReclamationHandler();
        }
        return instance;
    }
    // -------
    
    ArrayList<Reclamation> reclamationList = new ArrayList();
    
    public void addReclamation(String rID, String pID, String i){
        
        reclamationList.add(new Reclamation(rID, pID, i));
        
        
        
    }
    
    // Etsii annetun ID:n perusteella reklamaation listasta. Parametrin tulee olla oikeassa muodossa. 
    public Reclamation findReclamation (String search){
        
        Reclamation r;
        Iterator itr = reclamationList.iterator();
        
        // Haetaan reklamaatioID:n perusteella.
        if(search.length() == 10){
            while (itr.hasNext()) {
                r = (Reclamation) itr.next();
                if (r.getReclamationID().contains(search)) {
                    return r;
                }
            }
            
        // Haetaan pakettiID:n perusteella.     
        }else if (search.length() == 6){
            
            while (itr.hasNext()) {
                r = (Reclamation) itr.next();
                if (r.getPackageID().contains(search)) {
                    return r;
                }
            }
        }else{
            System.out.println("Väärä parametri");
            return null;
        }
        
        System.err.println("Haettua parametria ei löytynyt.");
        return null;
    }
}
