/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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
    
    public void addReclamation(String sID, String i){
        
        String rID = null;
        String tmp = null;
        
        if (reclamationList.isEmpty()) {
            rID = "R100000001";
        } else {
            tmp = reclamationList.get(reclamationList.size() - 1).getReclamationID();
            tmp = tmp.substring(1);
            tmp = String.valueOf(Integer.sum(Integer.valueOf(tmp), 1));
            rID = "R" + tmp;
        }
        
        Date time = new java.util.Date();
        
        reclamationList.add(new Reclamation(rID, sID, i, time, true));
    }

    public ArrayList<Reclamation> getReclamationList() {
        return reclamationList;
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
            
        // Haetaan ShipmentID:n perusteella.     
        }else if (search.length() == 6){
            
            while (itr.hasNext()) {
                r = (Reclamation) itr.next();
                if (r.getShipmentID().contains(search)) {
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
    
    
    public void updateReclamationList() {

        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-Opened database successfully for UPDATING RECLAMATIONS list");

        try {
            c.setAutoCommit(false);
            Statement stmt = null;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Reklamaatiot;");
            while (rs.next()) {
                String rID = rs.getString("ReklamaatioID");
                String sID = rs.getString("ShipmentID");
                String info = rs.getString("Kuvaus");
                Date time = rs.getDate("Aika");
                
                reclamationList.add(new Reclamation(rID, sID, info, time, false));

            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("RECLAMATIONS UPDATED successfully");

    }
}
