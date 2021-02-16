/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import eli.projects.spprototype.model.Setlist;
import javafx.collections.ObservableList;

/**
 * @author Eli
 * 
 * This service keeps track of a list of setlists.
 * This is intended for use with the setlists stored in the library.
 *
 */
public interface ListService {

	ObservableList<Setlist> getSetlists();
	
}
