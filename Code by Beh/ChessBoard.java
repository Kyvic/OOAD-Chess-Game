import java.util.*;

public class ChessBoard{
    private Spot[][] boxes = new Spot[8][7];
    public ChessBoard(){
        this.resetBoard() ;
    }
    
    public void resetBoard(){
        for(int i = 0; i < 8; i ++){
            for(int j = 0 ; j < 7 ; j++){
                boxes[i][j] = new Spot(i,j,null);
            }
        }
        boxes[0][0].setPiece(new Plus(false));
        boxes[0][1].setPiece(new Triangle(false));
        boxes[0][2].setPiece(new Chevron(false));
        boxes[0][3].setPiece(new Sun(false));
        boxes[0][4].setPiece(new Chevron(false));
        boxes[0][5].setPiece(new Triangle(false));
        boxes[0][6].setPiece(new Plus(false));
        boxes[1][0].setPiece(new Arrow(false,false));
        boxes[1][2].setPiece(new Arrow(false,false));
        boxes[1][4].setPiece(new Arrow(false,false));
        boxes[1][6].setPiece(new Arrow(false,false));
        boxes[6][0].setPiece(new Arrow(true,false));
        boxes[6][2].setPiece(new Arrow(true,false));
        boxes[6][4].setPiece(new Arrow(true,false));
        boxes[6][6].setPiece(new Arrow(true,false));
        boxes[7][0].setPiece(new Plus(true));
        boxes[7][1].setPiece(new Triangle(true));
        boxes[7][2].setPiece(new Chevron(true));
        boxes[7][3].setPiece(new Sun(true));
        boxes[7][4].setPiece(new Chevron(true));
        boxes[7][5].setPiece(new Triangle(true));
        boxes[7][6].setPiece(new Plus(true));
    }
    
    public Spot getBox(int i, int j){
        return boxes[i][j];
    }    
}