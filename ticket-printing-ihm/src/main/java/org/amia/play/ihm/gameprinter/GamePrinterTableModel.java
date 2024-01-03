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
	private final String[] columnNames = new String[]{"ID", "Name", "Game", "Printer", "Image"};
    private List<GamePrinter> gamePrinters;

    public GamePrinterTableModel() {
        gamePrinters = new ArrayList<>();
    }

    public void setGamePrinters(List<GamePrinter> gamePrinters) {
        this.gamePrinters = new ArrayList<>(gamePrinters);  // Create a copy of the list
        fireTableDataChanged();  // Notify the table that the data has changed
    }

    public GamePrinter getGamePrinterAt(int rowIndex) {
        return gamePrinters.get(rowIndex);
    }

    public void addGamePrinter(GamePrinter gamePrinter) {
        gamePrinters.add(gamePrinter);
        fireTableRowsInserted(gamePrinters.size() - 1, gamePrinters.size() - 1);
    }

    public void removeGamePrinter(int rowIndex) {
        if(rowIndex >= 0 && rowIndex < gamePrinters.size()) {
            gamePrinters.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
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
        switch (columnIndex) {
            case 0: return gamePrinter.getGamePrinterId();
            case 1: return gamePrinter.getName();
            case 2: return gamePrinter.getGame().getGameName(); // Assuming Game has a name
            case 3: return gamePrinter.getPrinter().getName(); // Assuming Printer has a name
            case 4: return new ImageIcon(gamePrinter.getGameImage()); // Convert BLOB to ImageIcon
            default: return null;
        }
    }
}
