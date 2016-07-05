/*
 * Pakettiluokka.
 * Parametrit: Paketin tuotetyyppi ja omistajan tunnus. 
 */
package timotei;

import java.sql.*;

/**
 *
 * @author Kousa
 */
public class Package {
    
    String packetID;
    String name;
    String size;
    double weight; 
    String ownerID;
    int itemType;
    boolean fragile;
    
    public Package(int iType, String oID, String pID){
        
        itemType = iType;
        ownerID = oID;
        packetID = pID;
        
        // Avataan tietokanta
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        
        // Haetaan tietokannasta annetun tuotetyypin tiedot. 
        try {         
            c.setAutoCommit(false);
            Statement stmt = null;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Tuotetyypit;");
            while (rs.next()) {
                name = rs.getString("Nimi");
                size = rs.getString("Koko");
                weight = rs.getDouble("Paino");
                fragile = rs.getBoolean("Fragile");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");   
        
    }

    public String getPacketID() {
        return packetID;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public double getWeight() {
        return weight;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public int getItemType() {
        return itemType;
    }

    public boolean isFragile() {
        return fragile;
    }
    
    
    
    
}
