/*
 * Käsittelee SmartPosteihin liittyvät haut ja pitää yllä listaa.
 * Singleton osoitteesta: http://www.javaworld.com/article/2073352/core-java/simply-singleton.html
 * 
 */
package timotei;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Kousa
 */
public class SmartPostHandler {
    
    ArrayList<SmartPost> smartPostList = new ArrayList();
    
    
    // Singleton-konstruktori
    private static SmartPostHandler instance = null;
    protected SmartPostHandler() {
        // Exists only to defeat instantiation.        
    }
    public static SmartPostHandler getInstance() {
        if (instance == null) {
            instance = new SmartPostHandler();
        }
        return instance;
    }
    // -------
    
    // Etsii annettua merkkijonoa vastaavan pakettiautomaatin. Nimet muotoa "Pakettiautomaatti, K-citymarket Päiväranta"
    public ArrayList findSmartPost(String search){
        
        ArrayList<String> matchingSmartPostList = new ArrayList();
        
        // Alustetaan iteraattori ja SmartPost-olio.
        Iterator itr = smartPostList.iterator();
        SmartPost sp;
        
        // Käydään läpi kaikki oliot ja tarkistetaan löytyykö nimestä haettu sana
        // Jos löytyy, lisätään listaan
        while(itr.hasNext()){
            sp = (SmartPost)itr.next();
            if(sp.getName().contains(search))
                matchingSmartPostList.add(sp.getName());
        }
        
        return matchingSmartPostList;
    }
    
    // Haetaan automaattien tiedot, luodaan oliot ja lisätään ne listaan.
    public void getSmartPostList(){
        
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully for UPDATING SMARTPOST list");
        
        
        try {
            c.setAutoCommit(false);
            Statement stmt = null;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Smartposts;");
            while (rs.next()) {
                String name = rs.getString("Name");
                String openhours = rs.getString("Openhours");
                String street = rs.getString("Street");
                String postcode = rs.getString("Postcode");
                String city = rs.getString("City");
                String lat = rs.getString("Lat");
                String lng = rs.getString("Lng");
                boolean haspacket = rs.getBoolean("haspacket");
                
                smartPostList.add(new SmartPost(name, openhours, street, postcode, city, lat, lng, haspacket));
                
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("SMARTPOSTlist UPDATED successfully");
        
        
    }
    
    public SmartPost getSmartPostByName(String name){
        
        ArrayList<SmartPost> matchingSmartPostList = new ArrayList();

        // Alustetaan iteraattori ja SmartPost-olio.
        Iterator itr = smartPostList.iterator();
        SmartPost sp;

        // Käydään läpi kaikki oliot ja tarkistetaan löytyykö nimestä haettu sana
        // Jos löytyy, lisätään listaan
        while (itr.hasNext()) {
            sp = (SmartPost) itr.next();
            if (sp.getName().contains(name)) {
                return sp;
            }
        }

        return null;
        
    }
    
}
