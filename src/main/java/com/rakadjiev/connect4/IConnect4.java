package com.rakadjiev.connect4;

import com.rakadjiev.connect4.exceptions.InvalidInsertException;
import com.rakadjiev.connect4.exceptions.InvalidLocationException;
import com.rakadjiev.connect4.exceptions.InvalidPlayerException;

/**
 * Connect4 game.
 * Consists of a board and players, who take turns in inserting discs into the board.
 * The game ends if one of the players connects the required number of their own discs, 
 * or if the board fills up.
 * 
 * @author rakadjiev
 *
 */
public interface IConnect4 {
	
	/**
	 * Get the board used for this game.
	 * 
	 * @return The board used for this game
	 */
	IBoard getBoard();
	
	/**
	 * Get the number of connected discs required to win the game.
	 * 
	 * @return The number of connected discs required to win the game
	 */
	int getDiscsToWin();
	
	/**
	 * Insert a disc of the specified player into the specified column.
	 * Checks if the move leads to victory, and if needed, update state of the game and board.
	 * 
	 * @param player The player, who plays
	 * @param col The column into which to insert the disc
	 * @return true if the move resulted in victory
	 * @throws InvalidInsertException If the insert was invalid
	 * @throws InvalidPlayerException If the player doesn't participate in this game
	 * @throws InvalidLocationException If the specified column is invalid
	 */
	boolean insertDisc(IPlayer player, int col) throws InvalidInsertException, InvalidPlayerException, InvalidLocationException;
	
	/**
	 * Check if this game has been won by one of the players.
	 * 
	 * @return true if one of the players has won
	 */
	boolean isWon();
	
	/**
	 * Check if this game has been finished with a tie (i.e. board is full, but nobody won).
	 * @return true if the game finished with a tie
	 */
	boolean isTie();
	
	/**
	 * Check if the game is finished (victory or tie).
	 * 
	 * @return true if the game has finished
	 */
	boolean isFinished();
	
	/**
	 * Restart the game
	 */
	void restart();
	
}
