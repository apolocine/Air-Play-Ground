package org.amia.play.ihm;
import javax.swing.*;

import org.amia.play.ihm.user.LoginForm;
import org.amia.playground.dao.service.UserService;
import org.amia.playground.dto.User;
import org.hmd.angio.ApplicationGUI;
import org.hmd.angio.conf.Config;
import org.hmd.angio.install.sgbd.DatabaseManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartWorkingForm extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 
    private static final Logger LOGGER = Logger.getLogger(StartWorkingForm.class.getName());

  
    private JButton startWorking;
    private JButton applicationSetting;
    public StartWorkingForm( ) {
        
    	// Initialize and set up the main frame
		 setTitle("Application Config");
		 setSize(350, 75);
		 setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		 setLocationRelativeTo(null);
		 
		 
        initializeUI();
     // Initialize and set up the main frame
     	 
     		
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Create fields
        
        startWorking = new JButton("Start Working ");
        // Handle login button click
        startWorking.addActionListener(this::startWorking);
        
        applicationSetting = new JButton("Application Setting "); 
        applicationSetting.addActionListener(this::applicationSetting);
        
        
        // Layout components
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(0, 2));
          
        
        fieldsPanel.add(applicationSetting, BorderLayout.EAST);
        fieldsPanel.add(startWorking, BorderLayout.EAST);
        // Add components to panel
        add(fieldsPanel, BorderLayout.NORTH);

       
    }
    
    private void applicationSetting(ActionEvent event) {
    	
    	 LOGGER.info("User authenticated successfully");
//     	String className = "org.amia.play.ihm.ConfigurationPanel";
//     	ApplicationGUI.startApplication(  className,this);
     	 
		ConfigurationPanel firstFrame = new  ConfigurationPanel();
		firstFrame.setVisible(true); // Ouvrir la seconde JFrame
     this.dispose(); // Fermer la JFrame actuelle
     
     
    }
    private void startWorking(ActionEvent event) {
    	
    	
    	 LOGGER.info("startWorking successfully");
//     	String className = "org.amia.play.ihm.TicketPrintingIHM";
//     	ApplicationGUI.startApplication(  className,this);
    	 
    	 
    	 TicketPrintingIHM firstFrame = new  TicketPrintingIHM();
		firstFrame.setVisible(true); // Ouvrir la seconde JFrame
     this.dispose(); // Fermer la JFrame actuelle
 		 
    }
    
    
    
	
    public static void main(String[] args) {
		 
    	StartWorkingForm frame = new StartWorkingForm();
		// Initialize and set up the main frame
		 frame.setTitle("Start Working ");
		 frame.setSize(350, 75);
		 frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		 frame.setLocationRelativeTo(null); 
		 frame.setVisible(true);
//			SwingUtilities.invokeLater(ConfigurationPanel::new);
		}

 
	
}
