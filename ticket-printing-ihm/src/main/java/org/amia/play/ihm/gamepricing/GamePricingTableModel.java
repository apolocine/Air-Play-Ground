package org.amia.play.ihm.gamepricing;
import javax.swing.table.AbstractTableModel;

import org.amia.playground.dto.GamePricing;

import java.util.ArrayList;
import java.util.List;

public class GamePricingTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = {"Game", "Price", "Valid From", "Valid To"};
    private List<GamePricing> pricings; // Liste des objets GamePricing

    public GamePricingTableModel(List<GamePricing> pricings) { 
        this.pricings = pricings;
    }
    
    

    public GamePricingTableModel() {
		super();
	}



	@Override
    public int getRowCount() {
        return pricings.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        GamePricing pricing = pricings.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return pricing.getGameID(); // Vous voudrez peut-être afficher le nom du jeu
            case 1: return pricing.getPrice();
            case 2: return pricing.getValidFrom();
            case 3: return pricing.getValidTo();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    // Ajouter des méthodes pour mettre à jour les données si nécessaire
    
    public void addPricing(GamePricing pricing) {
        pricings.add(pricing);
        fireTableRowsInserted(pricings.size() - 1, pricings.size() - 1); // Notifie le tableau qu'une ligne a été ajoutée
    }
    
    public void updatePricing(int rowIndex, GamePricing newPricing) {
        if (rowIndex >= 0 && rowIndex < pricings.size()) {
            pricings.set(rowIndex, newPricing);
            fireTableRowsUpdated(rowIndex, rowIndex); // Notifie le tableau que les données de la ligne ont été modifiées
        }
    }
    public void removePricing(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < pricings.size()) {
            pricings.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex); // Notifie le tableau qu'une ligne a été supprimée
        }
    }

    
    public void setPricings(List<GamePricing> newPricings) {
        pricings = new ArrayList<>(newPricings); // Remplace les données existantes par les nouvelles
        fireTableDataChanged(); // Notifie le tableau que toutes les données ont été modifiées
    }



	public GamePricing getGamePricingAt(int rowIndex) {
		  return pricings.get(rowIndex);
	}

    

}
