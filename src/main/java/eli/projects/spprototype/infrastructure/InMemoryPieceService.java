/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import eli.projects.spprototype.model.Piece;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

/**
 * @author Eli
 *
 */
public class InMemoryPieceService implements PieceService {

	private ObservableList<Piece> pieces;
	
	/*
	 * Instantiates a list with made up pieces (not part of any library)
	 */
	public InMemoryPieceService() {
		
		
		
		// Add all the properties that would make classes observing this list want to update what they're doing.
		Callback<Piece, Observable[]> extractor = new Callback<Piece, Observable[]>() {

			@Override
			public Observable[] call(Piece p) {
				return new Observable[] {p.arrangerProperty(), p.composerProperty(), p.durationProperty(), p.partCountProperty(), p.titleProperty()};
			}
			
		};
		
		pieces = FXCollections.observableArrayList(extractor);
		pieces.addAll(Piece.getAllFakePieces());
		
	} 
	
	@Override
	public ObservableList<Piece> getItems() {
		
		return pieces;
	}

}
