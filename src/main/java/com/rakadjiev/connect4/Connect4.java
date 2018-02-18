package com.rakadjiev.connect4;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Connect4 game.
 * Consists of a board and 2 players, who take turns in inserting discs into the board.
 * The game ends if one of the players connects the required number of their own discs, 
 * or if the board fills up.
 * 
 * @author rakadjiev
 *
 */
public class Connect4 {

	/** The Connect4 board */
	private final Board board;
	/** The Connect4 players */
	private final Player[] players;
	
	/** For reading user input from stdin */
	private static final Scanner scanner = new Scanner(System.in);
	
	/**
	 * Create a new Connect4 game.
	 * 
	 * @param playerOne The first player
	 * @param playerTwo The second player
	 */
	public Connect4(final Player playerOne, final Player playerTwo) {
		this.board = new Board();
		this.players = new Player[]{playerOne, playerTwo};
	}
	
	/**
	 * Starts the game.
	 * 
	 * Alternates between the 2 players, reads and executes their moves, and reports the status.
	 */
	public void play() {
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
				Player currentPlayer = players[currentPlayerId];
				
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
						board.insertDisc(currentPlayer.getDisc(), col);
					} catch (InvalidInsertException e) {
						// If the move was invalid, print the reason, and let the player try again
						System.err.println("Invalid move: " + e.getMessage());
						continue;
					}
					
					// Valid move, we can move on
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
	
	/**
	 * Restart the game
	 */
	public void restart() {
		board.reset();
	}
	
	public static void main(String[] args) {
		Player playerOne = new Player("RED", 'R');
		Player playerTwo = new Player("GREEN", 'G');
		
		Connect4 game = new Connect4(playerOne, playerTwo);
		
		game.play();
	}

}
