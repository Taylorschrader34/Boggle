package components;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import javafx.animation.RotateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Die {
	//Attributes shared by all Die objects
	static Random rand = new Random();
	static String[][] allDiceLettersEng = { 
			{"R","I","F","O","B","X"},{"I","F","E","H","E","Y"},{"D","E","N","O","W","S"},{"U","T","O","K","N","D"},
			{"H","M","S","R","A","O"},{"L","U","P","E","T","S"},{"A","C","I","T","O","A"},{"Y","L","G","K","U","E"},
			{"Qu","B","M","J","O","A"},{"E","H","I","S","P","N"},{"V","E","T","I","G","N"},{"B","A","L","I","Y","T"},
			{"E","Z","A","V","N","D"},{"R","A","L","E","S","C"},{"U","W","I","L","R","G"},{"P","A","C","E","M","D"},

	};
	static String[][] allDiceLettersSpan = {
			{"E","T","U","K","N","O"},{"E","V","G","T","I","N"},{"D","E","C","A","M","P"},{"I","E","L","R","U","W"},
			{"E","H","I","F","S","E"},{"R","E","C","A","L","S"},{"E","N","T","D","O","S"},{"O","F","X","R","I","A"},
			{"N","A","V","E","D","Z"},{"E","I","O","A","T","A"},{"G","L","E","N","Y","U"},{"B","M","A","Q","J","O"},
			{"T","L","I","B","R","A"},{"S","P","U","L","T","E"},{"A","I","M","S","O","R"},{"E","N","H","R","I","S"},
	};
	
	private String[] letters;
	private Rectangle rect;
	private Text text;
	private RotateTransition dieRotation;
	private RotateTransition textRotation;
	

	/*
	 * Creates all necessary fields for each of the 16 dice objects
	 * Rectangle and Text objects to display JavaFx graphics
	 * Creates dice in one position, a method to shuffle them will change the position of the graphics later
	 */
	public Die( int row, int col, int lang) {
		// set up the letters for every die
		this.letters = new String[6];
		if(lang == 0) {
			this.letters = Arrays.copyOf(allDiceLettersEng[4*col+row], 6); // English dice
		} else if(lang == 1) {
			this.letters = Arrays.copyOf(allDiceLettersSpan[4*col+row], 6); // Spanish dice

		}
		// set up rectangle parameters
		this.rect = new Rectangle();
		this.rect.setX(50+110*row);
		this.rect.setY(90+110*col);
		this.rect.setFill(Color.WHITE);
		this.rect.setHeight(100);
		this.rect.setWidth(100);
		this.rect.setArcHeight(10);
		this.rect.setArcWidth(10);
		this.rect.setStroke(Color.BLACK);
		//set up text parameters
		this.text = new Text();
		this.text.setText(this.roll());
		this.text.setTranslateX(85+110*row);
		this.text.setTranslateY(155+110*col);
		this.text.setStyle("-fx-font: 48 arial");
		
		this.dieRotation = new RotateTransition();
		this.dieRotation.setDuration(Duration.millis(250));
		this.dieRotation.setNode(this.getRect());
		this.dieRotation.setByAngle(360);
		this.dieRotation.setCycleCount(4);
		this.dieRotation.setAutoReverse(false);
		
		this.textRotation = new RotateTransition();
		this.textRotation.setDuration(Duration.millis(500));
		this.textRotation.setNode(this.getDieText());
		this.textRotation.setByAngle(360);
		this.textRotation.setCycleCount(2);
		this.textRotation.setAutoReverse(false);
	}
	
	/*
	 * Returns a random letter present on the die
	 */
	public String roll() {
		return this.letters[rand.nextInt(6)];
	}
	
	/*
	 * Shuffles the position of the dice in the allDiceLetters array to allow picking 
	 * of a random but unique set of letters for every Die in the dice array
	 */
	public static void shuffleAllDice() {
		Collections.shuffle(Arrays.asList(allDiceLettersEng));
	}
	/*
	 * return the rectangle object
	 */
	public Rectangle getRect() {
		return rect;
	}
	/*
	 * return the Text object
	 */
	public Text getDieText() {
		return text;
	}
	/*
	 * Self commenting code.... It animates the die!
	 */
	void animateDie() {
		this.dieRotation.play();
		this.textRotation.play();
	}
}
