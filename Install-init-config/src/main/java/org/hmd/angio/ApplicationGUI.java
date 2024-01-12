package org.hmd.angio;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
 
import org.hmd.angio.conf.Config;
import org.hmd.angio.conf.ConfigPanel; 
import org.hmd.angio.install.sgbd.DatabaseInitializerImpl;
import org.hmd.angio.install.sgbd.DatabaseManager; 

public class ApplicationGUI extends JFrame {

	
	 private final static Logger LOGGER = Logger.getLogger(ApplicationGUI.class.getName());
	 
	/**
	 * 
	 */
	private static int LOGIN_TAB = 0;
	private static int DB_INSTALL_TAB = 1;
	private static int CONFIG_PROPERTIES_TAB = 2; 
	
	private static final long serialVersionUID = 1L;
	private JTextField dbUrlField, usernameField, passwordField;
	private JPasswordField loginPassword;
	private JTextField loginUsername;
	private JTabbedPane tabbedPane;
	private boolean applicationLoginSuccessful = false; // Remplacez par votre logique de connexion

	public ApplicationGUI() {
		initializeUI();
	}

	private void initializeUI() {
		setTitle("Application");

		// Onglet 1
		JPanel tab1 = createTab1();

		// Onglet 2
		JPanel tab2 = createTab2();

		// Onglet 3
		JPanel tab3 = createTab3();
		JPanel tab4 = createTab4();
		// Ajout des onglets
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Connexion", tab1);
		tabbedPane.addTab("Configuration", tab2);
		tabbedPane.addTab("Update Configuration", tab3);
		tabbedPane.addTab("UNINSTALL", tab4);
		

		add(tabbedPane);

		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		
		if ( isConnectedToDB()) {

			
			if (isUserTableExiste()) { 
			 
 				tabbedPane.setSelectedIndex(LOGIN_TAB); 
				
			}else  {
				
				installOrUpdate();
				
			}
			
		} else {
			tabbedPane.setSelectedIndex(DB_INSTALL_TAB);
		}
		
		
		setVisible(true);
	}

	
	
	private JPanel createTab4() {
		// DROP DATABASE `dbangiographie`;
		JPanel panel = new JPanel();
	
		
		JButton install = new JButton("INSTALL");
		install.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					installOrUpdate(); 
					 
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
				

		});
		
		
		
		JButton uninstall = new JButton("UNINSTALL");
		uninstall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					DatabaseInitializerImpl.unInstallDB(Config.getDatabaseName());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
				

		});
		
		
		panel.add(install);
		panel.add(uninstall);

		return panel;
	}

	boolean isConnectedToDB() {

		try (
				Connection connection = DatabaseManager.getConnection(Config.getSGBDURL(), Config.getDatabaseUser(), Config.getDatabasePassword())
				//Connection connection = DatabaseManager.getConnection()
		// DriverManager.getConnection(JDBC_URL, USER, PASSWORD)

		) {

			applicationLoginSuccessful = connection != null ? true : false;

		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		return applicationLoginSuccessful;

	}

	boolean isUserTableExiste() { 
		String dbangiographie = Config.getDatabaseName();
		String tb_utilisateur =  Config.getDatabaseUserTablename(); 
		return DatabaseInitializerImpl.isTableExiste(dbangiographie,tb_utilisateur);
	}
	
	
	
	private JPanel createTab1() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));

		panel.add(new JLabel("Nom d'utilisateur:"));
		loginUsername = new JTextField("drmdh@msn.com");
		panel.add(loginUsername);

		panel.add(new JLabel("Mot de passe:"));
		loginPassword = new JPasswordField("azerty@26");
		panel.add(loginPassword);
		
		JLabel messageLabel = 	new JLabel("");
		panel.add(messageLabel);
		
		JButton loginButton = new JButton("Se connecter");
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Mettez ici le code pour vérifier la connexion à l'application
				// à partir des valeurs saisies dans loginUsername et loginPassword
//                boolean applicationLoginSuccessful = false; // Remplacez par votre logique de connexion

				// Mettez ici le code pour vérifier la connexion à la base de données
				// à partir des valeurs saisies dans dbUrlField, usernameField et passwordField
				// Remplacez par votre logique de connexion
				
				
				boolean dbConnectionSuccessful = DatabaseInitializerImpl.verifyPassword(loginUsername.getText(),
						new String(loginPassword.getPassword()));

				if (dbConnectionSuccessful) {
					// Si la connexion réussit, vous pouvez démarrer l'application ici
				//	startApplication();
					
					startLoginFormApplication();
					
			} else {
					tabbedPane.setSelectedIndex(0); // Si la connexion échoue, basculez vers l'onglet de connexion
					messageLabel.setText("wrong username or password");
					LOGGER.log(Level.SEVERE,"wrong username or password");
					}

			}

		});
		
		panel.add(loginButton);

		return panel;
	}

	private JPanel createTab2() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 2));

		panel.add(new JLabel("Lien vers la base de données:"));
		dbUrlField = new JTextField(Config.getDatabaseURL());
		panel.add(dbUrlField);

		panel.add(new JLabel("Nom d'utilisateur:"));
		usernameField = new JTextField(Config.getDatabaseUser());
		panel.add(usernameField);

		panel.add(new JLabel("Mot de passe utilisateur:"));
		passwordField = new JPasswordField(Config.getDatabasePassword());
		panel.add(passwordField);
		JLabel messageLabel = 	new JLabel("");
		panel.add(messageLabel);
		JButton connectButton = new JButton("Connecter");

		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				
				String dbangiographie = Config.getDatabaseName();
				String tb_utilisateur =  Config.getDatabaseUserTablename();
				
				
				try (
						//Connection connection = DatabaseManager.getConnection()
				// DriverManager.getConnection(JDBC_URL, USER, PASSWORD)
						Connection connection = DatabaseManager.getConnection(Config.getSGBDURL(), Config.getDatabaseUser(), Config.getDatabasePassword());
				) {

					applicationLoginSuccessful = connection != null ? true : false;
//connexion à la base de donnée existe 
					if (applicationLoginSuccessful) {
						
						//si la table utilisateur exite autoriser la connexion	
						//si non installer les Tables  
						if(DatabaseInitializerImpl.isTableExiste(dbangiographie,tb_utilisateur)) {
							tabbedPane.setSelectedIndex(LOGIN_TAB);
						}else {
							 tabbedPane.setSelectedIndex(DB_INSTALL_TAB);
							installOrUpdate(); 
							
						}
						 
					} else {
						tabbedPane.setSelectedIndex(CONFIG_PROPERTIES_TAB);
						
						JOptionPane.showMessageDialog(ApplicationGUI.this,
								"Échec de la connexion à l'application. Veuillez vérifier vos informations de connexion.",
								"Erreur de connexion", JOptionPane.ERROR_MESSAGE); 
					}

				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

			}
		});
		
		 
		panel.add(connectButton);

		return panel;
	}

	
	/**
	 * 
	 */
	protected void installOrUpdate() {
		
 int  result= JOptionPane.showConfirmDialog(
                     null,
                     "Do you want to Dump  Database & init data ?",
                     "DUMP  Database",
                     JOptionPane.YES_NO_CANCEL_OPTION);
         if (result == JOptionPane.YES_OPTION) {
         	
          
					try {
    						DatabaseInitializerImpl.sqlFileLoader();
					} catch (Exception e2) {
						 e2.printStackTrace();
					}
					 finally {
						 
						 
						 tabbedPane.setSelectedIndex(LOGIN_TAB);
						 
						 
						 
					}
					 
             
         } else if (result == JOptionPane.NO_OPTION) {
        	 int dumpResult= JOptionPane.showConfirmDialog(
                         null,
                         "Do you want to Install from Dump file & init data ?",
                         "Install from DUMP ",
                         JOptionPane.YES_NO_CANCEL_OPTION); 

             if (dumpResult == JOptionPane.YES_OPTION) {
             	
              
    					try {
        						DatabaseInitializerImpl.dumpDatabaseFile();
    					} catch (Exception e2) {
    						 e2.printStackTrace();
    					}
    					 finally {
    						 tabbedPane.setSelectedIndex(LOGIN_TAB);
    					}
    					 
                 
             } else if (dumpResult == JOptionPane.NO_OPTION) {
            	 int installDumpResult = JOptionPane.showConfirmDialog(
                 null,
                 "Do you want to Install Database & init data ?",
                 "Install Database",
                 JOptionPane.YES_NO_CANCEL_OPTION);

                 if (installDumpResult == JOptionPane.YES_OPTION) {
                 	
                  
        					try {
						DatabaseInitializerImpl.installFromFile();
        					} catch (Exception e2) {
        						 e2.printStackTrace();
        					}
        					 finally {
        						 tabbedPane.setSelectedIndex(LOGIN_TAB);
        					}
        					 
                     
                 } else if (installDumpResult == JOptionPane.NO_OPTION) {

                 
                 	 tabbedPane.setSelectedIndex(CONFIG_PROPERTIES_TAB);
                     
                 }
             
             	 
                 
             }
         
         	  
             
         }
	}

	ConfigPanel configPanel = new ConfigPanel();

	private JPanel createTab3() {

		JPanel panel = new JPanel();

		panel.add(configPanel);

		return panel;
	}

	private   void startApplication() {

		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String className = Config.getNameMainClazz(); // Remplacez par le nom complet de votre classe
			
			///  String className = "org.hmd.angio.PhotoOrganizerApp"; // Remplacez par le nom complet de votre classe
			try {
				initClass(className);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		});

	  setVisible(false);

//        // Mettez ici le code pour démarrer votre application après la connexion réussie
//        JOptionPane.showMessageDialog(this, "Application démarrée avec succès!", "Succès",
//                JOptionPane.INFORMATION_MESSAGE);
//        // Ajoutez ici le code pour le démarrage effectif de votre application
	}

	private static   void initClass(String className) throws InvocationTargetException {
		Class<?> clazz = null;
		try {
		    clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		    // Gérer l'exception, peut-être la classe n'existe pas ou le nom est incorrect
		}
		
		Object instance = null;
		try {
		    instance = clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
		    e.printStackTrace();
		    // Gérer les exceptions ici
		}
	}

	private   void startLoginFormApplication() {

		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String className = Config.getLoginClassNameMainClazz(); // Remplacez par le nom complet de votre classe
			
			///  String className = "org.hmd.angio.PhotoOrganizerApp"; // Remplacez par le nom complet de votre classe
			try {
				initClass(className);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		});

	  setVisible(false);

//        // Mettez ici le code pour démarrer votre application après la connexion réussie
//        JOptionPane.showMessageDialog(this, "Application démarrée avec succès!", "Succès",
//                JOptionPane.INFORMATION_MESSAGE);
//        // Ajoutez ici le code pour le démarrage effectif de votre application
	}
	
	public static   void startApplication(String className,Component comp) {

		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//String className = Config.getNameMainClazz(); // Remplacez par le nom complet de votre classe 
			try {
				initClass(className);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		});

		//  comp.getParent(). setVisible(false);

//        // Mettez ici le code pour démarrer votre application après la connexion réussie
//        JOptionPane.showMessageDialog(this, "Application démarrée avec succès!", "Succès",
//                JOptionPane.INFORMATION_MESSAGE);
//        // Ajoutez ici le code pour le démarrage effectif de votre application
	}

 
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new ApplicationGUI());
	}
}
