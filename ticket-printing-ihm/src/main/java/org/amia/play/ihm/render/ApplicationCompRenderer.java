package org.amia.play.ihm.render;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.amia.playground.dto.Game;
import org.amia.playground.dto.GamePrinter;
import org.amia.playground.dto.Role;
import org.amia.playground.dto.User;

public class ApplicationCompRenderer extends DefaultListCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof User) {
        	User user = (User) value;
            setText(user.getName()); // Utilisez le nom du jeu ou tout autre attribut que vous souhaitez afficher
            // Vous pouvez également définir une icône ici si vous le souhaitez, par exemple:
            // setIcon(new ImageIcon(game.getImagePath()));
        }else 
        	
        	if(value instanceof Role) {
        	Role role = (Role) value;
            setText(role.getRoleName()); // Utilisez le nom du jeu ou tout autre attribut que vous souhaitez afficher
            // Vous pouvez également définir une icône ici si vous le souhaitez, par exemple:
            // setIcon(new ImageIcon(game.getImagePath()));
       
        	
        } else 
        	
        	if (value instanceof Game) {
            Game game = (Game) value;
            setText(game.getGameName()); // Utilisez le nom du jeu ou tout autre attribut que vous souhaitez afficher
            // Vous pouvez également définir une icône ici si vous le souhaitez, par exemple:
            // setIcon(new ImageIcon(game.getImagePath()));
        }else   if (value instanceof GamePrinter) {
            GamePrinter gamePrinter = (GamePrinter) value;
            setText(gamePrinter.getGame().getGameName());  // Set text as Game's name or other details
            // Set icon as Game's image if you have one
            
            
            
        }
        return this;
    }
}