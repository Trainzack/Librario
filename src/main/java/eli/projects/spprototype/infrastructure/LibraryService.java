package eli.projects.spprototype.infrastructure;

import eli.projects.spprototype.model.Library;

public interface LibraryService {
	
	/**
	 * Returns the library object that this service provides
	 * @return Library
	 */
	Library getLibrary();
	
	/**
	 * Saves the library to the place it originates.
	 * @return true if the save was successful, false otherwise. 
	 */
	boolean saveLibrary();
}
