package com.hospitifi;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
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

	private UserService userService = UserServiceImpl.getInstance();;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (invalidCombination != null) {
			invalidCombination.setVisible(false);
        }
	}

	public static Parent load(final URL url, final ResourceBundle resBundle, final Object controller)
	        throws IOException {
		Objects.requireNonNull(url);
	    Objects.requireNonNull(controller);
	    
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(url);
	    //loader.setResources(resBundle);

	  //controller is synchronized between fxml files with setControllerFactory
	    loader.setControllerFactory(controllerClass -> {
	        if (controllerClass != null && !controllerClass.isInstance(controller)) {
	            throw new IllegalArgumentException("Invalid controller instance, expecting instance of class '" +
	                    controllerClass.getName() + "'");
	        }
	        return controller;
	    });
	    return (Parent) loader.load();
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
			String role = userService.getCurrentUserRole();
			Parent root;
			if (role.equalsIgnoreCase("admin")) {
				root = load(getClass().getResource("fxml/AdminMenu.fxml"), null, this);
			}
			else if (role.equalsIgnoreCase("manager")) {
				root = load(getClass().getResource("fxml/ManagerMenu.fxml"), null, this);
			}
			else {  //if receptionist
				root = load(getClass().getResource("fxml/ReceptionistMenu.fxml"), null, this);
			}
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
		Parent root = load(getClass().getResource("fxml/Login.fxml"), null, this);    
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		userService.logOut();
	}
}
