public class Arrow implements ChessPiece
{
	private boolean rotate;
    private boolean red;
    
    public Arrow(boolean red, boolean rotate)
    {
        this.red = red;
		this.rotate = rotate;
    }
    
    public boolean isRed()
    {
        return this.red == true;
    }
    
    public void rotated()
    {
        rotate = !rotate;
    }
    public boolean isRotated()
    {
        return this.rotate; 
    }
    public String pieceName()
    {
        if(this.isRed())
            return "Red Arrow";
        else 
            return "Blue Arrow";
    }
    public boolean canMove( Spot start, Spot end)
    {
        int x;
        if(end.getPiece() != null)
        {
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
    
    public String pieceImage()
    {
        //if(!this.rotate)
            if(this.isRed())
                return "arrowred.png" ;
            else
                return "arrowblue.png" ;
        //else
           // return rotatedPieceImage();
    }
    public String rotatedPieceImage()
    {
        //if(this.rotate)
            if(this.isRed())
                return "rotatedarrowred.png";
            else
                return "rotatedarrowblue.png";
        //else
            //return pieceImage();
    }
}