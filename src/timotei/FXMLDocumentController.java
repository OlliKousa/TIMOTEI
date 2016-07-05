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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author Kousa
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TextField addToMapSearch;
    @FXML
    private ComboBox<?> addToMapCombo;
    @FXML
    private Button addToMapButton;
    @FXML
    private Button clearMapButton;
    @FXML
    private TextField sendFromSearch;
    @FXML
    private ComboBox<?> sendFromCombo;
    @FXML
    private TextField destinationSearch;
    @FXML
    private ComboBox<?> destinationCombo;
    @FXML
    private ListView<?> availablePacketsListView;
    @FXML
    private ListView<?> sendPacketsListView;
    @FXML
    private Button addToSendButton;
    @FXML
    private Button removeFromSendButton;
    @FXML
    private Button sendShipmentButton;
    @FXML
    private ComboBox<?> packetTypeCombo;
    @FXML
    private TextField packetOwnerType;
    @FXML
    private Button acceptAndAddButton;
    @FXML
    private Button removePacketButton;
    @FXML
    private Button editPacketButton;
    @FXML
    private ListView<?> packetInfoListView;
    @FXML
    private ListView<?> eventListView;
    @FXML
    private ListView<?> eventInfoListView;
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
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addToMapAction(ActionEvent event) throws SQLException {
        
        XMLReader xml = new XMLReader();
        
    }

    @FXML
    private void clearMapAction(ActionEvent event) {
    }

    @FXML
    private void addToSendAction(ActionEvent event) {
    }

    @FXML
    private void removeFromSendAction(ActionEvent event) {
    }

    @FXML
    private void sendShipmentAction(ActionEvent event) {
    }

    @FXML
    private void acceptAndAddAction(ActionEvent event) {
    }

    @FXML
    private void removePacketAction(ActionEvent event) {
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
    
}
