package org.amia.play.ihm.gameprinter;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.amia.playground.dao.impl.GamePrinterRepository;
import org.amia.playground.dto.Game;
import org.amia.playground.dto.GamePrinter;
import org.amia.playground.dto.Printer;

public class GamePrinterForm extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final GamePrinterRepository gamePrinterRepository; // Adapt to your GamePrinterRepository
    private static final Logger LOGGER = Logger.getLogger(GamePrinterForm.class.getName());

    // Form components
    private JTextField nameField;
    private JComboBox<Game> gameComboBox;
    private JComboBox<Printer> printerComboBox;
    private JLabel imageLabel;

    private byte[] gameImage; // Class variable to hold the image data

    // Table components
    private JTable gamePrinterTable;
    private GamePrinterTableModel tableModel;

    public GamePrinterForm(GamePrinterRepository gamePrinterRepository) {
        this.gamePrinterRepository = gamePrinterRepository;
        setLayout(new BorderLayout());
        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Game:"));
        gameComboBox = new JComboBox<>(/* Populate with available games */);
        inputPanel.add(gameComboBox);

        inputPanel.add(new JLabel("Printer:"));
        printerComboBox = new JComboBox<>(/* Populate with available printers */);
        inputPanel.add(printerComboBox);

        JButton xButton = new JButton("X");
        inputPanel.add(xButton);
        
          JButton selectImageButton= new JButton("Select Image");
        selectImageButton.addActionListener(this::selectImage);
        inputPanel.add(selectImageButton);
        
        
        // Consider adding a component to handle image upload or selection
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this::deleteSelectedGamePrinter);
        inputPanel.add(deleteButton, BorderLayout.WEST);
        
        
      
        
        JButton addButton = new JButton("Add");
        addButton.addActionListener(this::addGamePrinter);
        inputPanel.add(addButton);

        return inputPanel;
    }
 
    private void selectImage(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));

        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                gameImage = Files.readAllBytes(selectedFile.toPath());
                // Optionally, update an image preview
                imageLabel.setIcon(new ImageIcon(gameImage));
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error reading selected image file", e);
                JOptionPane.showMessageDialog(this, "Error selecting image: " + e.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
                gameImage = null; // Reset or handle the error as appropriate
            }
        }
    }

    private JScrollPane createTablePanel() {
        tableModel = new GamePrinterTableModel();
        gamePrinterTable = new JTable(tableModel);
        loadGamePrinterData();

      
        
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(gamePrinterTable), BorderLayout.CENTER);
       

        return new JScrollPane(tablePanel);
    }

    // Implement addGamePrinter, loadGamePrinterData, and deleteSelectedGamePrinter
    
    private void addGamePrinter(ActionEvent event) {
        try {
            Game selectedGame = (Game) gameComboBox.getSelectedItem();
            Printer selectedPrinter = (Printer) printerComboBox.getSelectedItem();
            String name = nameField.getText();
            // byte[] gameImage = ... // Handle image selection or upload

            if (selectedGame != null && selectedPrinter != null && !name.isEmpty()) {
                GamePrinter gamePrinter = new GamePrinter();
                gamePrinter.setName(name);
                gamePrinter.setGame(selectedGame);
                gamePrinter.setPrinter(selectedPrinter);
                gamePrinter.setGameImage(gameImage);

                GamePrinter createdGamePrinter = gamePrinterRepository.create(gamePrinter);
                if (createdGamePrinter != null) {
                    tableModel.addGamePrinter(createdGamePrinter);
                    LOGGER.info("Game-Printer association added successfully.");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding game-printer association: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Error adding game-printer association: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGamePrinterData() {
        try {
            List<GamePrinter> gamePrinters = gamePrinterRepository.readAll();
            tableModel.setGamePrinters(gamePrinters);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading game-printer associations: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Error loading game-printer associations: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void deleteSelectedGamePrinter(ActionEvent event) {
        int row = gamePrinterTable.getSelectedRow();
        if (row >= 0) {
            try {
                GamePrinter gamePrinter = tableModel.getGamePrinterAt(row);
                boolean deleted = gamePrinterRepository.delete(gamePrinter.getGamePrinterId());
                if (deleted) {
                    tableModel.removeGamePrinter(row);
                    LOGGER.info("Game-Printer association deleted successfully.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error deleting game-printer association: " + e.getMessage(), e);
                JOptionPane.showMessageDialog(this, "Error deleting game-printer association: " + e.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Ensure methods handle the game and printer associations and game images
}
