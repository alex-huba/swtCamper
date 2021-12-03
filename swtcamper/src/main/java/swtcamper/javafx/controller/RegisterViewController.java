package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.userRole;
import swtcamper.backend.services.exceptions.GenericServiceException;

import static swtcamper.backend.entities.userRole.OPERATOR;
import static swtcamper.backend.entities.userRole.USER;

@Component
public class RegisterViewController {

    @Autowired
    private MainViewController mainViewController;

    @Autowired
    private UserController userController;

    @FXML
    public TextField inputLabel;

    @FXML
    public TextField passwordTf;

    @FXML
    public TextField emailTf;

    @FXML
    public TextField phoneTf;

    @FXML
    public TextField nameTf;

    @FXML
    public TextField surnameTf;

    @FXML
    public RadioButton operatorRb;

    @FXML
    public Button cancelBtn;

    @FXML
    public Button registerBtn;

    @FXML
    public void handleCancelBtn(ActionEvent actionEvent) {
        mainViewController.changeView("login");
    }

    public userRole userRoleRb;

    @FXML
    public void handleRegisterBtn(ActionEvent actionEvent) {
        String username = inputLabel.getText();
        String password = passwordTf.getText();
        String email = emailTf.getText();
        String phone = phoneTf.getText();
        String name = nameTf.getText();
        String surname = surnameTf.getText();

        if (username.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill in your username please!");
            alert.show();
        }
        if (password.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill in your password please!");
            alert.show();
        }
        if (password.length() <= 5){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Password is too short!");
            alert.show();
        }
        if (email.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill in your email please!");
            alert.show();
        }
        if (phone.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill in your phone please!");
            alert.show();
        }
        if (name.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill in your name please!");
            alert.show();
        }
        if (surname.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill in your surname please!");
            alert.show();
        }
        if(operatorRb.isSelected()){
            userRoleRb = OPERATOR;
        }else{
            userRoleRb = USER;
        }
        try{
            userController.register(username, name, surname, email, phone, password, userRoleRb);
        } catch (GenericServiceException e){
            e.printStackTrace();
        }



    }
}
