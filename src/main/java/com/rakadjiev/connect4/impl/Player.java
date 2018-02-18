package com.rakadjiev.connect4.impl;

import com.rakadjiev.connect4.IPlayer;

/**
 * @see IPlayer
 * @author rakadjiev
 */
public class Player implements IPlayer {

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
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public char getDisc() {
		return disc;
	}
	
}
