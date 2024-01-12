package org.amia.play.ihm.userroles;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.amia.play.ihm.render.ApplicationCompRenderer;
import org.amia.playground.dao.impl.UserRepository;
import org.amia.playground.dto.Role;
import org.amia.playground.dto.User;
import org.amia.playground.dto.UserRole;

public class UserRolesForm extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(UserRolesForm.class.getName());

	private JList<User> userList; // Pour sélectionner un utilisateur
	private JList<Role> roleList; // Pour afficher et sélectionner les rôles
	private JButton saveButton; // Pour sauvegarder les changements
	private JButton deleteButton;
	// Table components
	private JTable UserRoleTable;
	private UserRolesTableModel tableModel;

	UserRepository userRepository;

	public UserRolesForm(UserRepository userRepository) {

		this.userRepository = userRepository;
		setLayout(new BorderLayout());

		// Initialiser userComboBox avec la liste des utilisateurs

		add(createInputPanel(), BorderLayout.NORTH);
		add(createTablePanel(), BorderLayout.CENTER);

	}

	private JPanel createInputPanel() {
		List<User> users = userRepository.readAll();
		List<Role> roles = userRepository.readAllRoles();

		JPanel inputPanel = new JPanel(new GridLayout(0, 2));
		userList = new JList<>(new DefaultComboBoxModel<>(users.toArray(new User[0])));
		userList.setCellRenderer(new ApplicationCompRenderer());

		// Initialiser roleList avec les rôles disponibles
		roleList = new JList<>(new DefaultListModel<>());
		roleList.setCellRenderer(new ApplicationCompRenderer());
		for (Role role : roles) {
			((DefaultListModel<Role>) roleList.getModel()).addElement(role);
		}
		roleList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		

		inputPanel.add(new JLabel("Users :"));
		inputPanel.add(userList, BorderLayout.NORTH);
		inputPanel.add(new JLabel("__________ "));
		inputPanel.add(new JLabel("__________ ") );
		
		inputPanel.add(new JLabel("Roles :"));
//		JScrollPane JScrollPaneroleList = new JScrollPane(roleList);
		
		inputPanel.add(roleList, BorderLayout.CENTER);
		
		
		inputPanel.add(new JLabel("__________ "));
		inputPanel.add(new JLabel("__________ ") );
	
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this::deleteSelectedUserRole);
		inputPanel.add(deleteButton);
		
		// Initialiser et configurer saveButton
		saveButton = new JButton("Save");
		saveButton.addActionListener(e -> saveUserRoles());
		inputPanel.add(saveButton);
		
		return inputPanel;
	}

	private JScrollPane createTablePanel() {
		tableModel = new UserRolesTableModel();
		UserRoleTable = new JTable(tableModel);
		loadUserData();

		UserRoleTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = UserRoleTable.getSelectedRow();
					if (row >= 0) {
						UserRole user = tableModel.getUserRoleAt(row);
						updateUserForm(user);
					}
				}
			}

		
		});

		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(new JScrollPane(UserRoleTable));

		return new JScrollPane(tablePanel);
	}
	
	
	private void updateUserForm(UserRole user) {
				// TODO Auto-generated method stub
				
			}
	private void deleteSelectedUserRole(ActionEvent event) {
		int row = UserRoleTable.getSelectedRow();
		if (row >= 0) {
			try {
				UserRole gamePrinter = tableModel.getUserRoleAt(row);
				boolean deleted = userRepository.removeRoleFromUser(gamePrinter.getUserID(), gamePrinter.getRoleID());
				if (deleted) {
					tableModel.removeUserRole(row);
					LOGGER.info("User-Role  association deleted successfully.");
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Error deleting User-Role  association: " + e.getMessage(), e);
				JOptionPane.showMessageDialog(this, "Error deleting User-Role  association: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		loadUserData();

	}

	private void saveUserRoles() {
		// Obtenez l'utilisateur sélectionné
		User selectedUser = (User) userList.getSelectedValue();
		if (selectedUser == null) {
			// Gérer l'absence de sélection d'utilisateur
			return;
		}

		// Obtenez les rôles sélectionnés
		List<Role> selectedRoles = roleList.getSelectedValuesList();

		// Commencer une transaction ou une série d'opérations
		try {
			// Supprimez tous les rôles actuellement assignés à l'utilisateur
			userRepository.clearRolesForUser(selectedUser.getUserID());

			// Ajoutez chaque rôle sélectionné à l'utilisateur
			for (Role role : selectedRoles) {
				userRepository.addRoleToUser(selectedUser.getUserID(), role.getRoleID());
			}

			// Log success
			LOGGER.info("Updated roles for user: " + selectedUser.getName());

			// Optionnel: Mettre à jour l'affichage ou notifier l'utilisateur du succès
		} catch (Exception e) {
			// Log the exception and notify the user
			LOGGER.log(Level.SEVERE, "Error updating user roles", e);

			// Optionnel: notifier l'utilisateur de l'erreur
		}
		
		
		loadUserData();
	}

	private void loadUserData() {
		try {
			List<UserRole> userRoles = userRepository.readAllUserRole();
			tableModel.setUserRole(userRoles);
			 
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error loading user data: " + e.getMessage(), e);
			JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		
	}

	// ... Reste du code, y compris initialisation, gestion des événements, etc. ...
}
