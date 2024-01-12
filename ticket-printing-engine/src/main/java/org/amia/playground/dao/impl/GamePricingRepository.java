package org.amia.playground.dao.impl;
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

import org.amia.playground.dto.GamePricing;

public class GamePricingRepository {
	private static final Logger LOGGER = Logger.getLogger(GamePricingRepository.class.getName());
    private Connection conn; // Assurez-vous que cela est initialisé ailleurs

    public GamePricingRepository(Connection conn) {
        this.conn = conn;
    }

    // Méthode pour ajouter un nouveau prix à un jeu
    public void addGamePricing(GamePricing gamePricing) {
        String sql = "INSERT INTO GamePricing (GameID, Price, ValidFrom, ValidTo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gamePricing.getGameID());
            pstmt.setBigDecimal(2, gamePricing.getPrice());
            pstmt.setTimestamp(3, Timestamp.valueOf(gamePricing.getValidFrom()));
            pstmt.setTimestamp(4, Timestamp.valueOf(gamePricing.getValidTo()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Gestion des erreurs plus robuste recommandée
        }
    }

    // Méthodes pour lire, mettre à jour, et supprimer des prix...

    
    public void updateGamePricing(GamePricing gamePricing) {
        String sql = "UPDATE GamePricing SET Price = ?, ValidFrom = ?, ValidTo = ? WHERE PricingID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) { 
            pstmt.setBigDecimal(1, gamePricing.getPrice());
            pstmt.setTimestamp(2, Timestamp.valueOf(gamePricing.getValidFrom()));
            pstmt.setTimestamp(3, Timestamp.valueOf(gamePricing.getValidTo()));
            pstmt.setInt(4, gamePricing.getPricingId());
             
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                LOGGER.info("Pricing updated successfully for PricingID: " + gamePricing.getPricingId());
            } else {
                LOGGER.warning("No pricing was updated for PricingID: " + gamePricing.getPricingId());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating pricing for PricingID: " + gamePricing.getPricingId(), e);
        }
    }
    public boolean deleteGamePricing(int pricingId) {
        String sql = "DELETE FROM GamePricing WHERE PricingID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pricingId);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                LOGGER.info("Pricing deleted successfully for PricingID: " + pricingId);
                return true;
            } else {
                LOGGER.warning("No pricing was found (and thus deleted) for PricingID: " + pricingId);
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting pricing for PricingID: " + pricingId, e);
        }
        return false;
    }

    public List<GamePricing> readAll() {
        List<GamePricing> pricings = new ArrayList<>();
        String sql = "SELECT * FROM GamePricing";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                GamePricing pricing = new GamePricing(
                   
                    rs.getInt("GameID"),
                    rs.getBigDecimal("Price"),
                    rs.getTimestamp("ValidFrom").toLocalDateTime(),
                    rs.getTimestamp("ValidTo").toLocalDateTime()
                );
                pricing.setPricingId(rs.getInt("PricingID"));
                
                pricings.add(pricing);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Proper error handling should replace this
        }
        return pricings;
    }

    /**
	 * 
	 * @param gameID
	 * @return
	 */

	 public List<GamePricing> getGamePricingByGameAndCurrentDate(int gameId) {
	        List<GamePricing> pricings = new ArrayList<>();
	        String sql = "SELECT * FROM GamePricing WHERE GameID = ? AND ValidFrom <= ? AND ValidTo >= ?";
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
	
    // Assurez-vous d'implémenter des méthodes pour gérer tous les aspects de GamePricing.
}
