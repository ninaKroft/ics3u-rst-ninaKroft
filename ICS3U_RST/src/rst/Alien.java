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
	Alien(String type) {
		
		switch (type){
		case "regular":
			randomizeType();
			break;
			
		case "boss":
			
		}
		
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
	
	public void generateBoss() {
		URL boss1Location = SpaceInvaders.class.getResource("/images/AlienBoss1.png");
		Image bossImg1 = new Image(boss1Location.toString());
		alienImage.setImage(bossImg1);
		health = SpaceInvaders.BOSS_HEALTH;
		points = SpaceInvaders.BOSS_POINTS;
		vanquished = false;
		show();
		
		
	}
	
	public void hide() {
		alienImage.setVisible(false);
		
	}
	
	public void show() {
		//Only displaying aliens that are not vanquished
		if (!vanquished) {
			alienImage.setVisible(true);
		}
		
		
	}
}
