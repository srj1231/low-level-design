package org.saumya.lld.ticTacToe.entities;

import lombok.Getter;
import org.saumya.lld.ticTacToe.enums.GameStatus;

import java.util.List;
import java.util.UUID;


public class Game {
    @Getter
    private final UUID gameId;
    private final Grid grid;
    List<Player> playerList;
    private Player currentPlayer;
    private GameStatus gameStatus;
    @Getter
    private Player winner;

    public  Game(UUID gameId, Player player1, Player player2, Grid grid) {
        this.gameId = gameId;
        this.grid = grid;
        this.playerList = List.of(player1, player2);
        this.gameStatus = GameStatus.WAITING_FOR_PLAYERS;
    }

    public void startGame() {
        this.gameStatus = GameStatus.IN_PROGRESS;
    }

    public GameStatus checkGameStatus() {
        boolean isGameWon = grid.hasWon();
        if (isGameWon) {
            this.winner = currentPlayer;
            this.gameStatus = GameStatus.WON;
        } else {
            this.gameStatus = GameStatus.IN_PROGRESS;
        }
        return this.gameStatus;
    }

    public void makeMove(Player player, Cell cell) {
        this.currentPlayer = player;
        grid.makeMove(player, cell);
    }
}
