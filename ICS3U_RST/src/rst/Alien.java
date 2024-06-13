package rst;

import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Alien {

	//Defining data fields
	String type;
	int health, points;
	boolean vanquished;
	ImageView alienImage = new ImageView();
	
	//Constructor
	Alien() {
		randomizeType();
	}

	//The sum of GREEN_ALIEN_CHANCE and RED_ALIEN_CHANCE must be equal or lesser to ALIENS_PER_ROW
	public void randomizeType() {
		int num = SpaceInvaders.randomNumber(1, SpaceInvaders.ALIENS_PER_ROW);
		
		//If its within the green alien chance range
		if (num <= SpaceInvaders.GREEN_ALIEN_CHANCE) {
			type = "green";
			health = SpaceInvaders.GREEN_ALIEN_HEALTH;
			points = SpaceInvaders.GREEN_ALIEN_POINTS;
			vanquished = false;
			URL greenLocation = SpaceInvaders.class.getResource("/images/GreenAlien.png");
			Image greenImg = new Image(greenLocation.toString());
			alienImage.setImage(greenImg);
			show();
			
		//If its within the red alien chance range
		} else if (num >= SpaceInvaders.GREEN_ALIEN_CHANCE + 1 && num <= SpaceInvaders.GREEN_ALIEN_CHANCE + SpaceInvaders.RED_ALIEN_CHANCE) {
			type = "red";
			health = SpaceInvaders.RED_ALIEN_HEALTH;
			points = SpaceInvaders.RED_ALIEN_POINTS;
			vanquished = false;
			URL redLocation = SpaceInvaders.class.getResource("/images/RedAlien.png");
			Image redImg = new Image(redLocation.toString());
			alienImage.setImage(redImg);
			show();
			
		//If its not within either of those ranges, generate a void alien
		} else {
			vanquish();

		}

	}
	
	//Sets the alien type to void, simulating it being vanquished
	public void vanquish() {
		type = "void";
		vanquished = true;
		URL voidLocation = SpaceInvaders.class.getResource("/images/Void.png");
		Image voidImg = new Image(voidLocation.toString());
		alienImage.setImage(voidImg);
		hide();
	}
	

	
	public void hide() {
		//Have to set to 0.0001 because if its set to 0, it sets the width/height to the default
		alienImage.setFitWidth(0.001);
		alienImage.setFitHeight(0.001);
		
	}
	
	public void show() {
		//Only displaying aliens that are not vanquished
		if (!vanquished) {
			alienImage.setFitWidth(SpaceInvaders.ALIEN_WIDTH);
			alienImage.setFitHeight(SpaceInvaders.ALIEN_HEIGHT);
		}
		
		
	}
}
