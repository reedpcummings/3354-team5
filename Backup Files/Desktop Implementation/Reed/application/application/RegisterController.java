package application;

import javafx.application.Application;
import java.sql.*;
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

public class RegisterController extends Controller{

	@FXML TextField username;
	@FXML PasswordField password;
	@FXML TextField fullname;
	
    public static final String myurl = "jdbc:mysql://localhost/userdb";
    public static final String USER = "YOUR_DATABASE_USERNAME";
    public static final String PASSWORD = "YOUR_DATABASE_PASSWORD";
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    
	private String inputName;
	private String inputUserName;
	private String inputPassword;	
	
	@FXML
	private void createAccount(ActionEvent event) {
		 try
		    {
			 
			 
			 
		      // Store user in buffer object
		  	  inputName = fullname.getText();
			  inputUserName = username.getText();
			  inputPassword	= password.getText();  
			  User UserBufferObject1 = new User(inputName,inputUserName,inputPassword);
			  
			  
			  System.out.println(UserBufferObject1);
			  // send User to Controller, then to Model
		      super.addUser(UserBufferObject1);
		      
		      // print Database
		      Model.mainDatabase.toString();
		    
			  
			  // database block
//		      // create a mysql database connection	      
//		      Connection conn = DriverManager.getConnection(myurl, "root", "84738r");
//		    
//		      // the mysql insert statement
//		      String insert = " insert into users values(" + username.getText() + "," + password.getText() + "," + fullname.getText() + ");";
//
//		      // create the mysql insert preparedstatement
//		      PreparedStatement preparedStmt = conn.prepareStatement(insert);
//		     
//		     
//
//		      // execute the preparedstatement
//		      preparedStmt.execute();
//		      
//		      conn.close();
		    }
		    catch (Exception e)
		    {

		      System.err.println("Got an exception!");
		      System.err.println(e.getMessage());
		    }
	 }
	
	
}