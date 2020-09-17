public class Spot
{
    private ChessPiece piece;
    private int x;
    private int y;
    
    public Spot(int x, int y, ChessPiece piece)
    {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }
    
    public ChessPiece getPiece()
    {
        return this.piece;
    }
    
    public void setPiece(ChessPiece piece)
    {
        this.piece = piece;
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public int getX()
    {
        return this.x;
    }
    
    public void setY(int y)
    {
        this.y = y;
    }
    
    public int getY()
    {
        return this.y;
    }
}