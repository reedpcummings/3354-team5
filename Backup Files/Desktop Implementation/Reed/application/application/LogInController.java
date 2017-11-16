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

public class LogInController extends Controller{

	
	public void initiialize() {
		
	}
	
	
	@FXML
	public void openRegisterScene(ActionEvent event){
		
		//actiontarget.setText("create account pressed");
		
		
		try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Register.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Register");
            stage.setScene(new Scene(root1));  
            stage.show();
          }
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@FXML
	public void signIn(ActionEvent event){
		try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MonthlyView.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Monthly View");
            stage.setScene(new Scene(root1));  
            stage.show();
          }
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
