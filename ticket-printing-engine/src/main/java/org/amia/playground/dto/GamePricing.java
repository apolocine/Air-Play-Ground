package org.amia.playground.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GamePricing {
    private int pricingId;
    private int gameID;


	private BigDecimal price;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;

 

    public GamePricing(/*int pricingId,*/
    		int gameId,
    		BigDecimal price,
    		LocalDateTime validFrom,
    		LocalDateTime validTo) {
        //this.pricingId = pricingId;
        this.gameID = gameId;
        this.price = price;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }
    public int getGameID() {
		return gameID;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}
    // Getters and Setters
    public int getPricingId() {
        return pricingId;
    }

    public void setPricingId(int pricingId) {
        this.pricingId = pricingId;
    }




    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }

    // toString for easier debugging and logging
    @Override
    public String toString() {
        return "GamePricing{" +
                "pricingId=" + pricingId +
                ", gameId=" + gameID +
                ", price=" + price +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                '}';
    }
}
