package org.amia.playground.exception;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLExceptionHandler {
    private static final Logger LOGGER = Logger.getLogger(SQLExceptionHandler.class.getName());

    public static void handle(SQLException e, String message) {
        // Log the exception details along with a custom message
        LOGGER.log(Level.SEVERE, message + " - " + e.getMessage(), e);

        // Depending on your application's needs, you might rethrow the exception,
        // return a specific result, or perform additional actions like alerting admins.
    }
}
