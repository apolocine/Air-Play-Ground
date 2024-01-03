package org.amia.playground.dto;

import java.util.List;

public class Role {
    private int roleID;
    private String roleName;
    
    private List<Permission>  permissions;
    
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
	public List<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
 

    // Constructors, getters, setters, and methods for CRUD operations
    
}
