package org.amia.playground.dto;


/**
 * 
 *
 *
 
 1-1
 CREATE TABLE IF NOT EXISTS GamePrinters (
	Name varchar (250) NOT NULL,
    GameID INT,
    PrinterID INT,
    GameImage MEDIUMBLOB,
    PRIMARY KEY (GameID, PrinterID),
    FOREIGN KEY (GameID) REFERENCES Games(GameID),
    FOREIGN KEY (PrinterID) REFERENCES Printers(PrinterID)
);

1-n
CREATE TABLE IF NOT EXISTS GamePrinters (
    GameID INT UNIQUE,
    PrinterID INT,
    Name VARCHAR(255) NOT NULL,
    GameImage MEDIUMBLOB,
    PRIMARY KEY (GameID),
    FOREIGN KEY (GameID) REFERENCES Games(GameID),
    FOREIGN KEY (PrinterID) REFERENCES Printers(PrinterID)
);



 * 
 * 
 */
public class GamePrinter { 
    private String name;
    private Game game;
    private Printer printer;
    private byte[] gameImage; // Consider how you'll handle image data
 
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
