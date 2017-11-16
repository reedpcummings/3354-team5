package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;



public class MakeCalendar extends Application {
	
	
	
	
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Model myModel = new Model();
		View myView  = new View();
		Controller myController = new Controller();
		myController.setView(myView);
		myController.setModel(myModel);
		
		launch(args);
	    
	}
	

}
