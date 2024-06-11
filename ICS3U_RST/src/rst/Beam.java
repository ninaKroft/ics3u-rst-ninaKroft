package rst;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Beam {

	double xVal, yVal;
	Rectangle rect;
	
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
	}
	
	public void show() {
		rect.setWidth(SpaceInvaders.BEAM_WIDTH);
		rect.setHeight(SpaceInvaders.BEAM_HEIGHT);
	}
	
	public void hide() {
		rect.setWidth(0);
		rect.setHeight(0);
	}

}
