package org.amia.play.ihm;

import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.amia.play.ihm.game.GameForm;
import org.amia.play.ihm.gamepricing.GamePricingForm;
import org.amia.play.ihm.gameprinter.GamePrinterForm;
import org.amia.play.ihm.permission.PermissionForm;
import org.amia.play.ihm.printer.PrinterForm;
import org.amia.play.ihm.role.RoleForm;
import org.amia.play.ihm.ticket.TicketForm;
import org.amia.play.ihm.user.UserForm;
import org.amia.play.ihm.userroles.UserRolesForm;
import org.amia.playground.dao.impl.GamePricingRepository;
import org.amia.playground.dao.impl.GamePrinterRepository;
import org.amia.playground.dao.impl.GameRepository;
import org.amia.playground.dao.impl.PermissionsRepository;
import org.amia.playground.dao.impl.PrinterRepository;
import org.amia.playground.dao.impl.RoleRepository;
import org.amia.playground.dao.impl.UserRepository;
import org.hmd.angio.install.sgbd.DatabaseManager;

public class ConfigurationPanel extends JTabbedPane {
	 
	 public static void main(String[] args) {
		 
		 JFrame frame = new JFrame();
		// Initialize and set up the main frame
		 frame.setTitle("Application Config");
		 frame.setSize(800, 600);
		 frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		 frame.setLocationRelativeTo(null);
		 frame.add(new ConfigurationPanel( ));
		 frame.setVisible(true);
//			SwingUtilities.invokeLater(ConfigurationPanel::new);
		}
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection con =DatabaseManager.getConnection();
	 
    public ConfigurationPanel() {
    	// addTab("login", new LoginForm(new UserService(con) ));
		
    	addTab("Pricing", new GamePricingForm(new GamePricingRepository(con) , new GameRepository(con) )); 
    	addTab("Users", new UserForm(new UserRepository(con)));
		addTab("Roles", new RoleForm(new RoleRepository(con), new PermissionsRepository(con)));
		addTab("UserRoles", new UserRolesForm(new UserRepository(con)));
		addTab("Permissions", new PermissionForm(new PermissionsRepository(con)));
		addTab("Games", new GameForm(new GameRepository(con)));
		addTab("Printers", new PrinterForm( new PrinterRepository(con) ));
		addTab("Games&Printers", new GamePrinterForm( new GamePrinterRepository(con) ,new GameRepository(con),new PrinterRepository(con)));
		 	
        // ... potentially other tabs for more entities ...
		
		
		
		
		
    }
}
