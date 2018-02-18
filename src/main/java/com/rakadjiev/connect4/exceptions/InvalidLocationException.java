package com.rakadjiev.connect4.exceptions;

/**
 * Exception thrown in case an invalid location has been referenced on a Connect4 Board.
 * 
 * @author rakadjiev
 *
 */
public class InvalidLocationException extends IllegalArgumentException {

	private static final long serialVersionUID = 2875988934891481531L;

	public InvalidLocationException(String message) {
		super(message);
	}
	
}
