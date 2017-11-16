package application;

public class Model {
	// static attributes
	// these are the pipes to the mainDatabase
	// keep this the same throughout the project
	// this will allow parallel development on the Controllers without much re-work
	
	
	protected static UserDatabase mainDatabase;
	protected static User UserBufferObject;
	
	
	// here you have as much freedom as you want, as far as how the Model chooses to store the data
	// use any implementation you would like in the UserDatabase class
	// keep this method signature static and the mainDatabase and UserBufferObject static
	// the way the Controller can manipulate them
	
	public static void addUser(){
		System.out.println("\nAdding to UserDataBase with Model");
		mainDatabase.addUser(UserBufferObject);
	}
	

	
	// constructors
	Model(){
		System.out.println("\nModel Constructor");
		this.mainDatabase = new UserDatabase();
	}
}
