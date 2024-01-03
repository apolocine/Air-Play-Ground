package org.amia.play.ihm.printer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.amia.playground.dto.Printer;

public class PrinterTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = new String[]{"ID", "Name", "Location", "Status", "Description", "IsActive"};
    private List<Printer> printers;

    public PrinterTableModel() {
        printers = new ArrayList<>();
    }

    public void setPrinters(List<Printer> printers) {
        this.printers = new ArrayList<>(printers);  // Create a copy of the list
        fireTableDataChanged();  // Notify the table that the data has changed
    }

    public Printer getPrinterAt(int rowIndex) {
        return printers.get(rowIndex);
    }

    public void addPrinter(Printer printer) {
        printers.add(printer);
        fireTableRowsInserted(printers.size() - 1, printers.size() - 1);
    }

    public void removePrinter(int rowIndex) {
        if(rowIndex >= 0 && rowIndex < printers.size()) {
            printers.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public void updatePrinter(Printer printer, int rowIndex) {
        printers.set(rowIndex, printer);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public int getRowCount() {
        return printers.size();
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
        Printer printer = printers.get(rowIndex);
        switch (columnIndex) {
            case 0: return printer.getPrinterID();
            case 1: return printer.getName();
            case 2: return printer.getLocation();
            case 3: return printer.getStatus();
            case 4: return printer.getDescription();
            case 5: return printer.isActive() ? "Yes" : "No";
            default: return null;
        }
    }
}
