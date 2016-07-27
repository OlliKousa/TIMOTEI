/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Kousa
 */
public class WelcomeScreenController implements Initializable {

    @FXML
    private TextField userNameField;
    @FXML
    private TextField passwordField;
    @FXML
    private CheckBox downloadXMLBox;
    @FXML
    private CheckBox downloadDBBox;
    @FXML
    private CheckBox dogeIsWowBox;

    boolean authenticationOK = false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    

    @FXML
    private void signInAction(ActionEvent event) throws IOException {
        
        String userName = userNameField.getText();
        String passWord = passwordField.getText();
        if(userName.isEmpty() || passWord.isEmpty()){
            System.out.println("Kirjotas nyt jooko eka.");
        }else{
            if(userName.equals("Jumala") && passWord.equals("Jeesus123")){
                authenticationOK = true;
                System.out.println("gongratz");
            }
            else{
                System.out.println("Väärin meni.");
                System.out.println("Käyttäjänimi:" + userName);
                System.out.println("Salasana:" + passWord);
            }
        }
        
        if(authenticationOK){
            
            if(dogeIsWowBox.isSelected()){
                // suljetaan nykyinen ikkuna. 
                ((Stage) userNameField.getScene().getWindow()).close();

                // Avataan doge.
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Doge.fxml"));

                Stage stage = new Stage();
                stage.setScene(new Scene((Pane) loader.load()));
                DogeController controller = loader.<DogeController>getController();
                // Tässä siirretään asetukset
                controller.initData(downloadDBBox.isSelected());
                stage.setResizable(false);
                stage.show();
                
                
                
            }else{
                // suljetaan nykyinen ikkuna. 
                ((Stage) userNameField.getScene().getWindow()).close();

                // Avataan pääohjelma.
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));

                Stage stage = new Stage();
                stage.setScene(new Scene((Pane) loader.load()));
                FXMLDocumentController controller = loader.<FXMLDocumentController>getController();
                // Tässä siirretään asetukset
                controller.initData(downloadDBBox.isSelected());
                stage.setResizable(false);
                stage.show();
            }
            

            
        }
        
    }
    
    
    
}
