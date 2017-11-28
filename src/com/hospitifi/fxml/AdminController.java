package com.hospitifi.fxml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hospitifi.model.BedType;
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
import javafx.scene.control.CheckBox;
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
import javafx.scene.layout.AnchorPane;
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
	TableColumn<User, String> userLoginCol;
	@FXML
	TableColumn<User, String> userPasswordCol;
	@FXML
	TableColumn<User, String> userRoleCol;
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

		bedsChoiceBox.setItems(FXCollections.observableArrayList(1, 2));
		
		bedTypeChoiceBox.setItems(FXCollections.observableArrayList(BedType.values()));
		
		showUserDetails(null); //clear user details
		adminNewEditUserGridPane.setVisible(false); //user form hidden
		adminUserOkButton.setVisible(false);
		adminUserCancelButton.setVisible(false);
		
		roomsBottomPane.setVisible(false);
		
		userTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		userTable.getSelectionModel().selectedItemProperty().addListener((observable) -> hideAdminNewEditUser());
		userTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showUserDetails(newValue));
		
		roomTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		roomTable.getSelectionModel().selectedItemProperty().addListener((observable) -> hideRoomsBottomPane());
		
		userLoginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
		userPasswordCol.setCellValueFactory(new PropertyValueFactory<>("passwordHash"));
		userRoleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

		roomNumberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
		roomFloorCol.setCellValueFactory(new PropertyValueFactory<>("floor"));
		roomBedsCol.setCellValueFactory(new PropertyValueFactory<>("beds"));
		roomBedTypeCol.setCellValueFactory(new PropertyValueFactory<>("bedType"));
		roomSafeCol.setCellValueFactory(new PropertyValueFactory<>("safe"));
		roomBathCol.setCellValueFactory(new PropertyValueFactory<>("bath"));
		roomRateCategoryCol.setCellValueFactory(new PropertyValueFactory<>("rateCategory"));
		
		buildUserData();
		buildRoomData();

	}
	
	private void buildUserData() {
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
			adminDetailLogin.setText(":    None currently selected");
			adminDetailPassword.setText(":    None currently selected");
			adminDetailRole.setText(":    None currently selected");
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
			
			userService.delete(user.getId());
			
			buildUserData();
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
	private void adminUserCancelButtonPressed(ActionEvent event) {
		clearAdminUsersTextFields();
		hideAdminNewEditUser();
	}

	@FXML
	private void adminNewEditUserOkButtonPressed(ActionEvent event) {
		
		int index = userTable.getSelectionModel().getSelectedIndex();
		
		if (index < 0 ) {        //no cell selected, which means "New" is pressed
			
			String log = loginAdminEdit.getText();
			String pass = passwordAdminEdit.getText();
			
			if (log == null || pass == null || roleAdminEditChoiceBox.getValue() == null || log.equals("") || pass.equals("")) {
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
			
			buildUserData();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(null);
			alert.setHeaderText("User was successfully added");
			alert.setContentText("Added user: \n" + userData.get(userData.size() - 1));
			alert.showAndWait();
			
			userTable.getSelectionModel().clearSelection();
			
			adminNewEditUserGridPane.setVisible(false);
			adminUserOkButton.setVisible(false);
			adminUserCancelButton.setVisible(false);
		}
		
		else {//"Edit" button was pressed
			
			String log = loginAdminEdit.getText();
			String pass = passwordAdminEdit.getText();
			
			if (log == null || pass == null || roleAdminEditChoiceBox.getValue() == null || log.equals("") || pass.equals("")) {
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(adminDetailLogin.getScene().getWindow());
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Could not edit the user because one or more entries are blank.");
				alert.showAndWait();
				return;
			}
			
			long currentId = userTable.getSelectionModel().getSelectedItem().getId();
			User user = new User(currentId, loginAdminEdit.getText(), passwordAdminEdit.getText(), 
					null, roleAdminEditChoiceBox.getSelectionModel().getSelectedItem());
			
			userService.update(user);
			
			buildUserData();

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
	
	@FXML
	TableView<Room> roomTable;
	@FXML
	TableColumn<Room, String> roomNumberCol;
	@FXML
	TableColumn<Room, Integer> roomFloorCol;
	@FXML
	TableColumn<Room, Integer> roomBedsCol;
	@FXML
	TableColumn<Room, BedType> roomBedTypeCol;
	@FXML
	TableColumn<Room, String> roomSafeCol;
	@FXML
	TableColumn<Room, String> roomBathCol;
	@FXML
	TableColumn<Room, Integer> roomRateCategoryCol;
	@FXML
	Button adminRoomOkButton;
	@FXML
	Button adminRoomCancelButton;
	@FXML
	ChoiceBox<Integer> bedsChoiceBox = new ChoiceBox<>();
	@FXML
	ChoiceBox<BedType> bedTypeChoiceBox = new ChoiceBox<>();
	@FXML
	TextField roomNumberTextField;
	@FXML
	TextField floorNumberTextField;
	@FXML
	TextField rateCategoryTextField;
	@FXML
	CheckBox safeCheckBox;
	@FXML
	CheckBox bathCheckBox;
	@FXML
	Label editingCreatingLabel;
	@FXML
	AnchorPane roomsBottomPane;
	
	private RoomService roomService = ServiceFactory.getRoomService();

	private ObservableList<Room> roomData;
	
	private void buildRoomData() {
		roomData = FXCollections.observableArrayList();
		
		roomData.addAll(roomService.getAll());         //build data into list from database
		
		roomTable.setItems(roomData);               //use list in table
	}
	
	@FXML
	private void roomOkPressed(ActionEvent event) {
		
	}
	
	@FXML
	private void roomCancelPressed(ActionEvent event) {
		
	}
	
	@FXML
	private void newRoomClicked(ActionEvent event) {
		
	}
	
	@FXML
	private void editRoomClicked(ActionEvent event) {
		
	}
	
	@FXML
	private void deleteRoomClicked(ActionEvent event) {
		
	}
	
	@FXML
	private void hideRoomsBottomPane(){
		roomsBottomPane.setVisible(false);
	}
	
	@FXML
	private void clearBottomPane(){
		roomNumberTextField.clear();
		floorNumberTextField.clear();
		rateCategoryTextField.clear();
		bedsChoiceBox.setValue(null);
		bedTypeChoiceBox.setValue(null);
	}
}
