/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import java.util.List;

import eli.projects.spprototype.model.Ensemble;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ObservableList;

/**
 * @author Eli
 * 
 * This service keeps track of a list of things.
 * This is intended for use with the ensembles, pieces, and setlists stored in the library.
 *
 */
public interface ObservableListService <E> {

	/**
	 * Returns the list of items that this service provides
	 * @return
	 */
	ObservableList<E> getItems();
	
	/**
	 * Deletes the given item from the service
	 * @param item The item to remove from the service
	 */
	void deleteItem(E item);
	
	/**
	 * Deletes the given list of items from the service
	 * @param items A list containing the items to delete from the service
	 */
	void deleteItems(List<E> items);
	
	
	/**
	 * Saves this service to disk
	 * @return True if the save was sucessful, false otherwise
	 */
	boolean save();
	
}
