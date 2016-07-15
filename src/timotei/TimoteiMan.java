/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Kousa
 */
public class TimoteiMan extends Person {
    
    String toimipisteID = null;
    Integer stressLevel = null;
    
    
    public TimoteiMan(String pID, String fName, String lName, 
                  String s, String pC, String ci, String p, String toimipiste, int sLevel){
        
        super(pID, fName, lName, s, pC, ci, p);
        
        toimipisteID = toimipiste;
        stressLevel = sLevel; 
        
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
            String sql = "INSERT INTO Persons "
                    + "VALUES (?, ?, ?, ?, ?, ?);";

            stmt = c.prepareStatement(sql);
            stmt.setString(1, pID);
            stmt.setString(2, fName);
            stmt.setString(3, lName);
            stmt.setString(4, s);
            stmt.setString(5, pC);
            stmt.setString(6, ci);
            stmt.setString(7, p);
            stmt.setString(8, toimipiste);
            stmt.setInt(9, sLevel);
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Record into Person created successfully");
        //--------------------------    
    }
    
    public void setStressLevel(int sLevel){
        stressLevel = sLevel;
        
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
            String sql = "UPDATE Persons "
                    + "SET Stressitaso = ? "
                    + "WHERE PersonID = ?;";

            stmt = c.prepareStatement(sql);
            stmt.setInt(1, stressLevel);
            stmt.setString(2, personID);
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Record into Person created successfully");
        //-------------------------- 
        
    }

    public String getToimipisteID() {
        return toimipisteID;
    }

    public Integer getStressLevel() {
        return stressLevel;
    }
    
}