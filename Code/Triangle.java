public class Triangle implements ChessPiece{
    private boolean red;
    
    public Triangle(boolean red){
        this.red = red;
    }
    
    public boolean isRed(){
        return this.red == true;
    }
	
    public void rotated(){}
    public boolean isRotated(){return false;}
	
    public String pieceName(){
        if(this.isRed())
            return "Red Triangle";
        else 
            return "Blue Triangle";
    }
	
    public boolean canMove( Spot start, Spot end){
        if(end.getPiece() != null){
            if(end.getPiece().isRed() == start.getPiece().isRed()) 
                return false;
        }
        int x = Math.abs(start.getX() - end.getX());//vertical
        int y = Math.abs(start.getY() - end.getY());//horizontal
        if(x >= 1 && x == y)
            return true;
        return false;
    }
    
    public String pieceImage(){
        if(this.isRed())
            return "trianglered.png" ;
        else
            return "triangleblue.png" ;
    }
	
    public String rotatedPieceImage(){
        if(this.isRed())
            return "rotatedtrianglered.png";
        else
            return "rotatedtriangleblue.png";
    }
}