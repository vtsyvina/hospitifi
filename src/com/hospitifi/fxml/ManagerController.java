package com.hospitifi.fxml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hospitifi.service.RoomService;
import com.hospitifi.service.UserService;
import com.hospitifi.util.ServiceFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ManagerController implements Initializable{
	
	@FXML
	Button logoutButton;
	
	private UserService userService = ServiceFactory.getUserService();
	
	private RoomService roomService = ServiceFactory.getRoomService();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configureManager();
	}
	
	private void configureManager() {
		//To be written
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
