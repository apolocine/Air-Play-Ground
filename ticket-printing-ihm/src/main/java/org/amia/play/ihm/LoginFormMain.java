package org.amia.play.ihm;

import java.sql.Connection;

import javax.swing.*;

import org.amia.play.ihm.user.LoginForm;
import org.amia.playground.dao.service.UserService;
import org.hmd.angio.install.sgbd.DatabaseManager;

public class LoginFormMain extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoginFormMain() {
		// Initialize and set up the main frame
		setTitle("Login");
		setSize(300, 120);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		Connection con = DatabaseManager.getConnection();
		// Setup the layout and add components
		// ...
		add(new LoginForm(new UserService(con)));
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(LoginFormMain::new);
	}
}
