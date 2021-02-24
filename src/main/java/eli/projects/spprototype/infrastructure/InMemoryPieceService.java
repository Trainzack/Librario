/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import java.util.List;

import eli.projects.spprototype.model.Piece;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

/**
 * @author Eli
 *
 */
public class InMemoryPieceService implements PieceService {
	
	private BooleanProperty saved = new SimpleBooleanProperty(true);

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

	@Override
	public void deleteItem(Piece item) {
		pieces.remove(item);
		
	}

	@Override
	public void deleteItems(List<Piece> items) {
		pieces.removeAll(items);
		
	}
	

	@Override
	public boolean save() {
		// We have no disk-representation, so we just fake this.
		return false;
	}

}
