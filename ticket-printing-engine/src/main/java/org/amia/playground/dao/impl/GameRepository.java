package org.amia.playground.dao.impl;

import java.sql.*;
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
		String sql = "INSERT INTO Games (GameName, AgeRestriction) VALUES (?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, game.getGameName());
			stmt.setString(2, game.getAgeRestriction());

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
				return game;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error reading game with ID: " + id, e);
		}
		return null; // or consider throwing an exception
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
				games.add(game);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Error reading all games", e);
		}
		return games;
	}

	@Override
	public Game update(Game game) {
		String sql = "UPDATE Games SET GameName = ?, AgeRestriction = ? WHERE GameID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, game.getGameName());
			stmt.setString(2, game.getAgeRestriction());
			stmt.setInt(3, game.getGameID());

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

	public void addGameToPrinter(int gameId, int printerId) {

		if ((ifGameExists(gameId) && ifPrinterExists(printerId))) {

			if (!ifGamePrinterExists(gameId, printerId)) {

				// Proceed with adding the relationship as both entities exist
				String sql = "INSERT INTO GamePrinter (GameID, PrinterID) VALUES (?, ?)";
				try (PreparedStatement stmt = conn.prepareStatement(sql)) {
					stmt.setInt(1, gameId);
					stmt.setInt(2, printerId);
					int affectedRows = stmt.executeUpdate();
					if (affectedRows == 0) {
						// Handle the case where no rows were updated as needed
						LOGGER.log(Level.SEVERE, "Error adding Printer to Game");
					}
				} catch (SQLException e) {
					// Handle other SQL exceptions here
					LOGGER.log(Level.SEVERE, "Error adding Printer to Game", e);
				}
			} else {
				LOGGER.log(Level.INFO, "The  Game already has this Printer.");
			}

		} else {

			LOGGER.log(Level.INFO, "The   Game or Printer not Exists.");
		}
	}

	private boolean ifGamePrinterExists(int gameId, int printerId) {
		String sql = "SELECT COUNT(*) FROM GamePrinter WHERE GameID = ? AND PrinterID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, gameId);
			stmt.setInt(2, printerId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0; // True if count is greater than 0, meaning the association exists
			} else {
				LOGGER.log(Level.INFO, "The  Game not on this Printer.");
			}
		} catch (SQLException e) {
			// Log error and perhaps re-throw as a runtime exception
			LOGGER.log(Level.SEVERE, "The  Game already or not has this Printe.");
		}
		return false;
	}

	private boolean ifGameExists(int gameId) {
		String sql = "SELECT COUNT(*) FROM Games WHERE GameID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, gameId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0; // True if count is greater than 0, meaning the game exists
			} else {
				LOGGER.log(Level.INFO, "The   Game not Exists.");
			}
		} catch (SQLException e) {
			// Log error and perhaps re-throw as a runtime exception
			LOGGER.log(Level.SEVERE, "The  ifGameExists error .");
		}
		return false;
	}

	private boolean ifPrinterExists(int printerId) {
		String sql = "SELECT COUNT(*) FROM Printers WHERE PrinterID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, printerId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0; // True if count is greater than 0, meaning the printer exists
			} else {
				LOGGER.log(Level.INFO, "The   Printer not Exists.");
			}
		} catch (SQLException e) {
			// Log error and perhaps re-throw as a runtime exception
			LOGGER.log(Level.SEVERE, "The  ifPrinterExists error .");
		}
		return false;
	}

}