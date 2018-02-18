package com.rakadjiev.connect4;

/**
 * A Connect4 player, who has a name and a disc color.
 * 
 * @author rakadjiev
 */
public class Player {

	/** The name of the player */
	private final String name;
	/** The disc color of the player */
	private final char disc;
	
	/**
	 * Create a new player.
	 * 
	 * @param name The name of the player
	 * @param disc The disc color of the player
	 */
	public Player(final String name, final char disc) {
		this.name = name;
		this.disc = disc;
	}
	
	/**
	 * Returns the name of the player.
	 * 
	 * @return The name of the player
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the disc color of the player.
	 * 
	 * @return The disc color of the player
	 */
	public char getDisc() {
		return disc;
	}
	
}
