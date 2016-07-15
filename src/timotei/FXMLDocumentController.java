/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.io.IOException;
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
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Kousa
 */
public class FXMLDocumentController implements Initializable {
    
    
    
    @FXML
    private TextField addToMapSearch;
    private ComboBox<String> addToMapCombo;
    @FXML
    private TextField sendFromSearch;
    @FXML
    private TextField destinationSearch;
    @FXML
    private ListView<String> availablePacketsListView;
    @FXML
    private ListView<String> packetInfoListView;
    @FXML
    private ListView<String> eventListView;
    @FXML
    private ListView<String> eventInfoListView;
    @FXML
    private TextField reclamationShipmentSearch;
    @FXML
    private WebView webviewComponent;
    @FXML
    private ComboBox<String> shipmentTypeCombo;
    @FXML
    private Label ownerFoundLabel;
    @FXML
    private ListView<String> storageListView;
    @FXML
    private Label packageAddedNotification;
    private Label smartPostFound;
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
    @FXML
    private TextField packetOwnerID;
    @FXML
    private ListView<String> packetTypeListView;
    @FXML
    private TextField packetOwnerName;
    @FXML
    private ListView<String> foundOwnerList;
    

    @FXML
    private ListView<String> storagePacketInfoListView;
    @FXML
    private Button addToMapButton;
    @FXML
    private Button clearMapButton;
    @FXML
    private Button sendShipmentButton;
    @FXML
    private Label shipmentSentLabel;
    @FXML
    private Button acceptAndAddButton;
    @FXML
    private Button removePacketButton;
    @FXML
    private Button editPacketButton;
    @FXML
    private Button makeReclamationButton;
    @FXML
    private ListView<String> reclamationListView;
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
    private ListView<String> addToMapListView;

    SmartPostHandler sph = new SmartPostHandler();
    PersonHandler personHandler = new PersonHandler();
    Storage storage = new Storage();
    ShipmentHandler shipHandler = new ShipmentHandler();
    ReclamationHandler reclamationHandler = new ReclamationHandler();
    

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
        shipmentTypeCombo.getItems().add("1 - pikalähetys maksimissaan 150km, kaikki paketit särkyvät");
        shipmentTypeCombo.getItems().add("2 - Turvakuljetus, ei isoja paketteja");
        shipmentTypeCombo.getItems().add("3 - FedEx-paketti");

        // Update packageTypeList from database
        storage.updatePackageTypeList();
        
        // Update shipmentlist from database 
        shipHandler.updateShipmentList();

        // Populate PackageType-ComboBox with available PackageTypes
        packetTypeListView.getItems().clear();
        Iterator itr = storage.getPackageTypeList().iterator();
        while (itr.hasNext()) {
            packetTypeListView.getItems().add( ( ( (String)itr.next() ).split(":"))[0] );
        }
        
        // Set shipmentType value to not null.
        shipmentTypeCombo.setValue("Valitse lähetystyyppi");
        
        // Update persons from DB (only once per run, it's only possible to add persons through DB)
        personHandler.updatePersonList();
        
        // Update reclamations from DB
        reclamationHandler.updateReclamationList();
        
        // Update all the necessery fields. 
        update();
        
    }    



 // -------------------------------------------   
 // Varasto-välilehden toiminnallisuudet
    
    @FXML
    private void acceptAndAddAction(ActionEvent event) {
        
        if(packetTypeListView.getSelectionModel().isEmpty() || foundOwnerList.getSelectionModel().isEmpty()){
            
            System.out.println("Et ole valinnut kaikkia juttuja.");
            
        } else {
            storage.addPackage(Integer.parseInt(packetTypeListView.getSelectionModel().getSelectedItem().substring(0, 1)), 
                    foundOwnerList.getSelectionModel().getSelectedItem().substring(0,4));
            packageAddedNotification.setText("Paketti lisätty.");
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
    private void addOwnerByIDAction(KeyEvent event) {
        
        String name = null;
        ArrayList<Person> pList;
        pList = personHandler.getPersonByID(packetOwnerID.getText());

        if (!pList.isEmpty()) {
            foundOwnerList.getItems().clear();
            System.out.println("Henkilö löytyi");
            ownerFoundLabel.setText("Henkilö löytyi");
            Iterator itr = pList.iterator();
            Person p = null;
            while (itr.hasNext()) {
                p = (Person) itr.next();
                foundOwnerList.getItems().add(p.getPersonID() + ":\t" + p.getFirstName() + " " + p.getLastName());
            }
        } else {
            ownerFoundLabel.setText("Henkilöä ei löytynyt.");
        }

        
    }

    @FXML
    private void addOwnerByNameAction(KeyEvent event) {
        
        String name = null;
        ArrayList<Person> pList;
        pList = personHandler.getPersonByName(packetOwnerName.getText());

        if (!pList.isEmpty()) {
            foundOwnerList.getItems().clear();
            System.out.println("Henkilö löytyi");
            ownerFoundLabel.setText("Henkilö löytyi");
            Iterator itr = pList.iterator();
            Person p = null;
            while (itr.hasNext()) {
                p = (Person) itr.next();
                foundOwnerList.getItems().add(p.getPersonID() + ":\t" + p.getFirstName() + " " + p.getLastName());
            }
        } else {
            ownerFoundLabel.setText("Henkilöä ei löytynyt.");
        }

    }

    @FXML
    private void packetTypeListAction(MouseEvent event) {
        
        packetInfoListView.getItems().clear();
        packetInfoListView.getItems().add(storage.getPackageTypeList().get(Integer.valueOf(packetTypeListView.getSelectionModel().getSelectedItem().substring(0, 1))-1));
        
    }

    @FXML
    private void storagePacketInfoRequested(MouseEvent event) {
        
        if(!storageListView.getSelectionModel().isEmpty()){
            storagePacketInfoListView.getItems().clear();
            Package p;
            p = storage.findPackage(storageListView.getSelectionModel().getSelectedItem().substring(0, 6));
            storagePacketInfoListView.getItems().add("Packet ID: \t" + p.getPacketID());
            storagePacketInfoListView.getItems().add("Owner ID: \t" + p.getOwnerID());
            storagePacketInfoListView.getItems().add("Item info: \t" + storage.getPackageTypeList().get(p.getItemType()-1));
        }
        
    }
    
    @FXML
    private void addToMapSearchKeyReleased(KeyEvent event) {
        addToMapListView.getItems().clear();
        String search;
        search = addToMapSearch.getText();
        ArrayList<String> matchingSPList;
        matchingSPList = sph.findSmartPost(search);
        Iterator itr = matchingSPList.iterator();
        while (itr.hasNext()) {
            addToMapListView.getItems().add(itr.next().toString());
        }
    }

    @FXML
    private void sendFromKeyReleased(KeyEvent event) {
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
    private void destinationKeyReleased(KeyEvent event) {
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
    private void addToMapAction(ActionEvent event) throws SQLException {

        String parameter1 = null;
        String parameter2 = null;
        String parameter3 = null;
        SmartPost sp;
        sp = sph.getSmartPostByName(addToMapListView.getSelectionModel().getSelectedItem());

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
    private void clearMapAction(ActionEvent event) throws IOException {
        webviewComponent.getEngine().executeScript("document.deletePaths()");
    }


    // Paketin lähettäminen.
    @FXML
    private void sendShipmentAction(ActionEvent event) throws IOException {

        String color = null;
        double speed = 0;
        boolean okToSend = false;
        boolean settingError = false;
        Boolean shipmentBroke = false;
        ArrayList<String> errorText = new ArrayList();
        
        if (availablePacketsListView.getSelectionModel().isEmpty()){
            settingError = true;
            errorText.add("Et valinnut pakettia");
        }
        if(sendFromListView.getSelectionModel().isEmpty()){
            settingError = true;
            errorText.add("Et valinnut lähetyspaikkaa");
        }
        if(destinationListView.getSelectionModel().isEmpty()){
            settingError = true;
            errorText.add("Et valinnut lähetyskohdetta");
        }
        if(shipmentTypeCombo.getSelectionModel().isEmpty()){
            settingError = true;
            errorText.add("Et valinnut lähetystapaa");          
        }
        
        if(settingError){
            showError(errorText);
            return;
        }
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
        
        PackageType pt = storage.getPackageTypeObjectList().get(
                storage.findPackage(availablePacketsListView.getSelectionModel().getSelectedItem()).getItemType()-1);
        
        // lähetystyypin valintaan perustuvat asetukset. 
        switch(shipmentTypeCombo.getValue().substring(0, 1)){
            case "1": 
                if(length > 150){
                    System.out.println("Matka liian pitkä ykköstyypin lähetykselle, haluatko valita toisen lähetystyypin?");
                    errorText.add("Matka on liian pitkä ykköstyypin lähetykselle");
                }else{
                    speed = 0.5; 
                    color = "RED";
                    System.out.println("Herkät paketit menee rikki");
                    
                    if(pt.isItemIsFragile()){
                        errorText.add("Huom! Pakettinne saattaa rikkoutua lähetyksessä");
                        if(Math.random() > 0.5){
                            shipmentBroke = true;
                            System.out.println("Se meni sitte paskaks. Onks nyt hyvä mieli?!");
                            errorText.add("Se paketti meni sitte paskaks. Onks nyt hyvä mieli?!");
                        }
                        else{
                            System.out.println("Sulla oli tuuria.");
                        }
                    }
                    okToSend = true;
                }
                
                break;
            case "2":
                if((storage.findPackage(availablePacketsListView.getSelectionModel().getSelectedItem()).getItemType()) == 7){
                    System.out.println("Paketti on liian iso ja rikkoutuisi lähetyksessä, valitse toinen lähetystyyppi");
                    errorText.add("Paketti on liian iso ja rikkoutuisi lähetyksessä");
                }else{
                    speed = 3;
                    color = "YELLOW";
                    okToSend = true;
                    
                    if (pt.isItemIsFragile() && Math.random() > 0.95) {
                        shipmentBroke = true;
                        System.out.println("Se meni sitte paskaks. Tsooriii!");
                        errorText.add("Se sun paketti meni sitte paskaks. Tsooriii!");
                    }
                }
                
                
                
                break;
            case "3":
                
                speed = 10;
                color = "GREEN";
                System.out.println("Nyt viskellään!");
                okToSend = true;
                
                if(pt.isItemIsFragile()){
                    if (Math.random() > 0.05) {
                        shipmentBroke = true;
                        System.out.println("Se meni sitte paskaks. Onks nyt hyvä mieli?!");
                    }else{
                        System.out.println("Kuin ihmeen kaupalla paketti säilyi ehjänä. ");
                    }
                    System.out.println("Pakettinne särkyi.. Timotei-miehellä oli huono päivä.");
                }else if (pt.getItemName().contains("Nokia")){
                    System.out.println("Lähetitte sitte nokian fedex-pakettina... Kuriiri vähän viskeli ja rikkoi sillä tiiliseinän.");
                    System.out.println("ONKO NYT HYVÄ MIELI MITÄ?!");
                    errorText.add("Lähetitte sitte nokian fedex-pakettina... Kuriiri vähän viskeli ja rikkoi sillä tiiliseinän.");
                    errorText.add("ONKO NYT HYVÄ MIELI MITÄ?!");
               }

                

                
                
                break;
            default:
                System.out.println("Jotain meni vikaan.");
                break;
        }
        
        if(okToSend){
            webviewComponent.getEngine().executeScript("document.createPath(" + pathArray + ", '" + color + "'," + speed + ")");

            shipHandler.createShipment(availablePacketsListView.getSelectionModel().getSelectedItem(),
                    sendFromListView.getSelectionModel().getSelectedItem(),
                    destinationListView.getSelectionModel().getSelectedItem(),
                    shipmentTypeCombo.getValue(),
                    shipmentBroke,
                    length);
            
            sendPacketsInfoListView.getItems().clear();
            
            update();
        if(!errorText.isEmpty())
            showError(errorText);
        }
        
    
        

//    - pathLength(pathArray) - kahden pisteen latitude ja longitude listassa.Järjestys on lähtöpisteen Latitude, Longitude, Kohteen Latitude, Longitude.
    }
    
    @FXML
    private void sendFromListMouseClicked(MouseEvent event) {
        if (!sendFromListView.getSelectionModel().isEmpty()) {
            sendFromSearch.setText(sendFromListView.getSelectionModel().getSelectedItem());
        } else {
            System.out.println("Ei ole valittuna.");
        }
        
    }

    @FXML
    private void destinationListMouseClicked(MouseEvent event) {
        if (!destinationListView.getSelectionModel().isEmpty()) {
            destinationSearch.setText(destinationListView.getSelectionModel().getSelectedItem());
        } else {
            System.out.println("Ei ole valittuna.");
        }
    }
    
    
    // Käyttäjä klikkaa haluamaansa pakettia listasta ja sen tiedot avautuvat viereiseen listanäkymään. 
    @FXML
    private void availablePacketsMouseClicked(MouseEvent event) {
        
        if (!availablePacketsListView.getSelectionModel().isEmpty()) {
            sendPacketsInfoListView.getItems().clear();
            
            Package packet;
            packet = storage.findPackage(availablePacketsListView.getSelectionModel().getSelectedItem().substring(0, 6));

            sendPacketsInfoListView.getItems().add("Package ID:\t " + packet.getPacketID());
            sendPacketsInfoListView.getItems().add("Owner:\t " + packet.getOwnerID());
            sendPacketsInfoListView.getItems().add("Item type:\t " + packet.getItemType());
            sendPacketsInfoListView.getItems().add("Item name: " + storage.getPackageTypeList().get(packet.getItemType()-1));
        } else {
            System.out.println("Ei ole valittuna.");
        }

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



// -------------------------------------------   
// Asioiden päivittäminen kun jotain tapahtuu.
    private void update() {

        // Update packageList from database
        storage.updatePackageList();

        // Populate package-list with the available packets.
        availablePacketsListView.getItems().clear();
        Iterator itr = storage.getPackageList().iterator();
        while (itr.hasNext()) {
            availablePacketsListView.getItems().add(((Package) itr.next()).getPacketID());
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
            info = info + ": " + (storage.getPackageTypeList().get(p.getItemType() - 1)).split(":")[0];
            storageListView.getItems().add(info);
        }
       
        // Tapahtumalokin päivittäminen
        itr = shipHandler.getEventList().iterator();
        eventListView.getItems().clear();
        while (itr.hasNext()) {
            eventListView.getItems().add(((String) itr.next()));
        }

        // Reklamaatiolistan päivittäminen. 
        itr = reclamationHandler.getReclamationList().iterator();
        reclamationListView.getItems().clear();
        while (itr.hasNext()) {
            reclamationListView.getItems().add((itr.next()).toString());
        }
    }

    
    @FXML
    private void editPacketAction(ActionEvent event) {
    }

    @FXML
    private void statisticDateAction(ActionEvent event) {
    }

    @FXML
    private void statisticTypeAction(ActionEvent event) {
    }
    
    
// -------------------------------------------   
// Uuden ikkunan avaaminen virheiden näyttämistä varten. 
    
    public Stage showError(ArrayList<String> errors) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ErrorAlertWindow.fxml"));
        Stage stage = new Stage(StageStyle.DECORATED);

        stage.setScene(new Scene((Pane) loader.load()));

        ErrorAlertWindowController controller = loader.<ErrorAlertWindowController>getController();
        controller.initData(errors);
        stage.show();

        return stage;

    }

    
// -------------------------------------------   
// Reklamaatio
    @FXML
    private void sendReclamation(ActionEvent event) {
       reclamationHandler.addReclamation(reclamationShipmentSearch.getText(), reclamationDescriptionField.getText());
    }

    @FXML
    private void checkShipmentAction(ActionEvent event) {

        if (shipHandler.getShipmentByID(reclamationShipmentSearch.getText()) != null) {
            shipmentIDFoundLabel.setText("Lähetys löytyy järjestelmästä.");
        } else {
            shipmentIDFoundLabel.setText("Lähetystä ei löytynyt, tarkista ID.");
        }
    }



}

