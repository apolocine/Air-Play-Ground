package org.amia.playground.dto;
public class Game {
    private int gameID;
    private String gameName;
    private String ageRestriction;
    private byte[] gameImage;
    private byte[] logoImage;
    
    
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
	public byte[] getLogoImage() {
		return logoImage;
	}
	public void setLogoImage(byte[] logoImage) {
		this.logoImage = logoImage;
	}
	public byte[] getGameImage() {
		return gameImage;
	}
	public void setGameImage(byte[] gameImage) {
		this.gameImage = gameImage;
	}

    // Constructors, getters, setters, and methods for CRUD operations
    
}
