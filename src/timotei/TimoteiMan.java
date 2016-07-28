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
        System.out.println("-Opened database successfully for updating STRESS LEVEL");

        try {
            String sql = "UPDATE Persons "
                    + "SET Stressitaso = ? "
                    + "WHERE PersonID = ?;";

            stmt = c.prepareStatement(sql);
            stmt.setInt(1, stressLevel);
            stmt.setString(2, personID);
            stmt.executeUpdate();

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("STRESS LEVEL UPDATED successfully");
        //-------------------------- 
        
    }

    public String getToimipisteID() {
        return toimipisteID;
    }

    public Integer getStressLevel() {
        return stressLevel;
    }
    
}