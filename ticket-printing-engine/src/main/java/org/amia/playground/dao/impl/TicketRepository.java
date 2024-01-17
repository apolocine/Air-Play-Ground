package org.amia.playground.dao.impl;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.amia.playground.dao.CRUDRepository;
import org.amia.playground.dto.Game;
import org.amia.playground.dto.GamePricing;
import org.amia.playground.dto.Ticket;
import org.hmd.angio.install.sgbd.DatabaseManager;

public class TicketRepository implements CRUDRepository<Ticket> {
    private final Connection conn;
    private static final Logger LOGGER = Logger.getLogger(TicketRepository.class.getName());

     
     
    public TicketRepository(Connection conn) {
        this.conn = conn;
    }
 // Create a new ticket
    public Ticket create(Ticket ticket) {
        String sql = "INSERT INTO Tickets (GameID, GamePricingID, UserID, ValidDate, Barcode ) VALUES (?, ?, ?, ?, ? )";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, ticket.getGameID());
            stmt.setInt(2, ticket.getGamePricingID()); 
            stmt.setInt(3, ticket.getUserID()); 
            stmt.setTimestamp(4, Timestamp.valueOf(ticket.getValidDate()));
            stmt.setString(5, ticket.getBarcode()); 

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
                ticket.setGameID(rs.getInt("GameID"));
                ticket.setGamePricingID(rs.getInt("GamePricingID")); 
                ticket.setUserID(rs.getInt("UserID")); 
                ticket.setValidDate(rs.getTimestamp("ValidDate").toLocalDateTime()); 
                
                ticket.setBarcode(rs.getString("Barcode")); 
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
            	
            	Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("TicketID"));
                ticket.setGameID(rs.getInt("GameID"));
                ticket.setGamePricingID(rs.getInt("GamePricingID")); 
                ticket.setUserID(rs.getInt("UserID")); 

                ticket.setValidDate(rs.getTimestamp("ValidDate").toLocalDateTime()); 
                ticket.setBarcode(rs.getString("Barcode")); 
            	
                tickets.add(ticket); // Replace with actual ticket object
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error reading all tickets: ", e);
        }
        return tickets;
    }

    @Override
    public Ticket update(Ticket ticket) {
        String sql = "UPDATE Tickets SET GameID = ?, GamePricingID = ?, UserID = ?, ValidDate = ?, Barcode = ?  WHERE TicketID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	 stmt.setInt(1, ticket.getGameID());
             stmt.setInt(2, ticket.getGamePricingID()); 
             stmt.setInt(3, ticket.getUserID()); 
             stmt.setTimestamp(4, Timestamp.valueOf(ticket.getValidDate()));

             stmt.setString(5, ticket.getBarcode()); 
             stmt.setInt(6, ticket.getTicketId()); 

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
    
    
  
    
	 public List<GamePricing> getGamePricingByGameAndCurrentDate(int gameId) {
	        List<GamePricing> pricings = new ArrayList<>();
	     //   String sql = "SELECT * FROM GamePricing WHERE GameID = ? AND ValidFrom <= ? AND ValidTo >= ?";
	        String sql = "SELECT GamePricing.* FROM GamePricing JOIN Tickets ON GamePricing.PricingID = Tickets.GamePricingID WHERE Tickets.TicketID = ?";
	        
	        LocalDateTime now = LocalDateTime.now();

	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, gameId);
	            pstmt.setTimestamp(2, Timestamp.valueOf(now));
	            pstmt.setTimestamp(3, Timestamp.valueOf(now));

	            try (ResultSet rs = pstmt.executeQuery()) {
	                while (rs.next()) {
	                    GamePricing pricing = new GamePricing(
	                      //  rs.getInt("PricingID"),
	                        gameId,
	                        rs.getBigDecimal("Price"),
	                        rs.getTimestamp("ValidFrom").toLocalDateTime(),
	                        rs.getTimestamp("ValidTo").toLocalDateTime()
	                    );
	                    pricing.setPricingId(rs.getInt("PricingID"));
	                    pricings.add(pricing);
	                }
	            }
	        } catch (SQLException e) {
	            // Gestion des exceptions et logging
	            LOGGER.log(Level.SEVERE, "Error fetching current game pricing for GameID: " + gameId, e);
	            // Vous pourriez relancer ou gérer l'exception selon votre logique d'application
	        }

	        return pricings;
	    }
	
	 
	 
	 public BigDecimal  getPriceByTicket(int ticketID) {
	  BigDecimal currentPrice = null;
	  String sql = "SELECT GamePricing.Price FROM GamePricing JOIN Tickets ON GamePricing.PricingID = Tickets.GamePricingID WHERE Tickets.TicketID = ?";
       
      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setInt(1, ticketID);
           
          try (ResultSet rs = pstmt.executeQuery()) {
              if (rs.next()) {
                  currentPrice = rs.getBigDecimal("Price");
              }
          }
      } catch (SQLException e) {
          e.printStackTrace(); // Proper error handling should replace this
      }

      return currentPrice; // May return null if no pricing info is found
      
      
}
//	 public BigDecimal  getPriceByTicket(int gameId) {
//	        List<GamePricing> pricings = new ArrayList<>();
//	     //   String sql = "SELECT * FROM GamePricing WHERE GameID = ? AND ValidFrom <= ? AND ValidTo >= ?";
//	        String sql = "SELECT GamePricing.Price FROM GamePricing JOIN Tickets ON GamePricing.PricingID = Tickets.GamePricingID WHERE Tickets.TicketID = ?";
//	        
//	        LocalDateTime now = LocalDateTime.now();
//
//	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//	            pstmt.setInt(1, gameId);
//	            pstmt.setTimestamp(2, Timestamp.valueOf(now));
//	            pstmt.setTimestamp(3, Timestamp.valueOf(now));
//
//	            try (ResultSet rs = pstmt.executeQuery()) {
//	                while (rs.next()) {
//	                    GamePricing pricing = new GamePricing(
//	                      //  rs.getInt("PricingID"),
//	                        gameId,
//	                        rs.getBigDecimal("Price"),
//	                        rs.getTimestamp("ValidFrom").toLocalDateTime(),
//	                        rs.getTimestamp("ValidTo").toLocalDateTime()
//	                    );
//	                    pricing.setPricingId(rs.getInt("PricingID"));
//	                    pricings.add(pricing);
//	                }
//	            }
//	        } catch (SQLException e) {
//	            // Gestion des exceptions et logging
//	            LOGGER.log(Level.SEVERE, "Error fetching current game pricing for GameID: " + gameId, e);
//	            // Vous pourriez relancer ou gérer l'exception selon votre logique d'application
//	        }
//
//	        return pricings;
//	    }
//	 
	public Game getGameByTicket(int id) {
		  String sql = "SELECT Games.* FROM Games JOIN Tickets ON Games.GameID = Tickets.GameID WHERE Tickets.TicketID = ?";
	      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				Game game = new Game();
				game.setGameID(rs.getInt("GameID"));
				game.setGameName(rs.getString("GameName"));
				game.setAgeRestriction(rs.getString("AgeRestriction"));
			    game.setGameImage(rs.getBytes("GameImage"));
			    game.setLogoImage(rs.getBytes("LogoImage"));
				return game;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error reading game with ID: " + id, e);
		}
		return null; // or consider throwing an exception
	}
	
	
	
	
	
	 public List<Ticket> fetchTicketsFromDatabase(int page, int pageSize) {
	        List<Ticket> tickets = new ArrayList<>();
	        String sql = "SELECT * FROM Tickets LIMIT ? OFFSET ?";

	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, pageSize);
	            pstmt.setInt(2, page * pageSize);

	            try (ResultSet rs = pstmt.executeQuery()) {
	                while (rs.next()) {
	                    Ticket ticket = new Ticket(); 
	                    ticket.setTicketId(rs.getInt("TicketID"));
	                    ticket.setGameID(rs.getInt("GameID"));
	                    ticket.setGamePricingID(rs.getInt("GamePricingID")); 
	                    ticket.setUserID(rs.getInt("UserID")); 

	                    ticket.setValidDate(rs.getTimestamp("ValidDate").toLocalDateTime()); 
	                    ticket.setBarcode(rs.getString("Barcode")); 
	                	
	                    tickets.add(ticket); // Replace with actual ticket object
	                    
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Ou une meilleure gestion des erreurs selon vos besoins
	        }

	        return tickets;
	    }
	public int getCount() {
		String selectSQL = "SELECT COUNT(*) AS row_count FROM Tickets ;";

		 LOGGER.info (selectSQL);

		try (PreparedStatement preparedStatement = conn.prepareStatement(selectSQL)) {

			// Exécuter la requête
			ResultSet resultSet = preparedStatement.executeQuery();

			// Traiter le résultat
			if (resultSet.next()) {
				int rowCount = resultSet.getInt("row_count");
				System.out.println(rowCount);
				return rowCount  ;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}
	 
	 
 
}


