package application;

import java.util.*;
import java.lang.*;
import java.math.*;

public class User{

	private String name;
	private String userName;
	private String password;
	private ArrayList<Event> Events;
	
	
	// value constructors
	User(String name,String userName,String password){
		
		this.name = name;
		this.userName = userName;
		this.password = password;
	}
	
	// Methods
	public void printUser(){
		System.out.println("\nUsers printline function");
		System.out.println("\nInformation:");
		System.out.println("\nName:" + name);
		System.out.println("\nUserName is:" + userName);
		System.out.println("\nPassword is:" + password);
		System.out.println("\nUsers printline function end");
	}
	
	// Getters
	
	public String getName(){
		return this.name;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public String getPassword(){
		return this.password;
	}
}
