package com.rakadjiev.connect4.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.rakadjiev.connect4.Board;
import com.rakadjiev.connect4.InvalidInsertException;

public class BoardTest {

	Board board;
	
	@Before
	public void createBoard() {
		board = new Board();
	}
	
	@Test
	public void testSize() {
		boolean discsFitHorizontally = Board.DISCS_TO_WIN <= Board.COLS;
		boolean discsFitVertically = Board.DISCS_TO_WIN <= Board.ROWS;
		assertTrue("The number of discs to win should fit into the table", discsFitHorizontally || discsFitVertically);
	}
	
	@Test
	public void testReset() {
		try {
			board.insertDisc('R', 1);
		} catch(InvalidInsertException e) {
			fail("Inserting a disc should not result in an exception");
		}
		
		board.reset();
		
		// Make sure the board is not full or won
		assertFalse("Board should not be full after reset", board.isFull());
		assertFalse("Board should not be won after reset", board.isWon());
		// Make sure all fields are empty
		for (int i = 1; i <= Board.COLS; i++) {
			for (int j = 1; j <= Board.ROWS; j++) {
				assertFalse("No field should be populated after reset", board.isPopulated(i, j));
			}
		}
	}
	
	/**
	 * Test 
	 * @throws InvalidInsertException
	 */
	@Test
	public void testIsFull() {
		try {
			fillBoard(board);
		} catch(InvalidInsertException e) {
			fail("Filling the board should not result in an exception");
		}
		
		assertTrue("Board should be full after inserting max number of discs", board.isFull());
	}
	
	/**
	 * Test if a winning move is recognized correctly
	 */
	@Test
	public void testIsWon() {
		boolean isWon = false;
		
		try {
			isWon = winBoard(board);
		} catch(InvalidInsertException e) {
			fail("Winning the board should not result in an exception");
		}
		
		assertTrue("insertDisc should return true after the winning move", isWon);
		assertTrue("Board should be won after the winning move", board.isWon());
	}
	
	/**
	 * Test if cells are populated correctly
	 */
	@Test
	public void testIsPopulated() {
		try {
			fillBoard(board);
		} catch(InvalidInsertException e) {
			fail("Filling the board should not result in an exception");
		}
		
		for (int i = 1; i <= Board.COLS; i++) {
			for (int j = 1; j <= Board.ROWS; j++) {
				assertTrue("Field should be populated after inserting a disc into it", board.isPopulated(i, j));
			}
		}
	}
	
	/**
	 * Test disc insertion into a full board
	 */
	@Test
	public void testInserDiscFull() {
		try {
			fillBoard(board);
		} catch(InvalidInsertException e) {
			fail("Filling the board should not result in an exception");
		}
		
		try {
			board.insertDisc('R', 1);
			fail("Inserting a disc to a full board should result in an exception");
		} catch (InvalidInsertException e) {
			assertTrue(e.getMessage().contains("completed"));
		}
	}
	
	/**
	 * Test disc insertion into a won board
	 */
	@Test
	public void testInserDiscWon() {
		try {
			winBoard(board);
		} catch(InvalidInsertException e) {
			fail("Winning the board should not result in an exception");
		}
		
		try {
			board.insertDisc('R', 1);
			fail("Inserting a disc to a won board should result in an exception");
		} catch (InvalidInsertException e) {
			assertTrue(e.getMessage().contains("completed"));
		}
	}
	
	/**
	 * Test disc insertion into invalid columns
	 */
	@Test
	public void testInserDiscInvalidColumn() {
		try {
			board.insertDisc('R', 0);
			fail("Inserting a disc to an invalid column should result in an exception");
		} catch (InvalidInsertException e) {
			assertTrue(e.getMessage().contains("Invalid column"));
		}
		try {
			board.insertDisc('R', -1);
			fail("Inserting a disc to an invalid column should result in an exception");
		} catch (InvalidInsertException e) {
			assertTrue(e.getMessage().contains("Invalid column"));
		}
		try {
			board.insertDisc('R', Board.COLS+1);
			fail("Inserting a disc to an invalid column should result in an exception");
		} catch (InvalidInsertException e) {
			assertTrue(e.getMessage().contains("Invalid column"));
		}
		try {
			board.insertDisc('R', Integer.MAX_VALUE);
			fail("Inserting a disc to an invalid column should result in an exception");
		} catch (InvalidInsertException e) {
			assertTrue(e.getMessage().contains("Invalid column"));
		}
		try {
			board.insertDisc('R', Integer.MIN_VALUE);
			fail("Inserting a disc to an invalid column should result in an exception");
		} catch (InvalidInsertException e) {
			assertTrue(e.getMessage().contains("Invalid column"));
		}
	}
	
	/**
	 * Test insertion of invalid discs
	 */
	@Test
	public void testInserDiscInvalid() {
		// Control characters aren't accepted as disc color
		try {
			board.insertDisc('\0', 1);
			fail("Inserting an invalid disc should result in an exception");
		} catch (InvalidInsertException e) {
			assertTrue(e.getMessage().contains("Invalid disc"));
		}
		try {
			char disc = 127;
			board.insertDisc(disc, 1);
			fail("Inserting an invalid disc should result in an exception");
		} catch (InvalidInsertException e) {
			assertTrue(e.getMessage().contains("Invalid disc"));
		}
	}
	
	/**
	 * Test disc insertion into a full column
	 */
	@Test
	public void testInserDiscFullColumn() {
		try {
			for (int j = 1; j <= Board.ROWS; j++) {
				board.insertDisc((j%2 == 0) ? 'R' : 'G', 1);
			}
		} catch(InvalidInsertException e) {
			fail("Inserting a disc should not result in an exception");
		}
		
		try {
			board.insertDisc('R', 1);
			fail("Inserting a disc into a full column should result in an exception");
		} catch (InvalidInsertException e) {
			assertTrue(e.getMessage().contains("already full"));
		}
	}
	
	/**
	 * Test normal disc insertion
	 */
	@Test
	public void testInserDisc() {
		try {
			board.insertDisc('R', 1);
			assertTrue("Cell should be populated after inserting a disc into it", board.isPopulated(1, 1));
		} catch (InvalidInsertException e) {
			fail("Inserting a disc should not result in an exception");
		}
	}
	
	/**
	 * Wins a board
	 * 
	 * @param board The board to win
	 * @return The return value of the last insertDisc call
	 * @throws InvalidInsertException
	 */
	private boolean winBoard(Board board) throws InvalidInsertException {
		boolean result = false;
		
		boolean discsFitHorizontally = Board.DISCS_TO_WIN <= Board.COLS;
		
		for (int i = 1; i <= Board.DISCS_TO_WIN; i++) {
			result = board.insertDisc('R', discsFitHorizontally ? i : 1);
		}
		
		return result;
	}

	/**
	 * Fills the board with different discs.
	 * 
	 * @param board The board to fill
	 * @throws InvalidInsertException
	 */
	private void fillBoard(Board board) throws InvalidInsertException {
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
}
