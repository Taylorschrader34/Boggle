package components;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WordChecker {
	
	public static int totalBoardWords = 0;
	private static String[] allValidWords = new String[1000];
	private static String dictionaryEng = "src/resources/scrabbleWordsEng.txt";
	private static String dictionarySpan = "src/resources/dictionarySpanish.txt";

	// Checks if a tile being accessed is in bounds and not yet visted
	private static boolean validBoardTile(int i, int j, boolean visitedTiles[][]) { 
		if(i >= 0 && j >= 0 && i < 4 && j < 4 && !visitedTiles[i][j]) {
			return true;
		}
		return false; 
	}
	// Search through the board to construct words recursively - called by find words
	private static void boggleBoardSearch(Trie trie, char boggleBoardChars[][], int row, 
			int col, boolean visitedTiles[][], String str, String[] valids) { 

		if(trie.isInTrie(str.toUpperCase()) && str.length() > 2) {
			valids[totalBoardWords] = str.toUpperCase();
			totalBoardWords++;
		}

		if (validBoardTile(row, col, visitedTiles)) { 
			// set the visited tile to TRUE in the array
			visitedTiles[row][col] = true; 

			//Goes through all the possible combinations of words based on that tile and previously
			// visited tiles
			for (int i =0; i < 26; i++) { 

				if (trie.getRoot().getChildren() != null) { 
					char ch = (char) (i + 'A') ; 

					// checks all the possible tiles next to it for words,
					// up, upLeft, upRight, left, right, down, downLeft, downRight
					if (validBoardTile(row+1,col, visitedTiles)  && boggleBoardChars[row+1][col]  == ch) {
						boggleBoardSearch(trie,boggleBoardChars,row+1, col, visitedTiles,str+ch, valids); 
					}
					if (validBoardTile(row+1,col-1,visitedTiles) && boggleBoardChars[row+1][col-1]  == ch) {
						boggleBoardSearch(trie,boggleBoardChars,row+1, col-1, visitedTiles,str+ch, valids); 
					}
					if (validBoardTile(row+1,col+1,visitedTiles) && boggleBoardChars[row+1][col+1] == ch) {
						boggleBoardSearch(trie,boggleBoardChars,row+1,col+1, visitedTiles,str+ch, valids); 
					}
					if (validBoardTile(row, col-1,visitedTiles)&& boggleBoardChars[row][col-1]  == ch) {
						boggleBoardSearch(trie,boggleBoardChars,row,col-1, 	visitedTiles,str+ch, valids); 
					}
					if (validBoardTile(row, col+1,visitedTiles)  && boggleBoardChars[row][col+1] == ch) {
						boggleBoardSearch(trie,boggleBoardChars,row, col+1, visitedTiles,str+ch, valids); 
					}
					if (validBoardTile(row-1, col,visitedTiles) && boggleBoardChars[row-1][col]  == ch) {
						boggleBoardSearch(trie,boggleBoardChars,row-1, col,  visitedTiles,str+ch, valids);
					} 
					if (validBoardTile(row-1,col-1,visitedTiles) && boggleBoardChars[row-1][col-1]  == ch) {
						boggleBoardSearch(trie,boggleBoardChars,row-1, col-1, visitedTiles,str+ch, valids); 
					}
					if (validBoardTile(row-1,col+1,visitedTiles) && boggleBoardChars[row-1][col+1] == ch) {
						boggleBoardSearch(trie,boggleBoardChars,row-1, col+1, visitedTiles,str+ch, valids);
					} 
				} 
			} 
			//since we are done testing this tile we set it back to being nonvisited for this test case
			visitedTiles[row][col] = false; 
		} 
	}
	/*
	 * Uses boggleBoardSearch to find all valid words on the board to later check
	 * against the list of user-entered words
	 */
	private static void findWords(char boggleBoardChars[][], Trie trie, String[] valids) { 
		boolean[][] visitedTiles = new boolean[4][4]; 

		String str = ""; 
		//searches for valid word starting at each die on board
		for (int row = 0 ; row < 4; row++) { 
			for (int col = 0 ; col < 4 ; col++) { 
				if (trie.isInTrie( (boggleBoardChars[row][col] + "").toUpperCase())) { 
					str = str+boggleBoardChars[row][col]; 
					boggleBoardSearch(trie, boggleBoardChars, row, col, visitedTiles, str, valids); 
					str = ""; 
				} 
			} 
		} 
	} 
	/*
	 * Called by GameBoard to create and return the list of allValidWords using other helper methods
	 */
	public static String[] validateAllWords(char[][] boggleBoardChars, int lang) throws FileNotFoundException { 
		// load dictionary
		File wordList = null;
		if(lang == 0) {
			wordList = new File(dictionaryEng);
		} else if(lang==1) {
			wordList = new File(dictionarySpan);
		}
		Scanner scan = new Scanner(wordList);
		// create trie to store dictionary words for later lookup and insert words
		Trie trie = new Trie(); 

		while(scan.hasNextLine()) {
			trie.insert(scan.nextLine()); 
		}
		//
		findWords(boggleBoardChars, trie ,allValidWords);
		
		//close scanner and return
		scan.close();
		return allValidWords;
	} 

}
