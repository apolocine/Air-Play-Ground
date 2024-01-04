package org.amia.play.ihm.gameprinter;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.amia.playground.dto.Printer;

class PrinterRenderer extends DefaultListCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Printer) {
            Printer printer = (Printer) value;
            setText(printer.getName()); // Assuming Printer has a getName method
            // Customize further as needed
        }
        return this;
    }
}
