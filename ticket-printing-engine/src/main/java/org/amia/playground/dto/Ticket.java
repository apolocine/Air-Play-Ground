package org.amia.playground.dto;

public class Ticket {
	

	
    public Ticket(int gameId) {
		super();
		this.gameId = gameId;
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
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public byte[] getGameImage() {
		return gameImage;
	}
	public void setGameImage(byte[] gameImage) {
		this.gameImage = gameImage;
	}
	public byte[] getLogoImage() {
		return logoImage;
	}
	public void setLogoImage(byte[] logoImage) {
		this.logoImage = logoImage;
	}
	private int ticketId;
    private int gameId;
    private double price;
    private String barcode; 
    private byte[] gameImage; // byte array for BLOB data
    private byte[] logoImage; // byte array for BLOB data
@Override
public boolean equals(Object obj) {
	Ticket tkt= (Ticket) obj;
	return this.getGameId()==tkt.getGameId();
}
    // Constructors, getters, and setters...
}
