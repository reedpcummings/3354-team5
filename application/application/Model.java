package application;

public class Model {
	protected UserDatabase mainDatabase;
	
	public void addUser(User inputUser){
		System.out.println("\nAdding to UserDataBase with Model");
		mainDatabase.addUser(inputUser);
	}	
}
