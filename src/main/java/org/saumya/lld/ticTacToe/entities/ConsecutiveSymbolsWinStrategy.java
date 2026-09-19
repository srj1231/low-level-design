package org.saumya.lld.ticTacToe.entities;

import lombok.Setter;
import org.saumya.lld.ticTacToe.enums.Symbols;

public class ConsecutiveSymbolsWinStrategy implements WinningStrategy {
    @Setter
    private int winNumber;

    public ConsecutiveSymbolsWinStrategy(int winNumber) {
        this.winNumber = winNumber;
    }

    @Override
    public boolean hasWon(Grid grid) {
        if (checkRows(grid)) return true;
        if (checkColumns(grid)) return true;
        return checkDiagonals(grid);
    }

    private boolean checkRows(Grid grid) {
        Cell[][] cells = grid.getCells();
        int size = grid.getSize();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col <= size - winNumber; col++) {
                if (checkConsecutive(cells[row], col)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkColumns(Grid grid) {
        Cell[][] cells = grid.getCells();
        int size = grid.getSize();
        for (int col = 0; col < size; col++) {
            for (int row = 0; row <= size - winNumber; row++) {
                if (checkConsecutive(cells, row, col, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonals(Grid grid) {
        Cell[][] cells = grid.getCells();
        int size = grid.getSize();
        // Check main diagonal (top-left to bottom-right)
        for (int row = 0; row <= size - winNumber; row++) {
            for (int col = 0; col <= size - winNumber; col++) {
                if (checkConsecutive(cells, row, col, 1)) {
                    return true;
                }
            }
        }
        // Check anti-diagonal (top-right to bottom-left)
        for (int row = 0; row <= size - winNumber; row++) {
            for (int col = winNumber - 1; col < size; col++) {
                if (checkConsecutive(cells, row, col, -1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkConsecutive(Cell[] row, int startCol) {
        Symbols firstSymbol = row[startCol].getSymbol();
        if (firstSymbol == null) return false;
        
        for (int i = 1; i < winNumber; i++) {
            Symbols currentSymbol = row[startCol + i].getSymbol();
            if (currentSymbol != firstSymbol) return false;
        }
        return true;
    }

    private boolean checkConsecutive(Cell[][] cells, int startRow, int startCol, int colDelta) {
        Symbols firstSymbol = cells[startRow][startCol].getSymbol();
        if (firstSymbol == null) return false;
        
        for (int i = 1; i < winNumber; i++) {
            Symbols currentSymbol = cells[startRow + i][startCol + i * colDelta].getSymbol();
            if (currentSymbol != firstSymbol) return false;
        }
        return true;
    }
}
