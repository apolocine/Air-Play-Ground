package org.amia.playground.dto;
public class Game {
    private int gameID;
    private String gameName;
    private String ageRestriction;
	public int getGameID() {
		return gameID;
	}
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getAgeRestriction() {
		return ageRestriction;
	}
	public void setAgeRestriction(String ageRestriction) {
		this.ageRestriction = ageRestriction;
	}
	public Game(String gameName, String ageRestriction) {
		super();
		this.gameName = gameName;
		this.ageRestriction = ageRestriction;
	}
	public Game() {
		// TODO Auto-generated constructor stub
	}

    // Constructors, getters, setters, and methods for CRUD operations
    
}
