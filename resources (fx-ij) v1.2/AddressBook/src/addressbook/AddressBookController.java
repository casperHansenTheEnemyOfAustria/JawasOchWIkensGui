
package addressbook;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import se.chalmers.cse.dat215.lab1.Presenter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class AddressBookController implements Initializable {
    
    @FXML private MenuBar menuBar;
    @FXML private Button newButton;
    @FXML private Button deleteButton;
    @FXML private ListView contactListView;

    @FXML private TextField fName;
    @FXML private TextField lName;
    @FXML private TextField phone;
    @FXML private TextField email;
    @FXML private TextField adress;
    @FXML private TextField postCode;
    @FXML private TextField city;

    private Presenter presenter;






    @Override
    public void initialize(URL url, ResourceBundle rb) {
        presenter = new Presenter(contactListView, fName, lName, phone, email, adress, postCode, city);
        presenter.init();
        contactListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                presenter.contactsListChanged();
            }

        });
        fName.focusedProperty().addListener(new TextFieldListener(fName));
        lName.focusedProperty().addListener(new TextFieldListener(lName));
        phone.focusedProperty().addListener(new TextFieldListener(phone));
        email.focusedProperty().addListener(new TextFieldListener(email));
        adress.focusedProperty().addListener(new TextFieldListener(adress));
        postCode.focusedProperty().addListener(new TextFieldListener(postCode));
        city.focusedProperty().addListener(new TextFieldListener(city));


    }
    
    @FXML 
    protected void openAboutActionPerformed(ActionEvent event) throws IOException{
    
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("addressbook/resources/AddressBook");
        Parent root = FXMLLoader.load(getClass().getResource("address_book_about.fxml"), bundle);
        Stage aboutStage = new Stage();
        aboutStage.setScene(new Scene(root));
        aboutStage.setTitle(bundle.getString("about.title.text"));
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.setResizable(false);
        aboutStage.showAndWait();
    }
    
    @FXML 
    protected void closeApplicationActionPerformed(ActionEvent event) throws IOException{
        
        Stage addressBookStage = (Stage) menuBar.getScene().getWindow();
        addressBookStage.hide();
    }

    @FXML
    protected void newContactActionPerformed (ActionEvent event){
        presenter.newContact();
    }

    @FXML
    protected void deleteContactActionPerformed(ActionEvent event){
        presenter.removeCurrentContact();
    }


    @FXML
    protected void textFieldActionPerformed(ActionEvent event){
        presenter.textFieldActionPerformed(event);
    }
    private class TextFieldListener implements ChangeListener<Boolean>{

        private TextField textField;

        public TextFieldListener(TextField textField){
            this.textField = textField;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){
                presenter.textFieldFocusGained(textField);

            }
            else{
                presenter.textFieldFocusLost(textField);
            }
        }
    }
}


