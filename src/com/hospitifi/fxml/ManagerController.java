package com.hospitifi.fxml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hospitifi.model.Occupation;
import com.hospitifi.report.ReportComposer;
import com.hospitifi.service.EmployeeService;
import com.hospitifi.service.OccupationService;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ManagerController implements Initializable{
	
	@FXML
	private Button logoutButton;
	
	@FXML
	private Pane menuPane;
	
	@FXML
	private Button createReportButton;
	
	@FXML
	private Pane manageEmployeesButton;
	
	private UserService userService = ServiceFactory.getUserService();
	
	private OccupationService occupationservice = ServiceFactory.getOccupationService();
	
	private ReportComposer reportComposer;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configureManager();
	}
	
	private void configureManager() {
		reportComposer = ReportComposer.getInstance();
		menuPane.toFront();
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
	private void createReport(ActionEvent event) {
		
		//reportComposer.createOccupationReport(new Occupation(2, 0, 0, 0, , null, null, false, 0), 1, 1);
	}
	
}
