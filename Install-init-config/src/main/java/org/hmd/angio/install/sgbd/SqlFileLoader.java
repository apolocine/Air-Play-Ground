package org.hmd.angio.install.sgbd;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SqlFileLoader {

    public String loadSqlFromFile(String filePath) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(filePath));
        return new String(encoded);
    }
    
    
    public static void main(String[] args) {
        try {
            String dbName = "yourDatabaseName";
            String dbUser = "yourUsername";
            String dbPass = "yourPassword";
            String filePath = "path/to/your/dumpfile.sql";

            // Constructing the command to load the SQL file into the database
            String[] executeCmd = new String[]{"mysql", dbName, "-u" + dbUser, "-p" + dbPass, "-e", " source " + filePath};

            // Execute the command
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);

            int processComplete = runtimeProcess.waitFor();

            // Check the process exit value
            if (processComplete == 0) {
                System.out.println("Data has been restored successfully!");
            } else {
                System.out.println("Could not restore the Data!");
            }

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    // ... other methods ...
}
