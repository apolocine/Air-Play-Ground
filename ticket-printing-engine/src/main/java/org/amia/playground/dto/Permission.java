package org.amia.playground.dto;
public class Permission {
    private int permissionID;
    private String actionText;
	 
	public String getActionText() {
		return actionText;
	}
	public void setActionText(String actionText) {
		this.actionText = actionText;
	}
	public Permission(String actionText) {
		super();
		this.actionText = actionText;
	}

    public Permission() {
	 
	}
	public int getPermissionID() {
		return permissionID;
	}
	public void setPermissionID(int permissionID) {
		this.permissionID = permissionID;
	} 
}
