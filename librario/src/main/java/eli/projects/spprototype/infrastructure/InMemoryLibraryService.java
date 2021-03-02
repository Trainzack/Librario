/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import java.io.File;

import eli.projects.spprototype.model.Library;

/**
 * @author Eli
 *
 */
public class InMemoryLibraryService implements LibraryService {
	

	private Library library;
	
	public InMemoryLibraryService(File path) {
		library = Library.loadOldLibrary(path);
	}
	
	/**
	 * Instantiates a made up library for testing purposes
	 * @param pieces The number of pieces to 
	 */
	public InMemoryLibraryService(int pieces) {
		library = Library.generateTestingLibrary(pieces);
	}

	@Override
	public Library getLibrary() {
		return library;
	}
	

	@Override
	public boolean save() {
		// We have no disk-representation, so we just fake this.
		return false;
	}

}
