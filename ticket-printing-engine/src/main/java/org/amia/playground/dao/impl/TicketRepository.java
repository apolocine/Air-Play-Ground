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
import org.amia.playground.dto.Ticket;

public class TicketRepository implements CRUDRepository<Ticket> {
    private final Connection conn;
    private static final Logger LOGGER = Logger.getLogger(TicketRepository.class.getName());

    public TicketRepository(Connection conn) {
        this.conn = conn;
    }
 // Create a new ticket
    public Ticket create(Ticket ticket) {
        String sql = "INSERT INTO Tickets (GameID, Price, Barcode, GameImage, LogoImage) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, ticket.getGameId());
            stmt.setDouble(2, ticket.getPrice());
            stmt.setString(3, ticket.getBarcode());
            stmt.setBytes(4, ticket.getGameImage());
            stmt.setBytes(5, ticket.getLogoImage());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ticket.setTicketId(generatedKeys.getInt(1));
                    }
                }
            }
            return ticket;
        } catch (SQLException e) {
        	 LOGGER.log(Level.SEVERE, "Error creating ticket: ", e);
            // Handle exception
            return null;
        }
    }

    

    // Read a ticket by ID
    public Ticket read(int ticketId) {
        String sql = "SELECT * FROM Tickets WHERE TicketID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticketId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("TicketID"));
                ticket.setGameId(rs.getInt("GameID"));
                ticket.setPrice(rs.getDouble("Price"));
                ticket.setBarcode(rs.getString("Barcode"));
                ticket.setGameImage(rs.getBytes("GameImage"));
                ticket.setLogoImage(rs.getBytes("LogoImage"));
                return ticket;
            }
        } catch (SQLException e) {
        	 LOGGER.log(Level.SEVERE, "Error reading ticket: ", e);
            // Handle exception
        }
        return null;
    }
   

    @Override
    public List<Ticket> readAll() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM Tickets";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Extract data from result set and add to list
                tickets.add(new Ticket()); // Replace with actual ticket object
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error reading all tickets: ", e);
        }
        return tickets;
    }

    @Override
    public Ticket update(Ticket ticket) {
        String sql = "UPDATE Tickets SET GameID = ?, Price = ?, Barcode = ?, GameImage = ?, LogoImage = ? WHERE TicketID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Set parameters
            // ...

            stmt.executeUpdate();
            return ticket;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating ticket: ", e);
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Tickets WHERE TicketID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting ticket: ", e);
            return false;
        }
    }
}


