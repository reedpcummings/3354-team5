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


import java.util.*;
import java.lang.*;
import java.math.*;

// Calendar Application
// Alex Lundin
// 11-06-2017
// AML140830  
// SE 3354.004
// Software Engineering

public class DailyView {

   // attributes
   private String currentDay;


   // constructors
   
   // getters
   
   private String getDay(){
      return this.currentDay;
   }
   // setters
   
   private void setDay(String Day){
      this.currentDay = Day;
   }
   
   // methods
   
   private String displayTitle(String Day){
      String titleString = "null";
      return titleString;
   }

}