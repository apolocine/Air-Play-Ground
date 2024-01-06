package org.amia.playground.ihm.toobare;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.amia.playground.dao.impl.GamePrinterRepository;
import org.amia.playground.dao.impl.GameRepository;
import org.amia.playground.dto.GamePrinter;
import org.amia.playground.dto.Printer;
import org.hmd.angio.install.sgbd.DatabaseManager;

public class TicketPrintingIHM extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	GamePrinterRepository gamePrinterRepository;
	GameRepository gameRepository;
	// Components
    private JComboBox<GamePrinter> gameSelectionComboBox;
    private JSpinner ticketQuantitySpinner;
    private JButton printButton;
    JButton addButton ;
    public TicketPrintingIHM() {
        setTitle("Ticket Printing Interface");
        setSize(400, 300);
        Connection conn = DatabaseManager.getConnection();
		gamePrinterRepository = new GamePrinterRepository(conn);
        initializeUI();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
      
        // Game Selection
        gameSelectionComboBox = new JComboBox<GamePrinter>();  // Populate with games and printers
        
      List<GamePrinter> gamePrinters = gamePrinterRepository.readAll(); //gameRepository.readAllGamesWithoutPrinter();  gameRepository.readAllGamesWithoutPrinter()
      
      for (GamePrinter gamePrinter : gamePrinters) {
    	  gameSelectionComboBox.addItem(gamePrinter);
	}
         JButton addButton = new JButton("Add Printer");
        addButton.addActionListener(e -> addPrinter());
        add(addButton, BorderLayout.NORTH);
        gameSelectionComboBox.setRenderer(new GameSelectionRenderer());  // Custom renderer to show game images
        add(gameSelectionComboBox, BorderLayout.NORTH);
        
        // Ticket Quantity
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 100, 1);  // Default 1, min 1, max 100, step 1
        ticketQuantitySpinner = new JSpinner(model);
        add(ticketQuantitySpinner, BorderLayout.CENTER);
        
        // Print Button
        printButton = new JButton("Print Tickets");
        printButton.addActionListener(this::handlePrintAction);
        add(printButton, BorderLayout.SOUTH);
        
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicketPrintingIHM frame = new TicketPrintingIHM();
            frame.setVisible(true);
        });
    }


    
    
    private void addPrinter() {
        // Créez une boîte de dialogue ou un formulaire personnalisé pour saisir les informations de l'imprimante
        JTextField nameField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JTextField statusField = new JTextField(20);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(Box.createHorizontalStrut(15)); // un espace
        panel.add(new JLabel("Location:"));
        panel.add(locationField);
        panel.add(Box.createHorizontalStrut(15)); // un espace
        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        int result = JOptionPane.showConfirmDialog(null, panel, 
                 "Enter Printer Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Printer newPrinter = new Printer(nameField.getText(), locationField.getText(), statusField.getText());
            // Ici, utilisez votre méthode existante pour ajouter une imprimante à la base de données
            // Par exemple: printerDatabase.addPrinter(newPrinter);
            // Assurez-vous que cette méthode gère l'ajout de l'imprimante à votre base de données et le logging
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
