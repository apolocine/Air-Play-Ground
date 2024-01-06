package org.amia.play.ihm;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.amia.playground.dao.impl.GameRepository;
import org.amia.playground.dao.impl.TicketRepository;
import org.amia.playground.dao.service.TicketService;
import org.amia.playground.dto.Game;
import org.amia.playground.dto.Ticket;
import org.amia.playground.utils.ImageUtil;
import org.hmd.angio.ApplicationGUI;
import org.hmd.angio.install.sgbd.DatabaseManager;

public class TicketPrintingIHM extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int LWIDTH = 150;
	private static final int LHEIGHT = 150;

	  GameRepository gameRepository;
	  List<Ticket> tickets = new ArrayList<Ticket>(); 
		 JTextField  ticketPrice = new JTextField("0", 15);
	public TicketPrintingIHM() {
        setTitle("Ticket Printing Interface");
        setSize(800, 600);  // Adjust size as needed

        // Set a background image
        setLayout(new BorderLayout());
        
      
        JLabel background = new JLabel(ImageUtil.resizeImageIcon(new ImageIcon("files/icons/backgrond02.png"), 800, 800));
        setContentPane(background);
        background.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 200)); // Adjust layout and spacing as needed

        initializeButtons();
    }

    private void initializeButtons() {
    	
    	 Connection conn = DatabaseManager.getConnection();
		gameRepository = new GameRepository(conn );
    	
		
		  JPanel mainPanel = new JPanel();
	        mainPanel.setLayout(new GridLayout(6, 2, 10, 10));
		 JPanel panelAll =  getPanelTicketPrinting(gameRepository);
     
           getContentPane().add(panelAll);
        
           
          
           
           getContentPane().add(ticketPrice);
           
           JButton printButton = new JButton( chooseIcon(ImageUtil.resizeImageIcon(new ImageIcon("files/icons/imprimante01.png"), 70, 70)));  // Use gameIcon1, gameIcon2, or gameIcon3 depending on i
           printButton.addActionListener(e -> printTickets(tickets));
           getContentPane().add(printButton);   
           JButton printButton02 = new JButton( chooseIcon(ImageUtil.resizeImageIcon(new ImageIcon("files/icons/imprimante02.png"), 70, 70)));  // Use gameIcon1, gameIcon2, or gameIcon3 depending on i
           printButton02.addActionListener(e -> printTickets(tickets));
           getContentPane().add(printButton02);
           
           
    }

	

	private void printTickets(List<Ticket> tickets2) {
		TicketService rep= new TicketService(null); 
		for (Ticket ticket : tickets2) {
			rep.printTicket(ticket);
		}

	}

	public   JPanel   getPanelTicketPrinting(GameRepository gameRepository ) {
		
	
		 JPanel panelAll = new JPanel();
				 
		// Assuming you have icons for each game
//        Icon gameIcon1 = new ImageIcon("path/to/game1/icon.jpg");
//        Icon gameIcon2 = new ImageIcon("path/to/game2/icon.jpg");
//        Icon gameIcon3 = new ImageIcon("path/to/game3/icon.jpg");

        List<Game> gameList = gameRepository.readAll();
        for (Game game : gameList) {
		 
        // Create the ticket increment/decrement buttons and labels for each game
//        for (int i = 1; i <= gameList.size(); i++) {
            JPanel panel = new JPanel(new BorderLayout());
            JButton incrementButton = new JButton(" + ", chooseIcon(game));  // Use gameIcon1, gameIcon2, or gameIcon3 depending on i
            JButton decrementButton = new JButton(" - ");
            JTextField ticketCount = new JTextField("0", 15);
//            ticketCount.setFont(new Font(getName(), LWIDTH, LHEIGHT));
            ticketCount.setHorizontalAlignment(JTextField.CENTER);

            incrementButton.addActionListener(e -> incrementTicketCount(game,ticketCount));
            decrementButton.addActionListener(e -> decrementTicketCount(game, ticketCount));

            panel.add(incrementButton, BorderLayout.NORTH);
            panel.add(ticketCount, BorderLayout.CENTER);
            panel.add(decrementButton, BorderLayout.SOUTH);

            panelAll.add(panel); 
        } 
        return panelAll;
	}

	/**
	 * 
	 * @param ticketCountField
	 */
    private   void incrementTicketCount(Game game,JTextField ticketCountField) {
     BigDecimal price = gameRepository.getCurrentPriceForGame(game.getGameID());
        System.out.println(price);
         if(price==null) {
        	JOptionPane.showMessageDialog( this,
					"le prix du jeux "+game.getGameName()+" n'est pas fixé",
					"Erreur de prix", JOptionPane.ERROR_MESSAGE); 
        	return;
        }  
         
         int count = Integer.parseInt(ticketCountField.getText());
        count++;
        ticketCountField.setText(String.valueOf(count));
       
        Ticket ticket =  new Ticket(game.getGameID());
        ticket.setBarcode(""+ticket.getTicketId());
        tickets.add(ticket); 
        BigDecimal pricold =BigDecimal.valueOf(Double.valueOf(ticketPrice.getText()));
         ticketPrice.setText(""+(pricold.add(price)));
       
       
        
        
    }
/**
 * 
 * @param ticketCountField
 */
    private   void decrementTicketCount(Game game,JTextField ticketCountField) {
        int count = Integer.parseInt(ticketCountField.getText());
        if (count > 0) count--;
        ticketCountField.setText(String.valueOf(count));
        BigDecimal price = gameRepository.getCurrentPriceForGame(game.getGameID());
        Ticket ticket =  new Ticket(game.getGameID());
        ticket.setBarcode(""+ticket.getTicketId());
        for (Ticket ticketed : tickets) {
        	if(ticket.equals(ticketed)) {
        		 tickets.remove(ticket);
        		 BigDecimal pricold =BigDecimal.valueOf(Double.valueOf(ticketPrice.getText()));
        	        
        	        ticketPrice.setText(""+(pricold.subtract(price)));
        		 
        		 return;
        	}
			
		}
       
        
    }

    
    private Icon chooseIcon(ImageIcon resizeImageIcon) {
    	ImageIcon icon = ImageUtil.resizeImageIcon(resizeImageIcon, LWIDTH, LHEIGHT);
    	
        return icon;
	}
    
    
    private   Icon chooseIcon(Game game) {
       	
        return chooseIcon(game.getGameImage());
    }
    
    private   Icon chooseIcon(byte[]  gameImag) {
        // Logic to return gameIcon1, gameIcon2, or gameIcon3 based on gameNumber
        // This is a placeholder. Replace with your actual icon choosing logic
    	ImageIcon icon = ImageUtil.resizeImageIcon(new ImageIcon(gameImag), LWIDTH, LHEIGHT);
    	    	
        return icon;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicketPrintingIHM frame = new TicketPrintingIHM();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
