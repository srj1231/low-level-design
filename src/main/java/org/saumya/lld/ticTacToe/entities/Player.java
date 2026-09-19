package org.saumya.lld.ticTacToe.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.saumya.lld.ticTacToe.enums.Symbols;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Player {
    private UUID playerId;
    private String name;
    private Symbols symbol;
}
