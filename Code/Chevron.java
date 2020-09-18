public class Chevron implements ChessPiece{
    private boolean red;
    
    public Chevron(boolean red){
        this.red = red;
    }
    
    public boolean isRed(){
        return this.red == true;
    }

    public void rotated(){}
    public boolean isRotated(){return false;}
	
    public String pieceName(){
        if(this.isRed())
            return "Red Chevron";
        else 
            return "Blue Chevron";
    }
	
    public boolean canMove(Spot start, Spot end){
        if(end.getPiece() != null){
            if(end.getPiece().isRed() == start.getPiece().isRed()) 
                return false;
        }
        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        if((x == 2 && y == 1) || (y == 2 && x == 1)) // chevron bug
            return true;
        return false;
    }
    
    public String pieceImage(){
        if(this.isRed())
            return "Pieces/chevronred.png" ;
        else
            return "Pieces/chevronblue.png" ;
    }
	
    public String rotatedPieceImage(){
        if(this.isRed())
            return "Pieces/rotatedchevronred.png";
        else
            return "Pieces/rotatedchevronblue.png";
    }
}

