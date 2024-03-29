package eli.projects.spprototype.model;

import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * This is a list of pieces that users can create, delete, edit, and manipulate in any way.
 * @author Eli
 *
 */
public class Setlist {
	
	private StringProperty name = new SimpleStringProperty();
	
	// This variable contains the pieces that are in this setlist.
	private ObservableList<Piece> observableList;
	
	
	private IntegerProperty numberOfPieces = new SimpleIntegerProperty(0);
	
	public Setlist(String name) {
		super();
		
		this.name.set(name);
		observableList = FXCollections.observableArrayList(new ArrayList<Piece>());
		
		observableList.addListener(new ListChangeListener<Piece>() {
			@Override
			public void onChanged(Change<? extends Piece> c) {
				numberOfPieces.set(observableList.size());
				
			}
		});
	}


	public ObservableList<Piece> getPieceList() {
		return observableList;
	}
	
	public void add(Piece newPiece) {
		observableList.add(newPiece);
	}
	
	/**
	 * Adds the piece indicated by the given session-specific ID. This is intended for use by the dragboard.
	 * @param ID The ID of the piece to add
	 * @return true if successful, false otherwise.
	 */
	public boolean addFromID(int ID) {
		
		Piece p = Piece.getPiece(ID);
		if (p != null) {
			this.add(p);
			return true;
		}
		
		return false;
	}
	
	public void remove(Piece piece) {
		observableList.remove(piece);
	}

	public String getName() {
		return name.get();
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public StringProperty getNameProperty() {
		return name;
	}
	
	public IntegerProperty getLengthProperty() {
		return numberOfPieces;
	}


	/**
	 * @deprecated
	 * @return
	 */
	public boolean isUserList() {
		return true;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
}
