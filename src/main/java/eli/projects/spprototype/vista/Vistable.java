package eli.projects.spprototype.vista;

import javafx.scene.Node;

/**
 * Classes that implement this interface are able to be placed inside of the vista.
 * @author Eli
 *
 */
public interface Vistable {

	/**
	 * Called once when the vista is no longer being shown.
	 * Use this method to remove all listeners.
	 */
	public void remove();
	
	/**
	 * Returns the root node of this vista.
	 * @return The root node of this vista.
	 */
	// public Node getRoot();
	
	
	
}
