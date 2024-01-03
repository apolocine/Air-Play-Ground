package org.amia.play.ihm.user;
import javax.swing.*;

import org.amia.playground.dao.service.UserService;
import org.amia.playground.dto.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginForm extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final UserService userService;
    private static final Logger LOGGER = Logger.getLogger(LoginForm.class.getName());

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginForm(UserService userService) {
        this.userService = userService;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Create fields
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        // Layout components
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(2, 2));
        fieldsPanel.add(new JLabel("Username:"));
        fieldsPanel.add(usernameField);
        fieldsPanel.add(new JLabel("Password:"));
        fieldsPanel.add(passwordField);

        // Add components to panel
        add(fieldsPanel, BorderLayout.CENTER);
        add(loginButton, BorderLayout.SOUTH);

        // Handle login button click
        loginButton.addActionListener(this::performLogin);
    }

    private void performLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User authenticated = userService.authenticateUser(username, password);
        if (authenticated!=null) {
            LOGGER.info("User authenticated successfully");
            // Proceed to next part of your application
        } else {
            LOGGER.log(Level.WARNING, "Authentication failed");
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            // Clear fields or handle failed login
        }
    }
}
