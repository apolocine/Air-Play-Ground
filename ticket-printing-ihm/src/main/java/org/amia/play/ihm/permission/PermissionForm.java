package org.amia.play.ihm.permission;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.amia.playground.dao.impl.PermissionsRepository;
import org.amia.playground.dto.Permission;

public class PermissionForm extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final PermissionsRepository permissionsRepository; // Adapt to your PermissionRepository
    private static final Logger LOGGER = Logger.getLogger(PermissionForm.class.getName());

    // Form components
    private JTextField actionTextField;

    // Table components
    private JTable permissionTable;
    private PermissionTableModel tableModel;

    public PermissionForm(PermissionsRepository permissionsRepository) {
        this.permissionsRepository = permissionsRepository;
        setLayout(new BorderLayout());
        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));

        inputPanel.add(new JLabel("Action Text:"));
        actionTextField = new JTextField(20);
        inputPanel.add(actionTextField);
        
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this::deleteSelectedPermission);
        inputPanel.add(deleteButton, BorderLayout.WEST);
        
        JButton addButton = new JButton("Add");
        addButton.addActionListener(this::addPermission);
        inputPanel.add(addButton);

        return inputPanel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new PermissionTableModel();
        permissionTable = new JTable(tableModel);
        loadPermissionData();

       
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(permissionTable), BorderLayout.CENTER);
        

        return new JScrollPane(tablePanel);
    }
    private void addPermission(ActionEvent event) {
        String actionText = actionTextField.getText().trim();
        if (!actionText.isEmpty()) {
            try {
                Permission newPermission = new Permission();
                newPermission.setActionText(actionText);

                Permission createdPermission = permissionsRepository.create(newPermission); // Implement this method
                if (createdPermission != null) {
                    tableModel.addPermission(createdPermission);
                    LOGGER.info("Permission added successfully: " + actionText);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error adding permission: " + e.getMessage(), e);
                JOptionPane.showMessageDialog(this, "Error adding permission: " + e.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
 
    private void loadPermissionData() {
        try {
            List<Permission> permissions = permissionsRepository.readAll(); // Implement this method
            tableModel.setPermissions(permissions);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading permissions: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Error loading permissions: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void deleteSelectedPermission(ActionEvent event) {
        int row = permissionTable.getSelectedRow();
        if (row >= 0) {
            try {
                Permission permission = tableModel.getPermissionAt(row);
                boolean deleted = permissionsRepository.delete(permission.getPermissionID()); // Implement this method
                if (deleted) {
                    tableModel.removePermission(row);
                    LOGGER.info("Permission deleted successfully: " + permission.getActionText());
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error deleting permission: " + e.getMessage(), e);
                JOptionPane.showMessageDialog(this, "Error deleting permission: " + e.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
