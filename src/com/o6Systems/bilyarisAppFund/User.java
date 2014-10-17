package com.o6Systems.bilyarisAppFund;

public class User {
	int userID;
	String name;
	
	public User(String name){
		this.name = name;
	}
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
