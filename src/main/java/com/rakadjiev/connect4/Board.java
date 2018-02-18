package com.rakadjiev.connect4;

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
public class Board {

	/** The number of columns the board has */
	public static final int COLS = 7;
	/** The number of rows the board has */
	public static final int ROWS = 6;
	/** The number of connected discs required to win */
	public static final int DISCS_TO_WIN = 4;
	
	/** The value representing an empty space in the board (default value of char) */
	private static final char NO_DISC = '\0';
	
	/** The directions in which to search for connected discs */
	private static final int[] searchCols = {-1, -1, -1, 0, 1, 1, 1};
	private static final int[] searchRows = {1, 0, -1, -1, -1, 0, 1};
	
	/** The discs inserted to the board */
	private char[][] discs = new char[COLS][ROWS];
	
	/** The number of discs inserted */
	private int discsInserted = 0;
	/** If the board has been completed by connecting at least {@link #DISCS_TO_WIN} discs */
	private boolean isWon;
	
	/**
	 * Insert a disc into the specified column of the board from the top of the board.
	 * The disc will fall to the first available space in the column and stack on previous discs.
	 * 
	 * @param disc The disc to insert. Must be a printable ASCII character.
	 * @param col The column into which to insert the disc (has to be between 1 and {@value #COLS})
	 * @return true if the current move results in victory, i.e. at least {@value #DISCS_TO_WIN} discs of the same color become connected
	 * @throws InvalidInsertException If the disc cannot be inserted
	 */
	public boolean insertDisc(final char disc, final int col) throws InvalidInsertException {
		// Check if the board has been completed
		if (isWon() || isFull()) {
			throw new InvalidInsertException("The board has already been completed.");
		}
		
		// Check if the specified column exists
		if (col < 1 || col > COLS) {
			throw new InvalidInsertException("Invalid column: '" + col + "'. Column must be between 1 and " + COLS);
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
		
		// Check if the current move has resulted in victory
		return checkWin(colInternal, rowInternal);
	}
	
	/**
	 * Checks if the latest inserted disc in the specified position is part of a winning 
	 * combination, i.e. a sequence of at least {@value #DISCS_TO_WIN} discs of the same 
	 * color, which are connected horizontally, vertically, or diagonally.
	 * 
	 * @param col The column of the disc (0-based index)
	 * @param row The row of the disc (0-based index)
	 * @return true if the disc is part of a winning combination
	 */
	private boolean checkWin(final int col, final int row) {
		
		// If we have less discs in total than the number required to win, we short-circuit
		if (discsInserted < (DISCS_TO_WIN)) {
			return false;
		}
		
		char disc = discs[col][row];
		boolean result = false;
		
		/*
		 * Search in the following directions for discs of the same color:
		 * 
		 * - Horizontal: left, right
		 * - Vertical: bottom
		 * - Diagonal: top-left, bottom-left, bottom-right, top-right
		 * 
		 * Note that we don't search towards the top, since this method is supposed 
		 * to be used for newly inserted discs, which means that there can't be any 
		 * discs above.
		 * 
		 * As soon as we find a non-matching disc in the current search direction, we 
		 * abort that search.
		 * As soon as we find the required number of matching discs in any direction, 
		 * we return true.
		 */
		for (int searchIdx = 0; searchIdx < searchCols.length; searchIdx++) {
			// The start disc is the first of the sequence
			int discsConnected = 1;
			
			int i = col + searchCols[searchIdx];
			int j = row + searchRows[searchIdx];
			// Move in the specified direction, disc by disc
			while (i >= 0 && i < COLS && j >= 0 && j < ROWS && discsConnected < DISCS_TO_WIN) {
				/* 
				 * If the disc is the same color as the start disc, then we increment the 
				 * connected count.
				 * Otherwise, we abandon that direction
				 */
				if (discs[i][j] == disc) {
					discsConnected++;
				} else {
					break;
				}
				// Move to the next disc in the current direction
				i = i + searchCols[searchIdx];
				j = j + searchRows[searchIdx];
			}
			
			// Check if we have found the required number of connected discs
			if (discsConnected == DISCS_TO_WIN) {
				result = true;
				isWon = true;
				break;
			}
		}
		
		return result;
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
	
	/**
	 * Check if a cell in the table already contains a disc.
	 * 
	 * @param col The column to check (1-based index)
	 * @param row The row to check (1-based index)
	 * @return true if the cell already contains a disc
	 */
	public boolean isPopulated(final int col, final int row) {
		boolean result = false;
		
		if (discs[col - 1][row - 1] != NO_DISC) {
			result = true;
		}
		
		return result;
	}
	
	/**
	 * Reset the board to initial state.
	 */
	public void reset() {
		discs = new char[COLS][ROWS];
		discsInserted = 0;
		isWon = false;
	}
	
	/**
	 * Check if the board is full, i.e. all spaces have been filled by discs.
	 * 
	 * @return true if the board is full
	 */
	public boolean isFull() {
		return discsInserted == COLS * ROWS;
	}
	
	/**
	 * Check if the board has been won already, i.e. there's a sequence of at least 
	 * {@value #DISCS_TO_WIN} connected discs of the same color.
	 * @return true if the board has been won
	 */
	public boolean isWon() {
		return isWon;
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
