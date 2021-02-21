/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import java.util.ArrayList;
import java.util.Set;

import javax.management.AttributeList;

import eli.projects.spprototype.model.Ensemble;
import eli.projects.spprototype.model.Library;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

/**
 * @author Eli
 *
 */
public class InMemoryLibraryService implements LibraryService {

	private Library library;
	
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
	public boolean saveLibrary() {
		return false;
	}

}
