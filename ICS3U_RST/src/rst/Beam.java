package rst;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Beam {

	double xVal, yVal;
	Rectangle rect;
	boolean hit = false;
	
	//Constructor
	Beam(double xVal, double yVal) {
		this.xVal = xVal;
		this.yVal = yVal;
		this.rect = new Rectangle(xVal, yVal, SpaceInvaders.BEAM_WIDTH, SpaceInvaders.BEAM_HEIGHT);
		rect.setFill(Color.RED);
	
	}
	
	public void isShot(double playerX, double playerY) {
		show();
		rect.setX(playerX + SpaceInvaders.PLAYER_WIDTH / 2);
		rect.setY(playerY);
		hit = false;
	}
	
	public void show() {
		rect.setWidth(SpaceInvaders.BEAM_WIDTH);
		rect.setHeight(SpaceInvaders.BEAM_HEIGHT);
	}
	
	public void hide() {
		//Have to set to 0.001 because or else it is set to the default
		rect.setWidth(0.001);
		rect.setHeight(0.001);
	}

}
