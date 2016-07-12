/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;


/**
 *
 * @author Kousa
 */
public class Person {
    
    String personID = null;
    String firstName = null;
    String lastName = null;
    String street = null;
    String postCode = null; 
    String city = null;
    String phone = null;
    
    public Person(String pID, String fName, String lName, 
                  String s, String pC, String ci, String p){
        
        personID = pID;
        firstName = fName;
        lastName = lName;
        street = s;
        postCode = pC;
        city = ci;
        phone = p;
        
    }

    public String getPersonID() {
        return personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStreet() {
        return street;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }
    
    
    
}
