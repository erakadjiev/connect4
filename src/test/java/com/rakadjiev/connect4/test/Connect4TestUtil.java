package com.rakadjiev.connect4.test;

import com.rakadjiev.connect4.IBoard;
import com.rakadjiev.connect4.IConnect4;
import com.rakadjiev.connect4.IPlayer;
import com.rakadjiev.connect4.exceptions.InvalidInsertException;
import com.rakadjiev.connect4.exceptions.InvalidLocationException;
import com.rakadjiev.connect4.exceptions.InvalidPlayerException;
import com.rakadjiev.connect4.impl.Board;

public class Connect4TestUtil {

	/**
	 * Fills the board with different discs.
	 * 
	 * @param board The board to fill
	 * @throws InvalidInsertException
	 */
	public static void fillBoard(IBoard board) throws InvalidInsertException, InvalidLocationException {
		// Fill all fields with different discs
		char disc = 32;
		for (int i = 1; i <= Board.COLS; i++) {
			for (int j = 1; j <= Board.ROWS; j++) {
				if (disc > 126) {
					disc = 32;
				}
				board.insertDisc(disc++, i);
			}
		}
	}
	
	/**
	 * Wins a game for a player
	 * 
	 * @param game The game to win
	 * @param plyer The player with which to win
	 * @return The return value of the last insertDisc call
	 * @throws InvalidInsertException
	 * @throws InvalidLocationException 
	 * @throws InvalidPlayerException 
	 */
	public static boolean winGame(IConnect4 game, IPlayer player) throws InvalidInsertException, InvalidPlayerException, InvalidLocationException {
		boolean result = false;
		
		boolean discsFitHorizontally = game.getDiscsToWin() <= game.getBoard().getCols();
		
		for (int i = 1; i <= game.getDiscsToWin(); i++) {
			result = game.insertDisc(player, discsFitHorizontally ? i : 1);
		}
		
		return result;
	}
	
}
