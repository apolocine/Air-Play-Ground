package org.amia.play.ihm.ticket;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Logger;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.amia.play.ihm.TicketPrintingIHM;
import org.amia.playground.dao.impl.GamePrinterRepository;
import org.amia.playground.dao.impl.GameRepository;
import org.amia.playground.dto.GamePrinter;

public class TicketForm extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final GameRepository gameRepository;
	GamePrinterRepository gamePrinterRepository;  
    private JComboBox<GamePrinter> gameSelectionComboBox;
    private JSpinner ticketQuantitySpinner;
	private static final Logger LOGGER = Logger.getLogger(TicketForm.class.getName());

	// Form components
	private JTextField gameNameField; 

 

 
 
	public TicketForm( GamePrinterRepository gamePrinterRepository,GameRepository gameRepository) {
	super();
	this.gameRepository = gameRepository;
	this.gamePrinterRepository = gamePrinterRepository;
	setLayout(new BorderLayout());
	add(createInputPanel(), BorderLayout.NORTH); 
	//add(TicketPrintingIHM.getPanelTicketPrinting(gameRepository), BorderLayout.SOUTH); 
	
}

 

	private JPanel createInputPanel() {
		   // Game Selection
        gameSelectionComboBox = new JComboBox<GamePrinter>();  // Populate with games and printers
        
      List<GamePrinter> gamePrinters = gamePrinterRepository.readAll(); //gameRepository.readAllGamesWithoutPrinter();  gameRepository.readAllGamesWithoutPrinter()
      
      for (GamePrinter gamePrinter : gamePrinters) {
    	  gameSelectionComboBox.addItem(gamePrinter);
	}
      
      
      
		JPanel inputPanel = new JPanel(new GridLayout(0, 2));

		inputPanel.add(new JLabel("Game Name:"));
		gameNameField = new JTextField(20);
		inputPanel.add(gameNameField);

		inputPanel.add(new JLabel("Game Restriction:")); 

	   gameSelectionComboBox.setRenderer(new GameSelectionRenderer());  // Custom renderer to show game images
	   inputPanel.add(gameSelectionComboBox, BorderLayout.NORTH);
     
	   
	   // Ticket Quantity
      SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 100, 1);  // Default 1, min 1, max 100, step 1
      ticketQuantitySpinner = new JSpinner(model);
      inputPanel.add(ticketQuantitySpinner, BorderLayout.CENTER);

		JButton addButton = new JButton("Print");
		addButton.addActionListener(this::handlePrintAction);
		inputPanel.add(addButton);

		return inputPanel;
	}

 

	 private void handlePrintAction(ActionEvent e) {
	        GamePrinter selectedGamePrinter = (GamePrinter) gameSelectionComboBox.getSelectedItem();
	        int quantity = (Integer) ticketQuantitySpinner.getValue();
	        
	        // Implement the printing logic here using the selectedGamePrinter and quantity
	        printTickets(selectedGamePrinter, quantity);
	    }
	    
	    private void printTickets(GamePrinter gamePrinter, int quantity) {
	        // Printing logic goes here. It might interact with a printer service.
	        // Ensure to handle printer selection, game details, and error handling.
	    	String ticketContent = "Printing " + quantity + " tickets for " + gamePrinter.getGame().getGameName();
	        System.out.println(ticketContent);
	        
	        for (int i = 0; i < quantity; i++) {
				 printTicket(gamePrinter.getPrinter().getName(),   ticketContent);
			}
	       
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
	    
}
