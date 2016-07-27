/*
 * Class NAME: Customer
 * Created BY: Olli Kousa
 * Creation DATE: 4.7.2016
 * Last MODIFIED: 21.7.2016
 * Customer class is inherited from person
 * It's purpose is to hold the information about a customer.
 * At the moment it does not have any functionalities different from Person.
 * In the future it could have for example different statuses, like private or business-customer.
 * 
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
public class Customer extends Person {
    
    public Customer(String pID, String fName, String lName, 
                  String s, String pC, String ci, String p){
        
        super(pID, fName, lName, s, pC, ci, p);
        
    }
    
    
}
