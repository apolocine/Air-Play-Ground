package org.hmd.angio.install.sgbd;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class SqlExecutor {
	private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());
	
    public void executeSqlFromFile(String filePath,  Connection conn) {
    	
    	LOGGER.info("SqlFromFile : "+filePath);
    	
        SqlFileLoader loader = new SqlFileLoader();
        try {
            String sqlContent = loader.loadSqlFromFile(filePath);
            // Assuming each statement is on a new line
            String[] sqlStatements = sqlContent.split(";\\s*");

            try (Statement stmt = conn.createStatement()) {
                for (String statement : sqlStatements) {
                    stmt.execute(statement.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions
        }
    }

    
    
    
    /**
     * 
     * @param dbName
     * @param dbUser
     * @param dbPass
     * @param path
     */
    public static void mySqlFileLoader( String dbName, String dbUser, String dbPass, String path) {
    	
//    	  try { 
//              // Constructing the command to load the SQL file into the database
//              String[] executeCmd = new String[]{"mysql", dbName, "-u" + dbUser, "-p" + dbPass, "-e", " source " + path};
//
//              // Execute the command
//              Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
//
//              int processComplete = runtimeProcess.waitFor();
//
//              // Check the process exit value
//              if (processComplete == 0) {
//                  System.out.println("Data has been restored successfully!");
//              } else {
//                  System.out.println("Could not restore the Data!");
//              }
//
//          } catch (IOException | InterruptedException ex) {
//              ex.printStackTrace();
//          }
    }

    /**
     * 
     * @param dbName
     * @param dbUser
     * @param dbPass
     * @param path
     */
    public static void mysqldump( String dbName, String dbUser, String dbPass, String path) {
//        try {  
//            // Construct command
//            String command = "mysqldump -u " + dbUser + " -p" + dbPass + " " + dbName + " -r " + path;
//
//            // Execute the command
//            Process runtimeProcess = Runtime.getRuntime().exec(command);
//
//            int processComplete = runtimeProcess.waitFor();
//
//            // Check if dump is successful
//            if (processComplete == 0) {
//                System.out.println("Dump completed successfully");
//            } else {
//                System.out.println("Could not create the dump");
//            }
//        } catch (IOException | InterruptedException ex) {
//            ex.printStackTrace();
//        }
    }
    // ... other methods ...
}
