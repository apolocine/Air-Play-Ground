package org.amia.play.ihm.gamepricing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;

import org.amia.play.ihm.game.GameForm;
import org.amia.play.ihm.gameprinter.GamePrinterTableModel;
import org.amia.play.ihm.gameprinter.ImageRenderer;
import org.amia.play.ihm.render.ApplicationCompRenderer;
import org.amia.playground.dao.impl.GamePricingRepository;
import org.amia.playground.dao.impl.GameRepository;
import org.amia.playground.dto.Game;
import org.amia.playground.dto.GamePricing;
import org.amia.playground.dto.GamePrinter;
import org.amia.playground.dto.Printer;
import org.hmd.angio.install.sgbd.DatabaseManager;

public class GamePricingForm2 extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(GamePricingForm2.class.getName()); 
	// Components
	private JComboBox<Game> gameComboBox;
	private List<Game> availableGames; // List of all available games
	
	private JTextField pricingldField;
	private JTextField priceField;
	private JSpinner validFromSpinner, validToSpinner;
	
	private JTable pricingTable;
	GamePricingRepository gamePricingRepository;
	GameRepository gameRepository; 
	
	// Table components
	private JTable gamePricingTable;
	
	GamePricingTableModel tableModel;
	 
	
	
	
	public GamePricingForm2(GamePricingRepository gamePricingRepository, GameRepository gameRepository) {
		setLayout(new BorderLayout());

		this.gameRepository = gameRepository;

		this.gamePricingRepository = gamePricingRepository;

		tableModel = new GamePricingTableModel();

		gamePricingTable = new JTable(tableModel);
		

	 	// add(listingPanel, BorderLayout.CENTER); // If you're listing existing prices

//		List<GamePricing> pricings = gamePricingRepository.readAll();
//		createPricingTable(pricings);

		
		
		setLayout(new BorderLayout());
		add(createInputPanel(), BorderLayout.NORTH);
		add(createTablePanel(), BorderLayout.CENTER);
		
		
	}
	

	private JScrollPane createTablePanel() {
		 
	
		 
		loadGamePrricingData();
		loadAvailableGames(); 

		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(new JScrollPane(gamePricingTable), BorderLayout.CENTER);

		return new JScrollPane(tablePanel);
	}

	private void reload(ActionEvent event) {
		loadGamePrricingData();
		loadAvailableGames(); 
	}
	private void loadGamePrricingData() {
		try {
			List<GamePricing> gamePrinters = gamePricingRepository.readAll();
			tableModel.setPricings(gamePrinters);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error loading game-printer associations: " + e.getMessage(), e);
			JOptionPane.showMessageDialog(this, "Error loading game-printer associations: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void loadAvailableGames() {
		try {
			gameComboBox.removeAllItems();
			availableGames = gameRepository.readAll();  //  .readAllGamesWithoutPrinter(); Fetch all games WithoutPrinter
			for (Game game : availableGames) {
				gameComboBox.addItem(game); // Assumes Game has a meaningful toString implementation
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error loading games: " + e.getMessage(), e);
			JOptionPane.showMessageDialog(this, "Error loading games: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

 
	
	
	


	private JPanel createInputPanel() {
		
		
	 
		List<Game> games = gameRepository.readAll();
 
				// Initialize the upper form area
				JPanel inputPanel = new JPanel(new GridLayout(0, 2)); // Adjust layout as needed
				gameComboBox = new JComboBox<>(new DefaultComboBoxModel<>(games.toArray(new Game[0])));
				gameComboBox.setRenderer(new ApplicationCompRenderer());
				pricingldField = new JTextField();
				pricingldField.setEditable(false);
				priceField = new JTextField();
				validFromSpinner = new JSpinner(new SpinnerDateModel());
				validToSpinner = new JSpinner(new SpinnerDateModel());
				 JButton saveButton = new JButton("Save Pricing");

				
				
				
				// Add components to the form
				inputPanel.add(new JLabel("Select Game:"));
				inputPanel.add(gameComboBox);

				inputPanel.add(new JLabel("Pricing ld:"));
				inputPanel.add(pricingldField);

				inputPanel.add(new JLabel("Price:"));
				inputPanel.add(priceField);
				inputPanel.add(new JLabel("Valid From:"));
				inputPanel.add(validFromSpinner);
				inputPanel.add(new JLabel("Valid To:"));
				inputPanel.add(validToSpinner);

				// Set up the bottom area (possibly for listing existing pricings)
				// JPanel listingPanel = ...

				// Add action to save button
				saveButton.addActionListener(e -> savePricing());
				inputPanel.add(new JLabel("Save :"));
				inputPanel.add(saveButton);
			
				return inputPanel;
		
	}
	
	
 
	
	
	private void createPricingTable(List<GamePricing> pricings) {
		tableModel = new GamePricingTableModel(pricings);
		pricingTable = new JTable(tableModel);

		// Gestion du double clic sur une ligne de la table
		pricingTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int row = table.rowAtPoint(point);
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					// Double-clic détecté
					GamePricing selectedPricing = pricings.get(row);
					fillFormWithPricing(selectedPricing); // Remplir le formulaire avec les détails du prix sélectionné
				}
			}

		});

		add(new JScrollPane(pricingTable), BorderLayout.CENTER); // Ajoutez le JTable avec ScrollPane pour la navigation
	}
	private void savePricing() {
		try {

			// Récupération du jeu sélectionné
			Game selectedGame = (Game) gameComboBox.getSelectedItem();
			int gameId = selectedGame.getGameID(); // Assurez-vous que Game a un getter pour gameId

			// Récupération et validation du prix
			BigDecimal price = new BigDecimal(priceField.getText()); // Gérer NumberFormatException

			// Récupération des dates de validité
			Date validFrom = ((SpinnerDateModel) validFromSpinner.getModel()).getDate();
			Date validTo = ((SpinnerDateModel) validToSpinner.getModel()).getDate();

			// Conversion des dates en LocalDateTime ou en un autre format approprié
			LocalDateTime validFromLDT = validFrom.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			LocalDateTime validToLDT = validTo.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			// Création ou mise à jour de l'objet GamePricing
			GamePricing pricing = new GamePricing(gameId, price, validFromLDT, validToLDT);

			if (!pricingldField.getText().isEmpty()) {
				
				try {
					
					pricing.setPricingId(Integer.parseInt(pricingldField.getText()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				gamePricingRepository.updateGamePricing(pricing); // Ou updateGamePricing si c'est une mise à jour
			} else {
				// Appel au repository pour sauvegarder l'objet GamePricing
				gamePricingRepository.addGamePricing(pricing); // Ou updateGamePricing si c'est une mise à jour

			}
			
			initGamePricingForm();

			initTablePricingForm();
			
			// Mise à jour du tableau (si nécessaire) et notifications utilisateur
			// ...

		} catch (NumberFormatException e) {
			// Gérer l'erreur de format de prix et notifier l'utilisateur
		} catch (Exception e) {
			// Gérer toute autre exception et notifier l'utilisateur
		}
		
		
		 reload(null);
	}



	// Main method for demonstration purposes
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {

			JFrame frame = new JFrame("Game Pricing");

			Connection con = DatabaseManager.getConnection();
			frame.add(new GamePricingForm2(new GamePricingRepository(con), new GameRepository(con)));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		});
	}

	private void fillFormWithPricing(GamePricing selectedPricing) {
		// TODO Auto-generated method stub
	//	Game game = gameRepository.read(selectedPricing.getGameID());

		 // Assurez-vous que chaque composant (gameComboBox, priceField, etc.) est déjà initialisé

	    // Sélectionnez le bon jeu dans gameComboBox
	    for (int i = 0; i < gameComboBox.getItemCount(); i++) {
	        if (gameComboBox.getItemAt(i).getGameID() == selectedPricing.getGameID()) {
	            gameComboBox.setSelectedIndex(i);
	            break;
	        }
	    }
	    
	    
	    pricingldField.setText("" + selectedPricing.getPricingId());
	    // Définir le prix dans priceField
	    priceField.setText(selectedPricing.getPrice().toString());

	    // Définir la date 'ValidFrom' dans validFromSpinner
	    validFromSpinner.setValue(Date.from(selectedPricing.getValidFrom().atZone(ZoneId.systemDefault()).toInstant()));

	    // Définir la date 'ValidTo' dans validToSpinner
	    validToSpinner.setValue(Date.from(selectedPricing.getValidTo().atZone(ZoneId.systemDefault()).toInstant()));

	    // Vous pouvez également mettre à jour tout autre champ ou état lié au formulaire ici

	      
	}

	void initGamePricingForm(){
		 gameComboBox.setSelectedIndex(0);
		 
		 pricingldField.setText("" );
		    // Définir le prix dans priceField
		    priceField.setText("");

		    LocalDateTime currentDate = LocalDateTime.now(); 

	        // Calculez une date qui est 8 mois plus tard
	        LocalDateTime eightMonthsLater = currentDate.plus(8, ChronoUnit.MONTHS);
	        
		    // Définir la date 'ValidFrom' dans validFromSpinner
		    validFromSpinner.setValue(  Date .from(currentDate.atZone(ZoneId.systemDefault()).toInstant() ) );

		    // Définir la date 'ValidTo' dans validToSpinner
		    validToSpinner.setValue(Date .from(eightMonthsLater.atZone(ZoneId.systemDefault()).toInstant() ));
		    
		    
		    
		   
		    

	}
	
	void initTablePricingForm(){
		 List<GamePricing> pricings = gamePricingRepository.readAll();
		 
		 GamePricingTableModel model = new GamePricingTableModel(pricings);
		 
		 pricingTable.setModel(model);
			pricingTable.updateUI();
			
			
			
	}
	
	
	
	private List<Game> fetchames() {
		// Fetch games from the database or service
		return gameRepository.readAll();// List.of(new Game( ), new Game( )); // Placeholder
	}

}