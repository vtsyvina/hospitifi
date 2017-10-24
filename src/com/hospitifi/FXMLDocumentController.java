package com.hospitifi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hospitifi.service.UserService;
import com.hospitifi.service.impl.UserServiceImpl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {

	@FXML
	Button loginButton;
	@FXML
	TextField login;
	@FXML
	PasswordField password;
	@FXML
	Label invalidCombination;
	@FXML
	Button logoutButton;
	@FXML
	Button newUser;
	@FXML
	Button deleteUser;
	@FXML
	Button editUser;
	@FXML
	ListView<String> list;
	@FXML
	TextField synopsis;

	private UserService userService = UserServiceImpl.getInstance();;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (invalidCombination != null) {
			invalidCombination.setVisible(false);
        }
	}

	@FXML
	private void handleLoginButton(ActionEvent event) throws IOException{
		/*
		 * If login/pass are valid, change scene to menu.
		 * If invalid, set invalidCombination.setVisible(true);
		 */
		String log = login.getText();
		String pass = password.getText();
		
		if ((log != null && pass != null ) && userService.authenticateUser(log, pass)) {
			Stage stage = (Stage) loginButton.getScene().getWindow();  //get reference to button's stage   
			Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));    //load Menu.fxml
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
		else {
			invalidCombination.setVisible(true);
		}
	}
	
	@FXML
	private void handleLogoutButton(ActionEvent event) throws IOException{
		Stage stage = (Stage) logoutButton.getScene().getWindow();  
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));    
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}