package com.hospitifi.fxml;

import com.hospitifi.model.Room;
import com.hospitifi.model.User;
import com.hospitifi.service.UserService;
import com.hospitifi.service.impl.UserServiceImpl;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

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
	Button newRoom;
	@FXML
	Button deleteRoom;
	@FXML
	Button editRoom;
	@FXML
	ChoiceBox<String> choiceBox = new ChoiceBox<String>();
	@FXML
	SplitPane userPane;
	@FXML
	SplitPane roomPane;
	@FXML
	TableView<User> userTable;
	@FXML
	TableView<Room> roomTable;
	@FXML
	TableColumn<User, Long> userIdCol;
	@FXML
	TableColumn<User, String> userLoginCol;
	@FXML
	TableColumn<User, String> userPasswordCol;
	@FXML
	TableColumn<User, String> userRoleCol;
	@FXML
	TableColumn<Room, Long> roomIdCol;
	@FXML
	TableColumn<Room, String> roomNumberCol;
	@FXML
	TableColumn<Room, Integer> roomFloorCol;
	@FXML
	TableColumn<Room, Integer> roomBedsCol;
	@FXML
	TableColumn<Room, Integer> roomBedTypeCol;
	@FXML
	TableColumn<Room, Boolean> roomSafeCol;
	@FXML
	TableColumn<Room, Boolean> roomBathCol;
	@FXML
	TableColumn<Room, Integer> roomRateCategoryCol;
	@FXML
	TextField idAdminEdit;
	@FXML
	TextField loginAdminEdit;
	@FXML
	TextField passwordAdminEdit;
	@FXML
	ChoiceBox<String> roleAdminEdit = new ChoiceBox<>();
	
	private UserService userService = UserServiceImpl.getInstance();;
	
	private ObservableList<User> data;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// "invalid login/password" message will be invisible by default
		if (invalidCombination != null) {
			invalidCombination.setVisible(false);
		}
	}
//
	public void configureAdmin() {  //initial setup for admin menu.
		//if (userPane != null) userPane.setVisible(false);
		//if (roomPane != null) roomPane.setVisible(false);
		choiceBox.setItems(FXCollections.observableArrayList("Users","Rooms"));
		roleAdminEdit.setItems(FXCollections.observableArrayList("admin","manager","receptionist"));
		choiceBox.getSelectionModel().selectedItemProperty().addListener( 
				(ObservableValue<? extends String> observable, String oldValue, String newValue) -> 
				adminChoiceBoxChoice(newValue) );
		
		userTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		roomTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
		userLoginCol.setCellValueFactory(new PropertyValueFactory<>("userLogin"));
		userPasswordCol.setCellValueFactory(new PropertyValueFactory<>("userPassword"));
		userRoleCol.setCellValueFactory(new PropertyValueFactory<>("userRole"));

		data = FXCollections.observableArrayList();
		data.addAll(userService.getAll());
		userTable.setItems(data);
	}
	private void configureManager() {
		//To be written
	}
	private void configureReceptionist() {
		//To be written
	}
	
	public void newUserClicked(ActionEvent event) {
		User user = new User(Long.parseLong(idAdminEdit.getText()), loginAdminEdit.getText(), 
        		passwordAdminEdit.getText(), null, roleAdminEdit.getValue());
        userService.save(user);
        data.add(user);
    }
	public void deleteUserClicked(ActionEvent event) {
        userService.delete(userTable.getSelectionModel().getSelectedItem().getId());
        data.remove(userTable.getSelectionModel().getSelectedItem());
    }
	private void adminChoiceBoxChoice (String s) {
		if (s.equals("Users")) {
			userPane.setVisible(true);
			roomPane.setVisible(false);
		}
		else {
			roomPane.setVisible(true);
			userPane.setVisible(false);
		}
	}
	
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
				root = load(getClass().getResource("AdminMenu.fxml"), null, this);
				configureAdmin();
			}
			else if (role.equalsIgnoreCase("manager")) {
				root = load(getClass().getResource("ManagerMenu.fxml"), null, this);
				configureManager();
			}
			else {  
				root = load(getClass().getResource("ReceptionistMenu.fxml"), null, this);
				configureReceptionist();
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
		Parent root = load(getClass().getResource("Login.fxml"), null, this);    
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		userService.logOut();
	}
}
