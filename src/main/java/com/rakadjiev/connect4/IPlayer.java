package com.rakadjiev.connect4;

/**
 * A Connect4 player, who has a name and a disc color.
 * 
 * @author rakadjiev
 */
public interface IPlayer {

	/**
	 * Returns the name of the player.
	 * 
	 * @return The name of the player
	 */
	String getName();
	
	/**
	 * Returns the disc color of the player.
	 * 
	 * @return The disc color of the player
	 */
	char getDisc();
	
}
