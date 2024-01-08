package org.amia.play.ihm.game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.amia.playground.dao.impl.GameRepository;
import org.amia.playground.dto.Game;
import org.amia.playground.utils.ImageUtil;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameForm extends JPanel {
	private final GameRepository gameRepository;
	private static final Logger LOGGER = Logger.getLogger(GameForm.class.getName()); 

	// Form components
	private JTextField gameNameField;
	private JComboBox<String> ageRestrictionField;

	// Table components
	private JTable gameTable;
	private GameTableModel tableModel;

	// Constructor and initialization
		private JLabel gameImageLabel; // For displaying the game image
		private byte[] gameImage; // Class variable to hold the image data
		
		// Constructor and initialization
		private JLabel gameLogoImageLabel; // For displaying the game image
		private byte[] gameLogoImage; // Class variable to hold the image data
	
	public GameForm(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
		setLayout(new BorderLayout());
		add(createInputPanel(), BorderLayout.NORTH);
		add(createTablePanel(), BorderLayout.CENTER);
	}

	private JPanel createInputPanel() {
		JPanel inputPanel = new JPanel(new GridLayout(0, 2));

		inputPanel.add(new JLabel("Game Name:"));
		gameNameField = new JTextField(20);
		inputPanel.add(gameNameField);

		inputPanel.add(new JLabel("Age Restriction:"));
		ageRestrictionField = new JComboBox<>(new String[] { "minor", "teen", "adult" });
		inputPanel.add(ageRestrictionField);

		
		
		gameImageLabel = new JLabel();
		inputPanel.add(gameImageLabel);

		JButton selectGameImageButton = new JButton("Select Image");
		selectGameImageButton.addActionListener(this:: selectImageGameImage);
		inputPanel.add(selectGameImageButton);
		
		
		
		gameLogoImageLabel = new JLabel();
		inputPanel.add(gameLogoImageLabel);

		JButton selectGameLogoImageButton = new JButton("Select Image");
		selectGameLogoImageButton.addActionListener(this:: selectImageLogoGameImage);
		inputPanel.add(selectGameLogoImageButton);
		
		
		
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this::deleteSelectedGame);
		inputPanel.add(deleteButton);// , BorderLayout.WEST

		JButton addButton = new JButton("Add");
		addButton.addActionListener(this::saveGame);
		inputPanel.add(addButton);

		return inputPanel;
	}

	

	private JScrollPane createTablePanel() {
		tableModel = new GameTableModel();
		gameTable = new JTable(tableModel);
		loadGameData();

		// Add mouse listener for double-click event
		gameTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = gameTable.getSelectedRow();
					if (row >= 0) {
						Game game = tableModel.getGameAt(row);
						updateGameForm(game);
					}
				}
			}
		});

		// Panel wrapping the table and delete button
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(new JScrollPane(gameTable), BorderLayout.CENTER);

		return new JScrollPane(tablePanel);
	}

	private void saveGame(ActionEvent event) {
		try {
			Game game = new Game();
			// Assuming game ID is set automatically or not needed for new creation
			game.setGameName(gameNameField.getText());
			game.setAgeRestriction((String) ageRestrictionField.getSelectedItem());
			game.setGameImage(gameImage);
			game.setLogoImage(gameLogoImage);
			Game savedGame = gameRepository.create(game);
			LOGGER.info("Saved game with ID: " + savedGame.getGameID());
		} catch (Exception e) {
			LOGGER.severe("Error saving game: " + e.getMessage());
			JOptionPane.showMessageDialog(this, "Error saving game: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		loadGameData();
	}

	private void selectImageGameImage(ActionEvent event) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));

		int option = fileChooser.showOpenDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				gameImage = Files.readAllBytes(selectedFile.toPath());

				ImageIcon icon = ImageUtil.resizeImageIcon(new ImageIcon(gameImage), 40, 40);
				// Optionally, update an image preview
				gameImageLabel.setIcon(icon);

			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Error reading selected image file", e);
				JOptionPane.showMessageDialog(this, "Error selecting image: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				gameImage = null; // Reset or handle the error as appropriate
			}
		}
	}
	
	private void selectImageLogoGameImage(ActionEvent event) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));

		int option = fileChooser.showOpenDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				gameLogoImage = Files.readAllBytes(selectedFile.toPath());

				ImageIcon icon = ImageUtil.resizeImageIcon(new ImageIcon(gameLogoImage), 40, 40);
				// Optionally, update an image preview
				gameLogoImageLabel.setIcon(icon);

			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Error reading selected image file", e);
				JOptionPane.showMessageDialog(this, "Error selecting image: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				gameLogoImage = null; // Reset or handle the error as appropriate
			}
		}
	}
	
	
 
	// Method to load game data into table
	private void loadGameData() {
		List<Game> games = gameRepository.readAll();
		tableModel.setGames(games);
	}

	// Method to delete the selected game
	private void deleteSelectedGame(ActionEvent event) {
		int row = gameTable.getSelectedRow();
		if (row >= 0) {
			Game game = tableModel.getGameAt(row);
			if (gameRepository.delete(game.getGameID())) {
				tableModel.removeGame(row);
			} else {
				LOGGER.log(Level.SEVERE, "Unable to delete game");
			}
		}
	}

	// Method to populate form for updating a game
	private void updateGameForm(Game game) {
		gameNameField.setText(game.getGameName());
		ageRestrictionField.setSelectedItem(game.getAgeRestriction());
		// Potentially a "Save" button to handle updating the existing game

	}
}
