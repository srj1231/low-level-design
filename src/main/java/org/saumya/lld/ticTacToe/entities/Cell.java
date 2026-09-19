package org.saumya.lld.ticTacToe.entities;

import lombok.Getter;
import lombok.Setter;
import org.saumya.lld.ticTacToe.enums.Symbol;

@Getter
@Setter
public class Cell {
    private int row;
    private int col;
    private Symbol symbol;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
