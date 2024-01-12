package org.amia.playground.dao.service;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.swing.ImageIcon;

import org.amia.playground.dao.impl.GamePricingRepository;
import org.amia.playground.dao.impl.GamePrinterRepository;
import org.amia.playground.dao.impl.GameRepository;
import org.amia.playground.dao.impl.TicketRepository;
import org.amia.playground.dto.Game;
import org.amia.playground.dto.GamePricing;
import org.amia.playground.dto.Printer;
import org.amia.playground.dto.Ticket;
import org.amia.playground.utils.BarcodeUtil;
import org.amia.playground.utils.ImageUtil;
import org.hmd.angio.install.sgbd.DatabaseManager;

import net.coobird.thumbnailator.Thumbnails;

public class TicketService {
    private TicketRepository ticketRepository;
    private static final Logger LOGGER = Logger.getLogger(TicketService.class.getName());

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

   
    public Ticket createTicket(Ticket ticket) {
        // Generate barcode
        String barcode = BarcodeUtil.generateBarcode( ""+ticket.getTicketId());
        ticket.setBarcode(barcode);

        // Save ticket using repository
        return ticketRepository.create(ticket);
    }


 

 
    
    public static void printTicket(String printerName, String ticketContent) {
        try {
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            PrintService myPrinter = null;
            for (PrintService service : services) {
                if (service.getName().equalsIgnoreCase(printerName)) {
                    myPrinter = service;
                    break;
                }
            }

            if (myPrinter != null) {
                try {
                    DocPrintJob job = myPrinter.createPrintJob();
                    byte[] bytes;

                    bytes = ticketContent.getBytes("UTF-8");

                    Doc doc = new SimpleDoc(bytes, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);

                    job.print(doc, null);
                    
                } catch (PrintException pe) {
                    System.err.println("Erreur lors de l'impression : " + pe.getMessage());
                } catch (Exception e) {
                    System.err.println("Problème lors de la préparation ou de l'envoi du ticket : " + e.getMessage());
                }
            } else {
                System.out.println("Imprimante non trouvée : " + printerName);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des services d'impression : " + e.getMessage());
        }
    }
    

    
    /**
     * 
     * @param ticket
     * @return
     */
    
    public   boolean printTicket(Ticket ticket) {
        // Define the printer name (ID or name associated with the game)
    	Connection conn=DatabaseManager.getConnection();
		GameRepository gamerRepository = new GameRepository(conn);
		GamePrinterRepository gamePrinterRepository = new GamePrinterRepository(conn); 
		
	
		
		LOGGER.info(""+ticket.getGameID());
    	Game game = gamerRepository.read(ticket.getGameID());
//    	BigDecimal  curentPrice = 	gamerRepository.getCurrentPriceForGame(ticket.getGameID());
    	
		List<GamePricing> gamePricings = gamerRepository.getGamePricingByGameAndCurrentDate(ticket.getGameID());
		 
		
    	List<Printer> printers = gamePrinterRepository.getPrintersForGame(ticket.getGameID());
    	Printer printer = null ;
    	if(printers!=null && printers.size()>0 ) {
    		printer=printers.getFirst();
    	}else{
    		   // Handle case where printer isn't found
        	LOGGER.log(Level.SEVERE, " no printer associated " );
            return false;
    	}
        String printerName = printer.getName(); // Adjust based on your data model
        
        // Fetch print service that matches the printer name
        PrintService printService = findPrintService(printerName);
        if (printService == null) {
            // Handle case where printer isn't found
        	LOGGER.log(Level.SEVERE, " case where printer isn't found " );
        	 return false;
        }

        // Create a print job for the selected print service
        PrinterJob job = PrinterJob.getPrinterJob();
        try {
            job.setPrintService(printService);
            // Define the paper and page format if necessary, especially for an 80mm width
            PageFormat pageFormat = new PageFormat();
            Paper paper = new Paper();
            
            
//            double width = 80; // width in 1/72 inch units
//            double height = 200; // adjust the height as needed
//            paper.setSize(width, height);
//            paper.setImageableArea(0, 0, width, height);
//            pageFormat.setPaper(paper);
 
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
            
            
            
           
            
            
            // Define the content and format of the print job
            job.setPrintable(new Printable() {
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    if (pageIndex != 0) {
                        return NO_SUCH_PAGE;
                    }
                    
                    
                    int count=0;
                	int inteval = 20;
                	int xStart=0;
                	int yStart=0;
                    
                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    
                    
  // Print the ImageIcon (if available)
                    int iconHeight = 0 ;
                    if (game.getLogoImage() != null) {
                        // Assuming getGameImage() returns a path or ImageIcon
                        byte[] img = game.getLogoImage() ;  // Get the image from ImageIcon
                        ImageIcon imageIcontmp = new ImageIcon(img); 
						int widthByPhotosCount=20;
						int heigtByPhotosCount=20;
						ImageIcon imageIcon = ImageUtil.resizeImageIcon(imageIcontmp, widthByPhotosCount, heigtByPhotosCount); 
						 yStart+=inteval;
						 iconHeight =imageIcon.getIconHeight();
                        g2d.drawImage(imageIcon.getImage(), 10, yStart, null);  // Adjust positioning and sizing as needed  
                    }
                   
                    
                    //incrémenter laposition suivante 
                  yStart+=inteval;
                     g2d.drawString(" " + game.getGameName(), 40, yStart); 
                     
                    for (GamePricing gamePricing : gamePricings) {
                     
                    	count++;
                  
                    	//xStart+=inteval;
                    	yStart+=inteval;
                    	g2d.drawString("Price: "+gamePricing.getPrice(), xStart, yStart);  // Adjust positioning as needed
                    	//xStart+=inteval;
                    	yStart+=inteval;
                    	g2d.drawString("Date From " + gamePricing.getValidFrom(), xStart, yStart); 
                    	//xStart+=inteval;
                    	yStart+=inteval;
                    	g2d.drawString("Date To " + gamePricing.getValidTo(), xStart, yStart); 
					}
               
                     
                  
                    
                     
                    
                 // Print the barcode
                    BufferedImage barcodeImage = BarcodeUtil.generateBarcodeImage(ticket.getBarcode());
                    if (barcodeImage != null) {
                        int x = 10;  // adjust for your ticket layout
                        int y = 50;  // adjust for after the price and image
                        yStart+=inteval;
                        y = yStart;
                        g2d.drawImage(barcodeImage, x, y, null);
                    }
                    
                    // Custom logic to draw the ticket on the graphics object goes here
                    // This might involve setting fonts, drawing strings, images, etc.
                    return PAGE_EXISTS;
                }
            },pageFormat);

        
            
            // Trigger the print job
            job.print();
            return true;
        } catch (PrinterException e) {
            // Handle printing errors here
//        	 LOGGER.log(Level.SEVERE, "Failed to print ticket: " + e.getMessage(), e);
        	 LOGGER.log(Level.SEVERE, "Failed to print ticket: "  );
        	 return false;
        }
        
        
      
    }

    private   PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().trim().equalsIgnoreCase(printerName.trim())) {
                return printService;
            }
        }
        return null;
    }
    
    
    
}
