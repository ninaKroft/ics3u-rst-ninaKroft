package rst;

import javafx.scene.image.ImageView;
import java.net.URL;


public class Player {

	//Data fields
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
	
	
	private ImageView generatePlayer() {
		
		URL location = SpaceInvaders.class.getResource("/images/Player.png");
		playerImage = new ImageView(location.toString());
		playerImage.setX(xVal);
		playerImage.setY(yVal);
		
		return playerImage;
	}
	
	public void hide() {
		//Have to set to 0.0001 because if its set to 0, it sets the width/height to the default
		playerImage.setFitWidth(0.001);
		playerImage.setFitHeight(0.001);
	}
	
	public void show() {
		playerImage.setFitWidth(SpaceInvaders.PLAYER_WIDTH);
		playerImage.setFitHeight(SpaceInvaders.PLAYER_HEIGHT);
	}
}
