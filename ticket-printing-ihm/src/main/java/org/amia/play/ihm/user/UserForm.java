package org.amia.play.ihm.user;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.amia.playground.dao.impl.UserRepository;
import org.amia.playground.dto.Role;
import org.amia.playground.dto.User;

public class UserForm extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final UserRepository userRepository; // Adapt to your UserRepository
    private static final Logger LOGGER = Logger.getLogger(UserForm.class.getName());

    // Form components
    private JTextField nameField;
    private JPasswordField passwordField;
    private JList<Role> roleList; // For selecting multiple roles

    // Table components
    private JTable userTable;
    private UserTableModel tableModel;

    public UserForm(UserRepository userRepository) {
        this.userRepository = userRepository;
        setLayout(new BorderLayout());
        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel("Roles:"));
        
        roleList = new JList<>(/* Populate with available roles */);
        roleList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane roleScrollPane = new JScrollPane(roleList);
        inputPanel.add(roleScrollPane);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this::deleteSelectedUser);
        inputPanel.add(deleteButton, BorderLayout.WEST);
        
        
        JButton addButton = new JButton("Add");
        addButton.addActionListener(this::addUser);
        inputPanel.add(addButton);

        return inputPanel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new UserTableModel();
        userTable = new JTable(tableModel);
        loadUserData();

        userTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = userTable.getSelectedRow();
                    if (row >= 0) {
                        User user = tableModel.getUserAt(row);
                        updateUserForm(user);
                    }
                }
            }
        });

        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(userTable), BorderLayout.CENTER);
       

        return new JScrollPane(tablePanel);
    }

    // Implement addUser, loadUserData, deleteSelectedUser, and updateUserForm
    
    private void addUser(ActionEvent event) {
        try {
            User user = new User();
            user.setName(nameField.getText());
            user.setPassword(new String(passwordField.getPassword())); // Consider hashing this!
            List<Role> selectedRoles = roleList.getSelectedValuesList();
            user.setRoles(selectedRoles);

            userRepository.create(user);
            loadUserData(); // Refresh the user data in the table
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding user: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUserData() {
        try {
            List<User> users = userRepository.readAll();
            tableModel.setUsers(users);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading user data: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedUser(ActionEvent event) {
        int row = userTable.getSelectedRow();
        if (row >= 0) {
            try {
                User user = tableModel.getUserAt(row);
                if (userRepository.delete(user.getUserID())) {
                    loadUserData(); // Refresh user data
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error deleting user: " + e.getMessage(), e);
                JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateUserForm(User user) {
        try {
            nameField.setText(user.getName());
            // Consider how to handle password updates, usually, passwords aren't displayed back in forms
            // roleList.setSelected...; // Set the selected roles based on the user's current roles

            // You might want to add a "Save" or "Update" button specifically for handling updates
            // and distinguish it from adding a new user.
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading user for update: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Error loading user for update: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    // Ensure methods handle the roles associated with each user
    
    
}
