package org.amia.playground.ihm;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class TicketPrint {

    public static void printTicket() {
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pageFormat = new PageFormat();
        Paper paper = new Paper();

        double mmInInch = 25.4;
        double widthInMM = 80;
        double heightInMM = 200; // Hauteur approximative, ajustez selon vos besoins

        double widthInPoints = (widthInMM / mmInInch) * 72; // Convertir mm en points
        double heightInPoints = (heightInMM / mmInInch) * 72; // Convertir mm en points

        // Définir la taille du papier et les marges
        paper.setSize(widthInPoints, heightInPoints);
        paper.setImageableArea(0, 0, widthInPoints, heightInPoints);

        pageFormat.setPaper(paper);
        pageFormat.setOrientation(PageFormat.PORTRAIT);
        String[] lines = new String[] { "Ligne 1", "Ligne 2", "Ligne 3" };
        
        job.setPrintable(new PrintableTicket(lines) {
		 
		 
		}, pageFormat); // Assurez-vous que MyPrintable implémente Printable

        try {
            job.print();
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    // Implémentation de Printable (MyPrintable) ici
    // ...
}
