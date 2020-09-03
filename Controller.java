import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Controller{
    static Controller controller = new Controller();
    static Model model = new Model();
    static int lastClicked[] = model.getLastClicked();
    static int justClicked[] = model.getJustClicked();
    static int[][] occupied = model.getOccupiedSquares();
    static int[][] pieceColor = model.getColorOfSquares();
    
    
    public boolean playerTurn(){
        boolean whiteTurn;
        if( (model.getTurnCounter() % 2) == 0){
            whiteTurn = true;

        }else{
            whiteTurn = false;
        }
        return whiteTurn;
    }
    
    public String whoseTurn(){
        String player = "";
        if (playerTurn()){
            player = "Player 1";
        }else{
            player = "Player 2";
        }
        return player;
    }
    

    public boolean checkIfLegal() {
        int move[] = {lastClicked[0], lastClicked[1], justClicked[0], justClicked[1]};
        boolean isLegal = false;
        
        boolean whitesTurn = playerTurn(); 
        
        
        if(pieceColor[move[0]][move[1]] == 1 && whitesTurn == false || pieceColor[move[0]][move[1]] == 0 && whitesTurn == true ){
            System.out.println("The color of the piece located in the square you Last (previously) Clicked is: "+model.colorLookup(pieceColor[move[0]][move[1]]));
            System.out.println("Move is NOT LEGAL because you clicked a "+model.colorLookup(pieceColor[move[0]][move[1]])+" piece.");
            return isLegal;
        } 
        
        int pieceRankNum = lastClicked[2]; 
        System.out.println("The value of the square you last clicked was: "+model.pieceLookup(pieceRankNum)+" ("+pieceRankNum+")");
        
        
        if(pieceRankNum == 10){
            return isLegal;
        }
        
        for (int i = 0; i < 4; i++) {
            if (!(0 <= move[i] || move[i] < 7)){
                System.out.printf("%d%s%n", move[i], " is not a space on the board");
                return isLegal;
            }
        }
            
         
        if(model.getJustClicked()[2] != 10){ 
            if(pieceColor[move[2]][move[3]] != model.lastPlayed){ 
                System.out.println("Attempting to capture your own piece. Not legal.");
                return isLegal;
            }else if(pieceColor[move[2]][move[3]] == model.lastPlayed){
                model.setCapture(); 
            }
         }

            switch (pieceRankNum) {
                case 4: 
                    if (occupied[move[2]][move[3]] == 10) {;
                        if (pieceColor[move[0]][move[1]] == 1) {
                            if ((move[1] == move[3]) && (move[0] == (move[2] + 1))) {
                                isLegal = true;
                            }
                            else if ((move[0] == 6) && (move[1] == move[3]) && (move[0] == (move[2] + 2))) {  
                                isLegal = true;
                            }
                        }
                        else if (pieceColor[move[0]][move[1]] == 0) {
                            if ((move[1] == move[3]) && (move[0] == (move[2] - 1))) {
                                isLegal = true;
                            }
                            else if ((move[0] == 1) && (move[1] == move[3]) && (move[0] == (move[2] - 2))) { 
                                isLegal = true;
                            }
                        }
                        else {
                            System.out.printf("%s%n", "That spot is occupied");
                            isLegal = false;
                        }
                    }
                    else if (pieceColor[move[0]][move[1]] == 1) {
                        if ((move[2] == move[0] - 1) && ((move[3] == move[1] - 1) || (move[3] == move[1] + 1))) {
                            isLegal = true;
                        }
                    }
                    else if (pieceColor[move[0]][move[1]] == 0) {
                        if ((move[2] == move[0] + 1) && ((move[3] == move[1] - 1) || (move[3] == move[1] + 1))) {
                            isLegal = true;
                        }
                    }
                    else {
                        System.out.printf("%d%s%d%s\n", move[2], "  ", move[3], " Illegal move");
                        isLegal = false;
                    }
                    break;
                
                case 3:
                    if (move[2] == (move[0] - 1)) { 
                        if ((move[3] == (move[1] - 2)) || (move[3] == (move[1] + 2))) {
                            isLegal = true;
                        }
                    }
                    else if (move[2] == (move[0] - 2)) {
                        if ((move[3] == (move[1] - 1)) || (move[3] == (move[1] + 1))) {
                            isLegal = true;
                        }
                    }
                    else if (move[2] == (move[0] + 1)) {
                        if ((move[3] == (move[1] - 2)) || (move[3] == (move[1] + 2))) {
                            isLegal = true;
                        }
                    }
                    else if (move[2] == (move[0] + 2)) {
                        if ((move[3] == (move[1] - 1)) || (move[3] == (move[1] + 1))) {
                            isLegal = true;
                        }
                    }
                    else {
                        System.out.printf("%d%s%d%s\n", move[2], "  ", move[3], " Illegal move");
                        isLegal = false;
                    }
                    break;
                
                case 2: 
                    if ((Math.abs(move[0] - move[2])) == (Math.abs(move[1] - move[3]))) { 
                        isLegal = true;
                        if (move[0] < move[2]) { 
                            if (move[3] < move[1]) { 
                                for (int i = 1; i < move[2] - move[0]; i++) {
                                    if (occupied[move[0]+i][move[1]-i] != 10) {
                                        isLegal = false;
                                    }
                                }   
                            }
                            else { 
                                for (int i = 1; i < move[2] - move[0]; i++) {
                                    if (occupied[move[0]+i][move[1]+i] != 10) {
                                        isLegal = false;
                                    }
                                }
                            }
                        }
                        else { 
                            if (move[3] < move[1]) {
                                for (int i = 1; i < move[0] - move[2]; i++) {
                                    if (occupied[move[0]-i][move[1]-i] != 10) {
                                        isLegal = false;
                                    }
                                }   
                            }
                            else {
                                for (int i = 1; i < move[0] - move[2]; i++) {
                                    if (occupied[move[0]-i][move[1]+i] != 10) {
                                        isLegal = false;
                                    }
                                }
                            }
                        }
                    }
                    else
                        isLegal = false;
                    break;

                case 1:
                    if ((move[0] == move[2] && move[1] != move[3]) || (move[0] != move[2] && move[1] == move[3])) {
                        isLegal = true;
                        if (move[0] < move[2]) {
                            for (int i = 1; i < move[2] - move[0]; i++) {
                                if (occupied[move[0]+i][move[1]] != 10) {
                                    isLegal = false;
                                }
                            }
                        }
                        else if (move[0] > move[2]) {
                            for (int i = 1; i < move[0] - move[2]; i++) {
                                if (occupied[move[2]+i][move[1]] != 10) {
                                    isLegal = false;
                                }
                            }
                        }
                        else {
                            for (int i = 0; i < move[0] - move[2]; i++) {
                                if (occupied[move[2]+i][move[3]] != 10) {
                                    isLegal = false;
                                }
                            } 
                        }
                    }
                    else
                        isLegal = false;
                    break;
                case 0:
                    if ((move[2] == (move[0] - 1)) || (move[2] == move[0] + 1)) { 
                        if ((move[3] == move[1] - 1) || (move[3] == move[1]) || (move[3] == move[1] + 1)) {
                            isLegal = true;
                        }
                    }
                    else if (move[2] == move[0]) {
                        if ((move[3] == move[1] - 1) || (move[3] == move[1] + 1)) {
                            isLegal = true;
                        }
                    }
                    
                    
                    else
                        isLegal = false;
                    break;
                default:
                    isLegal = false;
                    break;
            }
        System.out.println("Will move be legally allowed: "+isLegal);
        return isLegal;
    } 
    
    
    public boolean checkIfLegalForCheck(int rowFrom, int colFrom, int rowTo, int colTo, int positionRank, int[][] positions, int[][] colors) {
        int move[] = {rowFrom, colFrom, rowTo, colTo}; 
        int[][] occupied = positions;
        int[][] pieceColor = colors;
        boolean isLegal = false;
        
        boolean whitesTurn = playerTurn(); 
        
        int pieceRankNum = positionRank; 
        System.out.println("The value of the square you last clicked was: "+model.pieceLookup(pieceRankNum)+" ("+pieceRankNum+")");
        
        
        if(pieceRankNum == 10){
            return isLegal;
        }
        
        for (int i = 0; i < 4; i++) {
            if (!(0 <= move[i] || move[i] < 7)){
                System.out.printf("%d%s%n", move[i], " is not a space on the board");
                return isLegal;
            }
        }
           
            
            switch (pieceRankNum) {
                case 4: 
                    if (occupied[move[2]][move[3]] == 10) {
                        if (pieceColor[move[0]][move[1]] == 1) {
                            if ((move[1] == move[3]) && (move[0] == (move[2] + 1))) {
                                isLegal = true;
                            }
                            else if ((move[0] == 6) && (move[1] == move[3]) && (move[0] == (move[2] + 2))) { // legal to move up 2 spots on first move
                                isLegal = true;
                            }
                        }
                        else if (pieceColor[move[0]][move[1]] == 0) {
                            if ((move[1] == move[3]) && (move[0] == (move[2] - 1))) {
                                isLegal = true;
                            }
                            else if ((move[0] == 1) && (move[1] == move[3]) && (move[0] == (move[2] - 2))) { // legal to move up 2 spots on first move
                                isLegal = true;
                            }
                        }
                        else {
                            System.out.printf("%s%n", "That spot is occupied");
                            isLegal = false;
                        }
                    }
                    else if (pieceColor[move[0]][move[1]] == 1) {
                        if ((move[2] == move[0] - 1) && ((move[3] == move[1] - 1) || (move[3] == move[1] + 1))) {
                            isLegal = true;
                        }
                    }
                    else if (pieceColor[move[0]][move[1]] == 0) {
                        if ((move[2] == move[0] + 1) && ((move[3] == move[1] - 1) || (move[3] == move[1] + 1))) {
                            isLegal = true;
                        }
                    }
                    else {
                        System.out.printf("%d%s%d%s\n", move[2], "  ", move[3], " Illegal move");
                        isLegal = false;
                    }
                    break;
                
                case 3:
                    if (move[2] == (move[0] - 1)) {
                        if ((move[3] == (move[1] - 2)) || (move[3] == (move[1] + 2))) { 
                            isLegal = true;
                        }
                    }
                    else if (move[2] == (move[0] - 2)) {
                        if ((move[3] == (move[1] - 1)) || (move[3] == (move[1] + 1))) {
                            isLegal = true;
                        }
                    }
                    else if (move[2] == (move[0] + 1)) {
                        if ((move[3] == (move[1] - 2)) || (move[3] == (move[1] + 2))) { 
                            isLegal = true;
                        }
                    }
                    else if (move[2] == (move[0] + 2)) { 
                        if ((move[3] == (move[1] - 1)) || (move[3] == (move[1] + 1))) { 
                            isLegal = true;
                        }
                    }
                    else {
                        System.out.printf("%d%s%d%s\n", move[2], "  ", move[3], " Illegal move");
                        isLegal = false;
                    }
                    break;

                case 2: 
                    if ((Math.abs(move[0] - move[2])) == (Math.abs(move[1] - move[3]))) { 
                        isLegal = true;
                        if (move[0] < move[2]) { 
                            if (move[3] < move[1]) {  
                                for (int i = 1; i < move[2] - move[0]; i++) { 
                                    if (occupied[move[0]+i][move[1]-i] != 10) {
                                        isLegal = false;
                                    }
                                }   
                            }
                            else { 
                                for (int i = 1; i < move[2] - move[0]; i++) {
                                    if (occupied[move[0]+i][move[1]+i] != 10) {
                                        isLegal = false;
                                    }
                                }
                            }
                        }
                        else { 
                            if (move[3] < move[1]) { 
                                for (int i = 1; i < move[0] - move[2]; i++) {
                                    if (occupied[move[0]-i][move[1]-i] != 10) {
                                        isLegal = false;
                                    }
                                }   
                            }
                            else { 
                                for (int i = 1; i < move[0] - move[2]; i++) {
                                    if (occupied[move[0]-i][move[1]+i] != 10) {
                                        isLegal = false;
                                    }
                                }
                            }
                        }
                    }
                    else
                        isLegal = false;
                    break;
                    
                case 1:
                    if ((move[0] == move[2] && move[1] != move[3]) || (move[0] != move[2] && move[1] == move[3])) {
                        isLegal = true;
                        if (move[0] < move[2]) {
                            for (int i = 1; i < move[2] - move[0]; i++) {
                                if (occupied[move[0]+i][move[1]] != 10) {
                                    isLegal = false;
                                }
                            }
                        }
                        else if (move[0] > move[2]) {
                            for (int i = 1; i < move[0] - move[2]; i++) {
                                if (occupied[move[2]+i][move[1]] != 10) {
                                    isLegal = false;
                                }
                            }
                        }
                        else {
                            for (int i = 0; i < move[0] - move[2]; i++) {
                                if (occupied[move[2]+i][move[3]] != 10) {
                                    isLegal = false;
                                }
                            } 
                        }
                    }
                    else
                        isLegal = false;
                    break;
                
                case 0:
                    if ((move[2] == (move[0] - 1)) || (move[2] == move[0] + 1)) { 
                        if ((move[3] == move[1] - 1) || (move[3] == move[1]) || (move[3] == move[1] + 1)) {
                            isLegal = true;
                        }
                    }
                    else if (move[2] == move[0]) {
                        if ((move[3] == move[1] - 1) || (move[3] == move[1] + 1)) {
                            isLegal = true;
                        }
                    }
                    
                    
                    else
                        isLegal = false;
                    break;
                default:
                    isLegal = false;
                    break;
            }
        System.out.println("Will move be legally allowed: "+isLegal);
        return isLegal;
    }
    
        
        public static void main(String[] args)
        {
            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    ChessBoard chessGame = new ChessBoard(controller, model);
                    

                    JFrame f = new JFrame("Webale Chess Game");
                    f.add(chessGame.getGui());
                    
                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    f.setLocationByPlatform(true);

					f.pack();
					f.setMinimumSize(f.getSize());
					f.setVisible(true);
				}
			};
			SwingUtilities.invokeLater(r);
		}

}
