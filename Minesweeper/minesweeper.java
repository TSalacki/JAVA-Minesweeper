/**
 * Main class for minesweeper.
 * @author Tomek
 *
 */
public class minesweeper {

	public static void main(String[] args) {
		Model model;
		View view;
		Controller controller;
		model = new Model(8, 8, 10);
		view = new View(model);
		controller = new Controller(model, view);
	}

}
