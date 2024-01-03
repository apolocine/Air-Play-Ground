package org.amia.playground.dto;
public class GamePrinter {
    private int gamePrinterId;
    private String name;
    private Game game;
    private Printer printer;
    private byte[] gameImage; // Consider how you'll handle image data
	public int getGamePrinterId() {
		return gamePrinterId;
	}
	public void setGamePrinterId(int gamePrinterId) {
		this.gamePrinterId = gamePrinterId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public Printer getPrinter() {
		return printer;
	}
	public void setPrinter(Printer printer) {
		this.printer = printer;
	}
	public byte[] getGameImage() {
		return gameImage;
	}
	public void setGameImage(byte[] gameImage) {
		this.gameImage = gameImage;
	}

    // Constructors, getters, and setters...
}
