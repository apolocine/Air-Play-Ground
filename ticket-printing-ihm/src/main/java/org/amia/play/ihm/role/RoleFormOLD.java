package org.amia.play.ihm.role;
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

import org.amia.playground.dao.impl.RoleRepository;
import org.amia.playground.dto.Role;

public class RoleFormOLD extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final RoleRepository roleRepository; // Adapt to your RoleRepository
    private static final Logger LOGGER = Logger.getLogger(RoleFormOLD.class.getName());

    // Form components
    private JTextField roleNameField;

    // Table components
    private JTable roleTable;
    private RoleTableModel tableModel;

    public public RoleFormOLD (RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        setLayout(new BorderLayout());
        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));

        inputPanel.add(new JLabel("Role Name:"));
        roleNameField = new JTextField(20);
        inputPanel.add(roleNameField);
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this::deleteSelectedRole);
        inputPanel.add(deleteButton, BorderLayout.WEST);
        JButton addButton = new JButton("Add");
        addButton.addActionListener(this::addRole);
        inputPanel.add(addButton);

        return inputPanel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new RoleTableModel();
        roleTable = new JTable(tableModel);
        loadRoleData();

      
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(roleTable), BorderLayout.CENTER);
        

        return new JScrollPane(tablePanel);
    }

    private void addRole(ActionEvent event) {
        String roleName = roleNameField.getText().trim();
        if (!roleName.isEmpty()) {
            try {
                Role newRole = new Role();
                newRole.setRoleName(roleName);

                Role createdRole = roleRepository.create(newRole); // Implement this in your repository
                if (createdRole != null) {
                    tableModel.addRole(createdRole);
                    LOGGER.info("Role added successfully: " + roleName);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error adding role: " + e.getMessage(), e);
                JOptionPane.showMessageDialog(this, "Error adding role: " + e.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Role name cannot be empty.",
                                          "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void loadRoleData() {
        try {
            List<Role> roles = roleRepository.readAll(); // Implement this in your repository
            tableModel.setRoles(roles);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading roles: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Error loading roles: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void deleteSelectedRole(ActionEvent event) {
        int row = roleTable.getSelectedRow();
        if (row >= 0) {
            try {
                Role role = tableModel.getRoleAt(row);
                boolean deleted = roleRepository.delete(role.getRoleID()); // Implement this in your repository
                if (deleted) {
                    tableModel.removeRole(row);
                    LOGGER.info("Role deleted successfully: " + role.getRoleName());
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error deleting role: " + e.getMessage(), e);
                JOptionPane.showMessageDialog(this, "Error deleting role: " + e.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
