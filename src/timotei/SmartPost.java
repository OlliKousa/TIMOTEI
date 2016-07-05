/*
 * Yksittäinen automaatti ja sen alaluokkana koordinaatti-sijainti. 
 */
package timotei;

/**
 *
 * @author Kousa
 */
public class SmartPost {
    
    String name = null;
    String openhours = null;
    String street = null;
    String postcode = null;
    String city = null;

    boolean haspacket = false;
    
    String info = null;
    
    GeoPoint gp;


    // Konstruktori
    public SmartPost(String n, String o, String s, String p, String c, String la, String ln, boolean h){
        
        name = n;
        openhours = o;
        street = s;
        postcode = p;
        city = c;
        haspacket = h;
        
        gp = new GeoPoint(la,ln);
        
    }
    
    // Nimen palauttaminen esim hakufunktiota varten
    public String getName(){
        return name;
    }
    
    // Muiden tietojen palauttaminen niiden näyttämistä varten.
    // Haetaan tietokannasta luotaessa oliota.

    public String getOpenhours() {
        return openhours;
    }

    public String getStreet() {
        return street;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }



    public boolean isHaspacket() {
        return haspacket;
    }

    public String getInfo() {
        return info;
    }
    
    // Sisäluokka GeoPoint. Tän toteuttaminen erillisenä luokkana on ehkä hieman hölmöä. 
    public class GeoPoint{
        
        String lat = null;
        String lng = null;
    
        public GeoPoint(String la, String ln){
            
            lat = la;
            lng = ln;
            
        }
        
        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }
    
    }
    
        public GeoPoint getGp() {
        return gp;
    }
    
    
    
}
