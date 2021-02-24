/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.management.AttributeList;

import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
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
public class InMemoryListService implements ListService {

	private ObservableList<Setlist> lists;
	
	/**
	 * Instantiates an empty in-memory service.
	 */
	public InMemoryListService() {
		// Add all the properties that would make classes observing this list want to update what they're doing.
		Callback<Setlist, Observable[]> extractor = new Callback<Setlist, Observable[]>() {

			@Override
			public Observable[] call(Setlist list) {
				return new Observable[] {list.getLengthProperty(), list.getNameProperty()};
			}
			
		};
		
		lists = FXCollections.observableArrayList(extractor);
	}
	
	/**
	 * Instantiates an in-memory service containing the given Setlists.
	 * @param lists The setlists to include in this service.
	 */
	public InMemoryListService(List<Setlist> lists) {
		this();
		this.lists.addAll(lists);
	}
	
	/**
	 * Instantiates a list with made up Setlists (not part of any library)
	 * @param listCount The number of setlists to invent
	 */
	public InMemoryListService(int listCount) {
		this();
		
		ArrayList<Setlist> fakeSetlists = new ArrayList<Setlist>();
		
		for (int i = 0; i < listCount; i++) { 
			Setlist fakeSetlist = new Setlist("Test List " + i);
			
			for (Piece p : Piece.generateFakePieces(15)) {
				fakeSetlist.add(p);
			}
			
			fakeSetlists.add(fakeSetlist);
		}
		
		lists.addAll(fakeSetlists);
		
	} 
	
	@Override
	public ObservableList<Setlist> getItems() {
		
		return lists;
	}

	@Override
	public void deleteItem(Setlist item) {
		lists.remove(item);
		
	}

	@Override
	public void deleteItems(List<Setlist> items) {
		lists.removeAll(items);
		
	}
	

	@Override
	public boolean save() {
		// We have no disk-representation, so we just fake this.
		return false;
	}

}
