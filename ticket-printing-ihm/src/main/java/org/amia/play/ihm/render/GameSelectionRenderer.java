package org.amia.play.ihm.render;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.amia.playground.dto.GamePrinter;

// Implement the custom renderer for game selection
class GameSelectionRenderer extends DefaultListCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof GamePrinter) {
            GamePrinter gamePrinter = (GamePrinter) value;
            setText(gamePrinter.getGame().getGameName());  // Set text as Game's name or other details
            // Set icon as Game's image if you have one
            
            
            
        }
        return this;
    }
}