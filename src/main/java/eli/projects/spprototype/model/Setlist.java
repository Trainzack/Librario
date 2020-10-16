package eli.projects.spprototype.model;

import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * This is a list of pieces that users can create, delete, edit, and manipulate in any way.
 * @author Eli
 *
 */
public class Setlist {
	
	private String name;
	
	// This variable contains the pieces that are in this setlist.
	private ObservableList<Piece> observableList;
	
	
	private IntegerProperty numberOfPieces = new SimpleIntegerProperty(0);
	
	public Setlist(String name) {
		super();
		
		this.name = name;
		observableList = FXCollections.observableArrayList(new ArrayList<Piece>());
		
		observableList.addListener(new ListChangeListener<Piece>() {
			@Override
			public void onChanged(Change<? extends Piece> c) {
				numberOfPieces.set(observableList.size());
				
			}
		});
	}
	/**
	 * Generates a new UserSetlist filled with testing data.
	 * @return
	 */
	public static Setlist generateTestUserSetlist() {
		
		Setlist out = new Setlist("Test List");
		
		for (Piece p : Piece.generateFakePieces(4)) {
			out.add(p);
		}
		
		return out;
	}

	public ObservableList<Piece> getPieceList() {
		return observableList;
	}
	
	public void add(Piece newPiece) {
		observableList.add(newPiece);
	}
	
	public void remove(Piece piece) {
		observableList.remove(piece);
	}

	public String getName() {
		return name;
	}
	
	public IntegerProperty getLengthProperty() {
		return numberOfPieces;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isUserList() {
		return true;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
}
