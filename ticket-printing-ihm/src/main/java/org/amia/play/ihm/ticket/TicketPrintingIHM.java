package org.amia.play.ihm.ticket;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.amia.playground.dao.impl.GamePrinterRepository;
import org.amia.playground.dto.Game;
import org.amia.playground.dto.GamePrinter;
import org.hmd.angio.install.sgbd.DatabaseManager;

public class TicketPrintingIHM extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	GamePrinterRepository gamePrinterRepository;
	// Components
    private JComboBox<GamePrinter> gameSelectionComboBox;
    private JSpinner ticketQuantitySpinner;
    private JButton printButton;
    
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
        
      List<GamePrinter> gamePrinters =   gamePrinterRepository.readAll();
      
      for (GamePrinter gamePrinter : gamePrinters) {
    	  gameSelectionComboBox.addItem(gamePrinter);
	}
       
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
        System.out.println("Printing " + quantity + " tickets for " + gamePrinter.getGame().getGameName());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicketPrintingIHM frame = new TicketPrintingIHM();
            frame.setVisible(true);
        });
    }

    // Implement the custom renderer for game selection
    class GameSelectionRenderer extends DefaultListCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof GamePrinter) {
                GamePrinter gamePrinter = (GamePrinter) value;
                setText(gamePrinter.getGame().getGameName());  // Set text as Game's name or other details
                // Set icon as Game's image if you have one
            }
            return this;
        }
    }
}
