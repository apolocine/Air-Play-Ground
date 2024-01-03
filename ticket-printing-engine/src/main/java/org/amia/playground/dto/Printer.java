package org.amia.playground.dto;
public class Printer {
	  private int printerID;
	    private String name;
	    private String location;
	    private String status;
	    private String description;
	    private boolean isActive;
    
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Printer() { 
	}
    // Constructeur
    public Printer(String name, String location, String status) {
        this.name = name;
        this.location = location;
        this.status = status;
    }

    
    public int getPrinterID() {
		return printerID;
	}
	public void setPrinterID(int printerID) {
		this.printerID = printerID;
	}
	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

    // Vous pourriez vouloir inclure d'autres méthodes utiles ici
}
