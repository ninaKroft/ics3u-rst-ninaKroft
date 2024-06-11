package rst;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import java.net.URL;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SpaceInvaders extends Application {

	//Defining variables
	static final double SCREEN_WIDTH = 1400, SCREEN_HEIGHT = 700, PLAYER_WIDTH = 75, PLAYER_HEIGHT = 75, BEAM_WIDTH = 5, BEAM_HEIGHT = 10;

	boolean menuActive = true, runActive = false, lossActive = false;
	
	//Text objects
	Text txtInstructions, txtTitle, txtEnterToStart, txtSpaceToPause, txtCToContinue, txtRToRestart, txtScore;
	
	final int TITLE_SIZE = 50, INSTRUCTION_SIZE = 20, V_TITLE_POSITION = 3, V_INSTRUCTION_POSITION = 2, BORDER_WIDTH = 75, HIDDEN = 0,
			PLAYER_SPEED = 10, NUMBER_BEAMS = 10, ALIENS_PER_ROW = 6, BEAM_SPEED = 3;
	
	final double V_ENTER_TO_START_POSITION = 1.25;
	
	Beam[] beams = new Beam[NUMBER_BEAMS];
	
	Alien[] row1 = new Alien[ALIENS_PER_ROW], row2 = new Alien[ALIENS_PER_ROW], row3 = new Alien[ALIENS_PER_ROW],
			row4 = new Alien[ALIENS_PER_ROW];
	
	int score, xDisp;
	
	Scene scene;
	
	Group root = null, nodes = null, borders = null;
	
	Rectangle leftBorder, rightBorder;
	
	Player player;
	
	@Override
	public void start(Stage myStage) throws Exception {
		//Setting the stage and checking for keys pressed
		
		//MENU SCREEN
		
		//Create the elements to be displayed on the menu screen (text, decos)
		//add it to the group
		
		//List fonts
		//System.out.println(Font.getFontNames());
		
		//Title setup
		
		root = new Group(generateElements());
		
		GameTimer timer = new GameTimer();
		timer.start();
		
		//Generating the scene
		scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
		scene.setFill(Color.rgb(10, 3, 23));
		
		//Handling keys pressed and released
		scene.setOnKeyPressed(event -> handleKeyPressed(event));
		scene.setOnKeyReleased(event -> handleKeyReleased(event));
		
		myStage.setTitle("Space Invaders");
        myStage.setScene(scene);
        myStage.show();
		
	}
	
	class GameTimer extends AnimationTimer {
		
		@Override
		public void handle(long now) {
			//Updating the frame for animations and detecting collisions
			
			
			
			//Updating the elements if the screen is resized
			if (menuActive) {
				showMenu();
				updateMenu();
			} else if (runActive) {
				hideMenu();
				updatePlayer();
				updateBeams();
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
			
			for (int i = 0; i < NUMBER_BEAMS; i++) {
				//The beam must have reached the top so that it doesn't get re-shot prematurely
				if (beams[i].rect.getY() <= 0) {
					//Repositioning the beam to be above the player
					beams[i].isShot(player.playerImage.getX(), player.playerImage.getY());
					
					i = NUMBER_BEAMS;
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
		txtTitle.setFont(Font.font("verdana", HIDDEN));
		txtTitle.setX(SCREEN_WIDTH / 2 - (txtTitle.maxWidth(TITLE_SIZE) / 2));
		txtTitle.setY(SCREEN_HEIGHT / V_TITLE_POSITION - (txtTitle.maxHeight(TITLE_SIZE) / 2));
		txtTitle.setFill(Color.WHITE);
		
		//Instructions generation
		txtInstructions = new Text("INSTRUCTIONS: Use the left and right arrows (or A/D) to move side-to-side. \nUse the up arrow to shoot a beam."
				+ " Your goal is to hit and vanquish the \noncoming aliens before they reach you. Vanquish as many as possible"
				+ " \nfor the highest score!");
		txtInstructions.setFont(Font.font("verdana", HIDDEN));
		txtInstructions.setX(SCREEN_WIDTH / 2 - (txtInstructions.maxWidth(TITLE_SIZE) / 2));
		txtInstructions.setY(SCREEN_HEIGHT / V_INSTRUCTION_POSITION - (txtInstructions.maxHeight(TITLE_SIZE) / 2));
		txtInstructions.setFill(Color.WHITE);
		
		//Enter to start text generation
		txtEnterToStart = new Text("PRESS ENTER TO START");
		txtEnterToStart.setFont(Font.font("verdana", HIDDEN));
		txtEnterToStart.setX(SCREEN_WIDTH / 2 - (txtEnterToStart.maxWidth(TITLE_SIZE) / 2));
		txtEnterToStart.setY(SCREEN_HEIGHT / V_ENTER_TO_START_POSITION - (txtEnterToStart.maxHeight(TITLE_SIZE) / 2));
		txtEnterToStart.setFill(Color.WHITE);
		
		//Border generation
		leftBorder = new Rectangle(0, 0, BORDER_WIDTH, SCREEN_HEIGHT);
		leftBorder.setFill(Color.rgb(128, 3, 252));
		rightBorder = new Rectangle(SCREEN_WIDTH - BORDER_WIDTH, 0, BORDER_WIDTH, SCREEN_HEIGHT);
		rightBorder.setFill(Color.rgb(128, 3, 252));
		
		//Player generation
		player = new Player(SCREEN_WIDTH / 2 - (PLAYER_WIDTH / 2), SCREEN_HEIGHT * 0.8, true);
		player.hide();
		
		//Grouping the elements
		nodes = new Group(txtTitle, txtInstructions, txtEnterToStart, player.playerImage, leftBorder, rightBorder);
		
		//Beam generation + adding to group
		for (int i = 0; i < NUMBER_BEAMS; i++) {
			beams[i] = generateBeam();
			nodes.getChildren().add(beams[i].rect);
		}
		
		return nodes;
	}

	private void updateMenu() {
		//Handling if the user re-sizes the screen
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
	
	private void showMenu() {
		txtTitle.setFont(Font.font(TITLE_SIZE));
		txtInstructions.setFont(Font.font(INSTRUCTION_SIZE));
		txtEnterToStart.setFont(Font.font(TITLE_SIZE));
	}
	
	private void hideMenu() {
		txtTitle.setFont(Font.font(0));
		txtInstructions.setFont(Font.font(0));
		txtEnterToStart.setFont(Font.font(0));
	}
	
	private void updatePlayer() {
		
		double playerX = player.playerImage.getX();
		
		player.show();
		
		player.playerImage.setY(scene.getHeight() * 0.8);
		
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
			//The beam must be below the max height to move upwards again
			if (beams[i].rect.getY() >= 0) {
				beams[i].show();
				beams[i].rect.setY(beams[i].rect.getY() - BEAM_SPEED);
				
			//Hiding the beam if it reaches the top
			} else if (beams[i].rect.getY() <= 0){
				beams[i].hide();
			}
		}
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
