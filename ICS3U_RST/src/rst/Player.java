package rst;

import javafx.scene.image.ImageView;
import java.net.URL;


public class Player {

	//Defining data fields
	double xVal, yVal;
	boolean alive;
	ImageView playerImage;
	
	//Constructor
	Player(double xVal, double yVal, boolean alive) {
		this.xVal = xVal;
		this.yVal = yVal;
		this.alive = alive;
		this.playerImage = generatePlayer();
	}
	
	/**
	 * This method generates the player at the specified x and y coordinate the object was created with
	 * 
	 * @return The ImageView of the player
	 */
	private ImageView generatePlayer() {
		
		URL location = SpaceInvaders.class.getResource("/images/Player.png");
		playerImage = new ImageView(location.toString());
		playerImage.setX(xVal);
		playerImage.setY(yVal);
		
		return playerImage;
	}
	
	/**
	 * This method hides the player
	 */
	public void hide() {
		playerImage.setVisible(false);
	}
	
	/**
	 * This method shows the player
	 */
	public void show() {
		playerImage.setVisible(true);
	}
}
