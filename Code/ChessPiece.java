public interface ChessPiece{
    public abstract boolean isRed();
    public abstract boolean canMove(Spot start, Spot end);
    public abstract String pieceImage();
    public abstract String rotatedPieceImage();
    public abstract String pieceName();
    public abstract void rotated();
    public abstract boolean isRotated();
}