/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	
		
	/**
	 * Create empty piece service
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
	}
	
	/**
	 * Create a pieceService containing the given pieces
	 * @param pieces The pieces to be served by the piece service
	 */
	public InMemoryPieceService(List<Piece> pieces) {
		this();
		this.pieces.addAll(pieces);
	}
	
	/**
	 * Instantiates a piece service with made up pieces (not part of any library)
	 * @param count The number of fake pieces to add (-1 for all)
	 */
	public InMemoryPieceService(int count) {
		this();
		if (count < 0) {
			pieces.addAll(Arrays.asList(Piece.getAllFakePieces()));
		} else {
			pieces.addAll(Arrays.asList(Piece.generateFakePieces(count)));
		}
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
