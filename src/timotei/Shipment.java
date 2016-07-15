/*
 * Yksittäinen lähetys
 */
package timotei;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Kousa
 */
public class Shipment {
    
    String shipmentID = null;
    String packetID = null;
    String sentFrom = null;
    String destination = null; 
    Date sentTime = null;
    String shipmentType = null;
    Double shipmentLength = null;
    Boolean brokenInShipment = false; 

    public Shipment(String sID, String pID, String sF, String d, String sType, Date time, Double length, Boolean broken, boolean addToDB ){

        shipmentID = sID;
        packetID = pID;
        sentFrom = sF;
        destination = d;
        shipmentType = sType;
        sentTime = time;
        shipmentLength = length;
        brokenInShipment = broken;
                
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        
        
        if(addToDB){
            //-----------------------
            Connection c = null;
            PreparedStatement stmt = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
                c.setAutoCommit(false);
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
            System.out.println("Opened database successfully");

            try {
                String sql = "INSERT INTO Shipments "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

                stmt = c.prepareStatement(sql);
                stmt.setString(1, sID);
                stmt.setString(2, pID);
                stmt.setString(3, sF);
                stmt.setString(4, d);
                stmt.setString(5, format.format(sentTime));
                stmt.setString(6, sType);
                stmt.setString(7, length.toString());
                stmt.setBoolean(8, broken);
                stmt.executeUpdate();

                stmt.close();
                c.commit();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
            System.out.println("Records into Shipments created successfully");
            //--------------------------
        }
    }

    public String getShipmentID() {
        return shipmentID;
    }

    public String getPacketID() {
        return packetID;
    }

    public String getSentFrom() {
        return sentFrom;
    }

    public String getDestination() {
        return destination;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public String getShipmentType() {
        return shipmentType;
    }
    
        
        
    
}
