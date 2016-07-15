/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Kousa
 */
public class Reclamation {
    
    String reclamationID; 
    String shipmentID; 
    Date reclamationTime; 
    String info; 
    
    public Reclamation(String rID, String sID, String i, Date time, boolean writeToDB){
        
        reclamationID = rID;
        shipmentID = sID;
        info = i;
        reclamationTime = time;
        
        
        if(writeToDB){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
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
                String sql = "INSERT INTO Reklamaatiot "
                        + "VALUES (?, ?, ?, ?);";

                stmt = c.prepareStatement(sql);
                stmt.setString(1, rID);
                stmt.setString(2, sID);
                stmt.setString(3, info);
                stmt.setString(4, format.format(time));
                stmt.executeUpdate();

                stmt.close();
                c.commit();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
            System.out.println("Records into Reklamaatiot created successfully");
            //--------------------------
        }
    }

    public String getReclamationID() {
        return reclamationID;
    }

    public String getShipmentID() {
        return shipmentID;
    }

    public Date getTime() {
        return reclamationTime;
    }

    public String getInfo() {
        return info;
    }
    
    @Override
    public String toString(){
        String info = reclamationID + " " + shipmentID + " " + reclamationTime.toString();
        return info;
    }
    
    
}
