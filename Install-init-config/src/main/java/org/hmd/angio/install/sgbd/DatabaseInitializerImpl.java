package org.hmd.angio.install.sgbd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.hmd.angio.conf.Config; 

public class DatabaseInitializerImpl implements DatabaseInitializer {

	private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());
	
	
	private static String tableName = Config.getDatabaseUserTablename();

	
	 
	public static boolean verifyPassword(String username,String password) { 
		Object user = getUser(  username,  password);
		// String cryptedPasword  =  encrypt(password  );
		
		//hash du password avant enregistrement. 
		//  String hashedPassword = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
		 
		 //vérification de la resemblance du hash 
//		  if (user != null && BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()).verified) {
//			  
//		  }
		
		
		if(user!=null) {
			 return true;
		}else
			return false ;
		  
		
		 
}
	
	
	
	
    public static Object getUser(String username,String password) {
        
    	
  	  if (password == null) {
  	      throw new IllegalArgumentException("No such parameter present in config file");
  	    }

  	    
  	     
      try (Connection connection = 
      		DatabaseManager.getConnection()
      		//DriverManager.getConnection(JDBC_URL, USER, PASSWORD)
      		) {
          String selectSQL = "SELECT * FROM "+tableName+" WHERE `username` = ? AND password=?";
       
      	LOGGER.info(selectSQL);
      	
          try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
          	 
              preparedStatement.setString(1, username);  
              preparedStatement.setString(2, password);
              LOGGER.info ("selectSQL = \n"+preparedStatement.toString());
              
              try (ResultSet resultSet = preparedStatement.executeQuery()) {
                  
                  return resultSet.next();
              }
          }
      } catch (SQLException e) {
          e.printStackTrace();
      } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      return null;
  }
	
	public static boolean unInstallDB(String dbSch) throws Exception {
		Connection connection = DatabaseManager.getConnection(Config.getSGBDURL(), Config.getDatabaseUser(),
				Config.getDatabasePassword());
		Statement statement = connection.createStatement();
		statement.executeUpdate(" DROP DATABASE IF  EXISTS " + dbSch /*Config.getDatabaseName()*/ + ";");
		
		return connection.getAutoCommit();
		
	}
	public static boolean isDataBaseExiste(String dbSch) throws Exception {

		ResultSet rs = null;
		Connection connection = DatabaseManager.getConnection(Config.getSGBDURL(), Config.getDatabaseUser(),
				Config.getDatabasePassword());

		if (connection != null) {

			 LOGGER.info ("check if a database exists using java");

			rs = connection.getMetaData().getCatalogs();

			while (rs.next()) {
				String catalogs = rs.getString(1);

				if (dbSch.equals(catalogs)) {
					 LOGGER.info ("the database " + dbSch + " exists");
					return true;

				}
			}

		} else {
			 LOGGER.info ("unable to create database connection");
		}

		return false;

	}

	public static boolean isTableExiste(String dbSch, String table) {

		try {
			if (!isDataBaseExiste(dbSch)) {

				return false;

			}
		} catch (Exception e) { 
			e.printStackTrace();
		}

		try (

				Connection connection = DatabaseManager.getConnection()
		// Connection connection = DatabaseManager.getConnection(Config.getSGBDURL(),
		// Config.getDatabaseUser(), Config.getDatabasePassword())
		// DriverManager.getConnection(JDBC_URL, USER, PASSWORD)

		) {

			String selectSQL = "SELECT COUNT(*) AS row_count FROM " + tableName + ";";

			 LOGGER.info (selectSQL);

			try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

				// Exécuter la requête
				ResultSet resultSet = preparedStatement.executeQuery();

				// Traiter le résultat
				if (resultSet.next()) {
					int rowCount = resultSet.getInt("row_count");
					System.out.println(rowCount);
					return rowCount > 0;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}

	public static void installFromFile() throws Exception {

		try (Connection connection = createUseDatabaseIfNotexist()
		// DriverManager.getConnection(JDBC_URL, USER, PASSWORD)
		) {

			String filePath = Config.getSqlFilePathToInstall();

			new SqlExecutor().executeSqlFromFile(filePath, connection);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	 
	public static void sqlFileLoader() throws Exception {

		try (Connection connection = createUseDatabaseIfNotexist()
		// DriverManager.getConnection(JDBC_URL, USER, PASSWORD)
		) {

			String filePath = Config.getSqlDumpFilePath(); 
			String dbName = Config.getDatabaseName();
			String dbUser = Config.getDatabaseUser();
			String dbPass = Config.getDatabasePassword();
			String path = Config.getSqlDumpFilePath();

			new SqlExecutor().mySqlFileLoader(dbName, dbUser, dbPass, path);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
 
	
	public static void dumpDatabaseFile() throws Exception {

		String filePath = Config.getSqlDumpFilePath();
		String dbName = Config.getDatabaseName();
		String dbUser = Config.getDatabaseUser();
		String dbPass = Config.getDatabasePassword();
		String path = Config.getSqlDumpFilePath();

		new SqlExecutor().mysqldump(dbName, dbUser, dbPass, path);

	}
	 
	
	
	public static Connection createUseDatabaseIfNotexist() throws Exception {

		if (!isDataBaseExiste(Config.getDatabaseName())) {

			Connection connection = DatabaseManager.getConnection(Config.getSGBDURL(), Config.getDatabaseUser(),
					Config.getDatabasePassword());
			Statement statement = connection.createStatement();

			// Exécutez ici la requête SQL pour créer la base de données et les tables

			statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + Config.getDatabaseName() + ";");
			statement.executeUpdate("USE " + Config.getDatabaseName() + ";");
			return connection;
		}else {
			Connection connection = DatabaseManager.getConnection(Config.getDatabaseURL(), Config.getDatabaseUser(),
					Config.getDatabasePassword());
			return connection;
		}
		 

	}
	public static void initialize() throws Exception {

		Connection connection = DatabaseManager.getConnection(Config.getSGBDURL(), Config.getDatabaseUser(),
				Config.getDatabasePassword());
		Statement statement = connection.createStatement();

		// Exécutez ici la requête SQL pour créer la base de données et les tables

		statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + Config.getDatabaseName() + ";");

		statement.executeUpdate("USE " + Config.getDatabaseName() + ";");

		statement.executeUpdate("CREATE TABLE IF NOT EXISTS `tb_config` (" + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
				+ "  `key` varchar(255) NOT NULL," + "  `value` varchar(255) NOT NULL," + "  PRIMARY KEY (`id`)"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS `tb_patients` (" + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
				+ "  `nom` varchar(255) NOT NULL," + "  `prenom` varchar(255) NOT NULL,"
				+ "  `naissance` date NOT NULL," + "  PRIMARY KEY (`id`)"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;");

String tb_personne = "tb_personne";
		//		statement.executeUpdate( "CREATE TABLE IF NOT EXISTS tb_examen (\r\n"
//				+ "    id_examen int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,\r\n"
//				+ "    id_personne INT,\r\n"
//				+ "    date_examen DATE,\r\n"
//				+ "    FOREIGN KEY (id_personne) REFERENCES tb_patients(id)\r\n"
//				+ ");");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+tb_personne+" ("
                   + "id INT AUTO_INCREMENT PRIMARY KEY,"
                   + "nom VARCHAR(255),"
                   + "prenom VARCHAR(255),"
                   + "naissance DATE"
                   + ")");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS `tb_examen` (\r\n"
				+ "  `id_examen` int(11) NOT NULL AUTO_INCREMENT,\r\n"
				+ "  `id_personne` int(11) DEFAULT NULL,\r\n"
				+ "  `date_examen` date DEFAULT NULL,\r\n"
				+ "  PRIMARY KEY (`id_examen`),\r\n"
				+ "  KEY `id_personne` (`id_personne`)\r\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;" );
		
		
		// Initialisez les tables de la base de données pour les tests
//		statement.executeUpdate("CREATE TABLE IF NOT EXISTS  `tb_examen`  ("
//							+ "id_examen INT PRIMARY KEY AUTO_INCREMENT  ,"
//							+ "id_personne INT," 
//							+ "date_examen DATE,"
//							+ "type_examen VARCHAR(255)," 
//							+ "FOREIGN KEY (id_personne) REFERENCES "+tb_personne+"(id)" + ")");
					
					
					
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS `tb_utilisateur` ("
				+ "  `id` int(11) NOT NULL AUTO_INCREMENT," + "  `nom` varchar(250) NOT NULL,"
				+ "  `prenom` varchar(250) NOT NULL," + "  `naissance` date NOT NULL,"
				+ "  `username` varchar(250) NOT NULL," + "  `password` varchar(250) DEFAULT NULL,"
				+ "  `fonction` varchar(250) DEFAULT NULL," + "  `description` varchar(250) DEFAULT NULL,"
				+ "  PRIMARY KEY (`id`)," + "  UNIQUE KEY `username` (`username`)"
				+ ") ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;");

		statement.executeUpdate(
				"INSERT INTO `tb_utilisateur` (`id`, `nom`, `prenom`, `naissance`, `username`, `password`, `fonction`, `description`) VALUES"
						+ "(1, 'MADANI', 'Hamid', '1972-10-03', 'drmdh@msn.com', 'azerty@26', 'Ophtalmologue', 'PoXviCZNZBHGyTsac6XdKQ==:lTbS2WuC5p/tMyEVhLPfNA==');");

		connection.getAutoCommit();

	}

	public static void main(String[] args) {
		String tb_utilisateur = "tb_utilisateur";
		String dbangiographie = "angiographie";

		try {
			DatabaseInitializerImpl.installFromFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("La table " + Config.getDatabaseName() + "" + tb_utilisateur + " existe = "
				+ DatabaseInitializerImpl.isTableExiste(dbangiographie, tb_utilisateur));
	}
}
