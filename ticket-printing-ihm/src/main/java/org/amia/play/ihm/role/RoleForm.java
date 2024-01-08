package org.amia.play.ihm.role;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.amia.playground.dao.impl.PermissionsRepository;
import org.amia.playground.dao.impl.RoleRepository;
import org.amia.playground.dto.Permission;
import org.amia.playground.dto.Role;

public class RoleForm extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final RoleRepository roleRepository; // Assume you have this implemented
	private final PermissionsRepository permissionsRepository; // Assume you have this implemented
	private static final Logger LOGGER = Logger.getLogger(RoleForm.class.getName());

	// Form components
	private JTextField roleNameField;
	private JList<Permission> permissionList;

	// Table components
	private JTable roleTable;
	private RoleTableModel tableModel;

	public RoleForm(RoleRepository roleRepository, PermissionsRepository permissionsRepository) {
		this.roleRepository = roleRepository;
		this.permissionsRepository = permissionsRepository;
		setLayout(new BorderLayout());
		add(createInputPanel(), BorderLayout.NORTH);
		add(createTablePanel(), BorderLayout.CENTER);
	}

	private JPanel createInputPanel() {
		JPanel inputPanel = new JPanel(new GridLayout(0, 2));

		inputPanel.add(new JLabel("Role Name:"));
		roleNameField = new JTextField(20);
		inputPanel.add(roleNameField);

		inputPanel.add(new JLabel("Permissions:"));
		permissionList = new JList<>(/* Populate with available permissions */);
		permissionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
//		JScrollPane listScroller = new JScrollPane(permissionList);
		inputPanel.add(permissionList);

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

	// Implement addRole, loadRoleData, and deleteSelectedRole
	private void addRole(ActionEvent event) {
		String roleName = roleNameField.getText().trim();
		List<Permission> selectedPermissions = permissionList.getSelectedValuesList();

		if (!roleName.isEmpty()) {
			try {
				Role newRole = new Role();
				newRole.setRoleName(roleName);
				newRole.setPermissions(selectedPermissions);

				Role createdRole = roleRepository.create(newRole); // Implement this in your repository
				if (createdRole != null) {
					tableModel.addRole(createdRole);
					LOGGER.info("Role added successfully: " + roleName);
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Error adding role: " + e.getMessage(), e);
				JOptionPane.showMessageDialog(this, "Error adding role: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Role name cannot be empty.", "Validation Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void loadRoleData() {
		try {
			List<Role> roles = roleRepository.readAll(); // Implement this in your repository
			tableModel.setRoles(roles);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error loading roles: " + e.getMessage(), e);
			JOptionPane.showMessageDialog(this, "Error loading roles: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
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
				JOptionPane.showMessageDialog(this, "Error deleting role: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// Ensure methods handle the permissions associated with each role
}
