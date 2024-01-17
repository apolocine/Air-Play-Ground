package org.amia.play.ihm;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.amia.play.tools.DigitPanel;
import org.amia.playground.dao.impl.ConnectedUser;
import org.amia.playground.dao.impl.GameRepository;
import org.amia.playground.dao.impl.TicketRepository;
import org.amia.playground.dao.service.TicketService;
import org.amia.playground.dto.Game;
import org.amia.playground.dto.GamePricing;
import org.amia.playground.dto.Ticket;
import org.amia.playground.utils.BarcodeUtil;
import org.amia.playground.utils.ImageUtil;
import org.hmd.angio.install.sgbd.DatabaseManager;

public class TicketPrintingIHM extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int LWIDTH = 150;
	private static final int LHEIGHT = 150;
	private static final Logger LOGGER = Logger.getLogger(TicketPrintingIHM.class.getName());
	GameRepository gameRepository;
	TicketRepository ticketRepository ; 
	List<Ticket> tickets = new ArrayList<Ticket>(); 
	DigitPanel digitPanel= new DigitPanel(40,40);
	
	BigDecimal totalFacture= new BigDecimal(0);
	
	List<JTextField> ticketFieldCountList = new ArrayList<JTextField>();
	
	
	public TicketPrintingIHM() {
		setTitle("Ticket Printing Interface");
		setSize(800, 900); // Adjust size as needed

		// Set a background image
		setLayout(new BorderLayout());
		Random r = new Random();
        int n = r.nextInt(1,4);
		JLabel background = new JLabel(
				ImageUtil.resizeImageIcon(new ImageIcon("files/icons/backgrond0"+n+".png"), 800, 800));
		setContentPane(background);
		background.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 200)); // Adjust layout and spacing as needed

		initializeButtons();
	}

	private void initializeButtons() {

		Connection conn = DatabaseManager.getConnection();
		gameRepository = new GameRepository(conn);
		ticketRepository = new TicketRepository(conn);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3, 2, 10, 10));
		JPanel panelAll = getPanelTicketPrinting(gameRepository);
		 
		
		getContentPane().add(panelAll, BorderLayout.CENTER);
		getContentPane().add(digitPanel, BorderLayout.NORTH);
	
//           getContentPane().add(ticketPrice);

		JButton printButton = new JButton(
				chooseIcon(ImageUtil.resizeImageIcon(new ImageIcon("files/icons/imprimante01.png"), 70, 70)));  
		printButton.addActionListener(e -> printTickets(tickets));
		getContentPane().add(printButton);
		JButton printButton02 = new JButton(
				chooseIcon(ImageUtil.resizeImageIcon(new ImageIcon("files/icons/imprimante02.png"), 70, 70)));  
		printButton02.addActionListener(e -> printTickets(tickets));
		getContentPane().add(printButton02);
		
		
	}

	private void printTickets(List<Ticket> tickets2) {
		TicketService rep = new TicketService(null);
		List<Ticket> notPrintedTicket =  new ArrayList<Ticket>();
		
		for (Ticket ticket : tickets2) {
			
			
			if(rep.printTicket(ticket)) {
				// Mettre à jour la base de données pour indiquer que ce ticket a été imprimé
		        // Par exemple, enregistrer le ticket ou mettre à jour son statut
		  
//				au fure et a mesure suprimer les ticket déjà imrprimer 
//				eremetre le formulaire à zero
//				et decrementer la totalFacture
		//		tickets.remove(ticket);
			int count = 	ticketRepository.getCount();
			count++;
				  // Generate barcode
		String barcode = BarcodeUtil.generateBarcode( ""+count);
		ticket.setBarcode(barcode);
		ConnectedUser.getInstance();
		int userID = ConnectedUser.getUser().getUserID();
		ticket.setValidDate(LocalDateTime.now());
		ticket.setUserID(userID);
				
				ticketRepository.create(ticket);
			}else {
				notPrintedTicket.add(ticket);
			}
		}
		if (notPrintedTicket.isEmpty()) {
			// remise à zero les formulaire
			tickets2.clear();
			remiseaZeroFormul();

		} else {
						
				
			 // Affichez un message de confirmation
			   int result = JOptionPane.showConfirmDialog(
	                    null,
	                    "All of the Tickets was not printed. Do you want to reset form?",
	                    "Reset Forms",
	                    JOptionPane.YES_NO_OPTION);

				if (result == JOptionPane.YES_OPTION) {
					LOGGER.info("Remise à zero du formulaire");

					for (Ticket ticket : notPrintedTicket) {
						ticketRepository.delete(ticket.getTicketId());
					}
					notPrintedTicket.clear();
					remiseaZeroFormul();
				} else if (result == JOptionPane.NO_OPTION) {
					LOGGER.info("Pas de remise à zero du formulaire");
				}  
		}
		
		


	}

	
	private void remiseaZeroFormul() {
		tickets.clear();
		totalFacture=new BigDecimal(0);		
		digitPanel.displayNumber("" + totalFacture );
		
		for (JTextField jTextField : ticketFieldCountList) {
			jTextField.setText("0");
		}
	}

	public JPanel getPanelTicketPrinting(GameRepository gameRepository) {

		JPanel panelAll = new JPanel();

 
		List<Game> gameList = gameRepository.readAll();
		for (Game game : gameList) {

			// Create the ticket increment/decrement buttons and labels for each game
//        for (int i = 1; i <= gameList.size(); i++) {
			JPanel panel = new JPanel(new BorderLayout());
			JButton incrementButton = new JButton(" + ", chooseIcon(game)); // Use gameIcon1, gameIcon2, or gameIcon3
																			// depending on i
			JButton decrementButton = new JButton(" - ");
			
//			DigitPanel digitPanel= new DigitPanel(30,30,150,60);
			
			JTextField ticketCount = new JTextField("0", 15);
			ticketCount.setEditable(false);
			
			//metre toutes les JTextField dans une liste globale pour les controlé.
			ticketFieldCountList.add(ticketCount);
			
 
			ticketCount.setHorizontalAlignment(JTextField.CENTER);

			incrementButton.addActionListener(e -> incrementTicketCount(game, ticketCount));
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
	private void incrementTicketCount(Game game, JTextField ticketCountField) {

		BigDecimal price = null;
		try {
			price = gameRepository.getCurrentPriceForGame(game.getGameID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOGGER.info(""+price);
		
		if (price == null) {
			JOptionPane.showMessageDialog(this, "le prix du jeux " + game.getGameName() + " n'est pas fixé ou la date est dépassée.",
					"Erreur de prix Verifiez la date", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int count = Integer.parseInt(ticketCountField.getText());
		count++;
		ticketCountField.setText(String.valueOf(count));

		GamePricing gamePricing = gameRepository.getGamePricingByGameAndCurrentDate(game.getGameID()).getFirst();
		
		Ticket ticket = new Ticket(game.getGameID());
		ticket.setBarcode("" + ticket.getTicketId());
		ticket.setGamePricingID(gamePricing.getPricingId());
		
		tickets.add(ticket);
		
		
		//BigDecimal pricold = BigDecimal.valueOf(Double.valueOf(ticketPrice.getText()));
		totalFacture =totalFacture.add(price);
		digitPanel.displayNumber("" + totalFacture );
		LOGGER.info(" totalFacture "+totalFacture);
	}

	/**
	 * 
	 * @param ticketCountField
	 */
	private void decrementTicketCount(Game game, JTextField ticketCountField) {
		int count = Integer.parseInt(ticketCountField.getText());
		if (count > 0)
			count--;
		ticketCountField.setText(String.valueOf(count));
		BigDecimal price = gameRepository.getCurrentPriceForGame(game.getGameID());
		Ticket ticket = new Ticket(game.getGameID());
		ticket.setBarcode("" + ticket.getTicketId());
		//Si le tiket existe dans la liste nle retir 
		//
		for (Ticket ticketed : tickets) {
			if (ticket.equals(ticketed)) {
				tickets.remove(ticket);				 
				totalFacture =totalFacture.subtract(price);
				digitPanel.displayNumber("" + totalFacture );
				LOGGER.info(" totalFacture "+totalFacture);				 
				return;
			}
		}

	}

	private Icon chooseIcon(ImageIcon resizeImageIcon) {
		ImageIcon icon = ImageUtil.resizeImageIcon(resizeImageIcon, LWIDTH, LHEIGHT);
		return icon;
	}

	private Icon chooseIcon(Game game) {
		return chooseIcon(game.getGameImage());
	}

	private Icon chooseIcon(byte[] gameImag) {
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
