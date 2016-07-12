/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;

/**
 *
 * @author Kousa
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TextField addToMapSearch;
    @FXML
    private ComboBox<String> addToMapCombo;
    @FXML
    private Button addToMapButton;
    @FXML
    private Button clearMapButton;
    @FXML
    private TextField sendFromSearch;
    @FXML
    private TextField destinationSearch;
    @FXML
    private ListView<String> availablePacketsListView;
    @FXML
    private Button sendShipmentButton;
    @FXML
    private ComboBox<String> packetTypeCombo;
    @FXML
    private Button acceptAndAddButton;
    @FXML
    private Button removePacketButton;
    @FXML
    private Button editPacketButton;
    @FXML
    private ListView<String> packetInfoListView;
    @FXML
    private ListView<String> eventListView;
    @FXML
    private ListView<String> eventInfoListView;
    @FXML
    private ListView<?> ReclamationListView;
    @FXML
    private TextField reclamationShipmentSearch;
    @FXML
    private Button checkShipmentButton;
    @FXML
    private TextArea reclamationDescriptionField;
    @FXML
    private CheckBox contactMeCheckBox;
    @FXML
    private ListView<?> employeeStressListView;
    @FXML
    private DatePicker statisticDatePicker;
    @FXML
    private ComboBox<?> statisticTypeComboBox;
    @FXML
    private ListView<?> statisticListView;
    @FXML
    private WebView webviewComponent;
    @FXML
    private ComboBox<String> shipmentTypeCombo;
    @FXML
    private TextField packetOwnerText;
    @FXML
    private Label ownerFoundLabel;
    @FXML
    private Label ownerName;
    @FXML
    private ListView<String> storageListView;
    @FXML
    private Label packageAddedNotification;

    SmartPostHandler sph = new SmartPostHandler();
    PersonHandler personHandler = new PersonHandler();
    Storage storage = new Storage();
    ShipmentHandler shipHandler = new ShipmentHandler();
    @FXML
    private Label smartPostFound;
    @FXML
    private Label shipmentSentLabel;
    @FXML
    private Button makeReclamationButton;
    @FXML
    private Tab reclamationsTab;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label shipmentIDFoundLabel;
    @FXML
    private ListView<String> sendFromListView;
    @FXML
    private ListView<String> destinationListView;
    @FXML
    private ListView<String> sendPacketsInfoListView;

    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        System.out.println("initialize !!_!");
        // Download the map to the screen.
        webviewComponent.getEngine().load(getClass().getResource("index.html").toExternalForm());

        // Load the smartposts from the interwebs if they're not yet downloaded. 
        // Checking system to be added. 
        if(false){
            try {
                XMLReader xmlr = new XMLReader();
            } catch (SQLException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        sph.getSmartPostList();
        
        // Populate shipmentType combobox with available options. 
        shipmentTypeCombo.getItems().add("1");
        shipmentTypeCombo.getItems().add("2");
        shipmentTypeCombo.getItems().add("3");

        // Update packageTypeList from database
        storage.updatePackageTypeList();
        
        // Update shipmentlist from database 
        shipHandler.updateShipmentList();

        // Populate PackageType-ComboBox with available PackageTypes
        packetTypeCombo.getItems().clear();
        Iterator itr = storage.getPackageTypeList().iterator();
        while (itr.hasNext()) {
            packetTypeCombo.getItems().add((String) itr.next());
        }
        
        // Set shipmentType value to not null.
        
        shipmentTypeCombo.setValue("Valitse lähetystyyppi");
        
        
        // Update all the necessery fields. 
        update();
        
    }    



 // -------------------------------------------   
 // Varasto-välilehden toiminnallisuudet
    
    @FXML
    private void acceptAndAddAction(ActionEvent event) {
        System.out.println("Clicked AcceptAndAdd");
        
        if(packetTypeCombo.getValue().contains("0") && ownerFoundLabel.getText().contains("Henkilö löytyi")){
            storage.addPackage(Integer.parseInt(packetTypeCombo.getValue().substring(0, 2)), packetOwnerText.getText());
            packageAddedNotification.setText("Paketti lisätty.");
            System.out.println("Pakettia ainakin yritetty lisätä.");
            update();
        }
    }   

    @FXML
    private void removePacketAction(ActionEvent event) {
        
        // Tarkistetaan onko käyttäjä valinnut mitään. 
        if(!storageListView.getSelectionModel().isEmpty()){
            storage.removePackage(storageListView.getSelectionModel().getSelectedItem().substring(0, 6));
            update();
        }
    }

    @FXML
    private void editPacketAction(ActionEvent event) {
    }
    
    @FXML
    private void addOwnerAction(ActionEvent event) {
        personHandler.addPerson();
        String name = null;
        name = personHandler.getNameByID(packetOwnerText.getText());

        if (name != null) {
            System.out.println("Henkilö löytyi");
            ownerFoundLabel.setText("Henkilö löytyi");
            ownerName.setText(name);
        } else {
            ownerFoundLabel.setText("Henkilöä ei löytynyt.");
        }

    }

    @FXML
    private void packetTypeAction(ActionEvent event) {
        packetInfoListView.getItems().clear();
//        Iterator itr;
//        itr = storage.getPackageTypeList().iterator();
//        while (itr.hasNext()) {
//            packetInfoListView.getItems().add((String) itr.next());
//        }
        packetInfoListView.getItems().add(packetTypeCombo.getValue());


    }

 // -------------------------------------------   
 // Statistiikka-välilehden toiminnallisuudet
    
    @FXML
    private void statisticDateAction(ActionEvent event) {
    }

    @FXML
    private void statisticTypeAction(ActionEvent event) {
    }
    
    
// -------------------------------------------   
// Karttavälilehden toiminnallisuudet
    
    @FXML
    private void addToMapSearchAction(ActionEvent event) {
        
        addToMapCombo.getItems().clear();
        String search; 
        search = addToMapSearch.getText();
        ArrayList<String> matchingSPList;
        matchingSPList = sph.findSmartPost(search);
        Iterator itr = matchingSPList.iterator();
        while(itr.hasNext())    
            addToMapCombo.getItems().add(itr.next().toString());
        
        if(addToMapCombo.getItems().isEmpty())
            smartPostFound.setText("Hakuehdoilla ei löytynyt pakettiautomaattia.");
        else
            smartPostFound.setText("Hakuehdoilla löytyi: " + String.valueOf(addToMapCombo.getItems().size()) + " automaattia.");
        
    }
    @FXML
    private void addToMapAction(ActionEvent event) throws SQLException {

        String parameter1 = null;
        String parameter2 = null;
        String parameter3 = null;
        SmartPost sp;
        sp = sph.getSmartPostByName(addToMapCombo.getValue());

        // Osoite
        parameter1 = sp.getGp().getLat();
        // Muut tiedot
//        parameter2 = sp.getName() + " " + sp.getOpenhours();
        parameter2 = sp.getGp().getLng();
        // Väri
        parameter3 = sp.getName() + "   Avoinna: " + sp.getOpenhours();
        String script = "document.createMarker('" + parameter1 + "', '" + parameter2 + "', '"
                + parameter3 + "')";
        webviewComponent.getEngine().executeScript(script);

//        createMarker(lati, loni, info) - latitude, longitude missä tahansa muodossa, testattu
//				Double ja String, Mahdollinen info popuppiin stringinä.
    }

    @FXML
    private void clearMapAction(ActionEvent event) {

        webviewComponent.getEngine().executeScript("document.deletePaths()");

    }


    // Paketin lähettäminen.
    @FXML
    private void sendShipmentAction(ActionEvent event) {

        String color = null;
        int speed = 0;
        boolean okToSend = false;
        

        ArrayList<String> pathArray = new ArrayList();

        SmartPost.GeoPoint fGp = sph.getSmartPostByName(sendFromListView.getSelectionModel().getSelectedItem()).getGp();
        SmartPost.GeoPoint dGp = sph.getSmartPostByName(destinationListView.getSelectionModel().getSelectedItem()).getGp();

        pathArray.add(fGp.getLat());
        pathArray.add(fGp.getLng());
        pathArray.add(dGp.getLat());
        pathArray.add(dGp.getLng());
        
        Double length;
        length = (Double)webviewComponent.getEngine().executeScript("document.pathLength(" + pathArray + ")");
        System.out.println(length.toString());
        
        // pakettityypin valintaan perustuvat asetukset. 
        switch(shipmentTypeCombo.getValue()){
            case "1": 
                if(length > 150){
                    System.out.println("Matka liian pitkä ykköstyypin lähetykselle, haluatko valita toisen lähetystyypin?");
                }else{
                    speed = 9; 
                    color = "RED";
                    System.out.println("Herkät paketit menee rikki");
                    okToSend = true;
                }
                break;
            case "2":
                if((storage.findPackage(availablePacketsListView.getSelectionModel().getSelectedItem()).getItemType()) == 7){
                    System.out.println("Paketti on liian iso ja rikkoutuisi lähetyksessä, haluatko valita toisen lähetystyypin?");
                }else{
                    speed = 5;
                    color = "YELLOW";
                    okToSend = true;
                }
                break;
            case "3":
                speed = 1;
                color = "GREEN";
                System.out.println("Nyt viskellään!");
                okToSend = true;
                break;
            default:
                System.out.println("Et valinnut lähetystyyppiä.");
                break;
        }
        
        if(okToSend){
            webviewComponent.getEngine().executeScript("document.createPath(" + pathArray + ", '" + color + "', 9)");

            shipHandler.createShipment(availablePacketsListView.getSelectionModel().getSelectedItem(),
                    sendFromListView.getSelectionModel().getSelectedItem(),
                    destinationListView.getSelectionModel().getSelectedItem(),
                    shipmentTypeCombo.getValue(),
                    length);
            
            update();
        }
        
        

//    - pathLength(pathArray) - kahden pisteen latitude ja longitude listassa.Järjestys on lähtöpisteen Latitude, Longitude, Kohteen Latitude, Longitude.
    }


    @FXML
    private void sendFromKeyTyped(KeyEvent event) {
        
        sendFromListView.getItems().clear();
        String search;
        search = sendFromSearch.getText();
        ArrayList<String> matchingSPList;
        matchingSPList = sph.findSmartPost(search);
        Iterator itr = matchingSPList.iterator();
        while (itr.hasNext()) {
            sendFromListView.getItems().add(itr.next().toString());
        }
    }



    @FXML
    private void destinationKeyTyped(KeyEvent event) {
        
        destinationListView.getItems().clear();
        String search;
        search = destinationSearch.getText();
        ArrayList<String> matchingSPList;
        matchingSPList = sph.findSmartPost(search);
        Iterator itr = matchingSPList.iterator();
        while (itr.hasNext()) {
            destinationListView.getItems().add(itr.next().toString());
        }
    }
    
    @FXML
    private void availablePacketsMouseClicked(MouseEvent event) {
        
        System.out.println("Klikkailtiin juu");
        if (!availablePacketsListView.getSelectionModel().isEmpty()) {
            sendPacketsInfoListView.getItems().clear();
            
            System.out.println("Pitäis olla valittunaki.");
            Package packet;
            packet = storage.findPackage(availablePacketsListView.getSelectionModel().getSelectedItem().substring(0, 6));

            sendPacketsInfoListView.getItems().add("Package ID:\t " + packet.getPacketID());
            sendPacketsInfoListView.getItems().add("Owner:\t " + packet.getOwnerID());
            sendPacketsInfoListView.getItems().add("Item type:\t " + packet.getItemType());
            sendPacketsInfoListView.getItems().add("Item name: " + storage.getPackageTypeList().get(packet.getItemType()));
        } else {
            System.out.println("Ei ole valittuna.");
        }

    }


    
// -------------------------------------------   
// Asioiden päivittäminen kun jotain tapahtuu.
    private void update(){

        // Update packageList from database
        storage.updatePackageList();
        

        
        // Populate package-list with the available packets.
        availablePacketsListView.getItems().clear();
        Iterator itr = storage.getPackageList().iterator();
        while (itr.hasNext()) {
            availablePacketsListView.getItems().add(((Package)itr.next()).getPacketID());
        }

        // Populate storage-list with available packets.
        storageListView.getItems().clear();
        storage.updatePackageList();
        itr = storage.getPackageList().iterator();
        Package p;
        String info;
        while (itr.hasNext()) {
            p = (Package) itr.next();
            info = p.getPacketID();
            info = info + ": " + storage.getPackageTypeList().get(p.getItemType() - 1);
            storageListView.getItems().add(info);
        }
        System.out.println("Update done");
        
        // Tapahtumalokin päivittäminen
        
        itr = shipHandler.getEventList().iterator();
        eventListView.getItems().clear();
        while(itr.hasNext())
            eventListView.getItems().add(((String)itr.next()));
        

    }

    
// Tapahtumaloki-välilehden toiminnallisuudet     
    
    @FXML
    private void getEventInfo(MouseEvent event) {
        
        if(!eventListView.getSelectionModel().isEmpty()){
            eventInfoListView.getItems().clear();
            
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);
            
            Shipment shipment;
            shipment = shipHandler.getShipmentByID(eventListView.getSelectionModel().getSelectedItem().substring(0, 6));
            
            eventInfoListView.getItems().add("Shipment ID:\t " + shipment.getShipmentID());
            eventInfoListView.getItems().add("Package ID:\t " + shipment.getPacketID());
            eventInfoListView.getItems().add("Sent from:\t " + shipment.getSentFrom());
            eventInfoListView.getItems().add("Destination:\t " + shipment.getDestination());
            eventInfoListView.getItems().add("Shipment type: " + shipment.getShipmentType());
            eventInfoListView.getItems().add("Time:\t\t " + format.format(shipment.getSentTime()));
            
        }else{
            System.out.println("Ei ole valittuna.");
        }
        
    }

    @FXML
    private void makeReclamationAction(ActionEvent event) {
        
        if (!eventListView.getSelectionModel().isEmpty()){
            
            reclamationShipmentSearch.setText(
                shipHandler.getShipmentByID(eventListView.getSelectionModel().getSelectedItem().substring(0, 6)).getShipmentID());
            SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
            checkShipmentAction(event);
            shipmentIDFoundLabel.setText("Lähetys löytyy järjestelmästä.");
            selectionModel.select(reclamationsTab);
            
            
            
        } 
        
    }

    @FXML
    private void checkShipmentAction(ActionEvent event) {
        
        if(shipHandler.getShipmentByID(reclamationShipmentSearch.getText()) != null)
            shipmentIDFoundLabel.setText("Lähetys löytyy järjestelmästä.");
        else
            shipmentIDFoundLabel.setText("Lähetystä ei löytynyt, tarkista ID.");
    }

    @FXML
    private void sendFromSearchAction(ActionEvent event) {
    }

    @FXML
    private void destinationSearchAction(ActionEvent event) {
    }




}
