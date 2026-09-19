package org.saumya.lld.ticTacToe.entities;

import org.saumya.lld.ticTacToe.enums.Symbol;

public class ConsecutiveSymbolsWinStrategy implements WinningStrategy {
    private final int winNumber;

    public ConsecutiveSymbolsWinStrategy(int winNumber) {
        this.winNumber = winNumber;
    }

    @Override
    public boolean hasWon(Grid grid, Symbol symbol) {
        if (checkRows(grid, symbol)) return true;
        if (checkColumns(grid, symbol)) return true;
        return checkDiagonals(grid, symbol);
    }

    private boolean checkRows(Grid grid, Symbol symbol) {
        Cell[][] cells = grid.getCells();
        int size = grid.getSize();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col <= size - winNumber; col++) {
                if (checkConsecutive(cells[row], col, symbol)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkColumns(Grid grid, Symbol symbol) {
        Cell[][] cells = grid.getCells();
        int size = grid.getSize();
        for (int col = 0; col < size; col++) {
            for (int row = 0; row <= size - winNumber; row++) {
                if (checkConsecutive(cells, row, col, 0, symbol)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonals(Grid grid, Symbol symbol) {
        Cell[][] cells = grid.getCells();
        int size = grid.getSize();
        // Check main diagonal (top-left to bottom-right)
        for (int row = 0; row <= size - winNumber; row++) {
            for (int col = 0; col <= size - winNumber; col++) {
                if (checkConsecutive(cells, row, col, 1, symbol)) {
                    return true;
                }
            }
        }
        // Check anti-diagonal (top-right to bottom-left)
        for (int row = 0; row <= size - winNumber; row++) {
            for (int col = winNumber - 1; col < size; col++) {
                if (checkConsecutive(cells, row, col, -1, symbol)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkConsecutive(Cell[] row, int startCol, Symbol symbol) {
        Symbol firstSymbol = row[startCol].getSymbol();
        if (firstSymbol == null || firstSymbol != symbol) return false;
        
        for (int i = 1; i < winNumber; i++) {
            Symbol currentSymbol = row[startCol + i].getSymbol();
            if (currentSymbol != symbol) return false;
        }
        return true;
    }

    private boolean checkConsecutive(Cell[][] cells, int startRow, int startCol, int colDelta, Symbol symbol) {
        Symbol firstSymbol = cells[startRow][startCol].getSymbol();
        if (firstSymbol == null || firstSymbol != symbol) return false;
        
        for (int i = 1; i < winNumber; i++) {
            Symbol currentSymbol = cells[startRow + i][startCol + i * colDelta].getSymbol();
            if (currentSymbol != symbol) return false;
        }
        return true;
    }
}
