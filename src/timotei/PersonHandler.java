/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author Kousa
 */
public class PersonHandler {
    
    // Singleton-konstruktori
    private static PersonHandler instance = null;
    protected PersonHandler() {
        // Exists only to defeat instantiation.        
    }
    public static PersonHandler getInstance() {
        if (instance == null) {
            instance = new PersonHandler();
        }
        return instance;
    }
    // -------
    
    ArrayList<Customer> customerList = new ArrayList();
    ArrayList<TimoteiMan> courierList = new ArrayList();
    
    public boolean confirmPerson(String pID){

        // Alustetaan iteraattori.
        Iterator itr = customerList.iterator();

        // Käydään läpi kaikki oliot ja tarkistetaan löytyykö haettu henkilö listasta
        while (itr.hasNext()) {
            if (((Person)itr.next()).getPersonID().contains(pID))
                return true;
        }
        return false;
    }
    
    public ArrayList<Person> getPersonByID(String pID){
        
        ArrayList<Person> foundPersonList = new ArrayList();
        
        // Alustetaan iteraattori.
        Iterator itr = customerList.iterator();
        Person p;

        // Käydään läpi kaikki oliot ja tarkistetaan löytyykö haettu henkilö listasta
        while (itr.hasNext()) {
            p = (Person)itr.next();
            if (p.getPersonID().contains(pID)) {
                foundPersonList.add(p);
            }
        }

        return foundPersonList;
    }
    
    public ArrayList getPersonByName(String name){
        ArrayList<Person> foundPersonList = new ArrayList();

        // Alustetaan iteraattori.
        Iterator itr = customerList.iterator();
        Person p;

        // Käydään läpi kaikki oliot ja tarkistetaan löytyykö haettu henkilö listasta
        while (itr.hasNext()) {
            p = (Person) itr.next();
            if (p.getFirstName().contains(name)) {
                foundPersonList.add(p);
            }else if(p.getLastName().contains(name)){
                foundPersonList.add(p);
            }else if( (p.getFirstName() + " " + p.getLastName()).contains(name) ){
                foundPersonList.add(p);
            }
        }

        return foundPersonList;
    }
    
    public void updatePersonList(){
        customerList.clear();
        courierList.clear();
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-Opened database successfully for UPDATING PERSONS list");

        try {
            c.setAutoCommit(false);
            Statement stmt = null;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Persons;");
            while (rs.next()) {
                String pID = rs.getString("PersonID");
                String fName = rs.getString("Etunimi");
                String lName = rs.getString("Sukunimi");
                String street = rs.getString("katuosoite");
                String pCode = rs.getString("postinumero");
                String city = rs.getString("kaupunki");
                String pNro = rs.getString("puhnro");
                String toimipisteID = rs.getString("ToimipisteID");
                Integer sLevel = rs.getInt("Stressitaso");
                
                if(toimipisteID.isEmpty())
                    customerList.add(new Customer(pID, fName, lName, street, pCode, city, pNro));
                else
                    courierList.add(new TimoteiMan(pID, fName, lName, street, pCode, city, pNro, toimipisteID, sLevel));

            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("CUSTOMER and COURIER lists UPDATED successfully");
        
    }
 
    // Arvotaan satunnainen kuriiri lähetykselle. Tarvittaessa useampi. 
    // Parametrina kuriirien määrä, paluuarvona lista jossa haluttu määrä kuriireja. 
    public ArrayList<TimoteiMan> getCouriers(int amount){
        ArrayList<TimoteiMan> designatedCourierList = new ArrayList();
        ArrayList<TimoteiMan> availableCourierList = new ArrayList();
        availableCourierList.addAll(courierList);
        Random rand = new Random();
        
        if(availableCourierList.size() >= amount){
            for (int i = 0 ; i < amount ; i++){
                int chosenOne = rand.nextInt(availableCourierList.size());
                designatedCourierList.add(availableCourierList.get(chosenOne));
                availableCourierList.remove(chosenOne);
            }
        }else{
            System.out.println("Not enough couriers. They're all fired or shit.");
            return designatedCourierList;
        }
        return designatedCourierList;
    }
    
    public ArrayList<TimoteiMan> getCourierByID(String pID) {

        ArrayList<TimoteiMan> foundPersonList = new ArrayList();

        // Alustetaan iteraattori.
        Iterator itr = courierList.iterator();
        TimoteiMan t;

        // Käydään läpi kaikki oliot ja tarkistetaan löytyykö haettu henkilö listasta
        while (itr.hasNext()) {
            t = (TimoteiMan) itr.next();
            if (t.getPersonID().contains(pID)) {
                foundPersonList.add(t);
            }
        }

        return foundPersonList;
    }

    public ArrayList<Customer> getCustomerList() {
        return customerList;
    }

    public ArrayList<TimoteiMan> getCourierList() {
        return courierList;
    }
    
    public TimoteiMan getThrowerOfTheMonth(){
        TimoteiMan voittaja = null; 
        TimoteiMan haastaja;
        
        Iterator itr = courierList.iterator();
        
        if(itr.hasNext())
            voittaja = (TimoteiMan) itr.next();
                
        while (itr.hasNext()){
            haastaja = (TimoteiMan) itr.next();
            if( haastaja.getStressLevel() > voittaja.getStressLevel()){
                voittaja = haastaja;
            }
        }
        
        return voittaja;
    }
    
    public void fireTimoteiMan(TimoteiMan tm){
        
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-Opened database successfully for FIRING TIMOTEIMAN");

        try {
            c.setAutoCommit(false);
            Statement stmt = null;
            stmt = c.createStatement();
            stmt.executeUpdate("DELETE FROM Persons WHERE PersonID = '" + tm.getPersonID() + "';");
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("TIMOTEIMAN FIRED successfully");
        
    }
    
}
