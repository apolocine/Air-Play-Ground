package org.amia.playground.dao.impl;

import org.amia.playground.dto.User;

public class ConnectedUser {
    private static ConnectedUser instance;
    private static User user;
    private ConnectedUser() {}

    public static ConnectedUser getInstance() {
        if (instance == null) {
            instance = new ConnectedUser();
        }
        return instance;
    }

	public static User getUser() { 
		ConnectedUser.getInstance();
		return ConnectedUser.user;
	}

	public static void setUser(User user) {
		ConnectedUser.getInstance();
		ConnectedUser.user = user;
	}
    
    
    
    
}
