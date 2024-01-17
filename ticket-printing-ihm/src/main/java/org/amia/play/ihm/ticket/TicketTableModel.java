package org.amia.play.ihm.ticket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.amia.playground.dto.Ticket;

public class TicketTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Ticket> tickets;
    private final String[] columnNames = {"Ticket ID", "Game ID", "Pricing ID", "User ID", "Valid Date", "Barcode"};
    
    public TicketTableModel(List<Ticket> tickets) {
        this.tickets = tickets;
    }
    // Update the entire list of tickets
    public void setTickets(List<Ticket> tickets) {
        this.tickets = new ArrayList<>(  );  // Create a copy of the list
        fireTableDataChanged();  // Notify the table that the data has changed
    }

    // Get a user at a specific row
    public Ticket getTicketAt(int rowIndex) {
        return tickets.get(rowIndex);
    }

    // Remove a user from a specific row
    public void removeTicket(int rowIndex) {
        if(rowIndex >= 0 && rowIndex < tickets.size()) {
            tickets.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    // Add a user to the table model
    public void addTicket(Ticket user) {
        tickets.add(user);
        fireTableRowsInserted(tickets.size() - 1, tickets.size() - 1);
    }

    // Update an existing user's details
    public void updateTicket(Ticket user, int rowIndex) {
        tickets.set(rowIndex, user);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }


    @Override
    public int getRowCount() {
        return tickets.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ticket ticket = tickets.get(rowIndex);
        switch (columnIndex) {
            case 0: return ticket.getTicketId();
            case 1: return ticket.getGameID();
            case 2: return ticket.getGamePricingID();
            case 3: return ticket.getUserID();
            case 4: return ticket.getValidDate();
            case 5: return ticket.getBarcode();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    // Méthodes pour mettre à jour les données...
    // Implémentez getRowCount, getColumnCount et getValueAt
    // ...
    
    
    private static final int PAGE_SIZE = 30;
    private int currentPage = 0;

    // Méthode pour charger les données pour la page actuelle
//    public void loadTicketsForPage(int page) {
//        this.currentPage = page;
//        // Charger les données pour la page spécifiée
//        // Par exemple, en faisant une requête à la base de données
//        // ...
//        fireTableDataChanged(); // Notifie que les données du modèle ont changé
//    }
    public void loadTicketsForPage(int page, List<Ticket> tickets2) {
        if (page < 0) {
            return; // Ne pas charger si la page est inférieure à 0
        }
        this.currentPage = page;

        // Exemple de chargement de données (ajustez avec votre logique de base de données)
        this.tickets.clear();
//        this.tickets.addAll(fetchTicketsFromDatabase(page, PAGE_SIZE));
        this.tickets.addAll(tickets2);
        fireTableDataChanged(); // Notifie que les données du modèle ont changé
    }

    public int getCurrentPage() {
        return currentPage;
    }

    // Méthode pour récupérer les tickets de la base de données
    private List<Ticket> fetchTicketsFromDatabase(int page, int pageSize) {
        // Implémentez la logique pour récupérer les tickets
        // en fonction de la page et de la taille de la page
        return new ArrayList<>();
    }
    // Implémentez les autres méthodes nécessaires de AbstractTableModel
    // ...
}
