public class Plus implements ChessPiece{
    private boolean red;
    
    public Plus(boolean red){
        this.red = red;
    }
    
    public boolean isRed(){
        return this.red == true;
    }
	
    public void rotated(){}
    public boolean isRotated(){return false;}
    public String pieceName(){
        if(this.isRed())
            return "Red Plus";
        else 
            return "Blue Plus";
    }
	
    public boolean canMove( Spot start, Spot end){
        if(end.getPiece() != null){
            if(end.getPiece().isRed() == start.getPiece().isRed()) 
                return false;
        }
        int x = Math.abs(start.getX() - end.getX()); // vertical
        int y = Math.abs(start.getY() - end.getY()); // horizon
        if((x >= 1 && y == 0) || (y >= 1 && x ==0))
            return true;
        return false;
    }
    
    public String pieceImage(){
        if(this.isRed())
            return "plusred.png" ;
        else
            return "plusblue.png" ;
    }
	
    public String rotatedPieceImage(){
        if(this.isRed())
            return "plusred.png";
        else
            return "plusblue.png";
    }
}