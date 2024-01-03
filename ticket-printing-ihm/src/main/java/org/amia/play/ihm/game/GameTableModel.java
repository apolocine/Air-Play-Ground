package org.amia.play.ihm.game;
import javax.swing.table.AbstractTableModel;

import org.amia.playground.dto.Game;

import java.util.ArrayList;
import java.util.List;

public class GameTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = new String[]{"ID", "Game Name", "Age Restriction"};
    private List<Game> games;

    public GameTableModel() {
        this.games = new ArrayList<>();
    }

    public void setGames(List<Game> games) {
        this.games = games;
        fireTableDataChanged();
    }

    public Game getGameAt(int rowIndex) {
        return games.get(rowIndex);
    }

    public void removeGame(int rowIndex) {
        games.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    @Override
    public int getRowCount() {
        return games.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Game game = games.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return game.getGameID();
            case 1:
                return game.getGameName();
            case 2:
                return game.getAgeRestriction();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
