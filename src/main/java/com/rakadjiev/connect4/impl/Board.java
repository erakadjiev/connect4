package com.rakadjiev.connect4.impl;

import com.rakadjiev.connect4.IBoard;
import com.rakadjiev.connect4.exceptions.InvalidInsertException;
import com.rakadjiev.connect4.exceptions.InvalidLocationException;

/**
 * A Connect4 board, consisting of a pre-defined number of columns and rows.
 * Discs are inserted into the columns from the top, one at a time, and stack on top
 * of each other.
 * The board is completed if at least a pre-defined number of discs of the same color are connected 
 * horizontally, vertically, or diagonally.
 * If the board fills up and the board hasn't been completed yet, then that's a tie.
 * 
 * @author rakadjiev
 */
public class Board implements IBoard {

	/** The number of columns the board has */
	public static final int COLS = 7;
	/** The number of rows the board has */
	public static final int ROWS = 6;
	
	/** The value representing an empty space in the board (default value of char) */
	private static final char NO_DISC = '\0';
	
	/** The discs inserted to the board */
	private char[][] discs = new char[COLS][ROWS];
	
	/** The number of discs inserted */
	private int discsInserted = 0;
	/** If the board has been marked as won */
	private boolean isWon;
	
	/**
	 * Insert a disc into the specified column of the board from the top of the board.
	 * The disc will fall to the first available space in the column and stack on previous discs.
	 * 
	 * @param disc The disc to insert. Must be a printable ASCII character.
	 * @param col The column into which to insert the disc (has to be between 1 and {@value #COLS})
	 * @return The row into which the disc was inserted
	 * @throws InvalidInsertException If the disc cannot be inserted
	 * @throws InvalidLocationException If the specified column is invalid
	 */
	@Override
	public int insertDisc(final char disc, final int col) throws InvalidInsertException, InvalidLocationException {
		// Check if the board has been completed
		if (isWon() || isFull()) {
			throw new InvalidInsertException("The board has already been completed.");
		}
		
		// Check if the specified column exists
		if (col < 1 || col > COLS) {
			throw new InvalidLocationException("Invalid column: '" + col + "'. Column must be between 1 and " + COLS);
		}
		
		// Check if the disc representation is valid
		if (!isValidDisc(disc)) {
			throw new InvalidInsertException("Invalid disc color: '" + String.format ("\\u%04x", (int) disc) + "'. Disc color must be a printable ASCII character");
		}
		
		// Check if the column is full
		if (isPopulated(col, ROWS)) {
			throw new InvalidInsertException("Column: '" + col + "' already full.");
		}
		
		// Internally, we use 0-based indices
		int colInternal = col - 1;
		int rowInternal = 0;
		
		// Find the first empty space in the column
		while (discs[colInternal][rowInternal] != NO_DISC && rowInternal < ROWS) {
			rowInternal++;
		}
		
		// Insert the disc into the first empty space
		discs[colInternal][rowInternal] = disc;
		discsInserted++;
		
		return rowInternal + 1;
	}
	
	/**
	 * Check if the disc color/representation is valid.
	 * Only printable ASCII characters are accepted.
	 * 
	 * @param disc The color (char representation) of the disc
	 * @return true if the disc representation is valid
	 */
	private boolean isValidDisc(final char disc) {
		boolean result = false;
		
		if (disc > 31 && disc < 127) {
			result = true;
		}
		
		return result;
	}
	
	@Override
	public boolean isPopulated(final int col, final int row) throws InvalidLocationException {
		boolean result = false;
		
		if (getDisc(col, row) != NO_DISC) {
			result = true;
		}
		
		return result;
	}
	
	@Override
	public char getDisc(final int col, final int row) throws InvalidLocationException {
		if (col < 1 || col > COLS) {
			throw new InvalidLocationException("Invalid column: '" + col + "'. Column must be between 1 and " + COLS);
		}
		if (row < 1 || row > COLS) {
			throw new InvalidLocationException("Invalid row: '" + row + "'. Row must be between 1 and " + ROWS);
		}
		
		return discs[col - 1][row - 1];
	}
	
	@Override
	public int getCols() {
		return COLS;
	}

	@Override
	public int getRows() {
		return ROWS;
	}
	
	@Override
	public int getNumberOfDiscs() {
		return discsInserted;
	}
	
	@Override
	public boolean isFull() {
		return discsInserted == COLS * ROWS;
	}
	
	@Override
	public boolean isWon() {
		return isWon;
	}
	
	@Override
	public void setWon() {
		this.isWon = true;
	}
	
	@Override
	public void reset() {
		discs = new char[COLS][ROWS];
		discsInserted = 0;
		isWon = false;
	}
	
	/**
	 * Returns a string representation of the board, showing all inserted discs.
	 * 
	 * @return String representation of the board's current state
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int j = ROWS-1; j >= 0; j--) {
			sb.append('|');
			for (int i = 0; i < COLS; i++) {
				char disc = discs[i][j] == NO_DISC ? ' ' : discs[i][j];
				
				sb.append(disc);
				sb.append('|');
			}
			sb.append('\n');
		}
		
		return sb.toString();
	}
}
