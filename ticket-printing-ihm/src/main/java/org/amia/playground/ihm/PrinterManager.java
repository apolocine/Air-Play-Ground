package org.amia.playground.ihm;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image; 
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.amia.playground.dto.Printer;
import org.amia.playground.ihm.toobare.PrinterLister;
import org.hmd.angio.install.sgbd.DatabaseManager;

import net.coobird.thumbnailator.Thumbnails;
public class PrinterManager extends JFrame {
	private static final Logger LOGGER = Logger.getLogger(PrinterManager.class.getName());

	// Configuration du logger
	static {
	    Handler consoleHandler = new ConsoleHandler();
	    consoleHandler.setLevel(Level.ALL);
	    LOGGER.addHandler(consoleHandler);
	    LOGGER.setLevel(Level.ALL);
	}
	
    private JTable table;

    public PrinterManager() {
        setTitle("Gestion des Imprimantes");
        setSize(600, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initUI();
        Printer newPrinter = new Printer("HP Printer", "Bureau 101", "active");
        addPrinter(newPrinter);

        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Table pour lister les imprimantes
        table = new JTable();
        // Ici, vous pouvez remplir la table avec les données réelles des imprimantes

        // Barre de défilement pour la table
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panneau pour les boutons
        JPanel buttonPanel = new JPanel();

        ClassLoader classLoader = getClass().getClassLoader();
        String imagePath = "menutoolbare.png";
      		java.net.URL  image = classLoader.getResource(imagePath);
      		// Charger l'image complète
              ImageIcon  imageUrl= new ImageIcon(image);
              int decHoriizental=3;
              int decVertical=3;
              
              int widthByPhotosCount = imageUrl.getIconWidth()/5;
              BufferedImage resizedImage = null;
      	 
      			try {
					resizedImage = loadImage(image);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        ImageIcon  iconSheet = null;
		try {
			BufferedImage  iconSheet_= Thumbnails.of(resizedImage).width((int) widthByPhotosCount).keepAspectRatio(true)
					.asBufferedImage();
			iconSheet = new ImageIcon(iconSheet_);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        JButton addButton =new JButton(createIconFromSheet(iconSheet.getImage(), 0,54, 30, 30,decHoriizental,decVertical));// getButton(iconSheet.getImage(), 0,0, 30, 30,decHoriizental,decVertical);
        JButton editButton = new JButton(createIconFromSheet(iconSheet.getImage(), 0, 96, 30, 30,decHoriizental,decVertical));//getButton(iconSheet.getImage(), 0,1, 30, 30,decHoriizental,decVertical);
        JButton deleteButton =  new JButton(createIconFromSheet(iconSheet.getImage(), 0, 148, 30, 30,decHoriizental,decVertical));
        // Ajouter des boutons avec des icônes
//        JButton addButton = new JButton(new ImageIcon(getClass().getResource("/path/to/add_icon.png")));
        addButton.setToolTipText("add butto,");
        addButton.addActionListener(e -> addPrinter());
        buttonPanel.add(addButton);

//        JButton editButton = new JButton(new ImageIcon(getClass().getResource("/path/to/edit_icon.png")));
        editButton.addActionListener(e -> editPrinter());
        editButton.setToolTipText("edit  butto,");
        buttonPanel.add(editButton);

       // JButton deleteButton = new JButton(new ImageIcon(getClass().getResource("/path/to/delete_icon.png")));
        deleteButton.addActionListener(e -> deletePrinter());
        deleteButton.setToolTipText("delete butto,");
        buttonPanel.add(deleteButton);

        // Ajouter le panneau de boutons à la fenêtre
        add(buttonPanel, BorderLayout.SOUTH);
        
    }
    public void addPrinter(Printer printer) {
        try {
            String query = "INSERT INTO printers (name, location, status) VALUES (?, ?, ?)";
            PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(query);
            ps.setString(1, printer.getName());
            ps.setString(2, printer.getLocation());
            ps.setString(3, printer.getStatus());
            ps.executeUpdate();

            LOGGER.info("Imprimante ajoutée avec succès: " + printer.getName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout de l'imprimante", e);
        }
    }
    /**
  	 * 
  	 * @param file
  	 * @return
  	 * @throws IOException
  	 */
    public static BufferedImage loadImage(URL file) throws IOException {
        return ImageIO.read(file);
    }
    private void addPrinter() {
        // Créez une boîte de dialogue ou un formulaire personnalisé pour saisir les informations de l'imprimante
        JTextField nameField = new JTextField(20); 
        
        List<String> printerNames =  PrinterLister.getPrinters();
        JComboBox   printerComboBox  = new JComboBox<>(printerNames.toArray(new String[0]));

        JTextField statusField = new JTextField(20);

        JPanel panel = new JPanel( new GridLayout(3, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(Box.createHorizontalStrut(15)); // un espace
        panel.add(new JLabel("Location:"));
        panel.add(printerComboBox );
        panel.add(Box.createHorizontalStrut(15)); // un espace
        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        int result = JOptionPane.showConfirmDialog(null, panel, 
                 "Enter Printer Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Printer newPrinter = new Printer(nameField.getText(), (String) printerComboBox .getSelectedItem(), statusField.getText());
            addPrinter(  newPrinter);
            // Ici, utilisez votre méthode existante pour ajouter une imprimante à la base de données
            // Par exemple: printerDatabase.addPrinter(newPrinter);
            // Assurez-vous que cette méthode gère l'ajout de l'imprimante à votre base de données et le logging
        }
    }

    private void editPrinter() {
        // Logique pour éditer une imprimante sélectionnée
    }

    private void deletePrinter() {
        // Logique pour supprimer une imprimante sélectionnée
    }

    private ImageIcon createIconFromSheet(Image source, int x, int y, int width, int height, int decHoriizental,int decVertical) {
        BufferedImage iconImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = iconImage.createGraphics();

        // Dessiner la portion de l'image source dans la nouvelle image (icône)
        g.drawImage(source, 0, 0, width, height, x, y, x + width+decHoriizental, y + height+decVertical, null);
        g.dispose();

        return new ImageIcon(iconImage);
    }
	/**
	 * 
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PrinterManager printerManager = new PrinterManager();
            
            Printer newPrinter = new Printer("HP Printer", "Bureau 101", "active");
            printerManager.addPrinter(newPrinter);

            printerManager.setVisible(true);
        });
    }
}
