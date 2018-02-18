package com.rakadjiev.connect4.exceptions;

/**
 * Exception thrown in case an invalid player tries to play on a Connect4 Board.
 * 
 * @author rakadjiev
 *
 */
public class InvalidPlayerException extends IllegalArgumentException {

	private static final long serialVersionUID = 2875988934891481531L;

	public InvalidPlayerException(String message) {
		super(message);
	}
	
}
