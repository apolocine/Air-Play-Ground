package org.amia.play.ihm.gamepricing;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.amia.playground.dto.Game;

public class GameRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Game) {
            Game game = (Game) value;
            setText(game.getGameName()); // Utilisez le nom du jeu ou tout autre attribut que vous souhaitez afficher
            // Vous pouvez également définir une icône ici si vous le souhaitez, par exemple:
            // setIcon(new ImageIcon(game.getImagePath()));
        }
        return this;
    }
}
