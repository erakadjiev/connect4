package com.rakadjiev.connect4.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.rakadjiev.connect4.impl.Player;

public class PlayerTest {

	/**
	 * Test that a NullPointerException is thrown only if the player's name is null
	 */
	@Test
	public void testNameNull() {
		try {
			new Player(null, 'R');
			fail("Creating a player with null name should result in a NullPointerException.");
		} catch (NullPointerException e) {
		}
		try {
			new Player("RED", 'R');
		} catch (NullPointerException e) {
			fail("Creating a player with non-null name should not result in a NullPointerException.");
		}
	}

}
