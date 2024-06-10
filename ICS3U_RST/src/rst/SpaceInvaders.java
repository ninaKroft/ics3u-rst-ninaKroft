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
	static final double SCREEN_WIDTH = 1400, SCREEN_HEIGHT = 700;
	boolean menuActive = true, runActive = false, lossActive = false;
	
	//Text objects
	Text txtInstructions, txtTitle, txtEnterToStart, txtSpaceToPause, txtCToContinue, txtRToRestart;
	//Text sizes
	final int TITLE_SIZE = 50, INSTRUCTION_SIZE = 20, V_TITLE_POSITION = 3, V_INSTRUCTION_POSITION = 2, BORDER_WIDTH = 75;
	final double V_ENTER_TO_START_POSITION = 1.25;
	
	Scene scene;
	
	Group root = null;
	
	Rectangle leftBorder, rightBorder;
	
	@Override
	public void start(Stage myStage) throws Exception {
		//Setting the stage and checking for keys pressed
		
		//MENU SCREEN
		
		//Create the elements to be displayed on the menu screen (text, decos)
		//add it to the group
		
		//List fonts
		//System.out.println(Font.getFontNames());
		
		//Title setup
		
		if (menuActive) {
			txtTitle = new Text("SPACE INVADERS");
			txtTitle.setFont(Font.font("verdana", TITLE_SIZE));
			txtTitle.setX(SCREEN_WIDTH / 2 - (txtTitle.maxWidth(TITLE_SIZE) / 2));
			txtTitle.setY(SCREEN_HEIGHT / V_TITLE_POSITION - (txtTitle.maxHeight(TITLE_SIZE) / 2));
			txtTitle.setFill(Color.WHITE);
			
			//Instructions setup
			txtInstructions = new Text("INSTRUCTIONS: Use the left and right arrows to move side-to-side. \nUse the up arrow to shoot a beam."
					+ " Your goal is to hit and vanquish the \noncoming aliens before they reach you. Vanquish as many as possible"
					+ " \nfor the highest score!");
			txtInstructions.setFont(Font.font("verdana", INSTRUCTION_SIZE));
			txtInstructions.setX(SCREEN_WIDTH / 2 - (txtInstructions.maxWidth(TITLE_SIZE) / 2));
			txtInstructions.setY(SCREEN_HEIGHT / V_INSTRUCTION_POSITION - (txtInstructions.maxHeight(TITLE_SIZE) / 2));
			txtInstructions.setFill(Color.WHITE);
			
			//Enter to start text
			txtEnterToStart = new Text("PRESS ENTER TO START");
			txtEnterToStart.setFont(Font.font("verdana", TITLE_SIZE));
			txtEnterToStart.setX(SCREEN_WIDTH / 2 - (txtEnterToStart.maxWidth(TITLE_SIZE) / 2));
			txtEnterToStart.setY(SCREEN_HEIGHT / V_ENTER_TO_START_POSITION - (txtEnterToStart.maxHeight(TITLE_SIZE) / 2));
			txtEnterToStart.setFill(Color.WHITE);
			
			//Borders
			leftBorder = new Rectangle(0, 0, BORDER_WIDTH, SCREEN_HEIGHT);
			leftBorder.setFill(Color.rgb(128, 3, 252));
			rightBorder = new Rectangle(SCREEN_WIDTH - BORDER_WIDTH, 0, BORDER_WIDTH, SCREEN_HEIGHT);
			rightBorder.setFill(Color.rgb(128, 3, 252));
			
			//Grouping the elements
			root = new Group(txtTitle, txtInstructions, txtEnterToStart, leftBorder, rightBorder);
		
		} else if (runActive) {
			//Generate elements for running
		} else if (lossActive) {
			//Generate elements for loss screen
		}
		
		GameTimer timer = new GameTimer();
		timer.start();
		
		scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
		scene.setFill(Color.rgb(10, 3, 23));
		
		myStage.setTitle("Space Invaders");
        myStage.setScene(scene);
        myStage.show();
		
	}
	
	class GameTimer extends AnimationTimer {
		
		@Override
		public void handle(long now) {
			//Updating the frame for animations and detecting collisions
			
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
			
			//Borders
			leftBorder.setHeight(scene.getHeight());
			rightBorder.setHeight(scene.getHeight());
			rightBorder.setX(scene.getWidth() - BORDER_WIDTH);
		}
	}
	
	private void handleKeyPressed(KeyEvent event) {
		//handling keys pressed
	}
	
	private void handleKeyReleased(KeyEvent event) {
		//handling keys released
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
