package org.amia.play.ihm.printer;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.amia.playground.dao.impl.PrinterRepository;
import org.amia.playground.dto.Printer;

public class PrinterForm extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final PrinterRepository printerRepository; // Adapt to your PrinterRepository
    private static final Logger LOGGER = Logger.getLogger(PrinterForm.class.getName());

    // Form components
 
    List<String> printerNames ;
    JComboBox<String>   printerComboBox ;
    
//      printerNames =  org.amia.play.tools.PrinterLister.getPrinters();
//       printerComboBox  = new JComboBox<>(printerNames.toArray(new String[0]));
    
    
    private JTextField locationField;
    private JTextField statusField;
    private JTextArea descriptionArea;
    private JCheckBox isActiveCheckBox;

    // Table components
    private JTable printerTable;
    private PrinterTableModel tableModel;

    public PrinterForm(PrinterRepository printerRepository) {
        this.printerRepository = printerRepository;
        setLayout(new BorderLayout());
        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));

        inputPanel.add(new JLabel("Name:"));
    
      printerNames =  org.amia.play.tools.PrinterLister.getPrintersNotInDB();
      
      
      printerComboBox  = new JComboBox<>(printerNames.toArray(new String[0]));
      
      
        inputPanel.add(printerComboBox);

        inputPanel.add(new JLabel("Location:"));
        locationField = new JTextField(20);
        inputPanel.add(locationField);

        inputPanel.add(new JLabel("Status:"));
        statusField = new JTextField(20);
        inputPanel.add(statusField);

        inputPanel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        inputPanel.add(scrollPane);

        inputPanel.add(new JLabel("Is Active:"));
        isActiveCheckBox = new JCheckBox();
        inputPanel.add(isActiveCheckBox);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this::deleteSelectedPrinter);
        inputPanel.add(deleteButton, BorderLayout.WEST);
        
        
        JButton addButton = new JButton("Add");
        addButton.addActionListener(this::addPrinter);
        inputPanel.add(addButton);

        return inputPanel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new PrinterTableModel();
        printerTable = new JTable(tableModel);
        loadPrinterData();

      
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(printerTable), BorderLayout.CENTER);
        

        return new JScrollPane(tablePanel);
    }

    // Implement addPrinter, loadPrinterData, and deleteSelectedPrinter
    private void addPrinter(ActionEvent event) {
        try {
            Printer printer = new Printer();
            printer.setName((String) printerComboBox.getSelectedItem());
            printer.setLocation(locationField.getText());
            printer.setStatus(statusField.getText());
            printer.setDescription(descriptionArea.getText());
            printer.setActive(isActiveCheckBox.isSelected());

            Printer createdPrinter = printerRepository.create(printer); // Adapt to your method
            if (createdPrinter != null) {
                tableModel.addPrinter(createdPrinter);
                LOGGER.info("Printer added successfully: " + printer.getName());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding printer: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Error adding printer: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        resetForm();
    }

    private void loadPrinterData() {
        try {
        	printerComboBox.removeAllItems();
        	printerNames =  org.amia.play.tools.PrinterLister.getPrintersNotInDB();
        	for (String string : printerNames) {
					printerComboBox.addItem(string);
			}
        	printerComboBox.repaint();
            List<Printer> printers = printerRepository.readAll(); // Adapt to your method
            tableModel.setPrinters(printers);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading printers: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Error loading printers: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedPrinter(ActionEvent event) {
        int row = printerTable.getSelectedRow();
        if (row >= 0) {
            try {
                Printer printer = tableModel.getPrinterAt(row);
                boolean deleted = printerRepository.delete(printer.getPrinterID()); // Adapt to your method
                if (deleted) {
                    tableModel.removePrinter(row);
                    LOGGER.info("Printer deleted successfully: " + printer.getName());
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error deleting printer: " + e.getMessage(), e);
                JOptionPane.showMessageDialog(this, "Error deleting printer: " + e.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
         loadPrinterData();
    }
    
    
void resetForm(){
	   locationField.setText("");
	    statusField.setText("");
	 descriptionArea.setText("");
	   isActiveCheckBox.setSelected(false);
	   printerComboBox.setSelectedItem(null);
}
    
    // Ensure methods handle the printers' details
}
