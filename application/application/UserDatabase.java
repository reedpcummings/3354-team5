package application;

import java.util.*;
import java.lang.*;
import java.math.*;

public class UserDatabase{
	// attributes
	private ArrayList<User> Users = new ArrayList<User>();
	
	// Methods
	public void addUser(User inputUser){
		System.out.println("\nImplement add in UserDataBase");
	}
	
	// Methods
	@Override
	public String toString(){
		String stringOutput = "null";
		
		System.out.println("\nUser Database toString function");
		User currentUser = null;
		System.out.println("\nThe size of the Database ArrayList is: " + Users.size());
		
		for(int i = 0; i < Users.size(); i++) {   
			currentUser = Users.get(i);
			System.out.println("\nInformation:");
			System.out.println("\nName:" + currentUser.getName());
			System.out.println("\nUserName is:" + currentUser.getUserName());
			System.out.println("\nPassword is:" + currentUser.getPassword());			
		}
		

		return stringOutput;

	}
}


