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
import org.amia.playground.dto.Game;
import org.amia.playground.dto.GamePrinter;
import org.amia.playground.dto.Printer;

public class GamePrinterRepository implements CRUDRepository<GamePrinter> {
	private final Connection conn;
	private static final Logger LOGGER = Logger.getLogger(GamePrinterRepository.class.getName());

	private final GameRepository gameRepository; // Initialized elsewhere
	private final PrinterRepository printerRepository; // Initialized elsewhere

	public GamePrinterRepository(Connection conn) {
		this.conn = conn;
		gameRepository = new GameRepository(conn);
		printerRepository = new PrinterRepository(conn);
	}

	public void removeGamePrinter(int gameId, int printerId) {
		String sql = "DELETE FROM GamePrinter WHERE GameID = ? AND PrinterID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, gameId);
			stmt.setInt(2, printerId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// Log error and perhaps re-throw as a runtime exception
			LOGGER.log(Level.SEVERE, "Log error removeGamePrinter", e);
		}
	}

	public List<Game> getGamesForPrinter(int printerId) {
		List<Game> games = new ArrayList<>();
		String sql = "SELECT g.* FROM Games g INNER JOIN GamePrinter gp ON g.GameID = gp.GameID WHERE gp.PrinterID = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, printerId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Game game = new Game();
				game.setGameID(rs.getInt("GameID"));
				game.setGameName(rs.getString("GameName"));
				game.setAgeRestriction(rs.getString("AgeRestriction"));
				// Add other properties as necessary

				games.add(game); // Add the populated game object to the list
			}
		} catch (SQLException e) {
			// Handle exceptions - log or throw as needed
			LOGGER.log(Level.SEVERE, "Log error getGamesForPrinter", e);
		}

		return games; // Return the list of games
	}

	public List<Printer> getPrintersForGame(int gameId) {
		List<Printer> printers = new ArrayList<>();
		String sql = "SELECT p.* FROM Printers p INNER JOIN GamePrinter gp ON p.PrinterID = gp.PrinterID WHERE gp.GameID = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, gameId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Printer printer = new Printer();
				printer.setPrinterID(rs.getInt("PrinterID"));
				printer.setName(rs.getString("Name"));
				printer.setLocation(rs.getString("Location"));
				printer.setStatus(rs.getString("Status"));
				printer.setDescription(rs.getString("Description"));
				printer.setActive(rs.getBoolean("IsActive"));
				// Add other properties as necessary

				printers.add(printer); // Add the populated printer object to the list
			}
		} catch (SQLException e) {
			// Handle exceptions - log or throw as needed
			LOGGER.log(Level.SEVERE, "Log error getPrintersForGame", e);
		}

		return printers; // Return the list of printers
	}

//	public void addGamePrinter(int gameId, int printerId) {
//	    String sql = "INSERT INTO GamePrinter (GameID, PrinterID) VALUES (?, ?)";
//	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//	        stmt.setInt(1, gameId);
//	        stmt.setInt(2, printerId);
//	        stmt.executeUpdate();
//	    } catch (SQLException e) {
//	        // Log error and perhaps re-throw as a runtime exception
//	    	LOGGER.log(Level.SEVERE, "Error addGamePrinter", e);
//	    }
//	}
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

	@Override
	public GamePrinter read(int gamePrinterId) {
		String sql = "SELECT * FROM GamePrinters WHERE GamePrinterID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, gamePrinterId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					GamePrinter gamePrinter = new GamePrinter();
					// Set GamePrinter fields from the database result set
					// ...
					return gamePrinter;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Replace with more robust error handling
		}
		return null;
	}

	@Override
	public GamePrinter update(GamePrinter gamePrinter) {
		String sql = "UPDATE GamePrinters SET Name = ?, GameID = ?, PrinterID = ?, GameImage = ? WHERE GamePrinterID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, gamePrinter.getName());
			stmt.setInt(2, gamePrinter.getGame().getGameID());
			stmt.setInt(3, gamePrinter.getPrinter().getPrinterID());
			stmt.setBytes(4, gamePrinter.getGameImage());
			stmt.setInt(5, gamePrinter.getGamePrinterId());

			int affectedRows = stmt.executeUpdate();
			if (affectedRows > 0) {
				return gamePrinter;
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Replace with more robust error handling
		}
		return null;
	}

	@Override
	public GamePrinter create(GamePrinter gamePrinter) {
		// SQL statement to insert a new game printer association
		String sql = "INSERT INTO GamePrinters (Name, GameID, PrinterID, GameImage) VALUES (?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, gamePrinter.getName());
			stmt.setInt(2, gamePrinter.getGame().getGameID());
			stmt.setInt(3, gamePrinter.getPrinter().getPrinterID());
			stmt.setBytes(4, gamePrinter.getGameImage());

			int affectedRows = stmt.executeUpdate();
			if (affectedRows > 0) {
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						gamePrinter.setGamePrinterId(generatedKeys.getInt(1));
						return gamePrinter;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Replace with more robust error handling
		}
		return null;
	}

	@Override
	public List<GamePrinter> readAll() {
		List<GamePrinter> gamePrinters = new ArrayList<>();
		String sql = "SELECT * FROM GamePrinters";
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				GamePrinter gamePrinter = new GamePrinter();
				// Assuming you have setters for these fields or you might directly assign if
				// they are public
				gamePrinter.setGamePrinterId(rs.getInt("GamePrinterID"));
				gamePrinter.setName(rs.getString("Name"));

				// Here you would typically fetch the associated Game and Printer entities too
				// This might involve additional repository calls or joins in your original SQL
				Game game = fetchGame(rs.getInt("GameID")); // Implement fetchGame to retrieve game by ID
				Printer printer = fetchPrinter(rs.getInt("PrinterID")); // Implement fetchPrinter to retrieve printer by
																		// ID

				gamePrinter.setGame(game);
				gamePrinter.setPrinter(printer);

				// Handle game image data
				byte[] imgBytes = rs.getBytes("GameImage");
				if (imgBytes != null) {
					gamePrinter.setGameImage(imgBytes);
				}

				gamePrinters.add(gamePrinter);
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Replace with more robust error handling
		}
		return gamePrinters;
	}

	@Override
	public boolean delete(int gamePrinterId) {
		String sql = "DELETE FROM GamePrinters WHERE GamePrinterID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, gamePrinterId);
			int affectedRows = stmt.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			e.printStackTrace(); // Replace with more robust error handling
			return false;
		}
	}

	protected Game fetchGame(int gameId) {
		return gameRepository.read(gameId); // Implement this in your GameRepository
	}

	protected Printer fetchPrinter(int printerId) {
		return printerRepository.read(printerId); // Implement this in your PrinterRepository
	}

}
