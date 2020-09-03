import java.util.Arrays;

public class Model {
	private int[][] occupiedSquares = new int[8][7];
	private int[][] colorOfSquares = new int [8][7]; 
	
	public static final int SUN = 0, PLUS = 1, TRIANGLE = 2, CHEVRON = 3, ARROW = 4;
	public static final int BLACK = 0, WHITE = 1;
	
	private int [] justClicked = {0,0,10};
	private int [] lastClicked = {0,0,10};
	private int[] previousClicks = new int[6]; 
	
	private int turnCounter = 0; 
	private int undoCounter = 0;
	
	boolean capture = false; 
	int lastCaptured = 10; 
	int restorePosition[] = {0,0,10,2}; 
	int lastPlayed = 0; 
	
	private boolean check = false;
	private boolean checkMate = false;
	int[] threatPosition = new int[3]; 
	
	private Controller control = new Controller();
	
	public void setCheck(){
		int checkCounter = 0;
		if(control.playerTurn()){ 
			int[] sunsPosition = getBlueSunLocation();
			for(int i = 0; i < occupiedSquares.length; i++){
				for(int j = 0; j <occupiedSquares.length; j++){
					if(occupiedSquares[i][j] != 10 && colorOfSquares[i][j] == 1){ 
						System.out.println("ColorOfSquares: "+colorOfSquares[i][j]+" and Last played: "+lastPlayed);
						System.out.println("Found a possible "+colorLookup(lastPlayed)+" threat at: ["+i+","+j+"]");
					if(control.checkIfLegalForCheck(i, j, sunsPosition[0], sunsPosition[1], occupiedSquares[i][j], occupiedSquares, colorOfSquares)){
						check = true;
						checkCounter ++;
						threatPosition[0] = i;
						threatPosition[1] = j;
						threatPosition[2] = occupiedSquares[i][j];
						System.out.println("Check has been set by the opponent piece at: "+Arrays.toString(threatPosition));
					}else System.out.println("Wasn't found legal");
					}
				}
			}
		}else{ 
			int[] sunsPosition = getRedSunLocation();
			for( int i = 0; i < occupiedSquares.length; i++){
				for(int j = 0; j <occupiedSquares.length; j++){
					if(occupiedSquares[i][j] != 10 && colorOfSquares[i][j] == 0){ 
						System.out.println("ColorOfSquares: "+colorOfSquares[i][j]+" and Last played: "+lastPlayed);
						System.out.println("Found a possible "+colorLookup(lastPlayed)+" threat at: ["+i+","+j+"]");
					if(control.checkIfLegalForCheck(i, j, sunsPosition[0], sunsPosition[1], occupiedSquares[i][j], occupiedSquares, colorOfSquares)){
						check = true;
						checkCounter ++;
						threatPosition[0] = i;
						threatPosition[1] = j;
						threatPosition[2] = occupiedSquares[i][j];
						System.out.println("Check has been set by the opponent piece at: "+Arrays.toString(threatPosition));
					}else System.out.println("Wasn't found legal");
					}
				}
			}
		}
		if(checkCounter == 0){
			System.out.println("The checkCounter is 0");
		}else{
			System.out.println("check has been set by the opponent piece at: "+Arrays.toString(threatPosition));
		}
	}
	protected int[] getRedSunLocation(){
		int[] sunLocation = new int[3];
			for(int i = 0; i < occupiedSquares.length; i++){
				for(int j = 0; j <occupiedSquares.length; j++){
					if(occupiedSquares[i][j] == 0 && colorOfSquares[i][j] == 1){ 
						System.out.println("Found the white king at: ["+i+","+j+"]");
						sunLocation[0]=i;
						sunLocation[1]=j;
						sunLocation[2]=1; 
					}
				}
			}
		return sunLocation;
	}
	
	protected int[] getBlueSunLocation(){
		int[] sunLocation = new int[3];
			for(int i = 0; i < occupiedSquares.length; i++){
				for(int j = 0; j <occupiedSquares.length; j++){
					if(occupiedSquares[i][j] == 0 && colorOfSquares[i][j] == 0){ //if piece is a black king
						System.out.println("Found the black king at: ["+i+","+j+"]");
						sunLocation[0]=i;
						sunLocation[1]=j;
						sunLocation[2]=0; //third index holds king's color
					}
				}
			}
		return sunLocation;
	}
	
	public void checkBehavior(){
		int[][] defaultOccupied = occupiedSquares;
		int[][] defaultColored = colorOfSquares;
		if(check){
			defaultOccupied[justClicked[0]][justClicked[1]] = lastClicked[2];
			defaultColored[justClicked[0]][justClicked[1]] = defaultColored[lastClicked[0]][lastClicked[1]];
			if(control.checkIfLegalForCheck(lastClicked[0], lastClicked[1], justClicked[0], justClicked[1], defaultOccupied[lastClicked[0]][lastClicked[1]], defaultOccupied, defaultColored)){
				int checkCounter = 0;
					int[] sunsPosition = getRedSunLocation();
					for(int i = 0; i < defaultOccupied.length; i++){
						for(int j = 0; j <defaultOccupied.length; j++){
							if(defaultOccupied[i][j] != 10 && defaultColored[i][j] == 0){ 
								if(control.checkIfLegalForCheck(i, j, sunsPosition[0], sunsPosition[1], defaultOccupied[i][j], defaultOccupied, defaultColored)){
									check = true;
									checkCounter ++;
									threatPosition[0] = i;
									threatPosition[1] = j;
									threatPosition[2] = defaultOccupied[i][j];
									System.out.println("Check has been set by the opponent piece at: "+Arrays.toString(threatPosition));
							}else{
								System.out.println("Wasn't found legal");
							}
							}
						}
					sunsPosition = getBlueSunLocation();
					for(  i = 0; i < defaultOccupied.length; i++){
						for(int j = 0; j <defaultOccupied.length; j++){
							if(defaultOccupied[i][j] != 10 && defaultColored[i][j] == 1){
								if(control.checkIfLegalForCheck(i, j, sunsPosition[0], sunsPosition[1], defaultOccupied[i][j], defaultOccupied, defaultColored)){
									check = true;
									checkCounter ++;
									threatPosition[0] = i;
									threatPosition[1] = j;
									threatPosition[2] = defaultOccupied[i][j];
									System.out.println("Check has been set by the opponent piece at: "+Arrays.toString(threatPosition));
								}else {
									System.out.println("Wasn't found legal");
								}
							}
						}
					}
				}
				if(checkCounter == 0){
					check = false;
					System.out.println("The checkCounter is 0");
				}else{
					check = true;
					System.out.println("check has been set by the opponent piece at: "+Arrays.toString(threatPosition));
				}
			}
		}
	}
	
	public boolean getCheck(){
		return check;
	}
	
	public void setCheckMate(){
		checkMate = true;
	}
	public boolean getCheckMate(){
		return checkMate;
	}
	
	
	/**
	 * This method will return the 2D array holding the positions of all pieces on the board
	 * It attains the location of these values from the occupiedSquares 2D int array in this class
	 * 10 = empty space (this is indicated by 10 because the Queen is using 0 as an index and id,
	 * SUN = 0
	 * PLUS = 1
	 * TRIANGLE = 2
	 * CHEVRON = 3
	 * ARROW = 4
	 * @returns the occupiedSquares[][] int array holding the values of all the square contents
	 */
	public int[][] getOccupiedSquares(){
		return occupiedSquares;
	}
	
	/**
	 * This class will return the 2D array representing the game board, and holds the values of whatever color is in each square on the board
	 * The location of values being checked is stored in the occupiedSquares.
	 * If the cell content is:
	 * 	0 = Black piece
	 *  1 = White piece
	 *  2 = Empty square
	 * @returns the coloredSquares[][] int array holding values of the square content's colors
	 */
	public int[][] getColorOfSquares(){
		return colorOfSquares;
	}
	
	//Outside accessors to the justClicked and lastClicked values, intended to check for legal moves
	public int[] getLastClicked(){
		return lastClicked;
	}
	public int[] getJustClicked(){
		return justClicked;
	}
	
	public boolean isOccupied(int rowIndex, int colIndex){
		boolean squareHasPiece = false;
		if(occupiedSquares[rowIndex][colIndex] != 10){
			squareHasPiece = true;
		}
		return squareHasPiece;
	}
	

	/**
	 * This method will set a square's contents in the 2D array holding the positions of all pieces on the board (occupiedSquares)
	 * 		It will change the location of pieces on the board inside the occupiedSquares array, using a given index [row][column], and value to set in the position
	 * The following values should be used to assign a piece to a position on the board:
	 * 10 = empty space (this is indicated by 10 because the Queen is using 0 as an index and id,
	 * SUN = 0
	 * PLUS = 1
	 * TRIANGLE = 2
	 * CHEVRON = 3
	 * ARROW = 4
	 * @param rowIndex - the id for the row in occupiedSquares to lookup for changing
	 * @param colIndex - the id for the column in occupiedSquares to lookup for changing
	 * @param occupiedValue - the actual int value of the piece (or empty square) being set in the given index [row][column] of occupiedSquares
	 */
	public void setOccupiedSquares(int rowIndex, int colIndex, int occupiedValue){
		occupiedSquares[rowIndex][colIndex] = occupiedValue;
	}
	
	/**
	 * This class will set the color of a square's contents in the 2D array representing the color of game board contents
	 * The location of the square whose color value is being set is given by the parameters for index: [row][column]; and the int color value to be assigned
	 * The following int values are used to set what color the contents of the indexed square are:
	 * 	0 = Black piece
	 *  1 = White piece
	 *  2 = Empty square
	 * @param rowIndex - the id for the row in coloredSquares to lookup for changing
	 * @param colIndex - the id for the column in coloredSquares to lookup for changing
	 * @param occupiedColor - the actual int color value of the piece (or empty square) being set at the index [row][column] of coloredSquares
	 */
	public void setColorOfSquares(int rowIndex, int colIndex, int occupiedColor){
		colorOfSquares[rowIndex][colIndex] = occupiedColor;
	}
	
	public int getTurnCounter(){
		return turnCounter;
	}
	public int getUndoCounter(){
		return undoCounter;
	}
	public void setTurnCounter(int newTurnCounter){
		turnCounter = newTurnCounter;
	}
	public void setUndoCounter(int newUndoCounter){
		undoCounter = newUndoCounter;
	}
	
	protected void setJustClicked(String newClickedSquare){
		lastClicked[0] = justClicked[0];
		lastClicked[1] = justClicked[1];
		lastClicked[2] = justClicked[2]; 
		
		int iIndex = Integer.parseInt(newClickedSquare.substring(0, 1)); 
		int jIndex =  Integer.parseInt(newClickedSquare.substring(2)); 

		justClicked[0] = iIndex;
		justClicked[1] = jIndex;
		
		justClicked[2] = occupiedSquares[iIndex][jIndex];
		System.out.println("The Just Clicked values are ["+justClicked[0]+" "+justClicked[1]+" "+justClicked[2]+"]");
		System.out.println("The Last Clicked values are["+lastClicked[0]+" "+lastClicked[1]+" "+lastClicked[2]+"]");
	}
	
	/**
	 * Returns the numerical value of the piece occupying the square that was clicked, by looking up a given index in the occupiedSquares 2D array
	 * @param clickedButtonString is the string passed by the square that was just (most recently) clicked. This contains the int values of index location of the clicked square
	 * @returns the int value at the index given in occupiedSquares, this int value representing what is inside the square that was clicked
	 */
	protected int getOccupiedOnClick(String clickedButtonString) {
		int iIndex = Integer.parseInt(clickedButtonString.substring(0, 1));
		int jIndex =  Integer.parseInt(clickedButtonString.substring(2)); 
		
		
		return occupiedSquares[iIndex][jIndex];
	}
	
	protected void setDefaults(){
		clearPreviousSavedClicks();
		
		turnCounter=0;
		undoCounter=0;
		
		capture = false; 
		lastCaptured = 10; 
		
		check = false;
		checkMate = false;
		
	}
	protected void clearPreviousSavedClicks(){
		justClicked[0] = 0; 
		justClicked[1] = 0;
		justClicked[2] = 10;
		lastClicked [0] = 0;
		lastClicked [1] = 0;
		lastClicked [2] = 10;
		previousClicks[0] = 0; 
		previousClicks[1] = 10;
		previousClicks[2] = 0;
		previousClicks[3] = 0;
		previousClicks[4] = 10;
		restorePosition[0] = 0;
		restorePosition[1] = 0;
		restorePosition[2] = 10;
		restorePosition[3] = 2;
		threatPosition[0] = 0;
		threatPosition[1] = 0;
		threatPosition[2] = 10;
	}
	

	protected void setPreviousClicks(int[] justClicked){
			previousClicks[4] = justClicked[2];
			previousClicks[3] = justClicked[1];
			previousClicks[2] = justClicked[0]; 
			previousClicks[1] = lastClicked[2];
			previousClicks[0] = lastClicked[0]; 
			
			setRestorePositions();
			
			System.out.println("Previous clicks recorded for undo: "+Arrays.toString(previousClicks));
			
		
	}
	
	protected void setRestorePositions(){
		restorePosition[0] = justClicked[0];
		restorePosition[1] = justClicked[1]; 
		restorePosition[2] = lastCaptured; 
		restorePosition[3] = 1 - colorOfSquares[justClicked[0]][justClicked[1]]; 
	}
	
	protected int[] getPreviousClicks(){
		return previousClicks;
	}
	

	protected void setCapture(){
		capture = true;
		lastCaptured = justClicked[2];
		System.out.println("Capture of "+pieceLookup(justClicked[2])+" will occur: "+capture);
		
	}
	
	
	public boolean undoLastMove(){
		boolean isUndoOk = false;
		System.out.println("Previous clicks recorded for undo: "+Arrays.toString(previousClicks));
		if (undoCounter == 0 && turnCounter >=1 && capture==false){
			int num = 3;
			for(int i=0; i<3; i++){
				justClicked[i] = previousClicks[i];  
				lastClicked[i] = previousClicks[num]; 
				num++;
			}
			
			int swapValue = justClicked[2]; 
			justClicked[2] = lastClicked[2]; 
			lastClicked[2] = swapValue;
			undoCounter++;
			isUndoOk = true;
			turnCounter--;
		}
		else if (undoCounter == 0 && turnCounter >=1 && capture==true){
			System.out.println("Attempting to undo a move that included a capture");
			int num = 3;
			for(int i=0; i<3; i++){
				justClicked[i] = previousClicks[i];  
				lastClicked[i] = previousClicks[num]; 
				num++;
			}
			int swapValue = justClicked[2]; 
			justClicked[2] = lastClicked[2]; 
			lastClicked[2] = swapValue;
			
			undoCounter++;
			isUndoOk = true;
			turnCounter--; 
			
		}
		else{
			clearPreviousSavedClicks();
			System.out.println("Cannot undo move, sorry!");
		}
		System.out.println("New JustClicked Values: "+Arrays.toString(justClicked));
		System.out.println("New LastClicked Values: "+Arrays.toString(lastClicked));
		return isUndoOk;
	}
	
	public void restoreArrayPositions(){
		occupiedSquares[restorePosition[0]][restorePosition[1]] = restorePosition[2];
		colorOfSquares[restorePosition[0]][restorePosition[1]] = restorePosition[3];// because it was captured, color is opposite
	}
	public int[] getRestorePosition(){
		return restorePosition;
	}
	
	/**
	 * This will print the numerical values of all squares on the board. (Shows locations of all pieces) 
	 * It attains the location of these values from the occupiedSquares 2D int array in this class
	 * * = empty space (this is indicated by 10 in occupuedSquares, but represented as * here,
	 * 5 = pawn,
	 * 2 = rook,
	 * 3 = knight,
	 * 4 = bishop,
	 * 1 = queen,
	 * 0 = king
	 */
	protected void printOccupied() {
	    for(int i=0; i<8; i++) {
	       for(int j=0; j<7; j++)
	    	   if(occupiedSquares[i][j]==10){
	    		   System.out.print("* ");
	    	   }else{
	    		   System.out.print(occupiedSquares[i][j] + " ");
	    	   }
	       System.out.println();
	    }
	    System.out.println();
	}
	
	
	protected int getColorOnClick(String clickedButtonString) {
		int iIndex = Integer.parseInt(clickedButtonString.substring(0, 1)); 
		int jIndex =  Integer.parseInt(clickedButtonString.substring(2)); 
		
		return colorOfSquares[iIndex][jIndex];
	}
	
	public String pieceLookup(int pieceNumber){
		String pieceString = null;
		String su = "Sun";
		String pl = "Plus";
		String tri = "Triangle";
		String che = "Chevron";
		String arr = "Arrow";
		String em = "Empty";
		//* SUN = 0 PLUS = 1 TRIANGLE = 2 CHEVRON = 3 ARROW = 4
		if(pieceNumber == 0){
			pieceString = su;
		}else if(pieceNumber == 1){
			pieceString = pl;
		}else if(pieceNumber == 2){ 
			pieceString = tri;
		}else if(pieceNumber == 3){ 
			pieceString = che;
		}else if(pieceNumber == 4){ 
			pieceString = arr;
		}else if(pieceNumber == 10){ 
			pieceString = em;
		}else{
			pieceString = "unknown";
		}
		return pieceString;
	}
	
	public String colorLookup(int pieceColorNumber){
		String pieceString = null;
		String bl = "Black";
		String wh = "White";
		String em = "Empty";
		if(pieceColorNumber == 0){
			pieceString = bl;
		}else if(pieceColorNumber == 1){
			pieceString = wh;
		}else if(pieceColorNumber == 2){ 
			pieceString = em;
		}else{
			pieceString = "unknown";
		}
		return pieceString;
	}
}
