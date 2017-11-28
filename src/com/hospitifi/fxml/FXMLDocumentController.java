package com.hospitifi.fxml;

import com.hospitifi.service.EmployeeService;
import com.hospitifi.service.OccupationService;
import com.hospitifi.service.RoomService;
import com.hospitifi.service.UserService;
import com.hospitifi.util.ServiceFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class FXMLDocumentController implements Initializable {

	//FXMLDocumentController will contain login scene + some general methods.
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

	private UserService userService = ServiceFactory.getUserService();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//any code here will be called every time scene changes
		//(after all @FXML annotated members have been injected)
		configureLogin();
	}

	

	public void configureLogin(){

		// "invalid login/password" message will be invisible by default
		if (invalidCombination != null) {
			invalidCombination.setVisible(false);
		}

		// attempt login when enter key is pressed and while login TextField has focus
		login.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent ke)
			{
				if (ke.getCode().equals(KeyCode.ENTER))
				{
					try {
						handleLoginButton(new ActionEvent());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		// attempt login when enter key is pressed and while PasswordField has focus
		password.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent ke)
			{
				if (ke.getCode().equals(KeyCode.ENTER))
				{
					try {
						handleLoginButton(new ActionEvent());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	//this load method was needed when using one controller for multiple scenes,
	//not being used now that controllers have been separated
	public static Parent load(final URL url, final ResourceBundle resBundle, final Object controller)
			throws IOException {
		Objects.requireNonNull(url);
		Objects.requireNonNull(controller);

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(url);
		loader.setResources(resBundle);

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
		 * If login/pass are valid, change scene to user's corresponding menu.
		 * If invalid, display the error message.
		 */
		
		String log = login.getText();
		String pass = password.getText();

		if ((log != null && pass != null ) && userService.authenticateUser(log, pass)) {

			Stage stage = (Stage) loginButton.getScene().getWindow();  //get reference to button's stage  
			String role = userService.getCurrentUserRole();
			Parent root; 
			if (role.equalsIgnoreCase("admin")) {
				root = FXMLLoader.load(new File("src/com/hospitifi/fxml/AdminMenu.fxml").toURI().toURL());
			}
			else if (role.equalsIgnoreCase("manager")) {
				root = FXMLLoader.load(new File("src/com/hospitifi/fxml/ManagerMenu.fxml").toURI().toURL());
			}
			else {  
				root = FXMLLoader.load(new File("src/com/hospitifi/fxml/ReceptionistMenu.fxml").toURI().toURL());
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
		Parent root = FXMLLoader.load(new File("src/com/hospitifi/fxml/Login.fxml").toURI().toURL());
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		userService.logOut();
	}
	
}
