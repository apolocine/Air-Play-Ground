package org.amia.playground.dto;
public class Role {
    private int roleID;
    private String roleName;
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getRoleID() {
		return roleID;
	}
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	public Role(String roleName) {
		super();
		this.roleName = roleName;
	}
	public Role() {
		// TODO Auto-generated constructor stub
	}

    // Constructors, getters, setters, and methods for CRUD operations
    
}
