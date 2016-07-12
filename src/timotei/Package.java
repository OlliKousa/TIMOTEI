/*
 * Pakettiluokka.
 * Parametrit: Paketin tuotetyyppi ja omistajan tunnus. 
 */
package timotei;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kousa
 */
public class Package {
    
    String packetID;
    String ownerID;
    int itemType;

    
    public Package(int iType, String oID, String pID, boolean addToDB){
        
        itemType = iType;
        ownerID = oID;
        packetID = pID;
        
        // Uudet paketit lisätään tietokantaan. 
        if(addToDB){
            // Avataan tietokanta
            Connection c = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
                c.setAutoCommit(false);
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
            System.out.println("Opened database successfully for INSERTING PACKAGE");

            // --------------
            // Paketin tallentaminen tietokantaan. 
        
            PreparedStatement stmt = null;

            try {
                
                String sql = "INSERT INTO Paketit (PacketID, AsiakasID, TuoteID)"
                        + " VALUES (?, ?, ? );";

                stmt = c.prepareStatement(sql);

                stmt.setString(1, pID);
                stmt.setString(2, oID);
                stmt.setString(3, ("0" + String.valueOf(iType)));
                stmt.executeUpdate();

                stmt.close();
                c.commit();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

            System.out.println("Record into Package created successfully");
            
        }
    }

    public String getPacketID() {
        return packetID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public int getItemType() {
        return itemType;
    }  
    
}
