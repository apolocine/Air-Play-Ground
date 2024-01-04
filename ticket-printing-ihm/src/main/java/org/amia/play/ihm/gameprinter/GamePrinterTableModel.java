package org.amia.play.ihm.gameprinter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import org.amia.playground.dto.GamePrinter;

public class GamePrinterTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = new String[]{"Name", "Game", "Printer", "Image"};
    private List<GamePrinter> gamePrinters;

    public GamePrinterTableModel() {
        gamePrinters = new ArrayList<>();
    }

    public void addGamePrinter(GamePrinter gamePrinter) {
        gamePrinters.add(gamePrinter);
        fireTableRowsInserted(gamePrinters.size() - 1, gamePrinters.size() - 1);
    }

    public void removeGamePrinter(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < gamePrinters.size()) {
            gamePrinters.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public void setGamePrinters(List<GamePrinter> gamePrinters) {
        this.gamePrinters = new ArrayList<>(gamePrinters);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return gamePrinters.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        GamePrinter gamePrinter = gamePrinters.get(rowIndex);
        
        if(gamePrinters!=null) {
        	switch (columnIndex) {
            case 0: return gamePrinter.getName();
            case 1: return gamePrinter.getGame().getGameName(); // Assuming Game has a name
            case 2: return gamePrinter.getPrinter().getName(); // Assuming Printer has a name
            case 3: 
                if (gamePrinter.getGameImage() != null) {
                    return new ImageIcon(gamePrinter.getGameImage()); // Convert byte[] to ImageIcon
                }
                return null; // or some default image/icon
            default: return null;
        }
        }
        return null;
        
    }
    public GamePrinter getGamePrinterAt(int rowIndex) {
        return gamePrinters.get(rowIndex);
    }
    // Additional methods as needed for your table model...
}
