package org.amia.playground.ihm;

import javax.print.*;
import java.awt.print.PrinterException;
import javax.print.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Handler;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;



public class TicketPrinter {
	// Créez un logger pour cette classe
    private final static Logger LOGGER = Logger.getLogger(TicketPrinter.class.getName());

    static {
        // Configurez le logger pour afficher tous les niveaux de logs
        LOGGER.setLevel(Level.ALL);
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(consoleHandler);
    }
    
    
    
    public static void printTicket(String printerName, String ticketContent) {
    	 try {
    	   // Trouvez le service d'impression spécifique
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService myPrinter = null;
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                myPrinter = service;
                break;
            }
        }

        // S'assure que l'imprimante est trouvée
        if (myPrinter != null) {
            try {
            	
            	
            	  // Créez un travail d'impression
                DocPrintJob job = myPrinter.createPrintJob();
                byte[] bytes;

                // Assurez-vous que le codage du ticket est correct (ici, nous supposons du texte simple)
                bytes = ticketContent.getBytes("UTF-8");

                // Créez un document simple à imprimer
                Doc doc = new SimpleDoc(bytes, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);

                // Lancez l'impression
                job.print(doc, null);
                

                    LOGGER.info("Ticket imprimé avec succès sur l'imprimante : " + printerName);
                    
                } catch (PrintException pe) {
                    LOGGER.log(Level.SEVERE, "Erreur lors de l'impression", pe);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Problème lors de la préparation ou de l'envoi du ticket", e);
                }
            } else {
                LOGGER.warning("Imprimante non trouvée : " + printerName);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des services d'impression", e);
        }
    }

 
    
    
    

    // Supposons que cette méthode est appelée lorsqu'un utilisateur clique sur le bouton Imprimer
    public static void onPrintButtonClick(String ticketContent) {
        // Le nom de l'imprimante doit être celui qui est configuré pour ce ticket ou bouton particulier
        String printerName = "Nom_Imprimante_Assignée";
        printTicket(printerName, ticketContent);
    }
}
