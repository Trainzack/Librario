/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import eli.projects.spprototype.model.Ensemble;
import javafx.collections.ObservableList;

/**
 * @author Eli
 * 
 * This service keeps track of a list of things.
 * This is intended for use with the ensembles, pieces, and setlists stored in the library.
 *
 */
public interface ObservableListService <E> {

	ObservableList<E> getItems();
	
}
