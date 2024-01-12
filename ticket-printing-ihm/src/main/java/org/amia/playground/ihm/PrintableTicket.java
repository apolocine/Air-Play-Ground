package org.amia.playground.ihm;
import java.awt.*;
import java.awt.print.*;

public class PrintableTicket implements Printable {
	String[] lines;


    public PrintableTicket(String[] lines) {
		super();
		this.lines = lines;
	}


	@Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) { // Ne dispose que d'une seule page
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Exemple de texte à imprimer
        String[] lines = new String[] { "Ligne 1", "Ligne 2", "Ligne 3" };

        int y = 0; // Position initiale en Y
        int lineHeight = 15; // Hauteur de la ligne, ajustez selon les besoins

        for (String line : lines) {
            y += lineHeight; // Incrémenter la position y pour chaque nouvelle ligne
            g2d.drawString(line, 0, y);
        }

        return PAGE_EXISTS;
    }
}
