/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kousa
 */
public class DogeController implements Initializable {

    
    boolean useDataBase;
    @FXML
    private ImageView dogeImage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void dogeMouseClicked(MouseEvent event) throws IOException {
        // suljetaan nykyinen ikkuna. 
        ((Stage) dogeImage.getScene().getWindow()).close();

        // Avataan pääohjelma.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));

        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        FXMLDocumentController controller = loader.<FXMLDocumentController>getController();
        // Tässä siirretään asetukset
        controller.initData(useDataBase);
        stage.setResizable(false);
        stage.show();
    }
    
    public void initData(boolean useDB){
        useDataBase = useDB;
    }
}
