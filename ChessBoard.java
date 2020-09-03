import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ChessBoard {
	private final JPanel gui = new JPanel(new BorderLayout(3, 3));
	private JButton[][] chessBoardSquares = new JButton[8][7];
	
	private Image[][] chessPieceImages = new Image[2][5];
	private JPanel chessBoard;
	private final JLabel message = new JLabel("Chess");
	private static final String COLS = "ABCDEFG";
	
	public static final int SUN = 0, PLUS = 1, TRIANGLE = 2, CHEVRON = 3, ARROW = 4;
	public static final int BLACK = 0, WHITE = 1;
	public static final int[] STARTING_ROW = { PLUS, TRIANGLE, CHEVRON, SUN, CHEVRON, TRIANGLE, PLUS };
	
	Controller control; 
	Model model; 
	
	boolean whiteTurn = true;
	int turnCounter = 0;
	int undoCounter = 0; 
	
	JLabel playerLabel;
	String playerString = "";
	
	ChessBoard(Controller ctrl, Model mdl){
		control = ctrl;
		model = mdl;
		initializeGui();
	}

	public final void initializeGui(){
		createImages();

		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
		JToolBar tools = new JToolBar();
		tools.setFloatable(false);
		gui.add(tools, BorderLayout.PAGE_START);
		
		Action newGameAction = new AbstractAction("New Game") {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				playerLabel.setForeground(Color.RED);
				playerLabel.setBorder(new CompoundBorder(new LineBorder(Color.RED,5), new BevelBorder(BevelBorder.RAISED)));
				playerLabel.setText(playerString);
				playerLabel.setFont(new Font("Serif", Font.PLAIN, 50));
				setupNewGame();
			}
		};
		tools.add(newGameAction);
		
		
		tools.addSeparator();
	
		Action surrenderGameAction = new AbstractAction("Surrender Game") {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String loser = surrenderGame();
				playerLabel.setFont(new Font("Serif", Font.PLAIN, 20));
				playerLabel.setText(loser+" Surrenders");
			}
		};
		tools.add(surrenderGameAction);
		
		tools.addSeparator();	
		
		Action undoMoveAction = new AbstractAction("Undo Last Move") {
			public void actionPerformed(ActionEvent e)
			{
				int previousSpot = model.getPreviousClicks()[4]; 
				int previousSpotCol = model.getPreviousClicks()[1];
				int previousSpotRow = model.getPreviousClicks()[0];
				int previousSpot2 = model.getPreviousClicks()[2];
						
				int previousSpotColor = model.getColorOfSquares()[previousSpotRow][previousSpotCol];
				boolean canUndo = model.undoLastMove();
				if(canUndo) {
					move();
					System.out.println(previousSpot+" and "+previousSpot2);
					if(previousSpot != 10){
						replacePiece();
					}
					System.out.println("Current location of pieces:");
					model.printOccupied();
					model.clearPreviousSavedClicks(); 
					
					
					if(control.playerTurn()){
						playerLabel.setForeground(Color.RED);
						playerLabel.setBorder(new CompoundBorder(new LineBorder(Color.RED,5), new BevelBorder(BevelBorder.RAISED)));
						
					}else{
						playerLabel.setForeground(Color.BLUE);
						playerLabel.setBorder(new CompoundBorder(new LineBorder(Color.BLUE,5), new BevelBorder(BevelBorder.RAISED)));
					}
					playerLabel.setText(control.whoseTurn());
					
				}
			}
		};
		tools.add(undoMoveAction);
		
		tools.addSeparator();
		Action exitGameAction = new AbstractAction("Exit Game") {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				exitGame();
			}
		};
		tools.add(exitGameAction);
		tools.addSeparator();	
		
	
		JPanel playerStats = new JPanel(new GridLayout(4,0)){
			public final Dimension getPreferredSize(){
				Dimension d = super.getPreferredSize();
				Dimension prefSize = null;
				Component c = getParent();
				if (c == null)
				{
					prefSize = new Dimension((int) d.getWidth(), (int) d.getHeight());
				} 
				
				else if (c != null && c.getWidth() > d.getWidth() && c.getHeight() > d.getHeight())
				{
					prefSize = c.getSize();
				} 
				
				else
				{
					prefSize = d;
				}
				int w = (int) prefSize.getWidth();
				int h = (int) prefSize.getHeight();
				return new Dimension(w, h);
			}
		}; 
		playerStats.setBorder(new CompoundBorder(new EmptyBorder(8, 8, 8, 8), new EmptyBorder(1,1,1,1)));
		playerStats.setBackground(Color.LIGHT_GRAY);
		
		playerString = control.whoseTurn(); 
		
		playerLabel = new JLabel(playerString);
		playerLabel.setOpaque(true);
		playerLabel.setBorder(new CompoundBorder(new LineBorder(Color.RED,5), new BevelBorder(BevelBorder.RAISED)));
		playerLabel.setForeground(Color.RED);
		playerLabel.setVerticalTextPosition(SwingConstants.CENTER);
		playerLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		playerLabel.setFont(new Font("Serif", Font.PLAIN, 50));
	   
		playerStats.add(playerLabel);
		
		gui.add(new JLabel("Play the Game!"), BorderLayout.SOUTH);

		chessBoard = new JPanel(new GridLayout(0, 8)) {

			@Override
			public final Dimension getPreferredSize(){
				Dimension d = super.getPreferredSize();
				Dimension prefSize = null;
				Component c = getParent();
				if (c == null)
				{
					prefSize = new Dimension((int) d.getWidth(), (int) d.getHeight());
				} 
				
				else if (c != null && c.getWidth() > d.getWidth() && c.getHeight() > d.getHeight())
				{
					prefSize = c.getSize();
				} 
				
				else
				{
					prefSize = d;
				}
				int w = (int) prefSize.getWidth();
				int h = (int) prefSize.getHeight();
				return new Dimension(w, h);
			}
		}; 
		
		chessBoard.setBorder(new CompoundBorder(new EmptyBorder(8, 8, 8, 8), new EmptyBorder(1,1,1,1))); 
	
		chessBoard.setBackground(Color.white);
		
		JPanel playerStatsPanel = new JPanel(new GridLayout(4,0)); 
		
		JPanel boardConstrain = new JPanel(new GridBagLayout()); 
		boardConstrain.setBackground(Color.LIGHT_GRAY);
		boardConstrain.add(playerStats);
		boardConstrain.add(chessBoard);
		
		gui.add(boardConstrain);  
		
		Insets buttonMargin = new Insets(0, 0, 0, 0);
		BufferedImage white = null;
		try{
			white = ImageIO.read(new File("White.png"));
		} catch (IOException e) {
			System.out.println("The file could not be found."); 
		}
			
		
		for (int ii = 0; ii < 8; ii++)
		{
			for (int jj = 0; jj < 7; jj++)
			{
				JButton b = new JButton();
				b.setMargin(buttonMargin);
				String locName = ii+" "+jj; 
				b.setText(locName); 
				b.setIconTextGap(0); 
				b.setFont(new Font("Serif", Font.PLAIN, 0));; 
				

				b.setBorderPainted(false); 
				b.setOpaque(true); 

				ImageIcon icon = new ImageIcon(white);
				b.setIcon(icon);
				b.setBackground(Color.WHITE); 
				
				chessBoardSquares[ii][jj] = b; 
				model.setOccupiedSquares(ii,jj,10);
				model.setColorOfSquares(ii,jj,2); 
			}
		}
		

		chessBoard.add(new JLabel(""));
		for (int ii = 0; ii < 7; ii++)
		{
			chessBoard.add(new JLabel(COLS.substring(ii, ii + 1), SwingConstants.CENTER));
		}
		
		int intLabel = 1;
		for (int ii = 0; ii < 8; ii++){
			for (int jj = 0; jj < 7; jj++){
				switch (jj){
					case 0:
						chessBoard.add(new JLabel("" + (intLabel), SwingConstants.CENTER));
						intLabel++;
					default:
						chessBoard.add(chessBoardSquares[ii][jj]);
				}
			}
		}
		
		
		
		ActionListener listener = new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            if (e.getSource() instanceof JButton) {
	                String text = ((JButton) e.getSource()).getText();
	                
	                model.setJustClicked(text);
	                
	                System.out.println("The square you just clicked contains: "+model.pieceLookup(model.getOccupiedOnClick(text)));
	                System.out.println("The value of item located in square you just clicked is: "+model.getOccupiedOnClick(text));
	                System.out.println("The color of the item located in square you just clicked: "+ model.colorLookup(model.getColorOnClick(text)));
	                
	                
	            }
	        }
	    };
		
	    JLabel checkLabel = new JLabel("Check");
		playerLabel.setFont(new Font("Serif", Font.PLAIN, 50));
	    
		for (int ii = 0; ii < 8; ii++){
			for (int jj = 0; jj < 7; jj++){
				chessBoardSquares[ii][jj].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						if(control.checkIfLegal()){ 
								System.out.println("Check: "+model.getCheck());
								move();
								model.setTurnCounter(model.getTurnCounter()+1);
								model.setPreviousClicks(model.getJustClicked());
								model.setUndoCounter(0);
						}
						

						if(control.playerTurn()){
							model.lastPlayed = 0;
							playerLabel.setForeground(Color.RED);
							playerLabel.setBorder(new CompoundBorder(new LineBorder(Color.RED,5), new BevelBorder(BevelBorder.RAISED)));
							
						}else{
							model.lastPlayed = 1; 
							playerLabel.setForeground(Color.BLUE);
							playerLabel.setBorder(new CompoundBorder(new LineBorder(Color.BLUE,5), new BevelBorder(BevelBorder.RAISED)));
						}
						playerLabel.setText(control.whoseTurn());
						
						System.out.println("Person who just moved: "+model.lastPlayed);
						System.out.println("Current location of all pieces:");
						model.printOccupied();
					}
				});
				chessBoardSquares[ii][jj].addActionListener(listener);	
				
			}
		}
		
	}
	
	
	private void move(){
		BufferedImage white = null;
		try{
			white = ImageIO.read(new File("White.png"));
		} catch (IOException e) {
			System.out.println("The background file could not be found."); 
		}
			chessBoardSquares[model.getJustClicked()[0]][model.getJustClicked()[1]].setIcon(chessBoardSquares[model.getLastClicked()[0]][model.getLastClicked()[1]].getIcon());
			
			
			model.setOccupiedSquares(model.getJustClicked()[0], model.getJustClicked()[1], model.getLastClicked()[2]); 
			model.setOccupiedSquares(model.getLastClicked()[0], model.getLastClicked()[1], 10);
			
			model.setColorOfSquares(model.getJustClicked()[0], model.getJustClicked()[1], model.getColorOfSquares()[model.getLastClicked()[0]][model.getLastClicked()[1]]);
			model.setColorOfSquares(model.getLastClicked()[0], model.getLastClicked()[1], 2); 
			
			chessBoardSquares[model.getLastClicked()[0]][model.getLastClicked()[1]].setIcon(new ImageIcon(white));
	}
	
	private void replacePiece(){
		if(model.capture){
			model.restoreArrayPositions();
			int[] restorePosition = model.getRestorePosition();
			int pieceColor = model.getColorOfSquares()[restorePosition[0]][restorePosition[1]];
			System.out.println("Replacing piece info: "+Arrays.toString(restorePosition));
			chessBoardSquares[restorePosition[0]][restorePosition[1]].setIcon((new ImageIcon(chessPieceImages[pieceColor][restorePosition[2]])));
			model.capture=false;
		}
		
	}
	
	
	public final JComponent getGui()
	{
		return gui;
	}

	private final void createImages(){
		try
		{
			String chessPic = "Chess.png";
			BufferedImage bi = null;
			try {
			    bi = ImageIO.read(new File(chessPic));
			} catch (IOException e) {
				System.out.println("The file: '"+chessPic+"' could not be found."); 
			}
			
			for (int ii = 0; ii < 2; ii++)
			{
				for (int jj = 0; jj < 5; jj++)
				{
					chessPieceImages[ii][jj] = bi.getSubimage(jj * 64, ii * 64, 64, 64);
				}
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private final void setupNewGame()
	{
		message.setText("Make your move!");
		clearBoard();
		
		for (int ii = 0; ii < STARTING_ROW.length; ii++){
			chessBoardSquares[0][ii].setIcon(new ImageIcon(chessPieceImages[BLACK][STARTING_ROW[ii]]));
			if(ii % 2 == 0)
				chessBoardSquares[1][ii].setIcon(new ImageIcon(chessPieceImages[BLACK][ARROW]));
		}

		for (int ii = 0; ii < STARTING_ROW.length; ii++){
			if(ii % 2 == 0)
				chessBoardSquares[6][ii].setIcon(new ImageIcon(chessPieceImages[WHITE][ARROW]));
		}
		
		for (int ii = 0; ii < STARTING_ROW.length; ii++){
			chessBoardSquares[7][ii].setIcon(new ImageIcon(chessPieceImages[WHITE][STARTING_ROW[ii]]));
		}
		
		//Document positions in 2D array
		for (int i = 0; i < STARTING_ROW.length; i++){
			model.setOccupiedSquares(0, i,STARTING_ROW[i]); 
			model.setOccupiedSquares(1, i, ARROW); 
			model.setColorOfSquares(0, i, 0); //setting the color to black for 1st row
			model.setColorOfSquares(1, i, 0); //setting the color to black for 2nd row
			
			model.setOccupiedSquares(2, i, 10);
			model.setOccupiedSquares(3, i, 10);
			model.setOccupiedSquares(4, i, 10);
			model.setOccupiedSquares(5, i, 10);
			
			model.setColorOfSquares(2, i, 2);
			model.setColorOfSquares(3, i, 2);
			model.setColorOfSquares(4, i, 2);
			model.setColorOfSquares(5, i, 2);

			model.setOccupiedSquares(6, i, ARROW);
			model.setOccupiedSquares(7, i, STARTING_ROW[i]);
			model.setColorOfSquares(6, i, 1);
			model.setColorOfSquares(7, i, 1);
		}
	}
	
	private String surrenderGame(){
		String whoSurrenders;
		if(control.playerTurn()){
			whoSurrenders = control.whoseTurn();
		}else{
			whoSurrenders = control.whoseTurn();
		}
		clearBoard();
		return whoSurrenders;
	}
	
	private void exitGame(){
		System.exit(0);
	}
	

	private void clearBoard(){
		BufferedImage white = null;
		try{
			white = ImageIO.read(new File("White.png"));
		} catch (IOException e) {
			System.out.println("The file could not be found."); 
		}
		
		for (int i = 0; i < 8; i++){
			for(int j = 0; j < 7; j++){
				
				model.setOccupiedSquares(i,j,10); 
				model.setColorOfSquares(i,j,2);
				
				
				ImageIcon icon = new ImageIcon(white);
				chessBoardSquares[i][j].setIcon(icon);
			}
		}
		
		model.setDefaults(); 
		
	}
	
}

