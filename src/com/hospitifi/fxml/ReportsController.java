package com.hospitifi.fxml;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.hospitifi.model.Occupation;
import com.hospitifi.report.ReportComposer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ReportsController implements Initializable{
	
	@FXML 
	Button openButton;
	@FXML 
	Button closeButton;
	@FXML 
	ObservableList<String> list;
	@FXML 
	ListView<String> listView;
	
	private ReportComposer reportComposer;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadReports();
	}
	
	
	private List<String> docxFiles(String directory) {
		List<String> textFiles = new ArrayList<String>();
		File dir = new File(directory);
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith((".docx"))) {
				textFiles.add(file.getName());
			}
		}
		return textFiles;
	}
	
	@FXML 
	private void handleOpen(ActionEvent event) throws IOException{
		if(listView.getSelectionModel().getSelectedIndex() >= 0) { //something selected
			File file = new File("reports/occupation/" + listView.getSelectionModel().getSelectedItem());
			Desktop.getDesktop().open(file);
		
		}
	}
	@FXML 
	private void handleClose(ActionEvent event) {
		Node  source = (Node)  event.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
	}
	
	private void loadReports() { //LOAD reports
		list = FXCollections.observableArrayList();
		list.addAll(docxFiles("reports/occupation"));
		listView.setItems(list);
	}
	
}
