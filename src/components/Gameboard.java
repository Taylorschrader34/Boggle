package components;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration; 

import components.WordChecker;

public class Gameboard extends Application {
	//Class Attributes used in game functionality
	String[] valids = new String[1000]; 
	Die[][] dice = new Die[4][4];
	private int score = 0;
	private int language = 0; // 0 = English, 1 = Spanish
	public List<String> enteredWordsList = new ArrayList<String>();
	public List<String> ScoredWordsList = new ArrayList<String>();
	private static final Integer startTime = 180;
	private Timeline timeline;
	private Label timerLabel = new Label();
	private IntegerProperty timeSeconds = new SimpleIntegerProperty(startTime);
	private String uberLogoPath = "/resources/UberBoggleLogo.png";
	private String muyLogoPath = "/resources/MuyBoggleLogo.png";



	//JavaFX objects for displaying interface
	private Pane pane = new Pane();
	public void start(Stage stage) {

		//Creates the Pane 
		createPane(pane);

		//Creates Enter words text
		Text enterWords = new Text();
		createEnterWords(enterWords);

		//Creates Text box for already entered words
		Rectangle wordsEnteredRec = new Rectangle();     

		//Creates the Text for the entered Words
		Text wordsEntered = new Text("Words Entered:");
		createWordsEntered(wordsEntered, wordsEnteredRec);

		//Creating Text Filed for Word input by player    
		TextField wordInput = new TextField();     
		createWordInput(wordInput);

		//Creating New Game Button 
		Button newGameButton = new Button(); 
		createNewGameButton(newGameButton);

		//Creating ? Button
		Button helpButton = new Button();  
		createHelpButton(helpButton);

		//Create Language Switch Button
		Button switchLangButton = new Button();
		createSwitchLangButton(switchLangButton);

		//Creates the Logo for the top right of the game
		//Gets Logo Path
		ImageView logoView = new ImageView();
		if(language==0) {
			createLogo(uberLogoPath, logoView);
		}else if(language==1) {
			createLogo(muyLogoPath, logoView);
		}

		//Creates changing timer text
		timerLabel.textProperty().bind(timeSeconds.asString());

		//Creates timer rectangle
		Rectangle timeRectangle = new Rectangle();     
		createTimer(timeRectangle, timerLabel);

		//Create dice Tray
		Rectangle diceTray = new Rectangle();
		createDiceTray(diceTray);
		createDice();

		//Creates the scene object
		Scene mainScreen = new Scene(pane);  

		//Fills the Pane with the game objects
		fillPane(stage, mainScreen,  pane,  logoView,  enterWords, 
				wordsEnteredRec,  wordsEntered,  wordInput, 
				newGameButton,  helpButton, switchLangButton,  timerLabel,  timeRectangle, diceTray,
				dice);

		/*
		 * When enter is pressed in the word input text box, send input to the addWord() function
		 */
		wordInput.setOnKeyPressed(new EventHandler<KeyEvent>(){

			public void handle(KeyEvent ke){
				if (ke.getCode().equals(KeyCode.ENTER)){
					addWord(wordsEntered, wordInput);
				}
			}
		});
		/*
		 * When the new game button is pressed run the newGame method
		 */
		newGameButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				newGame(wordsEntered);
			}
		});
		/*
		 * When the help button is pressed display the help menu
		 */
		helpButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				displayHelp();
			}
		});
		/*
		 * Switch language when switch language button is pressed
		 */
		switchLangButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				switchLanguage(stage, enterWords, wordsEntered, newGameButton, switchLangButton, logoView);
			}
		});
	} 

	private void addWord(Text wordsEntered, TextField wordInput) {
		String newInput = wordInput.getText().toUpperCase();
		if(newInput.length() <= 16 && ((! newInput.matches(".*[^A-Z].*")) || (! newInput.matches(".*[^a-z].*"))) && !newInput.equals("") && (timeline!=null) && !enteredWordsList.contains(newInput)) {
			enteredWordsList.add(newInput);
			String currentText = wordsEntered.getText() + "\n";
			currentText = currentText.concat(newInput);
			
			//Keeps list within bounds of box
			StringBuilder builder = new StringBuilder();
			String[] currentWordsArray = currentText.split("\n");
			if(currentWordsArray.length > 28) {
				builder.append(currentWordsArray[0]);
				for(int i = 2; i < currentWordsArray.length; i++) {
					builder.append("\n" + currentWordsArray[i]);
					currentText = builder.toString();
				}
			}
			
			wordsEntered.setText(currentText);
			wordInput.clear();
		}

		else {
			wordInput.clear();
		}
	}
	/*
	 * Contains all functionality to start a new game. Triggered by pressing the "New Game" button
	 */
	private void newGame(Text wordsEntered) {
		//shuffle the dice
		createDice();
		//reset timer
		if (timeline != null) {
			timeline.stop();
		}
		timeSeconds.set(startTime);
		timeline = new Timeline();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(startTime+1),
						new KeyValue(timeSeconds, 0)));
		timeline.playFromStart();
		timeline.setOnFinished(event ->{
			try {
				gameOver(score);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		});
		//clear the entered word list
		enteredWordsList.clear();
		wordsEntered.setText("Words Entered:");
		//update the dice in the graphics pane
		updateDicePane();
	}
	private void displayHelp() {
		Alert helpMenuAlert = new Alert(AlertType.INFORMATION);
		if (language==0) {
			helpMenuAlert.setTitle("Help");
			helpMenuAlert.setHeaderText("How to Play:");
			helpMenuAlert.setContentText("Press \"New Game\" to start a new game\n"
					+ "To play, type words in the \"Enter Words:\" Box, and press the Enter key. "
					+ "Words will be listed in the \"Entered Words\" box.\n"
					+ "Score will be displayed when the timer runs out.");
		}else if(language==1) {
			helpMenuAlert.setTitle("Ayuda");
			helpMenuAlert.setHeaderText("Cómo Jugar:");
			helpMenuAlert.setContentText("Presione \"Juego Nuevo\" para comenzar un nuevo juego \n "
					+ "Para jugar, escriba palabras en el cuadro \"Entre palabras:\" y presione la tecla Intro. "
					+ "Las palabras se enumerarán en el cuadro \"Palabras entrados\". "
					+ "La puntuación se mostrará cuando se agote el tiempo. \n"
					+ "Puedes ignorar los acentos.");
		}
		helpMenuAlert.show();
	}
	/*
	 * Switches the language of literally everything!
	 */
	private void switchLanguage(Stage stage, Text enterWords, Text wordsEntered, Button newGameButton,
			Button switchLangButton, ImageView logoView) {
		if(language==0) { // to spanish
			language=1;
			//Change all the text
			switchLangButton.setText("Play in\nEnglish");
			wordsEntered.setText("Palabras Entrados:");
			enterWords.setText("Entre Palabras:");
			newGameButton.setText("Juego\nNuevo");
			stage.setTitle("Muy Boggle"); 
			//Reset timer and stop game if in progress
			if (timeline != null) {
				timeline.stop();
			}
			timeSeconds.set(startTime);
			//Change logo
			createLogo(muyLogoPath, logoView);
			pane.getChildren().remove(logoView);
			pane.getChildren().add(logoView);

		}else if(language==1) { // to english
			language=0;
			//Change Text
			switchLangButton.setText("Jugar en\nEspanol");
			wordsEntered.setText("Words Entered:");
			enterWords.setText("Enter Words:");
			newGameButton.setText("New\nGame");
			stage.setTitle("Uber Boggle");
			//Timer reset
			if (timeline != null) {
				timeline.stop();
			}
			timeSeconds.set(startTime);
			//Change logo
			createLogo(uberLogoPath, logoView);
			pane.getChildren().remove(logoView);
			pane.getChildren().add(logoView);

		}
	}
	//Function that create the dice tray for the board
	//Input is a Rectangle object
	private void createDiceTray(Rectangle diceTray) {
		diceTray.setX(40);
		diceTray.setY(80);
		diceTray.setHeight(450);
		diceTray.setWidth(450);
		diceTray.setFill(Color.color(.208, .408, .667));
		diceTray.setStroke(Color.BLACK);
	}

	/*
	 * Creates the timer box rectangle and text
	 */
	private void createTimer(Rectangle timeRectangle, Label timerLabel) {
		timeRectangle.setX(300);
		timeRectangle.setY(10);
		timeRectangle.setHeight(60);
		timeRectangle.setWidth(110);
		timeRectangle.setFill(Color.WHITE);
		timeRectangle.setStroke(Color.BLACK);

		timerLabel.setTranslateX(308);
		timerLabel.setTranslateY(15);
		timerLabel.setStyle("-fx-font: 48 arial");
	}

	//Function that creates the ImageView logo object
	//Input is a String with the path to the image file and a ImageView object
	private void createLogo(String logoPath, ImageView logoView) {
		//Sets the logoImage to the logoView
		Image logoImage = new Image(logoPath);
		logoView.setImage(logoImage);

		//Resize the Logo
		logoView.setFitWidth(127);
		logoView.setPreserveRatio(true);
		logoView.setX(40);
		logoView.setY(5);

	}
	//Function that create the a Help button
	//Input is a Button object
	private void createHelpButton(Button helpButton) {
		helpButton.setText("?");
		Rectangle helpButtonRec = new Rectangle(60,60);
		helpButtonRec.setArcHeight(10);
		helpButtonRec.setArcWidth(10);
		helpButton.setShape(helpButtonRec);
		helpButton.setTranslateX(625);
		helpButton.setTranslateY(10);
		helpButton.setPrefSize(60, 60);
		helpButton.setStyle("-fx-background-color: #3568aa; -fx-text-fill: #ffffff; -fx-border-style: solid; -fx-border-width: 1px; -fx-borrder-radius: 10px");
	}

	//Function that creates the newGame Button
	//Input is a Button object
	private void createNewGameButton(Button newGame) {
		newGame.setText("New\nGame");
		Rectangle newGameButtonRec = new Rectangle(60,60);
		newGameButtonRec.setArcHeight(10);
		newGameButtonRec.setArcWidth(10);
		newGame.setShape(newGameButtonRec);
		newGame.setTranslateX(525);
		newGame.setTranslateY(10);
		newGame.setPrefSize(60, 60);
		newGame.setStyle("-fx-background-color: #3568aa; -fx-text-fill: #ffffff; -fx-border-style: solid; -fx-border-width: 1px; -fx-borrder-radius: 10px");

	}
	private void createSwitchLangButton(Button switchLang) {
		switchLang.setText("Jugar en\nEspanol");
		Rectangle switchLangButtonRec = new Rectangle(60,60);
		switchLangButtonRec.setArcHeight(10);
		switchLangButtonRec.setArcWidth(10);
		switchLang.setShape(switchLangButtonRec);
		switchLang.setTranslateX(620);
		switchLang.setTranslateY(540);
		switchLang.setPrefSize(65, 65);
		switchLang.setStyle("-fx-background-color: #3568aa; -fx-text-fill: #ffffff; -fx-border-style: solid; -fx-border-width: 1px; -fx-borrder-radius: 10px");

	}
	/*
	 * Creates Language Switch Button
	 */

	//Function that creates a TextField for Player word input
	//Input is a TextField object
	private void createWordInput(TextField wordInput) {
		wordInput.setPrefHeight(25);
		wordInput.setPrefWidth(210);
		wordInput.setTranslateX(235);
		wordInput.setTranslateY(575);

	}

	/*
	 * Creates the text and background rectangle for the words entered box
	 */
	private void createWordsEntered(Text wordsEntered, Rectangle wordsEnteredRec) {
		wordsEntered.setText("Words Entered:\n");
		wordsEntered.setTranslateX(502);
		wordsEntered.setTranslateY(92);

		wordsEnteredRec.setX(500);
		wordsEnteredRec.setY(80);
		wordsEnteredRec.setHeight(450);
		wordsEnteredRec.setWidth(185);
		wordsEnteredRec.setFill(Color.WHITE);
		wordsEnteredRec.setStroke(Color.BLACK);

	}


	//Function creates the Enter Words text
	//Input is a Text object
	private void createEnterWords(Text enterWords) {
		enterWords.setText("Enter Words:");
		enterWords.setX(265);
		enterWords.setY(560);
		enterWords.setStyle("-fx-font: 24 arial");

	}

	//Function creates the Pane for the game
	//Input is a pane object
	private void createPane(Pane pane) {
		pane.setMinSize(720, 640);  
		pane.setStyle("-fx-background-color: #bac8d3");

	}

	public void fillPane(Stage stage,Scene scene, Pane pane, ImageView logoView, Text enterWords, 
			Rectangle wordsEnteredRec, Text wordsEntered, TextField wordInput, 
			Button newGameButton, Button helpButton, Button switchLangButton, Label timerLabel, Rectangle timeRectangle, 
			Rectangle diceTray, Die dice[][]) {

		pane.getChildren().add(logoView);
		pane.getChildren().add(enterWords);
		pane.getChildren().add(wordsEnteredRec);
		pane.getChildren().add(wordsEntered);
		pane.getChildren().add(diceTray);
		pane.getChildren().add(timeRectangle);
		pane.getChildren().add(timerLabel);
		pane.getChildren().add(wordInput);
		pane.getChildren().add(newGameButton);
		pane.getChildren().add(helpButton);
		pane.getChildren().add(switchLangButton);

		updateDicePane();

		//Creating a scene object 
		scene.setRoot(pane);

		//Setting title of the Game 
		stage.setTitle("Uber Boggle");

		//Adding scene to the stage 
		stage.setScene(scene); 

		//Displaying the contents of the stage 
		stage.show(); 
	}
	/*
	 * Updates the dice for display on the pane
	 */
	private void updateDicePane() {

		for(int i=0;i<dice.length;i++) {
			for(int j=0;j<dice.length;j++) {
				pane.getChildren().add(dice[i][j].getRect());
				pane.getChildren().add(dice[i][j].getDieText());
			}
		}

	}
	/*
	 * Creates a 4x4 array of Die objects for use on the Gameboard
	 */
	private void createDice() {
		// shuffles all dice before creating, so every position gets a random but unique set of letters
		Die.shuffleAllDice(); 
		for(int j = 0; j<dice.length; j++) {
			for(int i = 0; i<dice[0].length; i++) {
				if(dice[i][j]!=null) {
					pane.getChildren().remove(dice[i][j].getRect());
					pane.getChildren().remove(dice[i][j].getDieText());
				}

				dice[i][j] = new Die(i, j, language);
				dice[i][j].animateDie();
			}
		}
	}
	public static void launchBoard(String args[]) {
		launch(args);
	}
	private void gameOver(int score) throws FileNotFoundException {

		System.out.println("Game Over!");
		char boggleBoardChars[][] = new char[4][4];

		//Creates the char board of all boggle dice in their place
		for(int i = 0; i<4; i++) {
			for(int j = 0; j<4; j++) {
				boggleBoardChars[i][j] = dice[i][j].getDieText().getText().charAt(0);
			}
		}

		//Gets all valid words on the board 
		valids = WordChecker.validateAllWords(boggleBoardChars, this.language);
		for(String s : valids)
			if(s != null) {
				System.out.println(s);
			}
		//For each valid word, check if it was entered by user, if yes, increment score
		for(String s: valids) {
			if(enteredWordsList.contains(s) && !ScoredWordsList.contains(s)){
				ScoredWordsList.add(s);
				if(s.length() >2 && s.length() < 5) {
					System.out.println(s + " score was " + 1);
					score = score + 1;
				}else if(s.length() == 5) {
					System.out.println(s + " score was " + 2);
					score = score + 2;
				}else if(s.length() == 6) {
					System.out.println(s + " score was " + 3);
					score = score + 3;
				}else if(s.length() == 7) {
					System.out.println(s + " score was " + 5);
					score = score + 5;
				}if(s.length() > 7) {
					System.out.println(s + " score was " + 11);
					score = score + 11;
				}
			}
		}

		//stop accepting input in text box
		timeline = null;
		//display a game over screen
		Alert gameOverAlert = new Alert(AlertType.INFORMATION);
		if (language==0) {
			gameOverAlert.setTitle("Game Over!");
			gameOverAlert.setHeaderText("Game Over!");
			gameOverAlert.setContentText("Your score was " + score + ".\nPlay again to try and beat your score!");
		} else if (language == 1) {
			gameOverAlert.setTitle("Juego Terminado!");
			gameOverAlert.setHeaderText("Juego Terminado!");
			gameOverAlert.setContentText("Tu puntuaciï¿½n fue " + score + ". Juega de nuevo para tratar de superar tu puntuaciï¿½n ... ï¿½En espaï¿½ol!");
		}
		gameOverAlert.show();
	}
} 