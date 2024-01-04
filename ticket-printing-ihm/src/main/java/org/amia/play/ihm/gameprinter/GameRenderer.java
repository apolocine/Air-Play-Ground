package org.amia.play.ihm.gameprinter;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.amia.playground.dto.Game;

class GameRenderer extends DefaultListCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Game) {
            Game game = (Game) value;
            setText(game.getGameName()); // Assuming Game has a getName method
            // You can also set an icon or modify the appearance further
        }
        return this;
    }
}
