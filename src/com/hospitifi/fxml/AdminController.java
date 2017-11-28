package com.hospitifi.fxml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hospitifi.model.Room;
import com.hospitifi.model.User;
import com.hospitifi.service.RoomService;
import com.hospitifi.service.UserService;
import com.hospitifi.util.ServiceFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AdminController implements Initializable{
	
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
	SplitPane userPane;
	@FXML
	SplitPane roomPane;
	@FXML
	TableView<User> userTable;
	@FXML
	TableView<Room> roomTable;
	@FXML
	TableColumn<User, String> userLoginCol;
	@FXML
	TableColumn<User, String> userPasswordCol;
	@FXML
	TableColumn<User, String> userRoleCol;
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
	TextField loginAdminEdit;
	@FXML
	TextField passwordAdminEdit;
	@FXML
	ChoiceBox<String> roleAdminEditChoiceBox = new ChoiceBox<>();
	@FXML
	RadioButton adminUsersRadioButton;
	@FXML
	RadioButton adminRoomsRadioButton;
	@FXML
	Label adminDetailLogin;
	@FXML
	Label adminDetailPassword;
	@FXML
	Label adminDetailRole;
	@FXML
	GridPane adminNewEditUserGridPane;
	@FXML
	Button adminUserOkButton;
	@FXML
	Button adminUserCancelButton;

	private UserService userService = ServiceFactory.getUserService();
	
	private RoomService roomService = ServiceFactory.getRoomService();
	
	private ObservableList<User> userData;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configureAdmin();
	}
	
	private void configureAdmin() {  //initial setup for admin menu

		//ToggleGroup only allows one RadioButton to be selected at a time
		ToggleGroup tg = new ToggleGroup();
		adminUsersRadioButton.setToggleGroup(tg);
		adminRoomsRadioButton.setToggleGroup(tg);
		adminUsersRadioButton.setSelected(true); //Users RadioButton selected by default
		userPane.toFront();
		
		adminUsersRadioButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				userPane.toFront();
			}
		});
		
		adminRoomsRadioButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				roomPane.toFront();
			}
		});

		roleAdminEditChoiceBox.setItems(FXCollections.observableArrayList("admin","manager","receptionist"));

		showUserDetails(null); //clear user details
		adminNewEditUserGridPane.setVisible(false); //user form hidden
		adminUserOkButton.setVisible(false);
		adminUserCancelButton.setVisible(false);

		userTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		userTable.getSelectionModel().selectedItemProperty().addListener((observable) -> hideAdminNewEditUser());
		userTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showUserDetails(newValue));
		
		userLoginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
		userPasswordCol.setCellValueFactory(new PropertyValueFactory<>("passwordHash"));
		userRoleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

		userData = FXCollections.observableArrayList();
		
		userData.addAll(userService.getAll());         //build data into list from database
		
		userTable.setItems(userData);               //use list in table
	}

	private void hideAdminNewEditUser(){
		adminNewEditUserGridPane.setVisible(false); //user form hidden
		adminUserOkButton.setVisible(false);
		adminUserCancelButton.setVisible(false);
	}

	private void clearAdminUsersTextFields(){
		loginAdminEdit.clear();
		passwordAdminEdit.clear();
	}

	private void showUserDetails (User user){
		if (user != null) {
			// Fill the labels with info from the user object.
			adminDetailLogin.setText(":    " + user.getLogin());
			adminDetailPassword.setText(":    " + user.getPasswordHash());
			adminDetailRole.setText(":    " + user.getRole());

		} else {
			// Person is null, remove all the text.
			adminDetailLogin.setText("");
			adminDetailPassword.setText("");
			adminDetailRole.setText("");
		}
	}

	@FXML
	private void newUserClicked(ActionEvent event) {
		userTable.getSelectionModel().clearSelection();

		clearAdminUsersTextFields();

		adminNewEditUserGridPane.setVisible(true);
		adminUserOkButton.setVisible(true);
		adminUserCancelButton.setVisible(true);
	}

	@FXML
	private void deleteUserClicked(ActionEvent event) {
		int selectedIndex = userTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {  //something is selected
			
			User user = userTable.getSelectionModel().getSelectedItem();
			
			userService.delete(userTable.getSelectionModel().getSelectedItem().getId());
			
			userTable.getItems().remove(selectedIndex);
			//userData.remove(userTable.getSelectionModel().getSelectedItem());
			userTable.getSelectionModel().clearSelection();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("User deletion");
			alert.setHeaderText("User was successfully removed.");
			alert.setContentText("Removed user: \n" + user);
			alert.showAndWait();
			
		} else {  // nothing selected
			clearAdminUsersTextFields();
			hideAdminNewEditUser();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setGraphic(null);
			alert.initOwner(adminDetailLogin.getScene().getWindow());
			alert.setTitle("Delete - No Selection");
			alert.setHeaderText("No User Selected");
			alert.setContentText("Please select a user in the table to delete.");
			alert.showAndWait();
		}
	}

	@FXML
	private void editUserClicked(ActionEvent event) {
		int selectedIndex = userTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {  //something is selected

			loginAdminEdit.setText(userTable.getSelectionModel().getSelectedItem().getLogin());
			passwordAdminEdit.setText(userTable.getSelectionModel().getSelectedItem().getPassword());
			roleAdminEditChoiceBox.setValue(userTable.getSelectionModel().getSelectedItem().getRole());

			adminNewEditUserGridPane.setVisible(true);
			adminUserOkButton.setVisible(true);
			adminUserCancelButton.setVisible(true);

		} else {  // nothing selected
			clearAdminUsersTextFields();
			hideAdminNewEditUser();
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(adminDetailLogin.getScene().getWindow());
			alert.setTitle("Edit - No Selection");
			alert.setHeaderText("No User Selected");
			alert.setContentText("Please select a user in the table to edit.");
			alert.showAndWait();
		}
	}

	@FXML
	private void adminNewEditUserCancelButtonPressed(ActionEvent event) {
		clearAdminUsersTextFields();
		hideAdminNewEditUser();
	}

	@FXML
	private void adminNewEditUserOkButtonPressed(ActionEvent event) {
		int index = userTable.getSelectionModel().getSelectedIndex();
		if (index < 0 ) {        //no cell selected, which means "New" is pressed
			String log = loginAdminEdit.getText();
			String pass = passwordAdminEdit.getText();
			if (log == null || pass == null || roleAdminEditChoiceBox.getValue() == null 
					|| log == "" || pass == "") {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(adminDetailLogin.getScene().getWindow());
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Could not create because one or more entries are blank.");
				alert.showAndWait();
				return;
			}
			for (User user : userData) {
				if(user.getLogin().equals(loginAdminEdit.getText())) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(adminDetailLogin.getScene().getWindow());
					alert.setTitle("Error");
					alert.setHeaderText("Could not create because this login name already exists.");
					alert.setContentText("Please enter a unique login name.");
					alert.showAndWait();
					return;
				}
			}
			
			User user = new User(1234, loginAdminEdit.getText(), passwordAdminEdit.getText(), 
					null, roleAdminEditChoiceBox.getSelectionModel().getSelectedItem());
			
			userService.save(user);
			
			userTable.getItems().add(user);
			userTable.getSelectionModel().clearSelection();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(null);
			alert.setHeaderText("User was successfully added");
			alert.setContentText("Added user: \n" + user);
			alert.showAndWait();
			
			adminNewEditUserGridPane.setVisible(false);
			adminUserOkButton.setVisible(false);
			adminUserCancelButton.setVisible(false);
		}
		
		else {//"Edit" button was pressed
			String log = loginAdminEdit.getText();
			String pass = passwordAdminEdit.getText();
			if (log == null || pass == null || roleAdminEditChoiceBox.getValue() == null 
					|| log == "" || pass == "") {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(adminDetailLogin.getScene().getWindow());
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Could not edit the user because one or more entries are blank.");
				alert.showAndWait();
				return;
			}
			
			User user = userTable.getSelectionModel().getSelectedItem();
			
			userService.update(user);

			userTable.getItems().add(index, user);
			userTable.getItems().remove(index + 1);
			userTable.getSelectionModel().clearSelection();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("User update");
			alert.setHeaderText("User was successfully edited");
			alert.setContentText("Edited user: \n" + user);
			alert.showAndWait();
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
