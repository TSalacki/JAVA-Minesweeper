import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import java.awt.event.ActionListener;
import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.WindowEvent;

/**
 * View for minesweeper. It's function is to display game to user.
 * @author Tomek
 *
 */
public class View extends JFrame implements Observer{
	private static final long serialVersionUID = 1;
	private Model model;
	
	String[] options = {"Beginner", "Intermediate", "Expert", "Custom"};
	private JComboBox<String> menu = new JComboBox<String>(options);
	private JButton reset = new JButton("Reset");
	private JLabel bombsLeft = new JLabel("Bombs left:");
	private JLabel time = new JLabel("Time:");
	private JTextField bombsCounter = new JTextField(3);
	private JTextField timer = new JTextField(5);
	JLabel width = new JLabel("Width");
	JLabel height = new JLabel("Height") ;
	JLabel bombs = new JLabel("Bombs");
	JButton okButton = new JButton("OK");
	JTextField widthInput = new JTextField(3);
	JTextField heightInput = new JTextField(3);
	JTextField bombsInput = new JTextField(3);
	
	
	private JButton [][] gameboard;
	private JPanel menuPanel;
	private JPanel gamePanel;
	private JFrame customMenuFrame;
	private JPanel customMenuPanel;
	
	
	
	ActionListener buttonListener;
	
	View(Model model){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.model=model;
		this.model.addObserver(this);
		this.gameboard=new JButton[model.getWidth()][model.getHeight()];
		
		menuPanel = new JPanel();
		gamePanel = new JPanel();
		menuPanel.add(menu);
		menuPanel.add(bombsLeft);
		menuPanel.add(bombsCounter);
		menuPanel.add(reset);
		menuPanel.add(time);
		menuPanel.add(timer);
		gamePanel.setLayout(new GridLayout(model.getWidth(), model.getHeight()));
		
		for(int i=0; i<model.getWidth(); i++)
			for(int j=0; j<model.getHeight(); j++) {
				this.gameboard[i][j]=new JButton();
				this.gameboard[i][j].setPreferredSize(new Dimension(45,45));
				this.gameboard[i][j].setBackground(Color.BLACK);
			}
				
		for(int i=0; i<model.getWidth(); i++)
			for(int j=0; j<model.getHeight(); j++)
				gamePanel.add(gameboard[i][j]);
		this.add(menuPanel, BorderLayout.NORTH);
		this.add(gamePanel, BorderLayout.SOUTH);
		this.bombsCounter.setText(String.valueOf(model.getBombCounter()));
		this.timer.setText(String.valueOf(model.getTimer()));
		pack();
		this.setVisible(true);
	}
	
	/**
	 * This function is usually called when model parameters have been changed. It removes all objects from current window and dumps old gameboard. Then it constructs everything from the beginning, effectivly creating new game with new parameters.
	 */
	public void changeMode() {
		this.gameboard=null;
		this.gameboard=new JButton[model.getWidth()][model.getHeight()];

		menuPanel.removeAll();
		menuPanel.add(menu);
		menuPanel.add(bombsLeft);
		menuPanel.add(bombsCounter);
		menuPanel.add(reset);
		menuPanel.add(time);
		menuPanel.add(timer);
		
		
		gamePanel.removeAll();
		gamePanel.setLayout(new GridLayout(model.getWidth(), model.getHeight()));
		for(int i=0; i<model.getWidth(); i++)
			for(int j=0; j<model.getHeight(); j++) {
				this.gameboard[i][j]=new JButton();
				this.gameboard[i][j].setPreferredSize(new Dimension(45,45));
				this.gameboard[i][j].setBackground(Color.BLACK);
			}
				
		for(int i=0; i<model.getWidth(); i++)
			for(int j=0; j<model.getHeight(); j++)
				gamePanel.add(gameboard[i][j]);
		this.add(menuPanel, BorderLayout.NORTH);
		this.add(gamePanel, BorderLayout.SOUTH);
		this.bombsCounter.setText(String.valueOf(model.getBombCounter()));
		this.timer.setText(String.valueOf(model.getTimer()));
		pack();
		this.setVisible(true);
	}
	
	
	/**
	 * This function will create and show to user jframe with fields for width, height and number of bombs input.
	 * Ok button has actionListener.
	 * @param listenForCustom - action listener for ok button
	 */
	public void showCustomMenu(ActionListener listenForCustom) {		
		okButton.addActionListener(listenForCustom);
		
		this.customMenuPanel = new JPanel();
		this.customMenuFrame = new JFrame();
		this.customMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		customMenuPanel.add(width);
		customMenuPanel.add(widthInput);
		
		customMenuPanel.add(height);
		customMenuPanel.add(heightInput);
		
		customMenuPanel.add(bombs);
		customMenuPanel.add(bombsInput);
		
		customMenuPanel.add(okButton);
		
		customMenuFrame.add(customMenuPanel);
		
		customMenuFrame.pack();
		customMenuFrame.setVisible(true);		
	}
	
	/**
	 * This function is used to dispose custom menu.
	 */
	public void disposeCustomMenu() {
		this.customMenuFrame.dispatchEvent(new WindowEvent(customMenuFrame, WindowEvent.WINDOW_CLOSING));
	}
	
	
	
	/**
	 * This function is overrided update function. It watches for messages and based on recieved notification calls different functions.
	 * "redraw" - redrawBoard()
	 * "bombCounter" - setBombCounter()
	 * "timer" - setTimer()
	 * "won" - setWon()
	 * "reset" - resetReset()
	 * "lost" - setLost()
	 * "changedMode" - changeMode()
	 */
	@Override
	public void update(Observable o, Object arg){
		if(arg.equals("redraw"))
			this.redrawBoard();
		else if(arg.equals("bombCounter"))
			this.setBombCounter();
		else if(arg.equals("timer"))
			this.setTimer();
		else if(arg.equals("won"))
			this.setWon();
		else if(arg.equals("reset"))
			this.resetReset();
		else if(arg.equals("lost"))
			this.setLost();
		else if(arg.equals("changedMode"))
			this.changeMode();
	}
	
	/**
	 * Set value of bombCOunter field to current one obtained from model.
	 */
	public void setBombCounter() {
		this.bombsCounter.setText(String.valueOf(model.getBombCounter()));
	}
	/**
	 * Set value of timer field to current one obtained from model.
	 */
	public void setTimer() {
		this.timer.setText(String.valueOf(model.getTimer()));
	}
	
	/**
	 * Sets background of reset button to yellow and sets it's text to "You've won!"
	 */
	public void setWon() {
		this.reset.setBackground(Color.YELLOW);
		this.reset.setText("You've won!");
	}
	
	/**
	 * Sets background of reset button to red and sets it's text to "You've lost!"
	 */
	public void setLost() {
		this.reset.setBackground(Color.RED);
		this.reset.setText("You've lost!");
	}
	
	/**
	 * Sets background of reset button to null and sets it's text to Reset.
	 */
	public void resetReset() {
		this.reset.setBackground(null);
		this.reset.setText("Reset");
	}
	
	/**
	 * This function redraws gamefield that is goes through whole gameboard and based on fields values sets appropriate colors, marks etc.
	 * Unrevealed fields ale whole black. Flagged fields are blue with black F. Fields not neighboring bombs are plain white when revealed and fields with bombs are white with red X.
	 */
	void redrawBoard()	{
		for(int i=0; i<model.getWidth(); i++)
			for(int j=0; j<model.getHeight(); j++) 
				if(this.model.getField(i, j).getRevealed()) 
					if(this.model.getField(i, j).getNeighbours()==0 && this.model.getField(i, j).getBomb()==false) {
						this.gameboard[i][j].setBackground(Color.WHITE);
						this.gameboard[i][j].setForeground(Color.WHITE);
					}else if(this.model.getField(i, j).getBomb()){
						this.gameboard[i][j].setForeground(Color.RED);
						this.gameboard[i][j].setBackground(Color.WHITE);
						this.gameboard[i][j].setText("X");
					}else{
						this.gameboard[i][j].setForeground(Color.WHITE);
						this.gameboard[i][j].setText(String.valueOf(this.model.getField(i, j).getNeighbours()));
					}
				else if(this.model.getField(i, j).getFlag()) {
						this.gameboard[i][j].setBackground(Color.BLUE);
						this.gameboard[i][j].setText("F");
				}else {
					this.gameboard[i][j].setBackground(Color.BLACK);
					this.gameboard[i][j].setForeground(Color.BLACK);
				}
	}
	
	/**
	 * Add ActionListener to reset button.
	 * @param listenForResetButton - ActionListener added to Reset button.
	 */
	void addActionResetListener(ActionListener listenForResetButton) {
		reset.addActionListener(listenForResetButton);
	}
	
	/**
	 * Add MouseAdapter to every field of gameboard.
	 * @param listenForGridButton - MouseAdapater wchich will be added to every field of gameboard
	 */
	void addMouseGridListener(MouseAdapter listenForGridButton) {
		for(int i=0; i<model.getWidth(); i++)
			for(int j=0; j<model.getHeight(); j++) {
				this.gameboard[i][j].addMouseListener(listenForGridButton);
			}
	}
	
	/**
	 * This function adds ActionListener to drop-down menu.
	 * @param listenForMenu - ActionListener which will be added to drop-down menu.
	 */
	void addActionMenuListener(ActionListener listenForMenu) {
		menu.setSelectedIndex(0);
		menu.addActionListener(listenForMenu);
	}
	
	
	/**
	 * This function will remove all ActionListeners and MouseAdapters from all buttons and gameboard.
	 */
	void removeListeners() {
		reset.removeMouseListener(reset.getMouseListeners()[0]);
		for(int i=0; i<model.getWidth(); i++)
			for(int j=0; j<model.getHeight(); j++) {
				this.gameboard[i][j].removeMouseListener(this.gameboard[i][j].getMouseListeners()[0]);
			}
		menu.removeActionListener(menu.getActionListeners()[0]);
	}
	
	public JButton[][] getGameboard()
	{
		return this.gameboard;
	}
}