package org.saumya.lld.ticTacToe.entities;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.saumya.lld.ticTacToe.enums.MoveResult;
import org.saumya.lld.ticTacToe.enums.Symbol;

public class Grid {
    @Getter
    private final int size;
    @Getter
    private final Cell[][] cells;
    private final WinningStrategy winningStrategy;

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

    public boolean hasWon(Symbol symbol) {
        return winningStrategy.hasWon(this, symbol);
    }

    public boolean isBoardFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cells[i][j].getSymbol() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public MoveResult makeMove(Player player, int row, int col) {
        if (!isValidCell(row, col)) {
            logger.info("Invalid cell coordinates: ({}, {})", row, col);
            return MoveResult.INVALID;
        }
        if (!isCellEmpty(row, col)) {
            logger.info("Cell ({}, {}) is already occupied", row, col);
            return MoveResult.INVALID;
        }
        logger.info("Player {} made a move at cell ({}, {})", player.getName(), row, col);
        cells[row][col].setSymbol(player.getSymbol());
        
        // Check if this move resulted in a win
        if (hasWon(player.getSymbol())) {
            return MoveResult.WIN;
        }
        
        // Check if board is full (draw)
        if (isBoardFull()) {
            return MoveResult.DRAW;
        }
        
        return MoveResult.SUCCESS;
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    private boolean isCellEmpty(int row, int col) {
        return cells[row][col].getSymbol() == null;
    }
}
