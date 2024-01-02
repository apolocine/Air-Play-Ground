package org.amia.playground.dto;
public class Printer {
    private int id;
    private String name;
    private String location;
    private String status; 
    private boolean isActive;
    private String description;
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

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
