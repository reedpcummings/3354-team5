package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class Controller {


	// attributes


	// constructors

	// abstract methods

	// methods
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
