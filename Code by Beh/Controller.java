import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;
import java.io.*;
import java.util.*;

public class Controller{
	private View theView;
    private Model theModel; 
	
	public Controller(View theView, Model theModel){
		this.theView = theView;
		this.theModel = theModel;
	}
}