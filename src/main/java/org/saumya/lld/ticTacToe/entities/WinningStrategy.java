package org.saumya.lld.ticTacToe.entities;

import org.saumya.lld.ticTacToe.enums.Symbol;

public interface WinningStrategy {
    boolean hasWon(Grid grid, Symbol symbol);
}
