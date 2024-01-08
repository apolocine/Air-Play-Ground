package org.amia.playground.dao.impl;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.amia.playground.dao.CRUDRepository;
import org.amia.playground.dto.Game;

public class GameRepository implements CRUDRepository<Game> {

	private final Connection conn;
	private static final Logger LOGGER = Logger.getLogger(GameRepository.class.getName());

	public GameRepository(Connection conn) {
		this.conn = conn;
	}

	private boolean ifGameNameExists(String gameName) {
		String sql = "SELECT COUNT(*) FROM Games WHERE GameName = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, gameName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0; // true if the count is greater than 0
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error checking if game name exists", e);
		}
		return false; // Default to false if not found or error occurs
	}

	public Game create(Game game) {
		// First, check if the GameName already exists
		if (ifGameNameExists(game.getGameName())) {
			LOGGER.log(Level.WARNING, "Game name '" + game.getGameName() + "' already exists.");
			return game; // or throw a custom exception
		}

		// Proceed with the creation as the name is unique
		String sql = "INSERT INTO Games (GameName, AgeRestriction, GameImage, LogoImage) VALUES (?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, game.getGameName());
			stmt.setString(2, game.getAgeRestriction());
			stmt.setBytes(3, game.getGameImage());
			stmt.setBytes(4, game.getLogoImage());
		    
			int affectedRows = stmt.executeUpdate();
			if (affectedRows > 0) {
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						game.setGameID(generatedKeys.getInt(1));
					}
				}
				return game;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error creating game: ", e);
		}
		return null; // or consider throwing an exception
	}

	// Implement read, update, delete methods...

	@Override
	public Game read(int id) {
		String sql = "SELECT * FROM Games WHERE GameID = ?";
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
	 public List<Game> readAllGamesWithoutPrinter() {
	        List<Game> gamesWithoutPrinters = new ArrayList<>();
	        String sql = "SELECT * FROM Games WHERE GameID NOT IN (SELECT GameID FROM GamePrinters)";

	        try (Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            
	            while (rs.next()) {
	                Game game = new Game(); // Assuming a Game class with appropriate constructor or setters
	                game.setGameID(rs.getInt("GameID"));
	                game.setGameName(rs.getString("GameName"));
	                game.setAgeRestriction(rs.getString("AgeRestriction"));
	                game.setGameImage(rs.getBytes("GameImage"));
	                game.setLogoImage(rs.getBytes("LogoImage"));
	                // ... set other fields as necessary ...

	                gamesWithoutPrinters.add(game);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Replace with more robust error handling
	        }
	        return gamesWithoutPrinters;
	    }
	 
	 public List<Game> readAllGamesWithPrinter() {
	        List<Game> gamesWithoutPrinters = new ArrayList<>();
	        String sql = "SELECT * FROM Games WHERE GameID   IN (SELECT GameID FROM GamePrinters)";

	        try (Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            
	            while (rs.next()) {
	                Game game = new Game(); // Assuming a Game class with appropriate constructor or setters
	                game.setGameID(rs.getInt("GameID"));
	                game.setGameName(rs.getString("GameName"));
	                game.setAgeRestriction(rs.getString("AgeRestriction"));
	                game.setGameImage(rs.getBytes("GameImage"));
	                game.setLogoImage(rs.getBytes("LogoImage"));
	                // ... set other fields as necessary ...

	                gamesWithoutPrinters.add(game);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Replace with more robust error handling
	        }
	        return gamesWithoutPrinters;
	    }
	@Override
	public boolean delete(int id) {
		String sql = "DELETE FROM Games WHERE GameID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);

			int affectedRows = stmt.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error deleting game with ID: " + id, e);
		}
		return false; // or consider throwing an exception
	}

	@Override
	public List<Game> readAll() {
		List<Game> games = new ArrayList<>();
		String sql = "SELECT * FROM Games";

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Game game = new Game();
				game.setGameID(rs.getInt("GameID"));
				game.setGameName(rs.getString("GameName"));
				game.setAgeRestriction(rs.getString("AgeRestriction"));
			    game.setGameImage(rs.getBytes("GameImage"));
			    game.setLogoImage(rs.getBytes("LogoImage"));
				games.add(game);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error reading all games", e);
		}
		return games;
	}

	@Override
	public Game update(Game game) {
		String sql = "UPDATE Games SET GameName = ?, AgeRestriction = ?, GameImage = ?, LogoImage = ? WHERE GameID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, game.getGameName());
			stmt.setString(2, game.getAgeRestriction());
			
			stmt.setBytes(3, game.getGameImage());
			stmt.setBytes(4, game.getLogoImage());
			
			
			stmt.setInt(5, game.getGameID());
			
			
			
			int affectedRows = stmt.executeUpdate();
			if (affectedRows > 0) {
				return game;
			}
		} catch (SQLException e) {
			if (e.getSQLState().equals("23000")) { // Check for duplicate game name
				LOGGER.log(Level.WARNING, "A game with the name '" + game.getGameName() + "' already exists.");
			} else {
				LOGGER.log(Level.SEVERE, "Error updating game: ", e);
			}
		}
		return null; // or consider throwing an exception
	}
	
	/**
	 * 
	 * @param gameID
	 * @return
	 */
	public BigDecimal getCurrentPriceForGame(int gameID) {
        BigDecimal currentPrice = null;
        String sql = "SELECT Price FROM GamePricing WHERE GameID = ? AND ValidFrom <= ? AND ValidTo >= ? ORDER BY ValidTo DESC";
        LocalDateTime now = LocalDateTime.now(); // Capture the current time

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gameID);
            pstmt.setTimestamp(2, Timestamp.valueOf(now)); // ValidFrom
            pstmt.setTimestamp(3, Timestamp.valueOf(now)); // ValidTo
LOGGER.info(pstmt.toString());
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

}
