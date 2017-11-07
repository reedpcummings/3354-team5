package application;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller {
	

	// attributes
	protected Model model;
	protected View view;
	// constructors

	// methods
	
	public void addUser(User inputUser1){
		System.out.println("\nAdding to Model with Controller");
		System.out.println("\nUser: " + inputUser1);
		model.addUser(inputUser1);
	}	
	
    public void setModel(Model m){
    	System.out.println("\nSetting Model for Controller");
    	this.model = m;
    	
    }
    public void setView(View v){
    	System.out.println("\nSetting View for Controller");
    	this.view = v;
    }	
    
	// 11-6-2017 -- refactor number 1, pull up method
    @FXML
    void goToAddEventView(ActionEvent event) {

    	try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addEvent.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Event");
            stage.setScene(new Scene(root1));
            stage.show();
          }
		catch(Exception e){
			e.printStackTrace();
		}


    }
    


}
