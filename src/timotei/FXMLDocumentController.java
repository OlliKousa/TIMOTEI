/*
 * Class NAME: FXMLDocumentController
 * Created BY: Olli Kousa
 * Creation DATE: 4.7.2016
 * Last MODIFIED: 21.7.2016
 * This is the controller of the main window. 
 * It holds up all the functionalities that involve manipulating the visible components.
 * The window is opened by WelcomeScreenController after user has given correct login information. 
 */
package timotei;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
    private ListView<String> employeeStressListView;
    @FXML
    private DatePicker statisticDatePicker;
    @FXML
    private ComboBox<String> statisticTypeComboBox;
    @FXML
    private ListView<String> statisticListView;
    @FXML
    private ListView<String> addToMapListView;
    @FXML
    private Button showStatisticButton;
    @FXML
    private ListView<String> statisticLongListView;
    @FXML
    private CheckBox removeCascadeCheckBox;
    @FXML
    private Pane bannerPane;
    @FXML
    private Label reclamationSentLabel;
    @FXML
    private ListView<String> throwerMonthListView;
    
    // Creating all the handlers for different classes. 
    SmartPostHandler sph = new SmartPostHandler();
    PersonHandler personHandler = new PersonHandler();
    Storage storage = new Storage();
    ShipmentHandler shipHandler = new ShipmentHandler();
    ReclamationHandler reclamationHandler = new ReclamationHandler();
    LogOperator logOperator = new LogOperator();

    java.util.Date dateLimit;

    
    public void initData(boolean useDB){
        
        if(useDB){
            // If user wants to use old data, datelimit for shipments, 
            // packages and reclamations to be shown is set to date zero (year 1970 something)
            dateLimit = new java.util.Date(0);
        }else{
            // If user doesn't want to use old data, datelimit is set to current time (program startup)
            // so only data created in the current session is shown. 
            dateLimit = new java.util.Date();
        }
        
        System.out.println("Alustetaan ohjelma!");
        // Download map to the screen.
        webviewComponent.getEngine().load(getClass().getResource("index.html").toExternalForm());

        // Load smartposts from interwebs to database if they're not yet downloaded. 
        if (!sph.isSmartPostDownloaded()) {
            try {
                XMLReader xmlr = new XMLReader();
            } catch (SQLException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // Load smartposts from the database.
        sph.getSmartPostList();

        // Populate shipmentType combobox with available options. 
        shipmentTypeCombo.getItems().add("1 - pikalähetys maksimissaan 150km, kaikki paketit särkyvät");
        shipmentTypeCombo.getItems().add("2 - Turvakuljetus, ei isoja paketteja");
        shipmentTypeCombo.getItems().add("3 - FedEx-paketti");

        // Update packageTypeList from database
        storage.updatePackageTypeList();        

        // Populate PackageType-ComboBox with available PackageTypes
        packetTypeListView.getItems().clear();
        Iterator itr = storage.getPackageTypeList().iterator();
        while (itr.hasNext()) {
            packetTypeListView.getItems().add((((String) itr.next()).split(":"))[0]);
        }

        // Set shipmentType value to not null.
        shipmentTypeCombo.setValue("Valitse lähetystyyppi");

        // Update persons from DB (only once per run, it's only possible to add persons through DB)
        personHandler.updatePersonList();

        // Update shipmentlist from database 
        shipHandler.updateShipmentList();
        // Update reclamations from DB
        reclamationHandler.updateReclamationList();
        

        // Populate available statistic info
        statisticTypeComboBox.getItems().add("1 - Tietyn päivän lähetysten yhteismatka");
        statisticTypeComboBox.getItems().add("2 - Tietyn päivän lähetysten keskimatka");
        statisticTypeComboBox.getItems().add("3 - Tietyn päivän kaikki lähetykset");
        statisticTypeComboBox.getItems().add("4 - Tietyn päivän hajonneiden lähetysten määrä");
        
        // Update all other necessery fields. 
        update();
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

// --------------------------------------------------------------------
// FUNCTIONALITIES OF THE MAP-TAB
    
    @FXML
    // Search for smartposts matching the given string. 
    // Results will include matches in name or city of the smartpost. 
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
    // See previous
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
    // See previous
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
    // Adds selected smartpost to the map through javascript-interface. 
    // Also adds opening hours and address of the smartpost that can be opened by clicking the point.
    private void addToMapAction(ActionEvent event) throws SQLException {

        String parameter1 = null;
        String parameter2 = null;
        String parameter3 = null;
        SmartPost sp;
        sp = sph.getSmartPostByName(addToMapListView.getSelectionModel().getSelectedItem());

        // Coordinates - Latitude
        parameter1 = sp.getGp().getLat();
        // Coordinates - Longitude
        parameter2 = sp.getGp().getLng();
        // Info (Address + Opening hours)
        parameter3 = sp.getName() + "   Avoinna: " + sp.getOpenhours();
        String script = "document.createMarker('" + parameter1 + "', '" + parameter2 + "', '"
                + parameter3 + "')";
        webviewComponent.getEngine().executeScript(script);

    }

    @FXML
    // Clears all points and routes from the map.
    private void clearMapAction(ActionEvent event) throws IOException {
        webviewComponent.getEngine().executeScript("document.deletePaths()");
    }
    
    @FXML
    // Sending the shipment. Quite complicated process with many variations.
    private void sendShipmentAction(ActionEvent event) throws IOException {

        String color = null;
        double speed = 0;
        boolean okToSend = false;
        boolean settingError = false;
        Boolean shipmentBroke = false;
        ArrayList<String> errorText = new ArrayList();
        ArrayList<TimoteiMan> couriers = new ArrayList();
        
        // First check if all the required selections are made.
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
        // If some selections are missing, method shows errorWindow and returns. 
        if(settingError){
            showError(errorText);
            return;
        }
        
        // Coordinates are given to JavaScript-method in ArrayList.
        ArrayList<String> pathArray = new ArrayList();

        // Acquiring the coordinates. 
        SmartPost.GeoPoint fGp = sph.getSmartPostByName(sendFromListView.getSelectionModel().getSelectedItem()).getGp();
        SmartPost.GeoPoint dGp = sph.getSmartPostByName(destinationListView.getSelectionModel().getSelectedItem()).getGp();
        pathArray.add(fGp.getLat());
        pathArray.add(fGp.getLng());
        pathArray.add(dGp.getLat());
        pathArray.add(dGp.getLng());
        
        // Counting the distance between the selected smartposts. 
        Double length;
        length = (double)webviewComponent.getEngine().executeScript("document.pathLength(" + pathArray + ")");
        length = (double)Math.round(length / 10) /100  ;
        System.out.println(length.toString());
        
        // Acquiring the packagetype for the package to be sended. 
        PackageType pt = storage.getPackageTypeObjectList().get(
                storage.findPackage(availablePacketsListView.getSelectionModel().getSelectedItem()).getItemType()-1);
        
        // Cases with relevant shipmenttypes. 
        switch(shipmentTypeCombo.getValue().substring(0, 1)){
            case "1": 
                // There's a distance limit of 150km for type 1. 
                if(length > 150){
                    System.out.println("Matka liian pitkä ykköstyypin lähetykselle, haluatko valita toisen lähetystyypin?");
                    errorText.add("Matka on liian pitkä ykköstyypin lähetykselle");
                }else{
                    
                    speed = 0.3;
                    color = "RED";
                    if(couriers.addAll(personHandler.getCouriers(4))){
                        okToSend = true;
                        
                        // Also if item is fragile, it can be broken due to the high-speed nature of the shipmenttype. 
                        if (pt.isItemIsFragile()) {
//                            errorText.add("Huom! Pakettinne saattaa rikkoutua lähetyksessä");
                            // Higher stresslevel of TimoteiMan equals to higher risk of shipment being broken.
                            if (Math.random() > ((0.8) / couriers.get(0).getStressLevel())) {
                                shipmentBroke = true;
                                errorText.add("Se paketti meni sitte paskaks. Onks nyt hyvä mieli?!");
                            }
                        }
                        
                    }else{
                        System.out.println("Salo.");
                        errorText.add("Ei kuriireja saatavilla. Kaikki ovat todennäköisesti saaneet potkut");
                    }
                }
                
                break;
                // END of case 1. 
            case "2":
                // There's a size limit for type 2. Ikea bed being the only large item it is explicitly implemented. 
                // The size could also be counted from the size attribute of the packagetype. Maybe to be added in the future if necessery.
                if((storage.findPackage(availablePacketsListView.getSelectionModel().getSelectedItem()).getItemType()) == 7){
                    errorText.add("Paketti on liian iso ja rikkoutuisi lähetyksessä");
                }else{
                    speed = 1;
                    color = "YELLOW";
                    okToSend = true;
                    couriers.addAll(personHandler.getCouriers(2));
                    // There's also a small chance of breaking the shipment in second type. 
                    // The chance is again counted from the stresslevel but now so that a courier that is not stressed wont break the shipment.
                    if (pt.isItemIsFragile() && Math.random() >  ( (1.0)/( ( couriers.get(0).getStressLevel()/5 ) + 0.8 ) )) {
                        shipmentBroke = true;
                        errorText.add("Se sun paketti meni sitte paskaks. Tsooriii!");
                    }
                }
                break;
                // END of case 2
                        
            case "3":
                speed = 4;
                color = "GREEN";
                System.out.println("Nyt viskellään!");
                okToSend = true;
                couriers.addAll(personHandler.getCouriers(1));
                
                if(pt.isItemIsFragile()){     
                    if (Math.random() > ((0.2)/couriers.get(0).getStressLevel())) {
                        shipmentBroke = true;
                        System.out.println("Se meni sitte paskaks. Onks nyt hyvä mieli?!");
                        // Koska kyseessä on stressinpurkupaketti,
                        couriers.get(0).setStressLevel(1);
                    }else{
                        System.out.println("Kuin ihmeen kaupalla paketti säilyi ehjänä. ");
                    }
                    System.out.println("Pakettinne särkyi.. Timotei-miehellä oli huono päivä.");
                }else if (pt.getItemName().contains("Nokia")){
                    errorText.add("Lähetitte sitte nokian fedex-pakettina... ");
                    errorText.add("Kuriiri vähän viskeli, rikkoi sillä tiiliseinän ja sai potkut.");
                    errorText.add("ONKO NYT HYVÄ MIELI MITÄ?!");
                    personHandler.fireTimoteiMan(couriers.get(0));
                }                
                break;
            default:
                // If for some reason shipmenttype cannot be acquired there's a terminal print. 
                System.out.println("ERROR: Lähetystyyppiä ei saatu.");
                break;
        }
        
        // If everything is ok, path is drawn between the smartposts and the shipment is created with according data. 
        if(okToSend){
            webviewComponent.getEngine().executeScript("document.createPath(" + pathArray + ", '" + color + "'," + speed + ")");

            shipHandler.createShipment(availablePacketsListView.getSelectionModel().getSelectedItem(),
                    sendFromListView.getSelectionModel().getSelectedItem(),
                    destinationListView.getSelectionModel().getSelectedItem(),
                    shipmentTypeCombo.getValue(),
                    shipmentBroke,
                    length,
                    couriers);
            
            sendPacketsInfoListView.getItems().clear();
        
            update();
            
        // If there's errors, they're shown. 

        }   
        
        if (!errorText.isEmpty()) {
            showError(errorText);
        }
    }
    
    @FXML
    // These two next methods are for indicating the selection of smartpost by adding its full name to the search bar. 
    private void sendFromListMouseClicked(MouseEvent event) {
        if (!sendFromListView.getSelectionModel().isEmpty()) {
            sendFromSearch.setText(sendFromListView.getSelectionModel().getSelectedItem());
        } else {
            System.out.println("Ei ole valittuna.");
        }
    }

    @FXML
    // See previous.
    private void destinationListMouseClicked(MouseEvent event) {
        if (!destinationListView.getSelectionModel().isEmpty()) {
            destinationSearch.setText(destinationListView.getSelectionModel().getSelectedItem());
        } else {
            System.out.println("Ei ole valittuna.");
        }
    }
    
    @FXML
    // Shows the info of package that user clicked in a listview
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
// ------------- MAP-TAB ENDS ----------------------------

// -----------------------------------------------------   
// FUNCTIONALITIES OF THE STORAGE-TAB
    
    @FXML
    // Add package with given settings to storage.  
    private void acceptAndAddAction(ActionEvent event) throws IOException {
        
        // Checker if user has selected all the necessery settings (package type and owner).
        // Show errorwindow if required settings are not given.
        if(packetTypeListView.getSelectionModel().isEmpty() || foundOwnerList.getSelectionModel().isEmpty()){
            ArrayList<String> errorList = new ArrayList();
            errorList.add("Et valinnut omistajaa tai pakettityyppiä.");
            showError(errorList);
        } else {
            // Add package to storage with packagetypeID and ownerID. 
            storage.addPackage(Integer.parseInt(packetTypeListView.getSelectionModel().getSelectedItem().substring(0, 1)), 
                    foundOwnerList.getSelectionModel().getSelectedItem().substring(0,4));
            // Add notification that the package has added for the requested person. 
            packageAddedNotification.setText("Paketti lisätty henkilölle: " + foundOwnerList.getSelectionModel().getSelectedItem().substring(0,4));
            update();
        }
    }   

    @FXML
    // Remove package from storage. User can select whether he wishes to delete all data that includes the package or not. 
    // If user doesn't want to delete all the related info, package is actually just marked to be "deleted" and is not shown. 
    // This is to avoid consistency problems in the database, as some information when shown shipments is acquired through package. 
    private void removePacketAction(ActionEvent event) {      
        // Checker if user has selected a package to be removed.
        if(!storageListView.getSelectionModel().isEmpty()){
            // Remove package with given ID and type of removal. 
            storage.removePackage(storageListView.getSelectionModel().getSelectedItem().substring(0, 6), 
                                  removeCascadeCheckBox.isSelected());
            update();
        }
    }
    
    
    @FXML
    // This method is invoked by pressing key in the ownerID-textfield.
    // It's purpose is to list all customers who match the given ID. (For example string 'P' matches all customers)
    private void addOwnerByIDAction(KeyEvent event) {
        
        // Acquire list of matching persons.
        ArrayList<Person> pList;
        pList = personHandler.getPersonByID(packetOwnerID.getText());

        if (pList.isEmpty()) {
            ownerFoundLabel.setText("Henkilöä ei löytynyt.");
        } else {
            foundOwnerList.getItems().clear();
            ownerFoundLabel.setText("Henkilö löytyi");
            Iterator itr = pList.iterator();
            Person p = null;
            while (itr.hasNext()) {
                p = (Person) itr.next();
                foundOwnerList.getItems().add(p.getPersonID() + ":\t" + p.getFirstName() + " " + p.getLastName());
            }
        }   
    }

    @FXML
    // Same as last method but for search by name. 
    private void addOwnerByNameAction(KeyEvent event) {
        
        ArrayList<Person> pList;
        pList = personHandler.getPersonByName(packetOwnerName.getText());

        if (pList.isEmpty()) {
            ownerFoundLabel.setText("Henkilöä ei löytynyt.");
        } else {
            foundOwnerList.getItems().clear();
            System.out.println("Henkilö löytyi");
            ownerFoundLabel.setText("Henkilö löytyi");
            Iterator itr = pList.iterator();
            Person p = null;
            while (itr.hasNext()) {
                p = (Person) itr.next();
                foundOwnerList.getItems().add(p.getPersonID() + ":\t" + p.getFirstName() + " " + p.getLastName());
            }
        }
    }

    @FXML
    // Acquires information about requested packagetype and shows it in the relevant listview component.
    // Note the different spelling of package. This doesn't mean anything and was an accident. Maybe to be fixed in the future.
    private void packetTypeListAction(MouseEvent event) {
        
        packetInfoListView.getItems().clear();
        // Gets item from the packagetypelist with given index. 
        // Index is acquired from the selection listview by getting a substring of selection and reducing one. 
        packetInfoListView.getItems().add(
                storage.getPackageTypeList().get(
                    Integer.valueOf(packetTypeListView.getSelectionModel().getSelectedItem().substring(0, 1))-1));
    }

    @FXML
    // Acquires and show information of requested package. (User click)
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
// --------------- STORAGE ENDS ---------------------------------------
    

// --------------------------------------------------------------
// Functionalities of the EVENTS-TAB
    
    @FXML
    // Shows info of the selected shipment in a listview. 
    private void getEventInfo(MouseEvent event) {
        
        if(!eventListView.getSelectionModel().isEmpty()){
            eventInfoListView.getItems().clear();
            
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);
            
            Shipment shipment;
            shipment = shipHandler.getShipmentByID(eventListView.getSelectionModel().getSelectedItem().substring(0, 6));
            
            eventInfoListView.getItems().add("Shipment ID:\t  " + shipment.getShipmentID());
            eventInfoListView.getItems().add("Package ID:\t  " + shipment.getPacketID());
            eventInfoListView.getItems().add("Sent from:\t  " + shipment.getSentFrom());
            eventInfoListView.getItems().add("Destination:\t  " + shipment.getDestination());
            eventInfoListView.getItems().add("Shipment type:  " + shipment.getShipmentType());
            eventInfoListView.getItems().add("Time:\t\t  " + format.format(shipment.getSentTime()));
            ArrayList<String> shipmentCouriers = shipHandler.getCouriersByShipment(shipment.getShipmentID());
            String courier1 = shipmentCouriers.get(0) + ": " 
                        + personHandler.getCourierByID(shipmentCouriers.get(0)).get(0).getFirstName() + " " 
                        + personHandler.getCourierByID(shipmentCouriers.get(0)).get(0).getLastName() ;
            eventInfoListView.getItems().add("Courier:\t\t  " + courier1);
            
            if(shipmentCouriers.size()>1){
                String courier2 = shipmentCouriers.get(1) + ": " 
                        + personHandler.getCourierByID(shipmentCouriers.get(1)).get(0).getFirstName() + " " 
                        + personHandler.getCourierByID(shipmentCouriers.get(1)).get(0).getLastName();
                eventInfoListView.getItems().add("Second courier: " + courier2);
            } 
        }else{
            System.out.println("Ei ole valittuna.");
        }
    }

    @FXML
    // If user has selected a shipment, he can move to make a reclamation about it. 
    //This jumps to reclamations tab and automatically fills the shipmentID field with the selected shipment
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
// Functionalities of the reclamation window.

    @FXML
    private void sendReclamation(ActionEvent event) {
        reclamationHandler.addReclamation(reclamationShipmentSearch.getText(), reclamationDescriptionField.getText());
        reclamationSentLabel.setText("Reklamaatio lähetykselle: " + reclamationShipmentSearch.getText() + " lähetetty.");
    }

    @FXML
    private void checkShipmentAction(ActionEvent event) {

        if (shipHandler.getShipmentByID(reclamationShipmentSearch.getText()) != null) {
            shipmentIDFoundLabel.setText("ID löytyy järjestelmästä.");
        } else {
            shipmentIDFoundLabel.setText("Ei löytynyt, tarkista ID.");
        }
    }
// ------------------ END OF RECLAMATION-TAB -----------------------------
    
// -----------------------------------------------------------------------------
// Functionalities of the STATISTIC-TAB
    @FXML
    // Shows selected type of statistic from a selected date.
    private void showStatisticAction(ActionEvent event) {
        if (statisticTypeComboBox.getSelectionModel().isEmpty() || statisticDatePicker.getValue() == null) {
            System.out.println("Perkele, valitse!");
        } else {
            statisticListView.getItems().clear();
            statisticLongListView.getItems().clear();
            Iterator itr = logOperator.getStatistic(statisticTypeComboBox.getSelectionModel().getSelectedIndex(), Date.valueOf(statisticDatePicker.getValue())).iterator();

            // If user selects to view all shipments, they're shown in a different, larger listview. 
            if (statisticTypeComboBox.getSelectionModel().getSelectedIndex() == 2) {
                while (itr.hasNext()) {
                    statisticLongListView.getItems().add((String) itr.next());
                }
            } else {
                while (itr.hasNext()) {
                    statisticListView.getItems().add((String) itr.next());
                }
            }
        }
    }
// ------------------ END OF STATISTIC-TAB -------------------------------
    


// -------------------------------------------   
// Updating all the relevant listviews etc when something changes. 
// These are all gathered in a single update-method to simplify the structure of the code. 
    private void update() {

        // Update packageList from database
        storage.updatePackageList();

        // Populate package-list with the available packets.
        availablePacketsListView.getItems().clear();
        Iterator itr = storage.getPackageList(dateLimit).iterator();
        while (itr.hasNext()) {
            availablePacketsListView.getItems().add(((Package) itr.next()).getPacketID());
        }

        // Populate storage-list with available packets.
        storageListView.getItems().clear();
        itr = storage.getPackageList(dateLimit).iterator();
        Package p;
        String info;
        while (itr.hasNext()) {
            p = (Package) itr.next();
            info = p.getPacketID();
            info = info + ": " + (storage.getPackageTypeList().get(p.getItemType() - 1)).split(":")[0];
            storageListView.getItems().add(info);
        }
       
        // Updating shipments-list.
        itr = shipHandler.getEventList(dateLimit).iterator();
        eventListView.getItems().clear();
        while (itr.hasNext()) {
            eventListView.getItems().add(((String) itr.next()));
        }

        // Updating reclamations-list 
        itr = reclamationHandler.getReclamationList().iterator();
        reclamationListView.getItems().clear();
        while (itr.hasNext()) {
            reclamationListView.getItems().add((itr.next()).toString());
        }
        
        // Updating courier-list. 
        itr = personHandler.getCourierList().iterator();
        employeeStressListView.getItems().clear();
        TimoteiMan pe;
        while (itr.hasNext()) {
            pe = (TimoteiMan) itr.next();
            employeeStressListView.getItems().add(pe.getPersonID() + " " 
                                                + pe.getFirstName() + " " 
                                                + pe.getLastName() + "\t Stressitaso:" 
                                                + pe.getStressLevel().toString());
        }
        TimoteiMan tm = personHandler.getThrowerOfTheMonth();
        throwerMonthListView.getItems().add("Nimi: " + tm.getFirstName() + " " + tm.getLastName());
        throwerMonthListView.getItems().add("Stressitaso: " + tm.getStressLevel() );
                
    }
 // --------------------- END OF UPDATE ----------------------------------------
    
    

    
// ---------------------------------------------------------   
// Open a new window to show the errors given in an ArrayList. 
    
    public Stage showError(ArrayList<String> errors) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ErrorAlertWindow.fxml"));
        Stage stage = new Stage(StageStyle.DECORATED);

        stage.setScene(new Scene((Pane) loader.load()));

        ErrorAlertWindowController controller = loader.<ErrorAlertWindowController>getController();
        controller.initData(errors);
        stage.show();
        return stage;
    }

    


}

