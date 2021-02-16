/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import java.util.ArrayList;
import java.util.Set;

import javax.management.AttributeList;

import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
		
		lists = FXCollections.observableArrayList(fakeSetlists);
		
	} 
	
	@Override
	public ObservableList<Setlist> getSetlists() {
		
		return lists;
	}

}
