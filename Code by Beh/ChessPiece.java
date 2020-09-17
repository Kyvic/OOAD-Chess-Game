import java.util.*;

abstract class ChessPiece{
    private boolean killed;
    private boolean red;
    
    public ChessPiece(boolean red){
        this.red = red;
    }
    
    public boolean isRed(){
        return this.red == true;
    }
    
    public abstract boolean canMove(Spot start, Spot end);
    public abstract String pieceImage();
    public abstract String rotatedPieceImage();
    public abstract String pieceName();
    public abstract void rotated();
    public abstract boolean isRotated();   
}

class Chevron extends ChessPiece{
    public Chevron(boolean red){
        super(red);
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
        if(x == 2 && y == 1)
            return true;
        return false;
    }
    
    public String pieceImage(){
        if(this.isRed())
            return "chevronred.png" ;
        else
            return "chevronblue.png" ;
    }
	
    public String rotatedPieceImage(){
        if(this.isRed())
            return "rotatedchevronred.png";
        else
            return "rotatedchevronblue.png";
    }
}

class Sun extends ChessPiece{
    public Sun(boolean red){
        super(red);
    }
    public void rotated(){}
    public boolean isRotated(){return false;}
    public String pieceName(){
        if (this.isRed())
            return "Red Sun";
        else 
            return "Blue Sun";
    }
	
    public boolean canMove(Spot start, Spot end){
        if(end.getPiece() != null){
            if(end.getPiece().isRed() == start.getPiece().isRed()) 
                return false;
        }
        int x = Math.abs(start.getX() - end.getX()); // vertical
        int y = Math.abs(start.getY() - end.getY()); // horizon
        if((x == 1 && y == 0) || (y == 1 && x ==0)) //can only move one steps
            return true;
        return false;
    }
    
	public String pieceImage(){
        if(this.isRed())
            return "sunred.png" ;
        else
            return "sunblue.png" ;
    }
	
    public String rotatedPieceImage(){
        if(this.isRed())
            return "sunred.png";
        else
            return "sunblue.png";
    }
}

class Plus extends ChessPiece{
    public Plus(boolean red){
        super(red);
    }
    public void rotated(){}
    public boolean isRotated(){return false;}
    public String pieceName(){
        if(this.isRed())
            return "Red Plus";
        else 
            return "Blue Plus";
    }
	
    public boolean canMove(Spot start, Spot end){
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

class Triangle extends ChessPiece{
    public Triangle(boolean red){
        super(red);
    }
    public void rotated(){}
    public boolean isRotated(){return false;}
    public String pieceName(){
        if(this.isRed())
            return "Red Triangle";
        else 
            return "Blue Triangle";
    }
	
    public boolean canMove(Spot start, Spot end){
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

class Arrow extends ChessPiece{
    private boolean rotate;
    public Arrow(boolean red, boolean rotate){
        super(red);
        this.rotate = rotate;
    }
    
    public void rotated(){
        rotate = !rotate;
    }
	
    public boolean isRotated(){
        return this.rotate; 
    }
	
    public String pieceName(){
        if(this.isRed())
            return "Red Arrow";
        else 
            return "Blue Arrow";
    }
	
    public boolean canMove( Spot start, Spot end){
        int x;
        if(end.getPiece() != null){
            if(end.getPiece().isRed() == start.getPiece().isRed()) 
                return false;
        }
        if(!this.rotate)
            x = start.getX() - end.getX();
        else 
            x = end.getX() - start.getX();
        int y = Math.abs(start.getY() - end.getY());//horizontal
        if((x == 1 || x == 2) && y == 0)
            return true;
        return false;
    }
    
    public String pieceImage(){
        //if(!this.rotate)
            if(this.isRed())
                return "arrowred.png" ;
            else
                return "arrowblue.png" ;
        //else
           // return rotatedPieceImage();
    }
	
    public String rotatedPieceImage(){
        //if(this.rotate)
            if(this.isRed())
                return "rotatedarrowred.png";
            else
                return "rotatedarrowblue.png";
        //else
            //return pieceImage();
    }
}