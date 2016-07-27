/*
 * Class NAME: LogOperator
 * Created BY: Olli Kousa
 * Creation DATE: 15.7.2016
 * Last MODIFIED: 20.7.2016
 * Purpose of the class is to retrieve asked statistics from the database. 
 */
package timotei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kousa
 */
public class LogOperator {
    
    // Singleton-konstruktori
    private static LogOperator instance = null;

    protected LogOperator() {
        // Exists only to defeat instantiation.        
    }

    public static LogOperator getInstance() {
        if (instance == null) {
            instance = new LogOperator();
        }
        return instance;
    }
    // -------
    
    // This method receives type and date as parameters and returns ArrayList containing the relevant info. 
    public ArrayList<String> getStatistic(int type, Date date){
        ArrayList<String> infoList = new ArrayList();
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        double totalDistance = 0;
        int totalShipments = 0;
        int broken = 0;
        
        try {
            Connection c = null;  
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
            System.out.println("-Opened database successfully for RETRIEVING Statistical data");
            
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = null;
        
            switch(type){
                
                // Total distance of the day. 
                case 0:
                    rs = stmt.executeQuery("SELECT Distance FROM Total_Distance_Per_Day WHERE day = '" + format.format(date) + "';");
                    while (rs.next()) {
                        totalDistance = rs.getDouble("Distance");
                        System.out.println(totalDistance);
                    }    
                    if(totalDistance > 0)
                        infoList.add("Päivän aikana toimitettujen lähetysten kokonaismatka oli: " +  (double)Math.round(totalDistance * 1000d) / 1000 + " km");
                    else
                        infoList.add("Päivän aikana ei toimitettu lähetyksiä.");
                    
                    break;
                    
                // Average distance of the day. Counts the average from total distance and amount of shipments.
                case 1:
                    
                    rs = stmt.executeQuery("SELECT Distance FROM Total_Distance_Per_Day WHERE day = '" + format.format(date) + "';");
                    while (rs.next()) {
                        totalDistance = rs.getDouble("Distance");
                        System.out.println(totalDistance);
                    }

                    rs = stmt.executeQuery("SELECT Shipments FROM Total_Shipments_Per_Day WHERE day = '" + format.format(date) + "';");
                    while (rs.next()) {
                        totalShipments = rs.getInt("Shipments");
                        System.out.println(totalDistance);
                    }

                    if (totalDistance > 0) {
                        double distancePerShipment = totalDistance/totalShipments;
                        infoList.add("Päivän aikana toimitettujen lähetysten keskimatka oli: " + (double) Math.round(distancePerShipment * 1000d) / 1000 + " km");
                    } else {
                        infoList.add("Päivän aikana ei toimitettu lähetyksiä.");
                    }
                    break;
                    
                // All shipments from one day.
                case 2: 
                    

                    rs = stmt.executeQuery("SELECT * FROM Shipments_Full WHERE day = '" + format.format(date) + "';");
                    while (rs.next()) {
                        String sID = rs.getString("ShipmentID");
                        String fName = rs.getString("Etunimi");
                        String lName = rs.getString("Sukunimi");
                        String pName = rs.getString("Nimi");
                        String cID = rs.getString("CourierID");
                        String sDistance = rs.getString("TravelLength");
                        String sFrom = rs.getString("SentFrom");
                        String dest = rs.getString("Destination");
                        String sBroken = String.valueOf(rs.getBoolean("BrokenInShipment"));

                        infoList.add(sID + " " + fName + " " + lName + " \t" + pName + " \t");
                        infoList.add(cID + " " + sDistance + "km " + sFrom + " " + dest + " Broken: " + sBroken);
                        infoList.add("------");
                    }

                    if (infoList.isEmpty())
                        infoList.add("Päivän aikana ei toimitettu lähetyksiä.");

                    break;
                   
                // Amount of broken shipments on a certain day.
                case 3: 
                    
                    rs = stmt.executeQuery("SELECT Broken FROM Broken_Shipments WHERE day = '" + format.format(date) + "';");
                    while (rs.next()) {
                        broken = rs.getInt("Broken");
                    }

                    if (broken > 0) {
                        infoList.add("Päivän aikana hajonneita lähetyksiä oli: " + broken + " kappaletta. Pahoittelemme, yksikin on liikaa!");
                    } else {
                        infoList.add("Päivän aikana ei hajonneita lähetyksiä.");
                    }

                    break;
            }
            
            rs.close();
            stmt.close();
            c.close();
            System.out.println("STATISTICS RETRIEVED successfully");
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(LogOperator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return infoList;
    }
    
    
}
