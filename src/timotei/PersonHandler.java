/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.util.ArrayList;
import java.util.Iterator;

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
    
    ArrayList<Person> personList = new ArrayList();
    
    public void addPerson(){
        
        personList.add(new Person("P666", "Pekka", "Puupää", "Pillunpolku 6", "04325", "Venäjä", "+358405543212"));
        
    }
    
    public boolean confirmPerson(String pID){

        // Alustetaan iteraattori.
        Iterator itr = personList.iterator();

        // Käydään läpi kaikki oliot ja tarkistetaan löytyykö haettu henkilö listasta
        while (itr.hasNext()) {
            if (((Person)itr.next()).getPersonID().contains(pID))
                return true;
        }
        return false;
    }
    
    public String getNameByID(String pID){
        
        // Alustetaan iteraattori.
        Iterator itr = personList.iterator();
        Person p;

        // Käydään läpi kaikki oliot ja tarkistetaan löytyykö haettu henkilö listasta
        while (itr.hasNext()) {
            p = (Person)itr.next();
            if (p.getPersonID().contains(pID)) {
                return (p.getFirstName() + " " + p.getLastName());
            }
        }
        return null;
        
    }
    
}
