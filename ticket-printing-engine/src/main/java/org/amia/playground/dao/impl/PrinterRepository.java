package org.amia.playground.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.amia.playground.dao.CRUDRepository;
import org.amia.playground.dto.Printer;

public class PrinterRepository implements CRUDRepository<Printer> {

    private final Connection conn;
    private static final Logger LOGGER = Logger.getLogger(PrinterRepository.class.getName());

    public PrinterRepository(Connection conn) {
        this.conn = conn;
    }

    
    @Override
    public Printer create(Printer printer) {
    	
    	if (ifPrinterNameExists(printer.getName())) {
            LOGGER.log(Level.WARNING, "Printer name '" + printer.getName() + "' already exists.");
            // Handle accordingly - maybe throw an exception or return a specific result
            return printer;
        }
    	
        String sql = "INSERT INTO Printers (Name, Location, Description, Status, IsActive) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, printer.getName());
            stmt.setString(2, printer.getLocation());
            stmt.setString(3, printer.getStatus());
            stmt.setString(4,printer.getDescription());
            stmt.setBoolean(5, printer.isActive());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        printer.setPrinterID(generatedKeys.getInt(1));
                    }
                }
            }
            return printer;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating printer", e);
            return null;
        }
    }

    
    
    private boolean ifPrinterNameExists(String printerName) {
        String sql = "SELECT COUNT(*) FROM Printers WHERE Name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, printerName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // True if count is greater than 0, meaning the printer name exists
            }
        } catch (SQLException e) {
            // Log error and perhaps re-throw as a runtime exception
            LOGGER.log(Level.SEVERE, "Error checking if printer name exists: " + printerName, e);
        }
        return false;
    }

    @Override
    public Printer read(int id) {
        String sql = "SELECT * FROM Printers WHERE PrinterID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Printer printer = new Printer();
                printer.setPrinterID(rs.getInt("PrinterID"));
                printer.setName(rs.getString("Name"));
                printer.setLocation(rs.getString("Location"));
                printer.setStatus(rs.getString("Status"));
                printer.setDescription(rs.getString("Description"));
                printer.setActive(rs.getBoolean("IsActive"));
                return printer;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error reading printer", e);
        }
        return null;
    }

    @Override
    /**
     * printer named in DB 
     */
    public List<Printer> readAll() {
        List<Printer> printers = new ArrayList<>();
        String sql = "SELECT * FROM Printers";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Printer printer = new Printer();
                printer.setPrinterID(rs.getInt("PrinterID"));
                printer.setName(rs.getString("Name"));
                printer.setLocation(rs.getString("Location"));
                printer.setStatus(rs.getString("Status"));
                printer.setDescription(rs.getString("Description"));
                printer.setActive(rs.getBoolean("IsActive"));
                printers.add(printer);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error reading all printers", e);
        }
        return printers;
    }
    @Override
    public Printer update(Printer printer) {
        String sql = "UPDATE Printers SET Name = ?, Location = ?, Status = ?, Description = ?, IsActive = ? WHERE PrinterID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, printer.getName());
            stmt.setString(2, printer.getLocation());
            stmt.setString(3, printer.getStatus());
            stmt.setString(4,printer.getDescription());
            stmt.setBoolean(5, printer.isActive());
            stmt.setInt(6, printer.getPrinterID());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return printer;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating printer", e);
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Printers WHERE PrinterID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting printer", e);
            return false;
        }
    }

}
