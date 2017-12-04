package com.hospitifi.fxml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hospitifi.model.Employee;
import com.hospitifi.model.Occupation;
import com.hospitifi.model.User;
import com.hospitifi.model.WorkingTimeUnit;
import com.hospitifi.report.ReportComposer;
import com.hospitifi.service.EmployeeService;
import com.hospitifi.service.OccupationService;
import com.hospitifi.service.RoomService;
import com.hospitifi.service.UserService;
import com.hospitifi.util.ServiceFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ManagerController implements Initializable{
	
	@FXML
	private Button logoutButton;
	
	@FXML
	private Button adaptingButton;
	
	@FXML
	private Pane menuPane;
	
	@FXML
	private Button manageEmployeesButton;
	
	@FXML
	private Button createReportButton;
	
	@FXML
	private Pane manageEmployeesPane;
	
	@FXML
	private Pane createReportPane;
	
	@FXML
	private GridPane addEditEmployeePane;
	
	@FXML
	private Pane workHoursPane;
	
	@FXML
	private Label welcomeMessage;

	@FXML
	private TableView<Employee> employeesTable;
	
	@FXML
	private TableColumn<Employee, String> nameCol;
	
	@FXML
	private TableColumn<Employee, String> positionCol;
	
	@FXML
	private ListView<WorkingTimeUnit> hoursList;
	
	@FXML
	private TextField nameField;
	
	@FXML
	private TextField positionField;
	
	@FXML
	private Button newEmployeeButton;
	
	@FXML
	private Button editEmployeeButton;
	
	@FXML
	private Button removeEmployeeButton;
	
	@FXML
	private Button okEmployeeButton;
	
	@FXML
	private Button clearEmployeeButton;
	
	private UserService userService = ServiceFactory.getUserService();
	
	private EmployeeService employeeService = ServiceFactory.getEmployeeService();
	
	private OccupationService occupationService = ServiceFactory.getOccupationService();
	
	private ObservableList<Employee> employeeData;
	
	private ReportComposer reportComposer;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configureManager();
	}
	
	private void configureManager() {
		reportComposer = ReportComposer.getInstance();
		
		menuPane.setVisible(true);
		manageEmployeesPane.setVisible(false);
		createReportPane.setVisible(false);
		adaptingButton.setVisible(false);
		addEditEmployeePane.setVisible(false);
		workHoursPane.setVisible(false);
		welcomeMessage.setText("Welcome, " + userService.getCurrentUser().getLogin() + ".");
		
		
		
		employeesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		employeesTable.getSelectionModel().selectedItemProperty().addListener((observable) -> showHoursPane());
		employeesTable.getSelectionModel().selectedItemProperty().addListener((observable) -> hideGridPane());
		
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
		
		buildEmployeeData();
	}
	
	private void buildEmployeeData() {
		employeeData = FXCollections.observableArrayList();
		
		employeeData.addAll(employeeService.getAll());         
		
		employeesTable.setItems(employeeData);               
	}

	private void showHoursPane() {
		workHoursPane.setVisible(true);
	}
	
	private void showGridPane() {
		addEditEmployeePane.setVisible(true);
	}
	
	private void hideGridPane() {
		addEditEmployeePane.setVisible(false);
	}
	
	@FXML
	private void newEmployeeClicked(ActionEvent event) {
		addEditEmployeePane.setVisible(true);
		workHoursPane.setVisible(false);
		employeesTable.getSelectionModel().clearSelection();
	}
	
	@FXML
	private void editEmployeeClicked(ActionEvent event) {
		
		int selectedIndex = employeesTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {  //something is selected
			addEditEmployeePane.setVisible(true);
			nameField.setText(employeesTable.getSelectionModel().getSelectedItem().getName());
			positionField.setText(employeesTable.getSelectionModel().getSelectedItem().getPosition());
		}
		else {
			addEditEmployeePane.setVisible(false);
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(welcomeMessage.getScene().getWindow());
			alert.setTitle("Edit - No Selection");
			alert.setHeaderText("No Employee Selected");
			alert.setContentText("Please select an employee to edit.");
			alert.showAndWait();
		}
	}
	@FXML
	private void removeEmployeeClicked(ActionEvent event) {
		
		int selectedIndex = employeesTable.getSelectionModel().getSelectedIndex();
		
		if (selectedIndex >= 0) {
			
			Employee employee = employeesTable.getSelectionModel().getSelectedItem();
			
			employeeService.delete(employee.getId());
			
			buildEmployeeData();
			employeesTable.getSelectionModel().clearSelection();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Employee deletion");
			alert.setHeaderText("Employee was successfully removed.");
			alert.setContentText("Removed employee: \n" + employee);
			alert.showAndWait();
		}
		else {
			workHoursPane.setVisible(false);
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(welcomeMessage.getScene().getWindow());
			alert.setTitle("Delete - No Selection");
			alert.setHeaderText("No Employee Selected");
			alert.setContentText("Please select an employee to delete.");
			alert.showAndWait();
		}
			
	}
	
	@FXML
	private void okEmployeeClicked(ActionEvent event) {
		
		int index = employeesTable.getSelectionModel().getSelectedIndex();
		
		if (index < 0 ) {//no cell selected, which means "New" was pressed
			
			String name = nameField.getText();
			String position = positionField.getText();
			
			if (name == null || position == null || name.equals("") || position.equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(welcomeMessage.getScene().getWindow());
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Could not create because one or both of name/position are blank.");
				alert.showAndWait();
				return;
			}
			//this point is reached if there are no errors
			Employee employee = new Employee(1234, name, position);
			
			employeeService.save(employee);
			
			buildEmployeeData();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(null);
			alert.setHeaderText("Employee was successfully added");
			alert.setContentText("Added employee: \n" + employeeData.get(employeeData.size() - 1));
			alert.showAndWait();
			
			employeesTable.getSelectionModel().clearSelection();

			workHoursPane.setVisible(false);
			hideGridPane();
		}
		else {//"Edit" button was pressed
			
			String name = nameField.getText();
			String position = positionField.getText();
			
			if (name == null || position == null || name.equals("") || position.equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(welcomeMessage.getScene().getWindow());
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Could not create because one or both of name/position are blank.");
				alert.showAndWait();
				return;
			}
			
			long currentId = employeesTable.getSelectionModel().getSelectedItem().getId();
			Employee employee = new Employee(currentId, name, position);
			
			employeeService.update(employee);
			
			buildEmployeeData();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Employee update");
			alert.setHeaderText("Employee was successfully edited");
			alert.setContentText("Edited employee: \n" + employeeData.get(employeeData.size() - 1));
			alert.showAndWait();
		}
	}
	
	@FXML
	private void clearEmployeeClicked(ActionEvent event) {
		nameField.clear();
		positionField.clear();
	}
	
	@FXML
	private void handleLogoutButton(ActionEvent event) throws IOException{
		Stage stage = (Stage) logoutButton.getScene().getWindow();  
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		userService.logOut();
	}
	
	@FXML
	private void manageEmployeesButtonPressed(ActionEvent event){
		menuPane.setVisible(false);
		manageEmployeesPane.setVisible(true);
		createReportPane.setVisible(false);
		adaptingButton.setVisible(true);
		adaptingButton.setText("Create a report");
	}
	
	@FXML
	private void createReportButtonPressed(ActionEvent event){
		menuPane.setVisible(false);
		manageEmployeesPane.setVisible(false);
		createReportPane.setVisible(true);
		adaptingButton.setVisible(true);
		adaptingButton.setText("Manage Employees");
	}
	
	@FXML
	private void adaptingButtonPressed(ActionEvent event){
		if (manageEmployeesPane.isVisible()) {
			createReportButtonPressed(event);
		}
		else {
			manageEmployeesButtonPressed(event);
		}
//		adaptingButton.setText(manageEmployeesPane.isVisible() ? "Create a report" : "Manage Employees");
//		createReportButtonPressed(event);
	}
	
	private void createReport() {
		//reportComposer.createOccupationReport(new Occupation(2, 0, 0, 0, , null, null, false, 0), 1, 1);
	}
	
}
