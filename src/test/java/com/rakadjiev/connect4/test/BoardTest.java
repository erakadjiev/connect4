package com.rakadjiev.connect4.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.rakadjiev.connect4.IBoard;
import com.rakadjiev.connect4.exceptions.InvalidInsertException;
import com.rakadjiev.connect4.exceptions.InvalidLocationException;
import com.rakadjiev.connect4.impl.Board;

public class BoardTest {

	IBoard board;
	
	@Before
	public void setUp() {
		board = new Board();
	}
	
	/**
	 * Test that resetting the board sets the state to the initial one
	 */
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
		assertEquals("Board should have 0 discs after reset", board.getNumberOfDiscs(), 0);
		// Make sure all fields are empty
		for (int i = 1; i <= Board.COLS; i++) {
			for (int j = 1; j <= Board.ROWS; j++) {
				assertFalse("No field should be populated after reset", board.isPopulated(i, j));
			}
		}
	}
	
	/**
	 * Test that the board is reported correctly to be full or not
	 */
	@Test
	public void testIsFull() {
		assertFalse("Board should not be full before inserting max number of discs", board.isFull());
		
		try {
			Connect4TestUtil.fillBoard(board);
		} catch (InvalidInsertException e) {
			fail("Filling the board should not result in an exception");
		}
		
		assertTrue("Board should be full after inserting max number of discs", board.isFull());
	}
	
	/**
	 * Test that the board is reported correctly to be won or not
	 */
	@Test
	public void testIsWon() {
		assertFalse("Board should not be won before marking it so", board.isWon());
		board.setWon();
		assertTrue("Board should be won after marking it so", board.isWon());
	}
	
	/**
	 * Test the reported number of discs inserted
	 */
	@Test
	public void testGetNumberOfDiscs() {
		assertEquals("Before inserting the first disc, the number of discs inserted should be 0", board.getNumberOfDiscs(), 0);
		
		try {
			board.insertDisc('R', 1);
		} catch(InvalidInsertException e) {
			fail("Inserting a disc should not result in an exception");
		}
		
		assertEquals("After inserting the first disc, the number of discs inserted should be 1", board.getNumberOfDiscs(), 1);
		
		board.reset();
		
		try {
			Connect4TestUtil.fillBoard(board);
		} catch (InvalidInsertException e) {
			fail("Filling the board should not result in an exception");
		}
		
		assertEquals("After filling the board, the number of discs inserted should be COLS*ROWS", board.getNumberOfDiscs(), board.getCols()*board.getRows());
	}

	/**
	 * Test if cells are populated correctly
	 */
	@Test
	public void testIsPopulated() {
		try {
			Connect4TestUtil.fillBoard(board);
		} catch (InvalidInsertException e) {
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
	public void testinsertDiscFull() {
		try {
			Connect4TestUtil.fillBoard(board);
		} catch (InvalidInsertException e) {
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
	public void testinsertDiscWon() {
		board.setWon();
		
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
	public void testinsertDiscInvalidColumn() {
		try {
			try {
				board.insertDisc('R', 0);
				fail("Inserting a disc to an invalid column should result in an exception");
			} catch (InvalidLocationException e) {
				assertTrue(e.getMessage().contains("Invalid column"));
			}
			try {
				board.insertDisc('R', -1);
				fail("Inserting a disc to an invalid column should result in an exception");
			} catch (InvalidLocationException e) {
				assertTrue(e.getMessage().contains("Invalid column"));
			}
			try {
				board.insertDisc('R', Board.COLS+1);
				fail("Inserting a disc to an invalid column should result in an exception");
			} catch (InvalidLocationException e) {
				assertTrue(e.getMessage().contains("Invalid column"));
			}
			try {
				board.insertDisc('R', Integer.MAX_VALUE);
				fail("Inserting a disc to an invalid column should result in an exception");
			} catch (InvalidLocationException e) {
				assertTrue(e.getMessage().contains("Invalid column"));
			}
			try {
				board.insertDisc('R', Integer.MIN_VALUE);
				fail("Inserting a disc to an invalid column should result in an exception");
			} catch (InvalidLocationException e) {
				assertTrue(e.getMessage().contains("Invalid column"));
			}
		} catch (InvalidInsertException e) {
			fail("Trying to insert a disc to an invalid column should result in an InvalidLocationException");
		}
	}
	
	/**
	 * Test insertion of invalid discs
	 */
	@Test
	public void testinsertDiscInvalid() {
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
	public void testinsertDiscFullColumn() {
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
	public void testInsertDisc() {
		try {
			int row = board.insertDisc('R', 1);
			assertTrue("Cell should be populated after inserting a disc into it", board.isPopulated(1, 1));
			assertEquals("Cell should contain the same disc as the one inserted", board.getDisc(1, row), 'R');
		} catch (InvalidInsertException | InvalidLocationException e) {
			fail("Inserting a disc should not result in an exception");
		}
	}
}
