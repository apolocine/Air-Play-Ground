package org.amia.playground.dto;

public class Ticket {
	
	private int ticketId;
	private int gamePricingID;
    private int gameID; 
    private String barcode;  
    public Ticket(int gameId) {
		super();
		this.gameID = gameId;
	}
	public Ticket() {
		// TODO Auto-generated constructor stub
	}
	public int getTicketId() {
		return ticketId;
	}
	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}
	public int getGameID() {
		return gameID;
	}
	public void setGameID(int gameId) {
		this.gameID = gameId;
	} 
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	} 
@Override
public boolean equals(Object obj) {
	Ticket tkt= (Ticket) obj;
	return this.getGameID()==tkt.getGameID();
}
public int getGamePricingID() {
	return gamePricingID;
}
public void setGamePricingID(int gamePricingID) {
	this.gamePricingID = gamePricingID;
}
  
}
