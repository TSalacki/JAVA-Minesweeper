import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * This is controller class for minesweeper. It's main purpose is to listen when different fields from view were clicked (and how) and act accordingly. 
 * @author Tomek
 *
 */
public class Controller{
	private Model model;
	private View view;
	int width, height, bombs;
	
	Controller(Model model, View view){
		this.model=model;
		this.view=view;
		
		this.view.addActionResetListener(new ActionResetListener());
		this.view.addMouseGridListener(new MouseGridListener());
		this.view.addActionMenuListener(new ActionMenuListener());
	}
	/**
	 * This class is used to listen for user to input desired game parameters if he had selected custom game mode.
	 * it will then call model to remake itself, call view to dispose customMenu and add gridListeners.
	 * @author Tomek
	 *
	 */
	class CustomMenuListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			width = Integer.parseInt(view.widthInput.getText());
			height = Integer.parseInt(view.heightInput.getText());
			bombs = Integer.parseInt(view.bombsInput.getText());
			model.changeMode(height, width, bombs);
			view.disposeCustomMenu();
			view.addMouseGridListener(new MouseGridListener());
		}
	}
	
	/**
	 * This class is used to listen for dropdown menu in view which contains predefined game settings.
	 * If the custom field is selected then the showCustomMenu() from view will be called
	 * @author Tomek
	 *
	 */
	class ActionMenuListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox)e.getSource();
			String msg = (String)cb.getSelectedItem();
			if(msg.equals("Beginner")) {
				model.changeMode(8, 8, 10);
				view.addMouseGridListener(new MouseGridListener());
			}else if(msg.equals("Intermediate")) {
				model.changeMode(16, 16, 40);
				view.addMouseGridListener(new MouseGridListener());
			}else if(msg.equals("Expert")) {
				model.changeMode(16, 30, 99);
				view.addMouseGridListener(new MouseGridListener());
			}else if(msg.equals("Custom")){
				view.showCustomMenu(new CustomMenuListener());
			}
		}
	}
	
	
	/**
	 * Purpose of this class is to listen for reset button to be clicked.
	 * @author Tomek
	 *
	 */
	class ActionResetListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {   
				model.reset();
	        }
		}
	
	/**
	 * Purpose of this class is to listen which field and by which mouse button was clicked.
	 * @author Tomek
	 *
	 */
	class MouseGridListener extends MouseAdapter {
		int x;
		int y;
		
		/**
		 * This function listen for mouse click on game field. After that it checks which field was clicked by running through whole board until it finds source.
		 * Then it checks which mouse button was pressed and with that knowledge it runs one of two model functions to reveal title of change it state.
		 * It passes params x and y which correspond to fouded field's position.
		 */
		public void mouseClicked(MouseEvent e) {
			for(int i=0; i<model.getWidth(); i++)
				for(int j=0; j<model.getHeight(); j++)
					if(view.getGameboard()[i][j].equals(e.getSource())) {
						x=i;
						y=j;
					}
	        switch (e.getButton()) {
	            case MouseEvent.BUTTON1:
	            {
	            	model.revealTile(x, y);
	            	break;
	            }
	            case MouseEvent.BUTTON3:
	            {	
	            	model.changeState(x, y);
	            	break;
	            }
	            default:
	                break;
	        }
		}
	}
}
