package com.rakadjiev.connect4;

/**
 * Exception thrown in case an invalid move has been made on a Connect4 Board.
 * 
 * @author rakadjiev
 */
public class InvalidInsertException extends Exception {

	private static final long serialVersionUID = 2804476695125663411L;

	public InvalidInsertException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidInsertException(String message) {
		super(message);
	}
	
}
