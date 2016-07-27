/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Kousa
 */
public class ShipmentHandler {
    
    // Singleton-konstruktori
    private static ShipmentHandler instance = null;
    protected ShipmentHandler() {
        // Exists only to defeat instantiation.
    }
    public static ShipmentHandler getInstance() {
        if (instance == null) {
            instance = new ShipmentHandler();
        }
        return instance;
    }
    // -------
    
    ArrayList<Shipment> shipmentList = new ArrayList();
    
    public void createShipment(String packageID, String sendFrom, String destination, 
            String shipmentType, Boolean broken, Double length, ArrayList couriers ){
    
        String sID = null;
        String tmp = null;
        
        if (shipmentList.isEmpty()) {
            sID = "S10001";
        } else {
            tmp = shipmentList.get(shipmentList.size()-1).getShipmentID();
            tmp = tmp.substring(1);
            tmp = String.valueOf(Integer.sum(Integer.valueOf(tmp), 1));
            sID = "S" + tmp;
        }
        
        Date time = new java.util.Date();
        
        shipmentList.add(new Shipment(sID, packageID, sendFrom, destination, shipmentType, time, length, broken, true));
        
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
        System.out.println("-Opened database successfully for ADDING SHIPMENT_COURIER");
        
        Iterator itr = couriers.iterator();
        
        try {
            while(itr.hasNext()){
            String sql = "INSERT INTO Shipment_Courier "
                    + "VALUES (?, ?);";

            stmt = c.prepareStatement(sql);
            stmt.setString(1, sID);
            stmt.setString(2, ((Person)itr.next()).getPersonID());
            stmt.executeUpdate();

            }

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        System.out.println("Records into SHIPMENT_COURIER created successfully");
        //--------------------------
        
        
    }
    
    public void updateShipmentList(){
        
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-Opened database successfully for UPDATING SHIPMENTS list");

        try {
            c.setAutoCommit(false);
            Statement stmt = null;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Shipments;");
            while (rs.next()) {
                String sID = rs.getString("ShipmentID");
                String pID = rs.getString("PacketID");
                String from = rs.getString("Sentfrom");
                String dest = rs.getString("Destination");
                Date time = rs.getDate("Senttime");
                String sType = rs.getString("Shipmenttype");
                Boolean broken = rs.getBoolean("BrokenInShipment");
                Double length = rs.getDouble("TravelLength");
                
                        
                shipmentList.add(new Shipment(sID, pID, from, dest, sType, time, length, broken, false));

            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("ShipmentsList UPDATED successfully");
        
    }

    ArrayList<String> eventList = new ArrayList();
    
    public ArrayList getEventList(java.util.Date timeLimit) {
        
        eventList.clear();
        Iterator itr = shipmentList.iterator();
        Shipment shipment;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String info;
        while(itr.hasNext()){
            shipment = (Shipment) itr.next();
            if(shipment.getSentTime().after(timeLimit)){
                info = shipment.getShipmentID();
                info = info + " " + format.format(shipment.getSentTime());
                eventList.add(info);
            }
        }
        
        return eventList;
    }
    
    public Shipment getShipmentByID(String sID){
        
        Shipment shipment;
        Iterator itr = shipmentList.iterator();
        
        while (itr.hasNext()) {
            shipment = (Shipment) itr.next();
            if(shipment.getShipmentID().contains(sID))
                return shipment;
        }
        return null;
    }
    
    public ArrayList<String> getCouriersByShipment(String sID){
        ArrayList<String> shipmentCouriers = new ArrayList();
        
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully for RETRIEVING SHIPMENT_COURIER");

        try {
            c.setAutoCommit(false);
            Statement stmt = null;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM shipment_courier WHERE ShipmentID = '" + sID + "';");
            while (rs.next()){
                String pID = rs.getString("CourierID");
                shipmentCouriers.add(pID);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("COURIERS RETRIEVED successfully");
        
        
        return shipmentCouriers;
    }
    
}
