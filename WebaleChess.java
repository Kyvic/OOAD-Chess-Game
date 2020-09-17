import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.util.*;

public class WebaleChess extends JFrame
{
    private Container contents;
    private JButton[][] squares = new JButton[8][7];
    private Color color = Color.WHITE;
    int[] previousClicked = {0,0};
    int movecount = 0;
    int rotatecount = 0;
    boolean gameend = false;
    boolean firstmove = true;
    boolean secondmove = false;
    boolean redtransformflag = true;
    boolean bluetransformflag = true;
    ChessBoard board = new ChessBoard();
    
    public WebaleChess()
    {
    super("Webale Chess");
    JPanel topBoard = new JPanel(new BorderLayout());  
    JPanel contents = new JPanel(new GridLayout(8,7));
    JButton saveBtn = new JButton("Save");
    saveBtn.addActionListener(new ActionListener()
    {
            public void actionPerformed(ActionEvent e)
            {
                JFrame f = new JFrame();   
                String filename=JOptionPane.showInputDialog(f,"Enter Name"); 
                save(filename,board,movecount);
            }
    });
    
    JButton restartBtn = new JButton("Restart");
    restartBtn.addActionListener(new ActionListener()
    {
            public void actionPerformed(ActionEvent e)
            {
                restart(board);
                movecount = 0;
                rotatecount = 0;
                redtransformflag = true;
                bluetransformflag = true;
                gameend = false;
            }
    });    
    JButton loadBtn = new JButton("Load");
    loadBtn.addActionListener(new ActionListener()
    {
            public void actionPerformed(ActionEvent e)
            {
                JFrame f = new JFrame();   
                String filename = JOptionPane.showInputDialog(f,"Enter Name"); 
                movecount = load(filename,board,movecount);
                rotatecount = movecount;
                gameend = false;
            }
    });
    topBoard.add(saveBtn, BorderLayout.WEST);
    topBoard.add(restartBtn, BorderLayout.CENTER);
    topBoard.add(loadBtn, BorderLayout.EAST);
    add(topBoard, BorderLayout.NORTH);
    add(contents, BorderLayout.CENTER);
    ButtonHandler buttonHandler = new ButtonHandler();
    
    for(int i = 0 ; i < 8 ; i++)
    {
        for(int j = 0 ; j < 7 ; j++)
        {
            squares[i][j] = new JButton();
            squares[i][j].setBackground(color);
            contents.add(squares[i][j]);
            squares[i][j].addActionListener(buttonHandler);
            if(board.getBox(i,j).getPiece() != null)
            {
                squares[i][j].setIcon(new ImageIcon(board.getBox(i,j).getPiece().pieceImage()));
            }
            else 
                squares[i][j].setIcon(null);
        }
    }
    setSize(800, 800);
    setResizable(false);
    setLocationRelativeTo(null); 
    setVisible(true);    
    }
    
    private class ButtonHandler implements ActionListener
    {
     
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
            
        for(int i = 0 ; i < 8 ; i++)
        {
            for(int j = 0 ; j < 7 ; j++)
            {
                if(source == squares[i][j])
                {
                        if(!gameend && firstmove && board.getBox(i,j).getPiece()!= null)
                        {
                            
                            previousClicked[0] = i;
                            previousClicked[1] = j;
                            firstmove = false;
                            secondmove = true;
                        }
                        else
                        {

                            if(!gameend && board.getBox(previousClicked[0],previousClicked[1]).getPiece() != null)
                            {   
                                processClick(i, j, previousClicked[0],previousClicked[1]);
                                previousClicked[0] = i;
                                previousClicked[1] = j;
                                firstmove = true;
                                secondmove = false;

                            }
                            return;
                        }
                    
                }
                
            }
        }
    }
    }
    
    private boolean processClick(int i , int j, int ii, int jj)
    {
       //int tempi = Math.abs(ii-i);
       //int tempj = Math.abs(jj-j);
       if(movecount % 2 != 0 && board.getBox(ii,jj).getPiece().isRed())
            return false;
            
       if(movecount % 2 == 0 && !(board.getBox(ii,jj).getPiece().isRed()))
            return false;
       
       if(i < ii && j == jj) //move up
       {
           for(int testi = i+1 ; testi < ii ; testi++)
           {
               if(board.getBox(testi,j).getPiece() != null)
                    return false;
           }
       }
       else if(i > ii && j == jj) //move down
       {
           for(int testi = ii+1 ; testi < i ; testi++)
           {
               if(board.getBox(testi,j).getPiece() != null)
                    return false;
           }
       }
       else if(j < jj && i == ii) // move left
       {
           for(int testj = j+1 ; testj < jj ; testj++)
           {
               if(board.getBox(i,testj).getPiece() != null)
                    return false;
           }
       }
       else if(j > jj && i == ii) // move right
       {
           for(int testj = jj+1 ; testj < j ; testj++)
           {
               if(board.getBox(i,testj).getPiece() != null)
                    return false;
           }
       }
       
       else if(i > ii && j > jj && (Math.abs(ii - i) == Math.abs(jj - j)))//move downright
       {
           int tempjj = jj;
           for(int testi = ii + 1; testi < i ; testi ++)
           {
               for(int testj = tempjj + 1 ;; )
               {
                   if(board.getBox(testi,testj).getPiece() != null)
                        return false;
                   tempjj++;
                   break;
                   
               }
           }
       }
       else if(i > ii && j < jj && (Math.abs(ii - i) == Math.abs(jj - j)))//move downleft
       {
           int tempjj = jj;
           for(int testi = ii + 1; testi < i ; testi ++)
           {
               for(int testj = tempjj - 1 ;; )
               {
                   if(board.getBox(testi,testj).getPiece() != null)
                        return false;
                   tempjj --;
                   break;
               }
           }
       }
       else if(i < ii && j > jj && (Math.abs(ii - i) == Math.abs(jj - j))) // moveupright triangle
       {
           int tempj = j;
           for(int testi = i + 1; testi < ii ; testi ++)
           {
               for(int testj = tempj - 1 ;;)
               {
                   if(board.getBox(testi,testj).getPiece() != null)
                        return false;
                   tempj-- ;
                   break;
               }
           }
       }
       else if(i < ii && j < jj && (Math.abs(ii - i) == Math.abs(jj - j))) //moveupleft triangle
       {
           int tempj = j;
           for(int testi = i + 1; testi < ii ; testi ++)
           {
               for(int testj = tempj + 1 ;; )
               {
                   if(board.getBox(testi,testj).getPiece() != null)
                        return false;
                   tempj++;
                   break;
               }
           }
       }
       
       if(board.getBox(ii,jj).getPiece().canMove(board.getBox(ii,jj),board.getBox(i,j))== false)
            return false;
       squares[ii][jj].setIcon(null);
       squares[i][j].setIcon(new ImageIcon(board.getBox(ii,jj).getPiece().pieceImage()));
       /*
       if((i == 7 || i == 0) && board.getBox(i,j).getPiece() != null &&(board.getBox(ii,jj).getPiece().pieceName().equals("Red Arrow") ||board.getBox(ii,jj).getPiece().pieceName().equals("Blue Arrow") ))
            squares[i][j].setIcon(new ImageIcon(board.getBox(ii,jj).getPiece().rotatedPieceImage()));
       else 
            squares[i][j].setIcon(new ImageIcon(board.getBox(ii,jj).getPiece().pieceImage()));*/
       
	   
	  
       
       if(board.getBox(i,j).getPiece() != null && board.getBox(i,j).getPiece().pieceName().equals("Blue Sun"))
       {
           gameend = true;
           JFrame f = new JFrame();
           JOptionPane.showMessageDialog(f,"Player 1(Red) Wins !"); 
           return true;
       }
       
       if(board.getBox(i,j).getPiece() != null && board.getBox(i,j).getPiece().pieceName().equals("Red Sun"))
       {
           gameend = true;
           JFrame f = new JFrame();
           JOptionPane.showMessageDialog(f,"Player 2(Blue) Wins !"); 
           return true;
       }
	   
	   board.getBox(i,j).setPiece(board.getBox(ii,jj).getPiece());
       board.getBox(ii,jj).setPiece(null);
       
       if((i==7 || i ==0) && (board.getBox(i,j).getPiece().pieceName().equals("Red Arrow") ||board.getBox(i,j).getPiece().pieceName().equals("Blue Arrow")))
       {
           board.getBox(i,j).getPiece().rotated(); 
           //squares[i][j].setIcon(new ImageIcon(board.getBox(i,j).getPiece().pieceImage()));
       }
       //movecount ++;
       
       if(movecount > 0 && movecount % 2 == 0)
       {
           if(redtransformflag)
           {
               redtransform(board);
               redtransformflag = false;
           }
           else 
                redtransformflag = true;
        }
       else if(movecount > 1 && movecount % 2 == 1)
       {
           if(bluetransformflag)
           {
               bluetransform(board);
               bluetransformflag = false;
           }
           else 
                bluetransformflag = true;
       }
       movecount ++;
       rotateBoard(squares,board,rotatecount++);

       return true;


    } 
    
    public void redtransform(ChessBoard board)
    {
        for(int i = 0 ; i < 8 ; i++)
        {
            for(int j = 0 ; j < 7 ; j++)
            {
                if(board.getBox(i,j).getPiece()!= null && board.getBox(i,j).getPiece().isRed() && board.getBox(i,j).getPiece().pieceName().equals("Red Plus"))
                    board.getBox(i,j).setPiece(new Triangle(true)); 
                else if(board.getBox(i,j).getPiece()!= null &&board.getBox(i,j).getPiece().isRed() &&  board.getBox(i,j).getPiece().pieceName().equals("Red Triangle"))
                    board.getBox(i,j).setPiece(new Plus(true));
                    
            }
        }
    }
    
    public void bluetransform(ChessBoard board)
    {
        for(int i = 0 ; i < 8 ; i++)
        {
            for(int j = 0 ; j < 7 ; j++)
            {
                if(board.getBox(i,j).getPiece()!= null && !(board.getBox(i,j).getPiece().isRed()) && board.getBox(i,j).getPiece().pieceName().equals("Blue Plus"))
                    board.getBox(i,j).setPiece(new Triangle(false)); 
                else if(board.getBox(i,j).getPiece()!= null && !(board.getBox(i,j).getPiece().isRed()) &&  board.getBox(i,j).getPiece().pieceName().equals("Blue Triangle"))
                    board.getBox(i,j).setPiece(new Plus(false));
                    
            }
        }
    }
    public void restart(ChessBoard board)
    {
        board.resetBoard();
        for(int i = 0 ; i < 8 ; i++)
        {
            for(int j = 0 ; j < 7 ; j ++)
            {
                if(board.getBox(i,j).getPiece() != null)
                    squares[i][j].setIcon(new ImageIcon(board.getBox(i,j).getPiece().pieceImage()));
                else
                    squares[i][j].setIcon(null);
            }
        }
    }
    public void save(String filename, ChessBoard board, int movecount)
    {
        String content ="";
        for(int i = 0 ; i < 8 ; i++)
        {
            for(int j = 0 ; j < 7 ; j++)
                {
                    if(board.getBox(i,j).getPiece() != null && !(board.getBox(i,j).getPiece() instanceof Arrow))
                        content += i + " " + j + " " + board.getBox(i,j).getPiece().pieceName() + "\n";
                        
                    else if(board.getBox(i,j).getPiece() instanceof Arrow && board.getBox(i,j).getPiece().isRotated())
                        content += i + " " + j + " " + "Rotated "+ board.getBox(i,j).getPiece().pieceName() + "\n"; 
                    
                    else if(board.getBox(i,j).getPiece() instanceof Arrow)
                        content += i + " " + j + " " + board.getBox(i,j).getPiece().pieceName() + "\n";
                    
                }
             
        }
        content += movecount;
        try{
        FileWriter fw = new FileWriter(filename + ".txt");
        fw.write(content);
        fw.close();
        }
        catch (Exception e){}
   
    }
    
    public int load(String filename, ChessBoard board, int movecount)
    {
        for(int i = 0 ; i <  8; i ++)
        {
            for(int j = 0 ; j < 7 ; j ++)
            {
                squares[i][j].setIcon(null);
                board.getBox(i,j).setPiece(null);
            }
        }
        try
        {
            Scanner fr = new Scanner(new File (filename + ".txt"));
            while (fr.hasNextLine()) 
            {
            String data = fr.nextLine();
           if(fr.hasNextLine())
            {
                if(data.substring(4).equals("Red Chevron"))
                    board.getBox(Integer.parseInt(data.substring(0,1)),Integer.parseInt(data.substring(2,3))).setPiece(new Chevron(true));
                else if(data.substring(4).equals("Blue Chevron"))
                    board.getBox(Integer.parseInt(data.substring(0,1)),Integer.parseInt(data.substring(2,3))).setPiece(new Chevron(false));
                else if(data.substring(4).equals("Red Sun"))
                    board.getBox(Integer.parseInt(data.substring(0,1)),Integer.parseInt(data.substring(2,3))).setPiece(new Sun(true));
                else if(data.substring(4).equals("Blue Sun"))
                    board.getBox(Integer.parseInt(data.substring(0,1)),Integer.parseInt(data.substring(2,3))).setPiece(new Sun(false));
                else if(data.substring(4).equals("Red Triangle"))
                    board.getBox(Integer.parseInt(data.substring(0,1)),Integer.parseInt(data.substring(2,3))).setPiece(new Triangle(true));                    
                else if(data.substring(4).equals("Blue Triangle"))
                    board.getBox(Integer.parseInt(data.substring(0,1)),Integer.parseInt(data.substring(2,3))).setPiece(new Triangle(false));    
                else if(data.substring(4).equals("Red Plus"))
                    board.getBox(Integer.parseInt(data.substring(0,1)),Integer.parseInt(data.substring(2,3))).setPiece(new Plus(true));
                else if(data.substring(4).equals("Blue Plus"))
                    board.getBox(Integer.parseInt(data.substring(0,1)),Integer.parseInt(data.substring(2,3))).setPiece(new Plus(false));                
                else if(data.substring(4).equals("Red Arrow"))
                    board.getBox(Integer.parseInt(data.substring(0,1)),Integer.parseInt(data.substring(2,3))).setPiece(new Arrow(true,false));                
                else if(data.substring(4).equals("Blue Arrow"))
                    board.getBox(Integer.parseInt(data.substring(0,1)),Integer.parseInt(data.substring(2,3))).setPiece(new Arrow(false,false)); 
                else if(data.substring(4).equals("Rotated Red Arrow"))
                    board.getBox(Integer.parseInt(data.substring(0,1)),Integer.parseInt(data.substring(2,3))).setPiece(new Arrow(true,true));                
                else if(data.substring(4).equals("Rotated Blue Arrow"))
                    board.getBox(Integer.parseInt(data.substring(0,1)),Integer.parseInt(data.substring(2,3))).setPiece(new Arrow(false,true));                      
                }
            else 
                movecount = Integer.parseInt(data.substring(0,1));
        
            }
            fr.close();
        }
        catch(FileNotFoundException e){}
        for(int i = 0 ; i < 8 ; i++)
        {
            for(int j = 0 ; j < 7 ; j++)
            {
                if(board.getBox(i,j).getPiece() != null)
                {
                    if(movecount % 2 == 0)
                            if(board.getBox(i,j).getPiece() instanceof Arrow && (board.getBox(i,j).getPiece().isRotated()))
                                squares[i][j].setIcon(new ImageIcon(board.getBox(i,j).getPiece().rotatedPieceImage()));
                            else
                                squares[i][j].setIcon(new ImageIcon(board.getBox(i,j).getPiece().pieceImage()));
                        //squares[i][j].setIcon(new ImageIcon(board.getBox(i,j).getPiece().pieceImage()));
                    else
                            if(board.getBox(i,j).getPiece() instanceof Arrow && (board.getBox(i,j).getPiece().isRotated()))
                                squares[i][j].setIcon(new ImageIcon(board.getBox(i,j).getPiece().pieceImage()));
                            else
                                squares[i][j].setIcon(new ImageIcon(board.getBox(i,j).getPiece().rotatedPieceImage()));
                        //squares[i][j].setIcon(new ImageIcon(board.getBox(i,j).getPiece().rotatedPieceImage()));
                }
    
            }
        }
     
        return movecount;
    }
    public void rotateBoard(JButton[][] squares, ChessBoard board,int rotatecount)
    {
         int ii = 0;
         int jj = 0;
         
         ChessBoard temp = new ChessBoard();
         
         for(int i = 0 ; i < 8 ; i++)
         {
             for(int j = 0 ; j < 7 ; j++) 
             {
                 temp.getBox(i,j).setPiece(board.getBox(i,j).getPiece());
             }
         }
      
         for(int i = 7 ; i > -1 ; i--)
         {
             for(int j = 6 ; j > -1 ; j--)
             {
                 if(temp.getBox(i,j).getPiece() != null)
                 {
                        if(rotatecount % 2 == 0 )
                                
                            if(temp.getBox(i,j).getPiece() instanceof Arrow && (temp.getBox(i,j).getPiece().isRotated()))
                                squares[ii][jj].setIcon(new ImageIcon(temp.getBox(i,j).getPiece().pieceImage()));
                            else
                                squares[ii][jj].setIcon(new ImageIcon(temp.getBox(i,j).getPiece().rotatedPieceImage()));

                        else
                            if(temp.getBox(i,j).getPiece() instanceof Arrow && (temp.getBox(i,j).getPiece().isRotated()))
                                squares[ii][jj].setIcon(new ImageIcon(temp.getBox(i,j).getPiece().rotatedPieceImage()));
                            else
                                squares[ii][jj].setIcon(new ImageIcon(temp.getBox(i,j).getPiece().pieceImage()));
                        board.getBox(ii,jj).setPiece(temp.getBox(i,j).getPiece());
                 }
                 else 
                   {
                       squares[ii][jj].setIcon(null);
                       board.getBox(ii,jj).setPiece(null);
                    }
 
                 jj++;
             }
             ii++;
             jj = 0; 
         }
    }
    public static void main(String args [])
    {
        WebaleChess gui = new WebaleChess (); 
    }
}