/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.sql.*;
import java.util.Arrays;

/**
 *
 * @author Kousa
 */
public class XMLReader {
    
    private Document doc;

    public XMLReader() throws SQLException {
        String content;
        
        content = this.getUrlXML("http://smartpost.ee/fi_apt.xml");
        
        try {
            this.docBuilder(content);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String getUrlXML(String urlOsoite) {
        
        
        String content = "";
        
        
        URL url = null;
        try {
            url = new URL(urlOsoite);
        } catch (MalformedURLException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        }


        String line;

        try {
            while((line = br.readLine()) != null){
                content += line + "\n";
            }
        } catch (IOException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println(content);       
        
        return content;
        
    }
    
    public void docBuilder(String content) throws ParserConfigurationException, SAXException, IOException, SQLException{
        
        
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        doc = dBuilder.parse(new InputSource(new StringReader(content)));
        
        doc.getDocumentElement().normalize();
        
        
        parseCurrentData();
        
    }

    private void parseCurrentData() throws SQLException {
        
        NodeList nodes = doc.getElementsByTagName("place");
        
        
        System.out.println(String.valueOf(nodes.getLength()));
        
        //----------------------------------------------------------------------
        // Avataan tietokanta --------------------------------------------------
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:timotei.sqlite3");
            c.setAutoCommit(false);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-Opened database successfully for INSERTING SMARTPOSTS");
        
        PreparedStatement stmt = null;
        
        
        //----------------------------------------------------------------------
        // Parsitaan tiedot yksi kerrallaan ja lisätään ne tietokantaan --------
        for(int i = 0 ; i < nodes.getLength() ; i++ ){
            
            
            Node nodePostCode = nodes.item(i);        
            Element ePostCode = (Element) nodePostCode;
            nodePostCode = doc.getElementsByTagName("code").item(i);
            
            
//            System.out.println(nodePostCode.getTextContent());

            Node nodeCity = nodes.item(i);
            Element eCity = (Element) nodeCity;
            nodeCity = doc.getElementsByTagName("city").item(i);
            
//            System.out.println(nodeCity.getTextContent());
            
            Node nodeAddress = nodes.item(i);
            Element eAddress = (Element) nodeAddress;
            nodeAddress = doc.getElementsByTagName("address").item(i);
            
//            System.out.println(nodeAddress.getTextContent());
            
            Node nodeAvailability = nodes.item(i);
            Element eAvailability = (Element) nodeAvailability;
            nodeAvailability = doc.getElementsByTagName("availability").item(i);
            
//            System.out.println(nodeAvailability.getTextContent());
            
            Node nodePostoffice = nodes.item(i);
            Element ePostoffice = (Element) nodePostoffice;
            nodePostoffice = doc.getElementsByTagName("postoffice").item(i);
            
            
            Node nodeLat = nodes.item(i);
            Element eLat = (Element) nodeLat;
            nodeLat = doc.getElementsByTagName("lat").item(i);
            
            Node nodeLng = nodes.item(i);
            Element eLng = (Element) nodeLng;
            nodeLng = doc.getElementsByTagName("lng").item(i);
            
            
            
            String sql = "INSERT INTO Smartposts"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, 'false');";
            
            // Paloitellaan nimi pilkulla
            String[] nimiArray = nodePostoffice.getTextContent().split(",");
            // Otetaan kaikki paitsi ensimmäinen palanen (jätetään se helvetin "PAKETTIAUTOMAATTI"-teksti pois)
            nimiArray = Arrays.copyOfRange(nimiArray, 1, nimiArray.length);
            // Muutetaan array stringiksi ja poistetaan alusta turhat jutut.
            String nimi = Arrays.toString(nimiArray).substring(2, Arrays.toString(nimiArray).length()-1);
            
            stmt = c.prepareStatement(sql);
            stmt.setString(1, nimi );
            stmt.setString(2, nodeAvailability.getTextContent() );
            stmt.setString(3, nodeAddress.getTextContent() );
            stmt.setString(4, nodePostCode.getTextContent() );
            stmt.setString(5, nodeCity.getTextContent() );
            stmt.setString(6, nodeLat.getTextContent());
            stmt.setString(7, nodeLng.getTextContent());            
            
            
            stmt.executeUpdate();

            
        }
        if(stmt != null)
            stmt.close();
        
        c.commit();
        c.close();
        
        System.out.println("SMARTPOSTS INSERTED into database.");

    }
}