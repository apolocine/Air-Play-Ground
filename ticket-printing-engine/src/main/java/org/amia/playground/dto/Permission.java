package org.amia.playground.dto;
public class Permission {
    private int id;
    private String actionText;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
}
