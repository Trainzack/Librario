package eli.projects.spprototype.infrastructure;

import eli.projects.spprototype.model.Library;
import javafx.beans.property.BooleanProperty;

public interface LibraryService {
	
	/**
	 * Returns the library object that this service provides
	 * @return Library
	 */
	Library getLibrary();
	
	
	/**
	 * Saves this service to disk
	 * @return true if the save was successful, false otherwise. 
	 */
	boolean save();
}
