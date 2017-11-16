
package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class MonthlyViewController extends Controller{

   
    @FXML
    void goToWeeklyView(ActionEvent event) {

    	try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WeeklyView.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Weekly View");
            stage.setScene(new Scene(root1));  
            stage.show();
          }
		catch(Exception e){
			e.printStackTrace();
		}
    	
    	
    }

}
