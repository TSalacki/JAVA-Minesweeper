import java.util.Observable;

/**
 * This is model class which contains whole game logic for minesweeper. It's observable and certain functions notify observers by certain messages.
 * @author Tomek
 *
 */
public class Model extends Observable {
	private Field [][]board;
	private int width, height;
	private int bombCounter;
	private int revealedCounter;
	private int totalBombs;
	private int timer; 
	private boolean timerRunning;
	private boolean firstClick;
	private Thread thread; 
	private String state;
	
	/** 
	 * This function is used to change parameters of game such as field size and number of bombs. It will build new board, update model parameters, reset timer, first-click flag and notify observers by "changed field" message.
	 * @param width - width of field
	 * @param height - height of field
	 * @param bombs - number of bombs in game
	 */
	public void changeMode(int width, int height, int bombs)	{
		this.width=width;
		this.height=height;
		this.bombCounter=bombs;
		this.revealedCounter=0;
		this.totalBombs=bombs;
		this.board=new Field[width][height];
		this.state="running";
		this.timerRunning=false;
		this.firstClick=true;
		this.resetTimerThread();
		this.buildBoard();
		this.setChanged();
		this.notifyObservers("changedMode");
	}
	
	public Model(int width, int height, int bombs)	{
		this.width=width;
		this.height=height;
		this.bombCounter=bombs;
		this.revealedCounter=0;
		this.totalBombs=bombs;
		this.board=new Field[width][height];
		this.state="running";
		this.timerRunning=false;
		this.firstClick=true;
		this.buildBoard();
	}
	
	/**
	 * This function resets timer back to zero and notify observers by message "timer".
	 */
	public void resetTimerThread() {
		this.timer=0;
		this.setChanged();
		this.notifyObservers("timer");
	}
	
	/**
	 * This functions sets timerRunning variable to true and calls {@link #resetTimerThread()} to start timer thread.
	 */
	public void startTimerThread() {
		this.timerRunning=true;
		this.setTimerThread();
	}
	
	/**
	 * This function sets new thread which purpose is to increment timer by one every second.
	 * It does it's when game is running (that means it is not won or lost and first move was made).
	 * It notifies observers when timer is incremented by message "timer".
	 */
	public void setTimerThread()
	{
		this.thread = new Thread() {
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				while(timerRunning && firstClick==false) {
					try {
						timer++;
						setChanged();
						notifyObservers("timer");
						this.sleep(1000);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		this.thread.start();
	}
	
	/**
	 * Fills board with blank fields.
	 */
	public void buildBoard() {
		for(int i=0; i<this.width; ++i) {
			for(int j=0; j<this.height; ++j) {
				this.board[i][j]=new Field(i, j);
			}
		}
	}
	/**
	 * Function places number of bombs specified by model's totalBombs variable.
	 * It places them by randomly selecting field without any bomb and which is not forbidden from having bomb.
	 * @param xForbidden - x position of field which can't have bomb.
	 * @param yForbidden - y position of field which can't have bomb.
	 */
	public void placeBombs(int xForbidden, int yForbidden) {
		int x, y;
		for(int i=0; i<this.totalBombs; ++i)
		{
			do {
				x=(int) Math.round((Math.random()*(this.width-1)));
				y=(int) Math.round((Math.random()*(this.height-1)));
				
			}while(this.board[x][y].getBomb()==true || (x==(xForbidden) && y==(yForbidden)));
			this.board[x][y].setBomb();
		}
	}
	
	/**
	 * Calculate how many bombs are on adjacent fields and mark it on that field's neighbors variable.
	 * Function checks whether or not it would go out of game board array boundaries. 
	 */
	public void calcNeighbours() {
		int neighbours;
		for(int i=0; i<this.width; ++i) {
			for(int j=0; j<this.height; ++j) {
				neighbours=0;
				if(this.board[i][j].getBomb()==true) 
					continue;
				if(i!=0) //not first column
					if(this.board[i-1][j].getBomb()==true) neighbours++;
				if(i!=(this.width-1)) //not last column
					if(this.board[i+1][j].getBomb()==true) neighbours++;
				if(j!=0) //not first row
					if(this.board[i][j-1].getBomb()==true) neighbours++;
				if(j!=this.height-1) //not last row
					if(this.board[i][j+1].getBomb()==true) neighbours++;
				
				if(i!=0 && j!=0) //not first column && not first row
					if(this.board[i-1][j-1].getBomb()==true) neighbours++;
				
				if(i!=0 && j!=this.height-1) //not first column && not last row
					if(this.board[i-1][j+1].getBomb()==true) neighbours++;
				
				if(i!=(this.width-1) && j!=0) //not last column && not first row
					if(this.board[i+1][j-1].getBomb()==true) neighbours++;
				
				if(i!=(this.width-1) && j!=this.height-1) //not last column && not last row
					if(this.board[i+1][j+1].getBomb()==true) neighbours++;
				
				this.board[i][j].setNeighbours(neighbours); 
			}
		}
	}
	/**
	 * This function resets whole game (removes bombs, zeroes timer etc.) and sends observers notifications
	 * When notifying observers it sends "bombCounter", "redraw" and "reset" messages.
	 * It uses multiple functions to reset each part of board (see {@link #resetBoard()} and {@link #resetTimerThread()})
	 */
	public void reset(){
		this.resetBoard();
		
		this.bombCounter=this.totalBombs;
		this.revealedCounter=0;
		this.timer=0;
		this.state="running";
		this.firstClick=true;
		
		this.resetTimerThread();
		
		this.setChanged();
		this.notifyObservers("bombCounter");
		this.setChanged();
		this.notifyObservers("redraw");
		this.setChanged();
		this.notifyObservers("reset");
	}
	
	/**
	 * This function resets game board. It calls reset function for each field on the board.
	 */
	public void resetBoard() {
		for(int i=0; i<this.width; ++i) {
			for(int j=0; j<this.height; ++j) {
				this.board[i][j].reset();
			}
		}
	}
	
    public int getTimer() {
        return this.timer;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getBombCounter() {
    	return this.bombCounter;
    }
    
    public Field getField(int width, int height) {
    	return this.board[width][height];
    }
    
    /**
     * This function is used to reveal all fields with zero neighbors adjacent to the clicked field (including it). It will do it as long as there are adjacent unrevealed zero fields and is within game board bounds. Function can call itself multiple times.
     * @param x - x position in array
     * @param y - y position in array
     */
    public void revealZeros(int x, int y) {
    	if(this.board[x][y].getRevealed())
    		return;
    	
    	this.revealedCounter++;
    	this.board[x][y].reveal();
    	
    	if(this.board[x][y].getNeighbours()!=0)
    		return;
    	
    	if(x!=0) //not first column
			this.revealZeros(x-1, y);
		if(y!=0) //not last column
			this.revealZeros(x, y-1);
		if(x!=this.getWidth()-1) //not first row
			this.revealZeros(x+1, y);
		if(y!=this.height-1) //not last row
			this.revealZeros(x, y+1);
		if(x!=0 && y!=0) //not first column && not first row
			this.revealZeros(x-1, y-1);
		if(x!=0 && y!=(this.height-1)) //not first column && not last row
			this.revealZeros(x-1, y+1);
		if(x!=(this.width-1) && y!=0) //not last column && not first row
			this.revealZeros(x+1, y-1);
		if(x!=(this.width-1) && y!=this.height-1) //not last column && not last row
			this.revealZeros(x+1, y+1);
    }
    
    /**
     * This function will reveal unrevealed field.
     * If it is first click of the game, function will call functions to place bombs and calculate neighbors.
     * If the revealed field contained bomb, function will set game status as lost.
     * It also checks if the move will end the game (see {@link #endGame()}).
     * It also notifies observers by message "redraw".
     * @param x - x position in array
     * @param y - y position in array
     */
    public void revealTile(int x, int y){
    	if(this.firstClick==true) {
    		this.placeBombs(x, y);
    		this.calcNeighbours();
    		this.firstClick=false;
    		this.startTimerThread();
    	}else if(this.state.equals("lost"))
    		return;
    
    	if(this.board[x][y].getRevealed())
    		return;
    	if(this.board[x][y].getFlag())
    		this.changeState(x, y);
    	if(this.board[x][y].getBomb()) {
    		this.board[x][y].reveal();
    		this.revealedCounter++;
    		this.state="lost";
    		this.timerRunning=false;
    		this.setChanged();
    		this.notifyObservers("redraw");
    	}
    	else if(this.board[x][y].getNeighbours()==0) {
        		this.revealZeros(x, y);
        		this.setChanged();
        		this.notifyObservers("redraw");
    	} else {
    		this.board[x][y].reveal();
    		this.revealedCounter++;
    		this.setChanged();
    		this.notifyObservers("redraw");
    	}
    	this.endGame();
    		
    }
    
    /**
     * This function is called when user wants to flag or remove flag from field.
     * It notifies observers by message "bombCounter" if flag was added or removed and by message "redraw".
     * It also checks for the end of game (see {@link #endGame()}
     * @param x - x position in array
     * @param y - y position in array
     */
    public void changeState(int x, int y) {
    	if(this.state.equals("lost") || this.board[x][y].getRevealed())
    		return;
    	if(this.getField(x, y).getFlag()) {
    		this.getField(x, y).removeFlag();
    		this.bombCounter++;
    		this.setChanged();
    		this.notifyObservers("bombCounter");
    	}
    	else {
    		this.getField(x, y).setFlag();
    		this.bombCounter--;
    		this.setChanged();
    		this.notifyObservers("bombCounter");
    	}
		this.setChanged();
		this.notifyObservers("redraw");
		this.endGame();
    }
    
    /**
     * This function checks if the game has ended and if yes in which way.
     * If game status is "lost" then the game is lost and function notifies observers by sending "lost".
     * Otherwise if all fields with bomb have been marked and all other fields were revealed then the game is won and observers are notifies by "won" message.
     */
    public void endGame() {
    	if(this.state.equals("lost")) {
    		this.setChanged();
    		this.notifyObservers("lost");
    	}else if(this.revealedCounter==(this.height*this.width-this.totalBombs) && this.bombCounter==0) {
    		this.timerRunning=false;
    		this.setChanged();
    		this.notifyObservers("won");
    	}
    	
    }
}
