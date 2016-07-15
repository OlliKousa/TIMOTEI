/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Kousa
 */




    
public class ErrorAlertWindowController implements Initializable {


    @FXML
    private ListView<String> errorListView;

    @FXML
    private Button cancelButton;

    ArrayList<String> erroriLista = new ArrayList();
    
    
    void initialize() {
    }

    void initData(ArrayList<String> error) {
        Iterator itr = error.iterator();
        while (itr.hasNext()) {
            errorListView.getItems().add((String) itr.next());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        System.out.println("Shiiit.");
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

}


