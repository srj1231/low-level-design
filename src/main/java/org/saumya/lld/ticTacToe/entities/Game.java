package org.saumya.lld.ticTacToe.entities;

import lombok.Getter;
import org.saumya.lld.ticTacToe.enums.GameStatus;
import org.saumya.lld.ticTacToe.enums.MoveResult;

import java.util.List;
import java.util.UUID;


public class Game {
    @Getter
    private final UUID gameId;
    private final Grid grid;
    List<Player> playerList;
    private Player currentPlayer;
    @Getter
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
        this.currentPlayer = playerList.get(0);
    }

    public synchronized void makeMove(int row, int col) {
        if(gameStatus != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Game is already over");
        }
        
        MoveResult result = grid.makeMove(currentPlayer, row, col);
        
        switch (result) {
            case WIN:
                this.winner = currentPlayer;
                this.gameStatus = GameStatus.WON;
                break;
            case DRAW:
                this.gameStatus = GameStatus.DRAW;
                break;
            case SUCCESS:
                switchPlayer();
                break;
            case INVALID:
                // Move failed, don't switch player
                break;
        }
    }

    private void switchPlayer() {
        this.currentPlayer = currentPlayer.equals(playerList.get(0)) ? playerList.get(1) : playerList.get(0);
    }
}
