package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class AddEventController {

	@FXML TextField e_Name;
	@FXML TextField e_startTime;
	@FXML TextField e_endTime;
	
	String eventName;
	String eventStart;
	String eventEnd;
	
	
	public void initiialize() {
		
	}
	
	
	@FXML
	public void createEvent(ActionEvent event){
		eventName = e_Name.getText();
		eventStart = e_startTime.getText();
		eventEnd = e_endTime.getText();
		
		Event event1 = new Event(eventName,eventStart,eventEnd);
		
		
	}
	

}
