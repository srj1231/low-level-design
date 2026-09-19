package org.saumya.lld.ticTacToe.entities;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Grid {
    @Getter
    private final int size;
    @Getter
    private final Cell[][] cells;
    private final WinningStrategy winningStrategy;
    @Setter
    private Player currentPlayer;

    Logger logger = LogManager.getLogger();

    public Grid(WinningStrategy winningStrategy, int size) {
        this.winningStrategy = winningStrategy;
        this.size = size;
        this.cells = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    public boolean hasWon() {
        return winningStrategy.hasWon(this);
    }

    public void makeMove(Player player, Cell cell) {
        boolean isTurnValid = validateTurn(player, cell);
        if (!isTurnValid) {
            logger.info("Invalid turn for player " + player.getName() + " at cell " + cell);
            return;
        }
        logger.info("Player " + player.getName() + " made a move at cell " + cell);
        cells[cell.getRow()][cell.getCol()].setSymbol(player.getSymbol());
        switchPlayer();
    }

    public boolean validateTurn(Player player, Cell cell) {
        boolean isCellEmpty = cells[cell.getRow()][cell.getCol()].getSymbol() == null;
        boolean isPlayerTurn = currentPlayer == null || currentPlayer.equals(player);
        return isCellEmpty && isPlayerTurn;
    }

    private void switchPlayer() {
        this.currentPlayer = null;
    }
}
