package org.amia.play.ihm;

import java.sql.Connection;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.amia.play.ihm.game.GameForm;
import org.amia.play.ihm.gamepricing.GamePricingForm;
import org.amia.play.ihm.gameprinter.GamePrinterForm;
import org.amia.play.ihm.permission.PermissionForm;
import org.amia.play.ihm.printer.PrinterForm;
import org.amia.play.ihm.role.RoleForm;
import org.amia.play.ihm.ticket.TicketForm;
import org.amia.play.ihm.user.UserForm;
import org.amia.play.ihm.userroles.UserRolesForm;
import org.amia.play.tools.DigitPanel;
import org.amia.playground.dao.impl.GamePricingRepository;
import org.amia.playground.dao.impl.GamePrinterRepository;
import org.amia.playground.dao.impl.GameRepository;
import org.amia.playground.dao.impl.PermissionsRepository;
import org.amia.playground.dao.impl.PrinterRepository;
import org.amia.playground.dao.impl.RoleRepository;
import org.amia.playground.dao.impl.UserRepository;
import org.hmd.angio.install.sgbd.DatabaseManager;

public class ConfigurationPanel extends JFrame {
	 
	
	private static final Logger LOGGER = Logger.getLogger(ConfigurationPanel.class.getName());
	
	
	
	 public static void main(String[] args) {
		 SwingUtilities.invokeLater(() -> { 
		 ConfigurationPanel frame = new ConfigurationPanel();
		// Initialize and set up the main frame
		 frame.setTitle("Application Config");
		 frame.setSize(800, 600);
		 frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		 frame.setLocationRelativeTo(null);
		 
		 frame.setVisible(true);
//			SwingUtilities.invokeLater(ConfigurationPanel::new);
		  });	
		 }
	 
	 
//	  public static void main(String[] args) {
//	        SwingUtilities.invokeLater(() -> {
//	        	ConfigurationPanel frame = new ConfigurationPanel();
//	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	            frame.setVisible(true);
//	        });
//	    }
	  
	  
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection con =DatabaseManager.getConnection();
	 
    public ConfigurationPanel() {
    	
    	// Initialize and set up the main frame
    			 setTitle("Application Config");
    			 setSize(800, 600);
    			 setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    			 setLocationRelativeTo(null);
    			 
    			 
    			 
    			 
    	// addTab("login", new LoginForm(new UserService(con) ));
    	JTabbedPane jTabbedPane = new JTabbedPane();
    	
    	
    //	jTabbedPane.addTab("Users", new DigitPanel( ));
    	
    	jTabbedPane.addTab("Pricing", new GamePricingForm(new GamePricingRepository(con) , new GameRepository(con) )); 
    	jTabbedPane.addTab("Users", new UserForm(new UserRepository(con)));
    	jTabbedPane.addTab("Roles", new RoleForm(new RoleRepository(con), new PermissionsRepository(con)));
    	jTabbedPane.addTab("UserRoles", new UserRolesForm(new UserRepository(con)));
    	jTabbedPane.addTab("Permissions", new PermissionForm(new PermissionsRepository(con)));
    	jTabbedPane.addTab("Games", new GameForm(new GameRepository(con)));
    	jTabbedPane.addTab("Printers", new PrinterForm( new PrinterRepository(con) ));
    	jTabbedPane.addTab("Games&Printers", new GamePrinterForm( new GamePrinterRepository(con) ,new GameRepository(con),new PrinterRepository(con)));
		 	
    	this.add(jTabbedPane);
        // ... potentially other tabs for more entities ...
		
		
		
		
		
    }
}
