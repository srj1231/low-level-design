package org.saumya.lld.ticTacToe;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.saumya.lld.ticTacToe.entities.*;
import org.saumya.lld.ticTacToe.enums.Symbol;

import java.util.UUID;

public class TicTacToeRunner {
    private static final Log logger = LogFactory.getLog(TicTacToeRunner.class);

    public static void main(String[] args) {
        Player player1 = new Player(UUID.randomUUID(), "Player 1", Symbol.X);
        Player player2 = new Player(UUID.randomUUID(), "Player 2", Symbol.O);
        Grid grid = new Grid(new ConsecutiveSymbolsWinStrategy(3), 3);
        Game game = new Game(UUID.randomUUID(), player1, player2, grid);

        game.startGame();
        logger.info("Game started");

        game.makeMove(0, 0);
        game.makeMove(0, 1);
        game.makeMove(0, 2);
        game.makeMove(1, 1);
        game.makeMove(2, 2);
        game.makeMove(2, 1);

        logger.info("Game status: " + game.getGameStatus() + " by " + game.getWinner().getName());
    }
}
