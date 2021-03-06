/* (HANDLER)
 * Varasto vastaa uusien pakettien luomisesta ja hallinnoi varastossa olevia paketteja. 
 */
package timotei;

import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;

/**
 *
 * @author Kousa
 */
public class Storage {
    
    // Singleton-konstruktori
    private static Storage instance = null;
    protected Storage() {
        // Exists only to defeat instantiation.
    }
    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }
    // ------- Singleton ENDS -------
    
    ArrayList<Package> packageList = new ArrayList();
    ArrayList<PackageType> packageTypeList = new ArrayList();
    
    // Lisää paketin annetulla tyypillä ja omistajan ID:llä
    public void addPackage(int iType, String oID){
        
        String pID;
        
        String tmp;
        
        if(packageList.isEmpty())
            pID = "I10001";
        else{
            tmp = packageList.get(packageList.size()-1).getPacketID();
            tmp = tmp.substring(1);
            tmp = String.valueOf(Integer.sum(Integer.valueOf(tmp), 1));
            pID = "I" + tmp;
            
        }
        
        packageList.add(new Package(iType, oID, pID, new java.util.Date(), false, true));
        
    }
    
    // Etsii annetun pakettiID:n perusteella paketin ja palauttaa paketti-olion.
    public Package findPackage(String pID){
        
        Package p = null;
        
        Iterator itr = packageList.iterator();

        // Haetaan reklamaatioID:n perusteella.
        if (pID.length() == 6) {
            while (itr.hasNext()) {
                p = (Package) itr.next();
                if (p.getPacketID().contains(pID)) {
                    return p;
                }
            }
        }else{
            System.out.println("ID väärässä muodossa");
            return null;
        }
        
        return p;
    }
    
    public ArrayList<Package> getPackageList(java.util.Date dateLimit) {
        
        ArrayList<Package> showPackageList = new ArrayList();

        Iterator itr = packageList.iterator();
        Package p;
        while (itr.hasNext()) {
           p = (Package) itr.next();
           if(p.getCreationTime().after(dateLimit) && !p.isRemoved())
               showPackageList.add(p);          
        }
        return showPackageList;
    }
       
    // Hakee ja palauttaa listan kaikista saatavilla olevista pakettityypeistä.
    public ArrayList<String> getPackageTypeList() {
        ArrayList<String> packageTypeInfoList = new ArrayList();
        
        Iterator itr = packageTypeList.iterator();
        while(itr.hasNext())
            packageTypeInfoList.add(itr.next().toString());
        
        return packageTypeInfoList;
    }

    public ArrayList<PackageType> getPackageTypeObjectList() {
        return packageTypeList;
    }
    
    // Päivittää listan saatavilla olevista pakettityypeistä.
    public void updatePackageTypeList(){
        
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully for ADDING PACKAGE types");

        try {
            c.setAutoCommit(false);
            Statement stmt = null;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Tuotetyypit;");
            while (rs.next()) {
                String tuoteID = rs.getString("TuoteID");
                String name = rs.getString("Nimi");
                String size = rs.getString("Koko");
                String weight = rs.getString("Paino");
                boolean fragile = Boolean.valueOf(rs.getString("Fragile"));
                packageTypeList.add( new PackageType(Integer.valueOf(tuoteID), name, size, Double.valueOf(weight), fragile));

            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("PackageTypes read successfully");
        
    }
    
    // Päivittää pakettilistan tietokannasta. 
    public void updatePackageList(){
        
        packageList.clear();
        
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-Opened database successfully for UPDATING PACKAGES list");

        try {
            c.setAutoCommit(false);
            Statement stmt = null;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Paketit;");
            while (rs.next()) {
                String packetID = rs.getString("PacketID");
                String ownerID = rs.getString("AsiakasID");
                String packetType = rs.getString("TuoteID");
                Date creationTime = rs.getDate("CreationDate");
                boolean removed = rs.getBoolean("Poistettu");
                
                packageList.add(new Package(Integer.parseInt(packetType), ownerID, packetID, creationTime, removed, false));

            }
            rs.close();
            stmt.close();
            c.close();
        } catch (SQLException | NumberFormatException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("PACKAGE list updated successfully");
        
    }
    
    public void removePackage(String pID, boolean removeCascade){
        
        Package p = null;

        Iterator itr = packageList.iterator();

        // Haetaan reklamaatioID:n perusteella.
        while (itr.hasNext()) {
            p = (Package) itr.next();
            if (p.getPacketID().contains(pID)) {
                packageList.remove(p);
                
                if(removeCascade){
                    
                    try {
                        Connection c = null;
                        PreparedStatement stmt;
                        Class.forName("org.sqlite.JDBC");
                        c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
                        c.setAutoCommit(false);
                        System.out.println("-Opened database successfully for DELETING PACKAGE");
                        
                        String sql = "DELETE FROM Paketit "
                                + "WHERE PacketID = ? ;";
                        stmt = c.prepareStatement(sql);
                        
                        stmt.setString(1, pID);
                        
                        stmt.executeUpdate();

                        stmt.close();
                        c.commit();
                        c.close();
                    } catch (ClassNotFoundException | SQLException e) {
                        System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    }
                    
                    System.out.println("Records DELETED successfully");
                    
                }else{
                    
                    try {
                        Connection c = null;
                        PreparedStatement stmt;
                        Class.forName("org.sqlite.JDBC");
                        c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
                        c.setAutoCommit(false);
                        System.out.println("-Opened database successfully for MARKING PACKAGE DELETED");
                        
                        String sql = "UPDATE Paketit SET Poistettu = ? "
                                + "WHERE PacketID = ?;";
                        stmt = c.prepareStatement(sql);
                        
                        stmt.setBoolean(1, true);
                        stmt.setString(2, pID);
                        
                        stmt.executeUpdate();

                        stmt.close();
                        c.commit();
                        c.close();
                    } catch (ClassNotFoundException | SQLException e) {
                        System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    }
                    System.out.println("PACKAGE MARKED 'DELETED' successfully");
                    
                }
                return;
            }
        }
        
        System.out.println("The package you wanted to remove does not exist");
        

    }
}
