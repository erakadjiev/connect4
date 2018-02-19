package com.rakadjiev.connect4.impl;

import java.util.NoSuchElementException;
import java.util.Scanner;

import com.rakadjiev.connect4.IBoard;
import com.rakadjiev.connect4.IConnect4;
import com.rakadjiev.connect4.IPlayer;
import com.rakadjiev.connect4.exceptions.InvalidInsertException;
import com.rakadjiev.connect4.exceptions.InvalidLocationException;
import com.rakadjiev.connect4.exceptions.InvalidPlayerException;

/**
 * @see IConnect4
 * @author rakadjiev
 *
 */
public class Connect4 implements IConnect4 {

	/** The number of connected discs required to win */
	private static final int DISCS_TO_WIN = 4;
	
	/** The directions in which to search for connected discs */
	private static final int[] searchCols = {-1, -1, -1, 0, 1, 1, 1};
	private static final int[] searchRows = {1, 0, -1, -1, -1, 0, 1};
	
	/** The Connect4 board */
	private final IBoard board;
	/** The Connect4 players */
	private final IPlayer[] players;
	
	/** For reading user input from stdin */
	private static final Scanner scanner = new Scanner(System.in);
	
	/**
	 * Create a new Connect4 game.
	 * 
	 * @param playerOne The first player
	 * @param playerTwo The second player
	 * @throws NullPointerException If playerOne or playerTwo is null
	 */
	public Connect4(final IPlayer playerOne, final IPlayer playerTwo) throws NullPointerException {
		if (playerOne == null || playerTwo == null) {
			throw new NullPointerException("Players must not be null.");
		}
		this.board = new Board();
		this.players = new IPlayer[]{playerOne, playerTwo};
	}
	
	/**
	 * Starts an interactive game.
	 * 
	 * Alternates between the 2 players, reads and executes their moves, and reports the status.
	 */
	public void playInteractive() {
		System.out.println("Welcome to Connect4!\n");
		
		boolean finished = false;
		
		while (!finished) {
			// Keeps track of whose turn it is
			int currentPlayerId = -1;
			
			System.out.println(board.toString());
			
			// Play until the game is finished
			while (!board.isFull() || board.isWon()) {
				// Get the next player (modulo operation makes it circular, i.e. it wraps over at the end of the array)
				currentPlayerId = ++currentPlayerId % players.length;
				IPlayer currentPlayer = players[currentPlayerId];
				
				// If the player's move was valid
				boolean validMove = false;
				
				// The player can try again until they make a valid move
				while (!validMove) {
					System.out.print("Player " + (currentPlayerId + 1) + " [" + currentPlayer.getName() + "] - choose column (1-" + Board.COLS + "): ");
					
					// Read which column the player chose
					String colString = null;
					try {
						colString = scanner.nextLine().trim();
					} catch (NoSuchElementException | IllegalStateException e) {
						// The scanner/stream has been closed, we can't continue
						return;
					}
					int col = -1;
					// Check if the input was valid (i.e. an integer)
					try {
						col = Integer.parseInt(colString);
					} catch (NumberFormatException e) {
						System.err.println("Invalid column: '" + colString + "'. Column must be an integer.");
						// Invalid input - the player can try again
						continue;
					}
					
					try {
						// Execute the move
						insertDisc(currentPlayer, col);
					} catch (InvalidInsertException | InvalidLocationException e) {
						// If the move was invalid, print the reason, and let the player try again
						System.err.println("Invalid move: " + e.getMessage());
						continue;
					}
					
					validMove = true;
				}
				
				System.out.println(board.toString());
				
				// Check if the player won the game with their last move
				if (board.isWon()) {
					System.out.println("Player " + (currentPlayerId + 1) + " [" + currentPlayer.getName() + "] wins!");
					break;
				}
				
			}
			
			// If the board has filled up, but nobody has won, that's a tie
			if (board.isFull() && !board.isWon()) {
				System.out.println("That's a tie!");
			}
			
			System.out.print("Play again? [y/n] ");
			String playAgain = scanner.nextLine().trim();
			if (!playAgain.equalsIgnoreCase("y")) {
				finished = true;
			} else {
				restart();
				System.out.println("\n-------- NEW GAME --------\n");
			}
		}
	}
	
	@Override
	public boolean insertDisc(final IPlayer player, final int col) throws InvalidInsertException, InvalidPlayerException, InvalidLocationException {
		boolean validPlayer = false;
		for (IPlayer somePlayer : players) {
			if (somePlayer == player) {
				validPlayer = true;
				break;
			}
		}
		
		if (!validPlayer) {
			throw new InvalidPlayerException("Player " + (player != null ? player.getName() : "null") + " doesn't participate in this game.");
		}
		
		int row = board.insertDisc(player.getDisc(), col);
		
		boolean isWon = checkWin(col, row);
		// Update the board's status
		if (isWon) {
			board.setWon();
		}
		
		return isWon;
	}
	
	@Override
	public int getDiscsToWin() {
		return DISCS_TO_WIN;
	}
	
	@Override
	public boolean isWon() {
		return board.isWon();
	}
	
	@Override
	public boolean isTie() {
		return board.isFull() && !board.isWon();
	}
	
	@Override
	public boolean isFinished() {
		return isWon() || isTie();
	}
	
	@Override
	public void restart() {
		board.reset();
	}
	
	@Override
	public IBoard getBoard() {
		return board;
	}
	
	/**
	 * Checks if the latest inserted disc in the specified position is part of a winning 
	 * combination, i.e. a sequence of at least {@value #DISCS_TO_WIN} discs of the same 
	 * color, which are connected horizontally, vertically, or diagonally.
	 * 
	 * @param col The column of the disc
	 * @param row The row of the disc
	 * @return true if the disc is part of a winning combination
	 * @throws InvalidLocationException 
	 */
	private boolean checkWin(final int col, final int row) throws InvalidLocationException {
		
		// If we have less discs in total than the number required to win, we short-circuit
		if (board.getNumberOfDiscs() < DISCS_TO_WIN) {
			return false;
		}
		
		char disc = board.getDisc(col, row);
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
			while (i > 0 && i <= Board.COLS && j > 0 && j <= Board.ROWS && discsConnected < DISCS_TO_WIN) {
				/* 
				 * If the disc is the same color as the start disc, then we increment the 
				 * connected count.
				 * Otherwise, we abandon that direction
				 */
				if (board.getDisc(i, j) == disc) {
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
				break;
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		Player playerOne = new Player("RED", 'R');
		Player playerTwo = new Player("GREEN", 'G');
		
		Connect4 game = new Connect4(playerOne, playerTwo);
		
		game.playInteractive();
	}

}
