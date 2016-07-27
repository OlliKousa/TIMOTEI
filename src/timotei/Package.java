/*
 * Class NAME: Package
 * Created BY: Olli Kousa
 * Creation DATE: 4.7.2016
 * Last MODIFIED: 15.7.2016
 * This class holds information of individual package and is responsible for adding the package to the database. 
 */
package timotei;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * @author Kousa
 */
public class Package {
    
    String packetID;
    String ownerID;
    int itemType;
    java.util.Date creationTime;
    boolean removed;

    // Constructor of the new package. 
    public Package(int iType, String oID, String pID, java.util.Date cTime, boolean rem, boolean addToDB){
        
        itemType = iType;
        ownerID = oID;
        packetID = pID;
        creationTime = cTime;
        removed = rem;
        
        java.util.Date currentTime = new java.util.Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        
        // If package is added via program, it is also added to database. 
        // This is to distinct packages that are read from database from new packages. 
        if(addToDB){
            Connection c = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
                c.setAutoCommit(false);
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
            System.out.println("-Opened database successfully for INSERTING PACKAGE"); 
        
            PreparedStatement stmt = null;

            try {
                
                String sql = "INSERT INTO Paketit (PacketID, AsiakasID, TuoteID, Poistettu, CreationDate)"
                        + " VALUES (?, ?, ?, ?, ?);";

                stmt = c.prepareStatement(sql);

                stmt.setString(1, pID);
                stmt.setString(2, oID);
                stmt.setString(3, ("0" + String.valueOf(iType)));
                stmt.setBoolean(4, false);
                stmt.setString(5, format.format(currentTime));
                stmt.executeUpdate();

                stmt.close();
                c.commit();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
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

    public java.util.Date getCreationTime() {
        return creationTime;
    }

    public boolean isRemoved() {
        return removed;
    }
    
    
}
