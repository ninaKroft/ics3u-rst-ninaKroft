package rst;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import java.net.URL;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SpaceInvaders extends Application {

	//Defining variables
	static final double SCREEN_WIDTH = 1400, SCREEN_HEIGHT = 700, PLAYER_WIDTH = 75, PLAYER_HEIGHT = 75, BEAM_WIDTH = 5, BEAM_HEIGHT = 10,
			ALIEN_WIDTH = 50, ALIEN_HEIGHT = 50;
	
	static final int ALIENS_PER_ROW = 12, GREEN_ALIEN_CHANCE = 8, RED_ALIEN_CHANCE = 2, RED_ALIEN_HEALTH = 10, GREEN_ALIEN_HEALTH = 5, 
			BEAM_DAMAGE = 5, GREEN_ALIEN_POINTS = 1, RED_ALIEN_POINTS = 3, BOSS_HEALTH = 300, BOSS_POINTS = 50;

	boolean menuActive = true, runActive = false, lossActive = false, firstStart = true, bossActive = false, hideAliens, canBeShot = true,
			restartBoss = true, paused = false;
	
	//Text objects
	Text txtInstructions, txtTitle, txtEnterToStart, txtPaused, txtCToContinue, txtRToRestart, txtScore, txtLoss, txtFinalScore;
	
	final int TITLE_SIZE = 50, INSTRUCTION_SIZE = 20, V_TITLE_POSITION = 3, V_INSTRUCTION_POSITION = 2, BORDER_WIDTH = 75, HIDDEN = 0,
			NUMBER_BEAMS = 20, SCORE_SIZE = 25, BOSS_WIDTH = 300, BOSS_HEIGHT = 300, BOSS_SENTINEL = 200, BOSS_SHOOT_FREQUENCY = 1, 
			BOSS_BEAM_WIDTH = 50, BOSS_BEAM_HEIGHT = 40, PAUSE_WIDTH = 1000, PAUSE_HEIGHT = 250;
	
	final double V_ENTER_TO_START_POSITION = 1.25, ALIEN_SPEED = 0.2, BEAM_SPEED = 6, PLAYER_SPEED = 7, TOP_ROW_LOCATION = 0.1, PLAYER_Y_LOCATION = 0.8,
			ALIEN_ROW_X_LOCATION = 0.09, ALIEN_SPACING = 100, FINAL_SCORE_POSITION = 0.6, TXT_LOSS_POSITION = 0.3, RESTART_BTN_POSITION = 0.7, 
			BOSS_Y_LOCATION = 0.08, BOSS_SPEED = 2, BOSS_AMP = 2, BOSS_K_VAL = 0.02, BOSS_BEAM_SPEED = 4;
	
	Beam[] beams = new Beam[NUMBER_BEAMS];
	
	Alien[] row1 = new Alien[ALIENS_PER_ROW], row2 = new Alien[ALIENS_PER_ROW], row3 = new Alien[ALIENS_PER_ROW],
			row4 = new Alien[ALIENS_PER_ROW], row5 = new Alien[ALIENS_PER_ROW], row6 = new Alien[ALIENS_PER_ROW], 
			row7 = new Alien[ALIENS_PER_ROW];
	
	//An array of arrays for the for loop that updates beams
	Alien[][] rowsList = {row1, row2, row3, row4, row5, row6, row7};
	
	Alien boss;
	
	int score, bossCount = 1;
	
	String font = "verdana";
	
	//exactPlayerLocation it updated every iteration, used to determine if the aliens are above the player
	double xDisp, bossXDisp = BOSS_SPEED, bossYDisp = 0;
	
	Scene scene;
	
	GameTimer timer;
	
	AudioClip beamSound, levelMusic, bossMusic;
	
	Button btnRestart;
	
	Group root = null, nodes = null, borders = null;
	
	Rectangle leftBorder, rightBorder, bossBeam, pauseBackground;
	
	Player player;
	
	@Override
	public void start(Stage myStage) throws Exception {
		//Setting the stage and checking for keys pressed
		
		//Create the elements to be displayed on the menu screen (text, decos)
		//add it to the group
		
		//List fonts
		//System.out.println(Font.getFontNames());
		
		root = new Group(generateElements());
		
		timer = new GameTimer();
		timer.start();
		
		//Generating the scene
		scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
		scene.setFill(Color.rgb(10, 3, 23));
		
		//Creating the sounds
		beamSound = new AudioClip(SpaceInvaders.class.getResource("/sounds/beamSound.wav").toString());
		levelMusic = new AudioClip(SpaceInvaders.class.getResource("/sounds/levelMusic.mp3").toString());
		bossMusic = new AudioClip(SpaceInvaders.class.getResource("/sounds/bossFightMusic.mp3").toString());
		
		//Handling keys pressed and released
		scene.setOnKeyPressed(event -> handleKeyPressed(event));
		scene.setOnKeyReleased(event -> handleKeyReleased(event));
		
		//Handling if the restart button is pressed
		btnRestart.setOnAction(event -> restart());
		
		myStage.setTitle("Space Invaders");
        myStage.setScene(scene);
        myStage.show();
		
	}
	
	class GameTimer extends AnimationTimer {
		
		@Override
		public void handle(long now) {
			
			//Updating the elements if the screen is resized
			if (menuActive) {
				updateMenu();
				
			} else if (runActive) {
				//Updating elements if the screen has been resized or moving according to speed
				hideMenu();
				if (!levelMusic.isPlaying()) {
					levelMusic.play();
				}
				
				//Stopping the boss music if its playing
				if (bossMusic.isPlaying()) {
					bossMusic.stop();
				}
				
				updatePlayer();
				updateBeams();
				updateScore();
				
				for (int i = 0; i < rowsList.length; i++) {
					if (updateAliens(i) == true) {
						runActive = false;
						lossActive = true;
					}
				}
				
			} else if (bossActive) {
				
				//Only need to hide the aliens once
				if (hideAliens) {
					pauseAliens();
					hideAliens = false;
				}
				
				if (restartBoss) {
					boss.health = BOSS_HEALTH * bossCount;
					boss.vanquished = false;
					boss.alienImage.setX(scene.getWidth() / 2);
					boss.alienImage.setY(scene.getHeight() * BOSS_Y_LOCATION);
				}
				
				restartBoss = false;
				
				if(levelMusic.isPlaying()) {
					levelMusic.stop();
				}
				
				if(!bossMusic.isPlaying()) {
					bossMusic.play();
				}
				
				updateBoss();
				updatePlayer();
				updateBeams();
				
			} else if (lossActive) {
				//run the loss screen and hide the level
				hideLevel();
				updateLossScreen();
			}
			
			updateBorders();
			
		}
	}
	
	private void handleKeyPressed(KeyEvent event) {
		//handling keys pressed
		KeyCode code = event.getCode();
		
		//User pressed enter; start the game
		if (code == KeyCode.ENTER) {
			menuActive = false;
			hideMenu();
			runActive = true;
		}
		//If the user pressed the left key or A, set movement to the left
		if (code == KeyCode.LEFT || code == KeyCode.KP_LEFT || code == KeyCode.A) {
			//simulating leftwards movement
			xDisp = PLAYER_SPEED * -1;
		}
		//If the user pressed the right key or D, set the movement to the right
		if (code == KeyCode.RIGHT || code == KeyCode.KP_RIGHT || code == KeyCode.D) {
			//simulating rightwards movement
			xDisp = PLAYER_SPEED;
		}
		//If the user pressed the up key, shoot a beam
		if (code == KeyCode.UP || code == KeyCode.KP_UP) {
			
			if (!paused) {
				for (int i = 0; i < NUMBER_BEAMS; i++) {
					//The beam must have reached the top so that it doesn't get re-shot prematurely
					if (beams[i].rect.getY() <= 0) {
						//Repositioning the beam to be above the player using the isShot method
						beams[i].isShot(player.playerImage.getX(), player.playerImage.getY());
						
						//Playing the shooting sound
						beamSound.play();
						
						//Ending the search once a beam has been shot
						i = NUMBER_BEAMS;
					}
				}
			}
		}
		
		//If the user pressed the space bar, pause the game
		if (code == KeyCode.SPACE) {
			
			//Only allow pausing if the menu and loss screen are not active
			if (!menuActive && !lossActive) {
				if (!paused) {
					displayPauseScreen();
					timer.stop();
					paused = true;
				} else if (paused) {
					timer.start();
					hidePauseScreen();
					paused = false;
				}
			}
			
		}
	}
	
	private void handleKeyReleased(KeyEvent event) {
		//handling keys released
		KeyCode code = event.getCode();
		
		if (code == KeyCode.LEFT || code == KeyCode.KP_LEFT || code == KeyCode.A) {
			xDisp = 0;
		}
		if (code == KeyCode.RIGHT || code == KeyCode.KP_RIGHT || code == KeyCode.D) {
			xDisp = 0;
		}
	}
	
	//Creates all of the elements of the game and adds them to a group. Everything is set to be hidden except for the borders
	private Group generateElements() {
		//Title generation
		txtTitle = new Text("SPACE INVADERS");
		txtTitle.setFont(Font.font(font, TITLE_SIZE));
		txtTitle.setVisible(false);
		txtTitle.setX(SCREEN_WIDTH / 2 - (txtTitle.maxWidth(TITLE_SIZE) / 2));
		txtTitle.setY(SCREEN_HEIGHT / V_TITLE_POSITION - (txtTitle.maxHeight(TITLE_SIZE) / 2));
		txtTitle.setFill(Color.WHITE);
		
		//Instructions generation
		txtInstructions = new Text("INSTRUCTIONS: Use the left and right arrows (or A/D) to move side-to-side. \nUse the up arrow to shoot a beam."
				+ " Your goal is to hit and vanquish the \noncoming aliens before they reach you. Vanquish as many as possible"
				+ " \nfor the highest score!");
		txtInstructions.setFont(Font.font(font, INSTRUCTION_SIZE));
		txtInstructions.setVisible(false);
		txtInstructions.setX(SCREEN_WIDTH / 2 - (txtInstructions.maxWidth(TITLE_SIZE) / 2));
		txtInstructions.setY(SCREEN_HEIGHT / V_INSTRUCTION_POSITION - (txtInstructions.maxHeight(TITLE_SIZE) / 2));
		txtInstructions.setFill(Color.WHITE);
		
		//Enter to start text generation
		txtEnterToStart = new Text("PRESS ENTER TO START");
		txtEnterToStart.setFont(Font.font(font, TITLE_SIZE));
		txtEnterToStart.setVisible(false);
		txtEnterToStart.setX(SCREEN_WIDTH / 2 - (txtEnterToStart.maxWidth(TITLE_SIZE) / 2));
		txtEnterToStart.setY(SCREEN_HEIGHT / V_ENTER_TO_START_POSITION - (txtEnterToStart.maxHeight(TITLE_SIZE) / 2));
		txtEnterToStart.setFill(Color.WHITE);
		
		//Border generation
		leftBorder = new Rectangle(0, 0, BORDER_WIDTH, SCREEN_HEIGHT);
		leftBorder.setFill(Color.rgb(128, 3, 252));
		rightBorder = new Rectangle(SCREEN_WIDTH - BORDER_WIDTH, 0, BORDER_WIDTH, SCREEN_HEIGHT);
		rightBorder.setFill(Color.rgb(128, 3, 252));
		
		//Score generation
		txtScore = new Text("SCORE: " + score);
		txtScore.setFont(Font.font(font, SCORE_SIZE));
		txtScore.setVisible(false);
		txtScore.setX(BORDER_WIDTH + 5);
		txtScore.setY(SCREEN_HEIGHT - 5);
		txtScore.setFill(Color.WHITE);
		
		//Loss screen elements generation
		txtLoss = new Text("You Lost!");
		txtLoss.setFont(Font.font(font, TITLE_SIZE));
		txtLoss.setVisible(false);
		txtLoss.setFill(Color.WHITE);
		
		txtFinalScore = new Text("Your final score is: " + score);
		txtFinalScore.setFont(Font.font(font, TITLE_SIZE));
		txtFinalScore.setVisible(false);
		txtFinalScore.setFill(Color.WHITE);
		
		//Button for the loss screen
		btnRestart = new Button();
		btnRestart.setText("Click to Restart");
		btnRestart.setVisible(false);
		
		//Player generation
		player = new Player(SCREEN_WIDTH / 2 - (PLAYER_WIDTH / 2), SCREEN_HEIGHT * PLAYER_Y_LOCATION, true);
		player.hide();
		
		//Boss alien generation
		boss = new Alien("boss");
		boss.hide();
		boss.alienImage.setFitWidth(BOSS_WIDTH);
		boss.alienImage.setFitHeight(BOSS_HEIGHT);
		//Center the boss alien
		boss.alienImage.setX(SCREEN_WIDTH / 2 - boss.alienImage.getFitWidth() / 2);
		boss.alienImage.setY(SCREEN_HEIGHT * BOSS_Y_LOCATION);
		
		//Boss beam generation
		bossBeam = new Rectangle(BOSS_BEAM_WIDTH, BOSS_BEAM_HEIGHT);
		bossBeam.setVisible(false);
		bossBeam.setFill(Color.ORANGE);
			
		//Pause screen elements generation
		//Background
		pauseBackground = new Rectangle(PAUSE_WIDTH, PAUSE_HEIGHT);
		pauseBackground.setFill(Color.PURPLE);
		pauseBackground.setVisible(false);
		
		//Paused text
		txtPaused = new Text("PAUSED");
		txtPaused.setVisible(false);
		txtPaused.setFill(Color.WHITE);
		txtPaused.setFont(Font.font(font, TITLE_SIZE));
		
		//Grouping the elements
		nodes = new Group(txtTitle, txtInstructions, txtEnterToStart, player.playerImage, leftBorder, rightBorder, txtScore, txtLoss, txtFinalScore,
				btnRestart, boss.alienImage, bossBeam, pauseBackground, txtPaused);
		
		//Beam generation + adding to group
		for (int i = 0; i < NUMBER_BEAMS; i++) {
			beams[i] = generateBeam();
			nodes.getChildren().add(beams[i].rect);
		}
		

		//Alien generation + adding to group
		for (int i = 0; i < rowsList.length; i++) {
			for (int j = 0; j < rowsList[i].length; j++) {
				rowsList[i][j] = generateAlien();
				
				//The first alien will be moved to the specified x coordinate, then each consecutive alien will be moved over by 
				//its number multiplied by the spacing. Alien 0 will be at the x coordinate, alien 1 will be ALIEN_SPACING away from alien 0, etc
				rowsList[i][j].alienImage.setX((SCREEN_WIDTH * ALIEN_ROW_X_LOCATION) + (j * ALIEN_SPACING));
				
				//Hiding the alien for startup
				rowsList[i][j].hide();
				
				nodes.getChildren().add(rowsList[i][j].alienImage);
			}
		}
		
		
		return nodes;
	}

	private void updateMenu() {
		//Handling if the user re-sizes the screen
		
		//Showing the menu
		txtTitle.setVisible(true);
		txtInstructions.setVisible(true);
		txtEnterToStart.setVisible(true);
		
		//Title
		txtTitle.setX(scene.getWidth() / 2 - (txtTitle.maxWidth(TITLE_SIZE)) / 2);
		txtTitle.setY(scene.getHeight() / V_TITLE_POSITION - (txtTitle.maxHeight(TITLE_SIZE) / 2));
		
		//Instructions
		txtInstructions.setX(scene.getWidth() / 2 - (txtInstructions.maxWidth(INSTRUCTION_SIZE)) / 2);
		txtInstructions.setY(scene.getHeight() / V_INSTRUCTION_POSITION - (txtInstructions.maxHeight(INSTRUCTION_SIZE) / 2));
		
		//Enter to start text
		txtEnterToStart.setX(scene.getWidth() / 2 - (txtEnterToStart.maxWidth(TITLE_SIZE)) / 2);
		txtEnterToStart.setY(scene.getHeight() / V_ENTER_TO_START_POSITION - (txtEnterToStart.maxHeight(TITLE_SIZE) / 2));
		
	}
	
	private void updateBorders() {
		//Borders
		leftBorder.setHeight(scene.getHeight());
		rightBorder.setHeight(scene.getHeight());
		rightBorder.setX(scene.getWidth() - BORDER_WIDTH);
	}
	
	private void hideMenu() {
		txtTitle.setFont(Font.font(0));
		txtInstructions.setFont(Font.font(0));
		txtEnterToStart.setFont(Font.font(0));
	}
	
	private void updatePlayer() {
		
		double playerX = player.playerImage.getX();
		
		player.playerImage.setFitWidth(SpaceInvaders.PLAYER_WIDTH);
		player.playerImage.setFitHeight(SpaceInvaders.PLAYER_HEIGHT);
		player.show();
		
		player.playerImage.setY(scene.getHeight() * PLAYER_Y_LOCATION);
		
		//Not allowing movement past the borders
		if (playerX >= BORDER_WIDTH && playerX <= scene.getWidth() - BORDER_WIDTH) {
			player.playerImage.setX(player.playerImage.getX() + xDisp);
		}
		//Moving the player to the right slightly if they are intersecting with the left border (so movement is allowed)
		if (playerX <= BORDER_WIDTH) {
			player.playerImage.setX(BORDER_WIDTH + 1);
		}
		//Moving the player to the left slightly if that are intersecting with the right border
		if (playerX >= (scene.getWidth() - BORDER_WIDTH - PLAYER_WIDTH)) {
			player.playerImage.setX(scene.getWidth() - BORDER_WIDTH - PLAYER_WIDTH - 1);
		}
		
	}
	
	private Beam generateBeam() {
		Beam beam = new Beam(0,0);
		beam.hide();
		return beam;
	}
	
	private void updateBeams() {
		
		//Making the beams move upward each frame
		for (int i = 0; i < NUMBER_BEAMS; i++) {
			
			beams[i].rect.setWidth(SpaceInvaders.BEAM_WIDTH);
			beams[i].rect.setHeight(SpaceInvaders.BEAM_HEIGHT);
			
			//Get the bounds of the beam
			Bounds beamBox = beams[i].rect.getBoundsInParent();
			
			//The beam must be below the max height to move upwards again
			if (beams[i].rect.getY() >= 0) {
				
				//Only show the beam if it has not hit anything
				if (!beams[i].hit) {
					beams[i].show();
				}
				
				beams[i].rect.setY(beams[i].rect.getY() - BEAM_SPEED);
				
				/* CHECKING FOR COLLISIONS BETWEEN THE BEAMS AND THE REGULAR ALIENS */
				//The beams only need to be checked for collisions if they are not at the max height
				
				//If the level is being run...
				if (runActive) {
					//For each row of aliens on the board...
					for (int j = 0; j < rowsList.length; j++) {
						//For each alien in that row...
						for (int x = 0; x < ALIENS_PER_ROW; x++) {
							
							//If that alien is not hidden...
							if (rowsList[j][x].alienImage.isVisible()) {
								
								//If the beam has not already hit an alien...
								if (!beams[i].hit) {
									//Get the bounds of the alien
									Bounds alienBox = rowsList[j][x].alienImage.getBoundsInParent();
									
									//If they are colliding...
									if (beamBox.intersects(alienBox)) {
										//Reduce that alien's health by the beam's damage
										rowsList[j][x].health -= BEAM_DAMAGE;
										
										//Set that the beam has hit an alien already
										beams[i].hit = true;
										
										//Hide that beam
										beams[i].hide();
										
										//If that alien's health is below or equal to zero...
										if (rowsList[j][x].health <= 0) {
											//Set that alien to vanquished
											rowsList[j][x].vanquish();
											
											//Add it's point value to the score
											//Have to use a for loop to increment the score by one so we can check each time if the score reaches
											// the boss sentinel
											
											for (int t = 0; t < rowsList[j][x].points; t++) {
												score += 1;
												
												//If the score is equal to the boss sentinel times the number of times it has been run...
												if (score == BOSS_SENTINEL * bossCount) {
													//Activate the boss loop, deactivate the run loop, pause the aliens
													bossActive = true;
													restartBoss = true;
													runActive = false;
													hideAliens = true;
												}
										
											}
											
										}
									}
								}
								
							}
							
						}
						
					}
					
				/* CHECKING FOR COLLISIONS BETWEEN THE BEAMS AND THE BOSS ALIEN */
				//If the boss scene is active...
				} else if (bossActive) {
				
					//Get the bounds of the boss
					Bounds bossBox = boss.alienImage.getBoundsInParent();
					
					//If the beam has not already hit something...
					if (!beams[i].hit) {
						
						//If the beam is intersecting with the boss alien...
						if (beamBox.intersects(bossBox)) {
							//Reducing the boss' health
							boss.health -= BEAM_DAMAGE;
							
							//Setting that that beam has collided with something
							beams[i].hit = true;
							
							//Hide that beam
							beams[i].hide();
							
							//Checking if the boss' health is less than or equal to zero
							//Set the alien boss to vanquished
							if (boss.health <= 0) {
								boss.vanquished = true;
							}
						}
						
					}
					
					
						
					//If the beam has reached the bottom of the screen, it needs to be hidden
					if (bossBeam.getY() + BOSS_BEAM_HEIGHT >= scene.getHeight()) {
						bossBeam.setVisible(false);
						canBeShot = true;
						boss.bossFrame1();
					}
					
				}
				
				
			//Hiding the beam if it reaches the top
			} else if (beams[i].rect.getY() <= 0){
				beams[i].hide();
			}
		}
		
	}
	
	private void updateScore() {
		//Setting the score to be visible
		txtScore.setVisible(true);
		
		//Repositioning if the user re sizes the screen
		txtScore.setY(scene.getHeight() - 5);
		
		//Updating the value of the score
		txtScore.setText("SCORE: "+ score);
	}
	
	private Alien generateAlien() {
		Alien alien = new Alien("regular");
		return alien;
	}
	
	private boolean updateAliens(int rowNum) {
		int vanquishedAlienCount = 0, prevArray;
		boolean lost = false;
		double prevAlienMinY, currAlienMaxY;
		
		Alien[] row = rowsList[rowNum];
		
		double topLocation = scene.getHeight() * TOP_ROW_LOCATION;
		
		for (int i = 0; i < ALIENS_PER_ROW; i++) {
			
			row[i].alienImage.setFitWidth(SpaceInvaders.ALIEN_WIDTH);
			row[i].alienImage.setFitHeight(SpaceInvaders.ALIEN_HEIGHT);
			
			//Updating the xValue if the player resizes the screen
			row[i].alienImage.setX((scene.getWidth() * ALIEN_ROW_X_LOCATION) + (i * ALIEN_SPACING));
			
			//If the first row is being moved, just set the prevArray index to something valid since we don't need to check if its 
			// intersecting with anything since there are no rows below it.
			if (rowNum == 0) {
				prevArray = 0;
			} else {
				prevArray = rowNum - 1;
			}
			
			//The whole "not moving until the other row has moved" thing is only relevant at the start
			if (firstStart) {
				//Get the minimum Y of the previous array's alien, i as the other index so it is checking the alien above it
				//Get the current alien's maximum Y
				prevAlienMinY = rowsList[prevArray][i].alienImage.getY();
				currAlienMaxY = row[i].alienImage.getY() + ALIEN_HEIGHT;
				
				//Only show and allow the possibility for movement if the row below is not intersecting with the row above 
				// or if its the first row
				if (rowNum == 0 || prevAlienMinY >= currAlienMaxY) {
					
					row[i].show();	
					
					//If they are above the player, move it downwards
					if (row[i].alienImage.getY() < player.playerImage.getY()) {
						row[i].alienImage.setY(row[i].alienImage.getY() + ALIEN_SPEED);
						
					//If they have reached the player and they are not vanquished, the game is lost
					} else if (row[i].alienImage.getY() + ALIEN_HEIGHT > player.playerImage.getY() && row[i].vanquished == false) {
						//LOST GAME
						lost = true;
					}
				
					//Checking if the alien is vanquished. If yes, adding to the counter. Used later to see if all aliens are vanquished in that row
					 if (row[i].vanquished == true) {
						//Count how many aliens are vanquished in that row
						vanquishedAlienCount += 1;
						
					}
				} 
				
			
			} else {
				
				row[i].show();	
				
				//If they are above the player, move it downwards
				if (row[i].alienImage.getY() < player.playerImage.getY()) {
					row[i].alienImage.setY(row[i].alienImage.getY() + ALIEN_SPEED);
					
				//If they have reached the player and they are not vanquished, the game is lost
				} else if (row[i].alienImage.getY() + ALIEN_HEIGHT > player.playerImage.getY() && row[i].vanquished == false) {
					//LOST GAME
					lost = true;
				}
			
				//Checking if the alien is vanquished. If yes, adding to the counter. Used later to see if all aliens are vanquished in that row
				 if (row[i].vanquished == true) {
					//Count how many aliens are vanquished in that row
					vanquishedAlienCount += 1;
				}
			}
		}
		
		//If all of the aliens in the row are vanquished and below the player, send the row back up to the top
		if (vanquishedAlienCount == ALIENS_PER_ROW && row[0].alienImage.getY() + ALIEN_HEIGHT > player.playerImage.getY()) {
			for (int i = 0; i < ALIENS_PER_ROW; i++) {
				row[i].alienImage.setY(topLocation);
				row[i].randomizeType();
				//Multiplying by the boss count to introduce difficulty scaling
				row[i].health *= bossCount;
				//The aliens will definitely be spaced out and nice if one reaches the bottom
				firstStart = false;
				
			}
		}
		
		return lost;
	}
	
	private void hideLevel() {
		//Hides all of the elements of the level
		
		//Hiding the player
		player.hide();
		
		//Hiding the beams
		for (int i = 0; i < beams.length; i++) {
			beams[i].hide();
		}
		
		//Hiding the aliens
		for (int i = 0; i < rowsList.length; i ++) {
			for (int j = 0; j < ALIENS_PER_ROW; j++) {
				rowsList[i][j].hide();
			}
		}
		
		//Hiding the score text
		txtScore.setVisible(false);
	}
	
	
	private void updateLossScreen() {
		//You Lost! text
		txtLoss.setFont(Font.font(font, TITLE_SIZE));
		txtLoss.setX(scene.getWidth() / 2 - txtLoss.maxWidth(TITLE_SIZE) / 2);
		txtLoss.setY(scene.getHeight() * TXT_LOSS_POSITION);
		txtLoss.setVisible(true);
		
		//THe final score text
		txtFinalScore.setFont(Font.font(font, TITLE_SIZE));
		txtFinalScore.setText("FINAL SCORE: "+ score);
		txtFinalScore.setX(scene.getWidth() / 2 - txtFinalScore.maxWidth(TITLE_SIZE) / 2);
		txtFinalScore.setY(scene.getHeight() * FINAL_SCORE_POSITION);
		txtFinalScore.setVisible(true);
		
		//Button allowing the user to restart
		btnRestart.setLayoutX(scene.getWidth() / 2 - btnRestart.getWidth() / 2);
		btnRestart.setLayoutY(scene.getHeight() * RESTART_BTN_POSITION);
		btnRestart.setVisible(true);
		
	}
	
	
	private void hideLossScreen() {
		//Hiding all of the loss screen stuff
		btnRestart.setVisible(false);
		txtLoss.setVisible(false);
		txtFinalScore.setVisible(false);
		
		
	}
	
	
	private void restart() {
		
		hideLossScreen();
		
		score = 0;
		
		bossCount = 1;
		
		//Put everything back at the beginning position in the level
		player.playerImage.setX(scene.getWidth() / 2 - (PLAYER_WIDTH / 2));
		
		//The aliens will be bunched up again so we need firstStart to run
		firstStart = true;
		
		for (int i = 0; i < rowsList.length; i++) {
			for (int j = 0; j < ALIENS_PER_ROW; j++) {
				rowsList[i][j].alienImage.setX((scene.getWidth() * ALIEN_ROW_X_LOCATION) + (i * ALIEN_SPACING));
				rowsList[i][j].alienImage.setY(scene.getHeight() * TOP_ROW_LOCATION);
				rowsList[i][j].randomizeType();
				rowsList[i][j].hide();
			}
		}
		
		for (int i = 0; i < NUMBER_BEAMS; i++) {
			//Move all of the beams to the top of the screen.
			beams[i].rect.setY(0);
		}
		
		//Hiding the boss and the boss beam if they were active
		if (bossActive) {
			boss.hide();
			bossBeam.setVisible(false);
		}
		
		restartBoss = true;
		lossActive = false;
		bossActive = false;
		runActive = true;
	}
	
	
	private void pauseAliens() {
		//Hide the aliens and pause them
		for (int i = 0; i < rowsList.length; i++) {
			for (int j = 0; j < ALIENS_PER_ROW; j++) {
				rowsList[i][j].hide();
			}
		}
	}
	
	
	private void updateBoss() {
		
		//Show the boss
		boss.show();
		
		if (!boss.vanquished) {
		
			//Moving the boss' x value
			boss.alienImage.setX(boss.alienImage.getX() + moveBossX());
			
			//Moving the boss' y value
			boss.alienImage.setY(boss.alienImage.getY() + moveBossY());
			
			//If the boss has been vanquished...
			if (boss.vanquished) {
				//Run the boss vanquished animation
				//Resume the game
				bossActive = false;
				
				//Resume the regular game play
				runActive = true;
			}
			
			//If the boss beam has reached the bottom of the screen, it is able to be shot again
			if (canBeShot) {
				//Multiplying the frequency by the boss count to introduce difficulty scaling
				if (randomNumber(0, 100) <= BOSS_SHOOT_FREQUENCY * bossCount) {
					//Getting a random number between the range of 0 and 100
					// If that number is within 0-boss shooting frequency range, the boss will shoot a beam
					// The animation frame of the boss is switched until that beam reaches the bottom of the screen
					boss.bossFrame2();
					shootBossBeam();
					canBeShot = false;
					
				}
				
			//Otherwise the beam is on the screen and needs to be moved downwards
			} else {
				updateBossBeam();
			}
			
		//The boss has been vanquished
		} else {
			bossBeam.setVisible(false);
			//Move upwards until the boss reaches the top of the screen
			if (boss.alienImage.getY() + boss.alienImage.getFitHeight() > 0) {
				boss.alienImage.setY(boss.alienImage.getY() - BOSS_SPEED);
			} else {
				boss.hide();
				bossCount += 1;
				score += BOSS_POINTS;
				bossActive = false;
				runActive = true;
			}
			
		}
		
		
	}
	
	
	private double moveBossX() {
		
		//Getting the bounds of the alien boss
		Bounds bossBox = boss.alienImage.getBoundsInParent();
		
		//Checking for collisions between the boss and the borders and if they collide with one, reverse the direction of X movement
		if (boss.alienImage.getX() + boss.alienImage.getFitWidth() >= scene.getWidth() - BORDER_WIDTH ) {
			//Setting the boss to no longer be colliding with the right border
			boss.alienImage.setX(scene.getWidth() / 2);
			//Reversing the direction of movement
			bossXDisp *= -1;
			
			//Setting the y value to be at the proper axis
			boss.alienImage.setY(scene.getHeight() * BOSS_Y_LOCATION);
			
		} else if (bossBox.getMinX() <= BORDER_WIDTH) {
			//Setting the boss to no longer be colliding with the left border
			boss.alienImage.setX(BORDER_WIDTH + 1);
			//Reversing the direction of movement
			bossXDisp *= -1;
			
			//Setting the y value to be at the proper axis
			boss.alienImage.setY(scene.getHeight() * BOSS_Y_LOCATION);
			
		}
		
		return bossXDisp;
		
	}
	
	
	private double moveBossY() {
		
		//Modeling the boss' y-movement to a cosine wave
		//the amp is how much variation from the axis I want, the k value influences the width of the cycles
		//No need for an axis shift because the value that this function outputs is added to the y value the boss alien is already at
		if (boss.alienImage.getX() <= scene.getWidth() - BORDER_WIDTH) {
			bossYDisp = BOSS_AMP * Math.cos(BOSS_K_VAL * boss.alienImage.getX());
		}
		
		return bossYDisp;
	}
	
	
	private void shootBossBeam() {
		bossBeam.setVisible(true);
		bossBeam.setX(boss.alienImage.getX() + boss.alienImage.getFitWidth() / 2);
		bossBeam.setY(boss.alienImage.getY() + boss.alienImage.getFitHeight());
		
	}
	
	private void updateBossBeam() {
		//If the beam is above the bottom of the screen...
		if (bossBeam.getY() + BOSS_BEAM_HEIGHT < scene.getHeight()) {
			//Move it downwards
			bossBeam.setY(bossBeam.getY() + BOSS_BEAM_SPEED);
			
			//Checking for collisions between the boss beam and the player
			Bounds playerBox = player.playerImage.getBoundsInParent();
			Bounds bossBeamBox = bossBeam.getBoundsInParent();
			
			
			//If they are intersecting, the user has lost
			if (bossBeamBox.intersects(playerBox)) {
				bossActive = false;
				lossActive = true;
				hideBossLevel();
				
			}
			
		//Otherwise, it has reached the bottom of the screen, and it should disappear
		} else {
			bossBeam.setVisible(false);
			canBeShot = true;
			boss.bossFrame1();
		}
		
	}
	
	private void hideBossLevel() {
		bossBeam.setVisible(false);
		boss.hide();
	}
	
	private void displayPauseScreen() {
		pauseBackground.setVisible(true);
		pauseBackground.setX(scene.getWidth() / 2 - PAUSE_WIDTH / 2);
		pauseBackground.setY(scene.getHeight() / 2 - PAUSE_HEIGHT / 2);
		
		txtPaused.setVisible(true);
		txtPaused.setX(scene.getWidth() / 2 - txtPaused.maxWidth(TITLE_SIZE) / 2);
		txtPaused.setY(scene.getHeight() / 2);
	}

	private void hidePauseScreen() {
		pauseBackground.setVisible(false);
		txtPaused.setVisible(false);
	}
	
	public static int randomNumber(int a, int b) {
	    int highNum = Math.max(a, b);
	    int lowNum = Math.min(a, b);
	    int range = highNum - lowNum + 1;
	    return (int) (Math.random() * range) + lowNum;
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
