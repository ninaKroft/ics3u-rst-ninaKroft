package rst;

import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Author: Nina Kroft
 * Date: Tuesday June 18th 2024
 * Course: ICS3U
 * Alien.java
 * A class for the RST / Final Project for the ICS3U course
 */

public class Alien {

	//Defining data fields
	int health, points;
	boolean vanquished;
	ImageView alienImage = new ImageView();
	
	//Constructor
	Alien(String type) {
		
		//Generating the specified type of alien
		switch (type) {
		case "regular":
			randomizeType();
			break;
			
		case "boss":
			generateBoss();
			break;
		}
		
	}

	/**
     * This method randomizes the alien type and assign's its respective health value, point value, and image
     * 
     * The sum of GREEN_ALIEN_CHANCE and RED_ALIEN_CHANCE must be equal or lesser to ALIENS_PER_ROW
     */
	public void randomizeType() {
		
		//Generating a random number
		int num = SpaceInvaders.randomNumber(1, SpaceInvaders.ALIENS_PER_ROW);
		
		//If its within the green alien chance range
		if (num <= SpaceInvaders.GREEN_ALIEN_CHANCE) {
			health = SpaceInvaders.GREEN_ALIEN_HEALTH;
			points = SpaceInvaders.GREEN_ALIEN_POINTS;
			vanquished = false;
			URL greenLocation = SpaceInvaders.class.getResource("/images/GreenAlien.png");
			Image greenImg = new Image(greenLocation.toString());
			alienImage.setImage(greenImg);
			alienImage.setFitWidth(SpaceInvaders.ALIEN_WIDTH);
			alienImage.setFitHeight(SpaceInvaders.ALIEN_HEIGHT);
			show();
			
		//If its within the red alien chance range
		} else if (num >= SpaceInvaders.GREEN_ALIEN_CHANCE + 1 && num <= SpaceInvaders.GREEN_ALIEN_CHANCE + SpaceInvaders.RED_ALIEN_CHANCE) {
			health = SpaceInvaders.RED_ALIEN_HEALTH;
			points = SpaceInvaders.RED_ALIEN_POINTS;
			vanquished = false;
			URL redLocation = SpaceInvaders.class.getResource("/images/RedAlien.png");
			Image redImg = new Image(redLocation.toString());
			alienImage.setImage(redImg);
			alienImage.setFitWidth(SpaceInvaders.ALIEN_WIDTH);
			alienImage.setFitHeight(SpaceInvaders.ALIEN_HEIGHT);
			show();
			
		//If its not within either of those ranges, generate a void alien
		} else {
			vanquish();
			alienImage.setFitWidth(SpaceInvaders.ALIEN_WIDTH);
			alienImage.setFitHeight(SpaceInvaders.ALIEN_HEIGHT);

		}

	}
	
	/**
     * This method sets the alien type to void, simulating it being vanquished
     */
	public void vanquish() {
		vanquished = true;
		URL voidLocation = SpaceInvaders.class.getResource("/images/Void.png");
		Image voidImg = new Image(voidLocation.toString());
		alienImage.setImage(voidImg);
		hide();
	}
	
	/**
     * This method generates a boss alien
     */
	public void generateBoss() {
		URL boss1Location = SpaceInvaders.class.getResource("/images/AlienBoss1.png");
		Image bossImg1 = new Image(boss1Location.toString());
		alienImage.setImage(bossImg1);
		health = SpaceInvaders.BOSS_HEALTH;
		points = SpaceInvaders.BOSS_POINTS;
		vanquished = false;
		show();	
	}
	
	/**
     * This method sets the alien's image to the first boss frame
     */
	public void bossFrame1() {
		URL boss1Location = SpaceInvaders.class.getResource("/images/AlienBoss1.png");
		Image bossImg1 = new Image(boss1Location.toString());
		alienImage.setImage(bossImg1);
	}
	
	/**
     * This method sets the alien's image to the second boss frame
     */
	public void bossFrame2() {
		URL boss2Location = SpaceInvaders.class.getResource("/images/AlienBoss2.png");
		Image bossImg2 = new Image(boss2Location.toString());
		alienImage.setImage(bossImg2);
	}
	
	/**
     * This method hides the alien
     */
	public void hide() {
		alienImage.setVisible(false);
		
	}
	
	/**
     * This method shows the alien if it is not vanquished
     */
	public void show() {
		//Only displaying aliens that are not vanquished
		if (!vanquished) {
			alienImage.setVisible(true);
		}
		
		
	}
}
