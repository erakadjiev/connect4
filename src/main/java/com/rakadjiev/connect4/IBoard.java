package com.rakadjiev.connect4;

import com.rakadjiev.connect4.exceptions.InvalidInsertException;
import com.rakadjiev.connect4.exceptions.InvalidLocationException;

/**
 * A Connect4 board, consisting of a pre-defined number of columns and rows.
 * 
 * @author rakadjiev
 */
public interface IBoard {

	/**
	 * Insert a disc into the specified column of the board.
	 * 
	 * @param disc The disc to insert.
	 * @param col The column into which to insert the disc
	 * @return The row into which the disc was inserted
	 * @throws InvalidInsertException If the disc cannot be inserted
	 * @throws InvalidLocationException If the specified column is invalid
	 */
	int insertDisc(char disc, int col) throws InvalidInsertException, InvalidLocationException;
	
	/**
	 * Return the number of columns of this board.
	 * 
	 * @return The number of columns of this board
	 */
	int getCols();
	
	/**
	 * Return the number of rows of this board.
	 * 
	 * @return The number of rows of this board
	 */
	int getRows();
	
	/**
	 * Check if a cell in the table already contains a disc.
	 * 
	 * @param col The column to check (1-based index)
	 * @param row The row to check (1-based index)
	 * @return true if the cell already contains a disc
	 * @throws InvalidLocationException If an invalid column or row has been specified
	 */
	boolean isPopulated(int col, int row) throws InvalidLocationException;
	
	/**
	 * Gets the disc at the specified location.
	 * 
	 * @param col The column
	 * @param row The row
	 * @return The disc at the specified location
	 * @throws InvalidLocationException If an invalid column or row has been specified
	 */
	char getDisc(int col, int row) throws InvalidLocationException;
	
	/**
	 * Get the total number of discs inserted into the board.
	 * 
	 * @return The number of discs inserted into the board
	 */
	int getNumberOfDiscs();
	
	/**
	 * Check if the board is full, i.e. all spaces have been filled by discs.
	 * 
	 * @return true if the board is full
	 */
	boolean isFull();
	
	/**
	 * Check if the board has been marked as won. 
	 * 
	 * @return true if the board has been marked as won
	 */
	boolean isWon();
	
	/**
	 * Mark the board as won.
	 */
	void setWon();
	
	/**
	 * Reset the board to initial state.
	 */
	void reset();
	
}
