package org.amia.play.ihm.gamepricing;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
 
import org.amia.playground.dao.impl.GamePricingRepository;
import org.amia.playground.dao.impl.GameRepository;
import org.amia.playground.dto.Game;
import org.amia.playground.dto.GamePricing;
import org.hmd.angio.install.sgbd.DatabaseManager;
public class GamePricingForm extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Components
    private JComboBox<Game> gameComboBox;
    private JTextField priceField;
    private JSpinner validFromSpinner, validToSpinner;
    private JButton saveButton;
    private JTable pricingTable;
    GamePricingRepository gamePricingRepository;
    GameRepository gameRepository;
    public GamePricingForm( GamePricingRepository gamePricingRepository,GameRepository gameRepository) {
        setLayout(new BorderLayout());

        this. gameRepository=gameRepository;
        
        this. gamePricingRepository=gamePricingRepository;
        
        List<Game> games = gameRepository.readAll ();
        
        // Initialize the upper form area
        JPanel formPanel = new JPanel(new GridLayout(0, 2)); // Adjust layout as needed
        gameComboBox = new JComboBox<>(new DefaultComboBoxModel<>(games.toArray(new Game[0]))); 
        gameComboBox.setRenderer(new GameRenderer());
        priceField = new JTextField();
        validFromSpinner = new JSpinner(new SpinnerDateModel());
        validToSpinner = new JSpinner(new SpinnerDateModel());
        saveButton = new JButton("Save Pricing");

        // Add components to the form
        formPanel.add(new JLabel("Select Game:"));
        formPanel.add(gameComboBox);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Valid From:"));
        formPanel.add(validFromSpinner);
        formPanel.add(new JLabel("Valid To:"));
        formPanel.add(validToSpinner);
 
        // Set up the bottom area (possibly for listing existing pricings)
        // JPanel listingPanel = ...

        // Add action to save button
        saveButton.addActionListener(e -> savePricing());

        add(formPanel, BorderLayout.NORTH);
        add(saveButton, BorderLayout.SOUTH); // or add to formPanel as needed
        // add(listingPanel, BorderLayout.CENTER); // If you're listing existing prices
        
        
        List<GamePricing> pricings = gamePricingRepository.getAllPricing();
		createPricingTable(pricings);
        
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
            // Appel au repository pour sauvegarder l'objet GamePricing
            gamePricingRepository.addGamePricing(pricing); // Ou updateGamePricing si c'est une mise à jour

            // Mise à jour du tableau (si nécessaire) et notifications utilisateur
            // ...

        } catch (NumberFormatException e) {
            // Gérer l'erreur de format de prix et notifier l'utilisateur
        } catch (Exception e) {
            // Gérer toute autre exception et notifier l'utilisateur
        }
    }

    
    private void createPricingTable(List<GamePricing> pricings) {
        GamePricingTableModel model = new GamePricingTableModel(pricings);
        pricingTable = new JTable(model);
        
        // Gestion du double clic sur une ligne de la table
        pricingTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
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
    
    // Main method for demonstration purposes
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
          
            JFrame frame = new JFrame("Game Pricing");
           
            Connection con =DatabaseManager.getConnection();
            frame.add(new GamePricingForm(new GamePricingRepository(con),new GameRepository(con) ));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
    
    
    	private void fillFormWithPricing(GamePricing selectedPricing) {
				// TODO Auto-generated method stub
    		Game game = gameRepository.read(selectedPricing.getGameID());
    		 gameComboBox.setSelectedItem(game);;
    		  priceField.setText(""+selectedPricing.getPrice());
    		    validFromSpinner.setValue(selectedPricing.getValidFrom()); 
    		    validToSpinner.setValue(selectedPricing.getValidTo()); 
			}
  

    private   List<Game> fetchGames() {
        // Fetch games from the database or service
        return gameRepository.readAll();//List.of(new Game( ), new Game( )); // Placeholder
    }
    
}