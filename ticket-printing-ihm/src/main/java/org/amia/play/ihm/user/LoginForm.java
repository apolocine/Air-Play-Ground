package org.amia.play.ihm.user;
import java.awt.BorderLayout;
import java.awt.GridLayout; 
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.amia.playground.dao.impl.UserRepository;
import org.amia.playground.dao.service.UserService;
import org.amia.playground.dto.Role;
import org.amia.playground.dto.User;
import org.hmd.angio.ApplicationGUI;
import org.hmd.angio.install.sgbd.DatabaseManager;

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
    private JButton createUser;
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
        // Handle login button click
        loginButton.addActionListener(this::performLogin);
        
        createUser = new JButton("Create user"); 
        createUser.addActionListener(this::performCreateUser);
        
        
        // Layout components
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(0, 2));
        fieldsPanel.add(new JLabel("Username:"));
        fieldsPanel.add(usernameField);
        fieldsPanel.add(new JLabel("Password:"));
        fieldsPanel.add(passwordField);
        
        fieldsPanel.add(createUser, BorderLayout.EAST);
        fieldsPanel.add(loginButton, BorderLayout.EAST);
        // Add components to panel
        add(fieldsPanel, BorderLayout.NORTH);

       
    }
    
    private void performCreateUser(ActionEvent event) {
    	
    	
    	  JOptionPane.showMessageDialog(this, "Login first as admin to Create user ", "Create user", JOptionPane.INFORMATION_MESSAGE);
    }
    private void performLogin(ActionEvent event) {
    	
    	
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User authenticated = userService.authenticateUser(username, password);
        if (authenticated!=null) {
            LOGGER.info("User authenticated successfully");
            
        
         Connection conc =DatabaseManager.getConnection();
		UserRepository repo = new UserRepository(conc);
		
          List<Role> roles =    repo.getRoles(authenticated.getUserID());
         for (Role role : roles) {
			if(role.getRoleName().equals("admin")){
					String className = "org.amia.play.ihm.StartWorkingForm";
					ApplicationGUI.startApplication(  className,this);
					  JOptionPane.showMessageDialog(this, "Loged as admin", "Login  success", JOptionPane.INFORMATION_MESSAGE);
					return;
					
			} 
		}
         JOptionPane.showMessageDialog(this, "Loged as exploitante", "Login  success", JOptionPane.INFORMATION_MESSAGE);
      	String className = "org.amia.play.ihm.TicketPrintingIHM";
      	ApplicationGUI.startApplication(  className,this);
           
            // Proceed to next part of your application
        } else {
            LOGGER.log(Level.WARNING, "Authentication failed");
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            // Clear fields or handle failed login
        }
        
        
        
    }
}
