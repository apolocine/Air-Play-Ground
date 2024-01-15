package org.amia.play.tools;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.amia.playground.dao.impl.PrinterRepository;
import org.amia.playground.dto.Printer;
import org.hmd.angio.install.sgbd.DatabaseManager;

public class PrinterLister {
		static Connection con = DatabaseManager.getConnection();
	public static List<String> getPrinters() {
	    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
	    List<String> printerNames = new ArrayList<>();
	    for (PrintService printer : printServices) {
	        printerNames.add(printer.getName());
	    }
	    return printerNames;
	}
	
	
	public static List<String> getPrintersFromDB() {
	
		PrinterRepository pPrinterRepository = new PrinterRepository(con );
		
		  List<String> printerNames = new ArrayList<>();
		  List<  Printer> printers= pPrinterRepository.readAll();
		   
	    for (Printer printer : printers) {
	        printerNames.add(printer.getName());
	    }
	    return printerNames;
	}
	
//	public static List<String> getPrintersNotInDB() {
//		List<String> printerNames = new ArrayList<>();
//		 List<String> printersFromDB= getPrintersFromDB();
//		 List<String> printServices= getPrinters();
//		 
//		 for (String printer : printServices) {
//			if(! printersFromDB.contains(printer)) {
//				 printerNames.add(printer);
//			}
//			
//		}
//		 return printerNames;
//		 
//	}
	
	
	
	
	public static List<String> getPrintersNotInDB() {
		List<String> printerNames = new ArrayList<>();
		 List<String> printersFromDB= getPrintersFromDB();
		 List<String> printServices= getPrinters();
		 
		 for (String printer : printServices) {
			 
			 if( ! printersFromDB.contains(printer)) {
				 printerNames.add(printer);
			}
			 
			 
			 
//			 for (String printerDB : printersFromDB) {
//				if(! printer.equals(printerDB) && ! printerNames.contains(printer)) {
//				 printerNames.add(printer);
//			}
//			
//			}
			
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
