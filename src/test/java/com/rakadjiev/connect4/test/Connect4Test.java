package com.rakadjiev.connect4.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.rakadjiev.connect4.IBoard;
import com.rakadjiev.connect4.IConnect4;
import com.rakadjiev.connect4.IPlayer;
import com.rakadjiev.connect4.exceptions.InvalidInsertException;
import com.rakadjiev.connect4.exceptions.InvalidLocationException;
import com.rakadjiev.connect4.exceptions.InvalidPlayerException;
import com.rakadjiev.connect4.impl.Connect4;
import com.rakadjiev.connect4.impl.Player;

public class Connect4Test {

	IConnect4 game;
	IBoard board;
	IPlayer playerOne;
	IPlayer playerTwo;
	IPlayer playerThree;
	
	@Before
	public void setUp() throws Exception {
		playerOne = new Player("RED", 'R');
		playerTwo = new Player("GREEN", 'G');
		playerThree = new Player("BLUE", 'B');
		game = new Connect4(playerOne, playerTwo);
		board = game.getBoard();
	}

	/**
	 * Test that the board can fit at least as many connected discs as needed to win
	 */
	@Test
	public void testSize() {
		boolean discsFitHorizontally = game.getDiscsToWin() <= board.getCols();
		boolean discsFitVertically = game.getDiscsToWin() <= board.getRows();
		assertTrue("The number of discs to win should fit into the board", discsFitHorizontally || discsFitVertically);
	}
	
	/**
	 * Test if a winning move is recognized correctly
	 */
	@Test
	public void testIsWon() {
		assertFalse("Board should note be won before the winning move", board.isWon());
		assertFalse("Game should not be won before the winning move", game.isWon());
		assertFalse("Game should not be finished before the winning move", game.isFinished());
		assertFalse("Game should not be tie before the winning move", game.isTie());
		
		boolean isWon = false;
		try {
			isWon = Connect4TestUtil.winGame(game, playerOne);
		} catch(InvalidInsertException | InvalidLocationException | InvalidPlayerException e) {
			fail("Winning the game should not result in an exception");
		}
		
		assertTrue("insertDisc should return true after the winning move", isWon);
		assertTrue("Board should be won after the winning move", board.isWon());
		assertTrue("Game should be won after the winning move", game.isWon());
		assertTrue("Game should be finished after the winning move", game.isFinished());
		assertFalse("Game should not be tie after the winning move", game.isTie());
	}
	
	/**
	 * Test if a tie is recognized correctly
	 */
	@Test
	public void testIsTie() {
		assertFalse("Board should note be won before filling it", board.isWon());
		assertFalse("Game should not be won before filling the board", game.isWon());
		assertFalse("Game should not be finished before filling the board", game.isFinished());
		assertFalse("Game should not be tie before filling the board", game.isTie());
		
		try {
			Connect4TestUtil.fillBoard(board);
		} catch(InvalidInsertException | InvalidLocationException e) {
			fail("Filing the board should not result in an exception");
		}
		
		assertTrue("Game should be tie after filling the board", game.isTie());
		assertFalse("Board should not be won after filling the board", board.isWon());
		assertFalse("Game should be won after filling the board", game.isWon());
		assertTrue("Game should be finished filling the board", game.isFinished());
	}
	
	/**
	 * Test that only the players of the game can insert discs
	 */
	@Test(expected = InvalidPlayerException.class)
	public void testInvalidPlayer() {
		try {
			game.insertDisc(playerThree, 1);
		} catch(InvalidInsertException e) {
			fail("Inserting a disc should not result in an InvalidInsertException");
		}
	}
	
	/**
	 * Test that restarting the game sets the state to the initial one
	 */
	@Test
	public void testReset() {
		try {
			game.insertDisc(playerOne, 1);
		} catch(InvalidInsertException e) {
			fail("Inserting a disc should not result in an exception");
		}
		
		game.restart();
		
		assertFalse("Game should not be won after restarting", game.isWon());
		assertFalse("Game should not be finished after restarting", game.isFinished());
		assertFalse("Game should not be tie after restarting", game.isTie());
		
		assertFalse("Board should not be full after restarting", board.isFull());
		assertFalse("Board should not be won after restarting", board.isWon());
		assertEquals("Board should have 0 discs after reset", board.getNumberOfDiscs(), 0);
	}
	
	/**
	 * Test normal disc insertion
	 */
	@Test
	public void testInsertDisc() {
		try {
			game.insertDisc(playerOne, 1);
			assertTrue("Cell should be populated after inserting a disc into it", board.isPopulated(1, 1));
			assertEquals("Cell should contain the same disc as the one inserted", board.getDisc(1, 1), playerOne.getDisc());
		} catch (InvalidInsertException | InvalidLocationException e) {
			fail("Inserting a disc should not result in an exception");
		}
	}
	
}
