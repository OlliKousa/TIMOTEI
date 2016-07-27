/*
 * Class NAME: ErrorAlertWindowController
 * Created BY: Olli Kousa
 * Creation DATE: 15.7.2016
 * Last MODIFIED: 21.7.2016
 * Purpose of the class is to hold functionalities of window that notifies user of possible errors.
 * Class is opened and controlled by method "showError()" which intializes the window with the relevant info to be shown to user. 
 * User can close the window and continue using the program after viewing the info. 
 */

package timotei;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

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

    // initData adds info or errormessages to the ListView-component. 
    void initData(ArrayList<String> error) {
        Iterator itr = error.iterator();
        while (itr.hasNext()) {
            errorListView.getItems().add((String) itr.next());
        }
    }

    @Override
    // initialize doesn't do anything. Data is easier to init in different method.
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    // cancelAction closes the window and lets the user to continue using the program.
    private void cancelAction(ActionEvent event) {
        System.out.println("Shiiit.");
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
}


