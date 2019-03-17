import java.util.Observable;

/**
 * This is class which describes field from which gameboard is build.
 * @author Tomek
 *
 */
public class Field extends Observable{
	private int posx, posy, neighbours;
	private boolean revealed;
	private boolean flagged;
	private boolean bomb;
	
	
	public Field(int x, int y)	{
		posx=x;
		posy=y;
		revealed=false;
		flagged=false;
		bomb=false;
	}
	
	public void reset() {
		revealed=false;
		flagged=false;
		bomb=false;
		neighbours=0;
	}
	
	public void setPos(int x, int y) {
		this.posx=x;
		this.posy=y;
	}
	
	public int getPosx() {
		return this.posx;
	}
	
	public int getPosy() {
		return this.posy;
	}
	
	public void reveal()
	{
		this.revealed=true;
	}
	
	public boolean getRevealed() {
		return this.revealed;
	}
	
	public void setBomb()	{
		this.bomb=true;
	}
	
	public boolean getBomb() {
		return this.bomb;
	}
	
	public void setFlag()	{
		this.flagged=true;
	}
	
	public boolean getFlag()	{
		return this.flagged;
	}
	
	public void removeFlag() {
		this.flagged=false;
	}
	
	public void setNeighbours(int x) {
		this.neighbours=x;
	}
	
	public int getNeighbours() {
		return this.neighbours;
	}
	
}
