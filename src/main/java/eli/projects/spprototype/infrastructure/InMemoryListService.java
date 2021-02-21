/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import java.util.ArrayList;
import java.util.Set;

import javax.management.AttributeList;

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
public class InMemoryListService implements ListService {

	private ObservableList<Setlist> lists;
	
	/*
	 * Instantiates a list with made up pieces (not part of any library)
	 */
	public InMemoryListService(int listCount) {
		
		ArrayList<Setlist> fakeSetlists = new ArrayList<Setlist>();
		
		for (int i = 0; i < listCount; i++) { 
			Setlist fakeSetlist = new Setlist("Test List " + i);
			
			for (Piece p : Piece.generateFakePieces(15)) {
				fakeSetlist.add(p);
			}
			
			fakeSetlists.add(fakeSetlist);
		}
		
		// Add all the properties that would make classes observing this list want to update what they're doing.
		Callback<Setlist, Observable[]> extractor = new Callback<Setlist, Observable[]>() {

			@Override
			public Observable[] call(Setlist list) {
				return new Observable[] {list.getLengthProperty(), list.getNameProperty()};
			}
			
		};
		
		lists = FXCollections.observableArrayList(extractor);
		lists.addAll(fakeSetlists);
		
	} 
	
	@Override
	public ObservableList<Setlist> getItems() {
		
		return lists;
	}

}
