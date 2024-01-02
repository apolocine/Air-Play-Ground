package org.amia.playground.ihm;
import java.awt.BorderLayout;
import java.awt.GridLayout; 
import java.sql.Connection;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.amia.playground.dao.impl.PrinterRepository;
import org.amia.playground.dto.Printer;
import org.hmd.angio.install.sgbd.DatabaseManager;

public class CRUDApplication {
	
	Connection con = DatabaseManager.getConnection();
	
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CRUDApplication::new);
    }

    public CRUDApplication() {
        // Create the main frame
        JFrame frame = new JFrame("CRUD Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Set the layout and add components
        frame.setLayout(new BorderLayout());
        frame.add(createMainPanel(), BorderLayout.CENTER);

        // Display the window.
        frame.setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));  // Adjust grid layout as needed

     // Assume you have a method getPrinters() that fetches printer data from the database
        List<Printer> printers = new PrinterRepository (con). readAll();  // This would be a call to your PrinterRepository or similar

        // Convert your data to a format suitable for JList (e.g., Vector or an array)
        Vector<String> printerNames = new Vector<>();
        for (Printer printer : printers) {
            printerNames.add(printer.getName());  // Assuming you want to display printer names
        }

         // Example of adding a JList for Printers (repeat for Games, Users, and Roles)
       // Now create a JList with this data
        JList<String> printerList = new JList<>(printerNames);
         JScrollPane printerScrollPane = new JScrollPane(printerList);
        panel.add(printerScrollPane);

        // Add other lists (Games, Users, Roles) similarly...

        return panel;
    }
    
    private JPanel createPrinterAddForm() {
        JPanel form = new JPanel(new GridLayout(0, 2));

        // Labels and fields
        form.add(new JLabel("Printer Name:"));
        JTextField printerNameField = new JTextField(20);
        form.add(printerNameField);

        // Add button
        JButton addButton = new JButton("Add Printer");
        addButton.addActionListener(e -> {
            String printerName = printerNameField.getText();
            
            Printer printer = new Printer( );
            printer. setName(printerName);
            printer.setActive(true);
            printer.setLocation("Location");
            
            new PrinterRepository (con).create(printer);  // Implement addPrinter to interact with the database
    
        });
        form.add(addButton);

        return form;
    }
    private void refreshPrinterList(JList<String> printerList) {
        List<Printer> printers = new PrinterRepository (con). readAll();   // Re-fetch the data
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Printer printer : printers) {
            model.addElement(printer.getName());  // Update the model
        }
        printerList.setModel(model);  // Set the new model
    }

    
    
}
