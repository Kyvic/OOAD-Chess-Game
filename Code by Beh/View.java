import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;
import java.io.*;
import java.util.*;

public class View extends JFrame{
	private Model model = new Model();
	
    public View(){
		super("Webale Chess");
		JPanel topBoard = new JPanel(new BorderLayout());  
		
		model.init();
		
		JButton saveBtn = new JButton("Save");
		JButton restartBtn = new JButton("Restart");
		JButton loadBtn = new JButton("Load");
		saveBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JFrame f = new JFrame();   
                String filename=JOptionPane.showInputDialog(f,"Enter Name"); 
                model.save(filename,model.getChessBoard(),model.getMovecount());
            }
		});
		
		restartBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                model.restart(model.getChessBoard());
				model.setMovecount(0);
                model.setRotatecount(0);
                model.setRedTransformFlag(true);
                model.setBlueTransformFlag(true);
                model.setGameEnd(false);
            }
		});  
		
		loadBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JFrame f = new JFrame();   
                String filename = JOptionPane.showInputDialog(f,"Enter Name"); 
				model.load(filename,model.getChessBoard(),model.getMovecount());
                model.setMovecount(model.getMovecount());
                model.setRotatecount(model.getMovecount());
                model.setGameEnd(false);
            }
		});
		
		topBoard.add(saveBtn, BorderLayout.WEST);
		topBoard.add(restartBtn, BorderLayout.CENTER);
		topBoard.add(loadBtn, BorderLayout.EAST);
		this.add(topBoard, BorderLayout.NORTH);
		this.add(model.getContent(), BorderLayout.CENTER);
		setSize(800, 800);
		setResizable(false);
		setLocationRelativeTo(null);    
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}