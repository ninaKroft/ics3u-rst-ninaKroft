package rst;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Author: Nina Kroft
 * Date: Tuesday June 18th 2024
 * Course: ICS3U
 * Beam.java
 * A class for the RST / Final Project for the ICS3U course
 */

public class Beam {

	//Defining data fields
	Rectangle rect;
	boolean hit = false;
	
	//Constructor
	Beam(double xVal, double yVal) {
		this.rect = new Rectangle(xVal, yVal, SpaceInvaders.BEAM_WIDTH, SpaceInvaders.BEAM_HEIGHT);
		rect.setFill(Color.RED);
	
	}
	
	/**
	 * This method sets the beam's x and y value to be centered to the player, giving the appearance of being shot
	 * 
	 * @param playerX
	 * 				The player's x value
	 * 
	 * @param playerY
	 * 				The player's y value
	 */
	public void isShot(double playerX, double playerY) {
		show();
		rect.setX(playerX + SpaceInvaders.PLAYER_WIDTH / 2);
		rect.setY(playerY);
		hit = false;
	}
	
	/**
	 * This method shows the beam
	 */
	public void show() {
		rect.setVisible(true);
	}
	
	/**
	 * This method hides the beam
	 */
	public void hide() {
		//Have to set to 0.001 because or else it is set to the default
		rect.setVisible(false);
	}

}
