package org.amia.playground.dto;

import java.util.ArrayList;
import java.util.List;

public class User {
	private int userID;
	private String name;
	private String password; // Consider how you'll handle password security

	private List<Role> roles;

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String name, String password, Role role) {
		super();
		this.name = name;
		this.password = password;
		if (roles != null) {
			roles.add(role);
		} else {
			roles = new ArrayList<Role>();
			roles.add(role);
		}
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	 

 
	// Constructors, getters, setters, and methods for CRUD operations
	@Override
	public boolean equals(Object obj) {
		User ouUser = (User) obj;

		return ouUser.getName().equals(ouUser.getName()) && ouUser.getPassword().equals(ouUser.getPassword());
	}
}
