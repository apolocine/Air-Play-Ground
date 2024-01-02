package org.amia.playground.ihm.toobare;

import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class PrinterLister {
	
	public static List<String> getPrinters() {
	    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
	    List<String> printerNames = new ArrayList<>();
	    for (PrintService printer : printServices) {
	        printerNames.add(printer.getName());
	    }
	    return printerNames;
	}
	
	
    public static void main(String[] args) {
        // Obtenez et listez tous les services d'impression (imprimantes) disponibles
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

        // Vérifiez si aucune imprimante n'est trouvée
        if(services.length == 0) {
            System.out.println("Aucune imprimante trouvée.");
        }

        // Imprimez les noms des imprimantes disponibles
        for (PrintService service : services) {
            System.out.println("Imprimante trouvée : " + service.getName());
        }
    }
}
