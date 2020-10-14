package eli.projects.spprototype.model;


import java.util.ArrayList;
import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class represents the Library: A collection of Pieces, SAetlists, and Ensembles.
 * 
 * We should be able to load and save libraries to disk.
 * 
 * We might consider ways of having more than one library open at once
 * 
 * @author Eli
 *
 */
public class Library {

	// This arraylist contains all of the pieces that this library contains
	private ObservableList<Piece> pieces;
	// This ObjectProperty contains the currently selected piece
	private final ObjectProperty<Piece> currentPiece = new SimpleObjectProperty<Piece>(null);

	// This arraylist contains all of the setlists that this library contains
	private final ObservableList<Setlist> setlists;
	// This ObjectProperty contains the currently selected setlist
	private final ObjectProperty<Setlist> currentSetlist = new SimpleObjectProperty<Setlist>(null);
	
	
	// This arraylist contains all of the ensembles that this library contains
	private ObservableList<Ensemble> ensembles;
	// This ObjectProperty contains the currently selected ensemble
	private final ObjectProperty<Ensemble> currentEnsemble = new SimpleObjectProperty<Ensemble>(null);
	
	/**
	 * TODO: This needs a name variable for save file names
	 * @param pieces
	 * @param setlists A list of setlists. Should *NOT* include the librarysetlist.
	 * @param ensembles
	 */
	public Library(ArrayList<Piece> pieces, ArrayList<Setlist> setlists, ArrayList<Ensemble> ensembles) {
		super();
		this.pieces = FXCollections.observableArrayList(pieces);
		this.setlists = FXCollections.observableArrayList(setlists);
		this.ensembles = FXCollections.observableArrayList(ensembles);
		
	}
	
	public Library(Piece[] pieces, Setlist[] setlists, Ensemble[] ensembles) {
		this(
				new ArrayList<Piece>(Arrays.asList(pieces)),
				new ArrayList<Setlist>(Arrays.asList(setlists)),
				new ArrayList<Ensemble>(Arrays.asList(ensembles))
			);
	}


	/** Pieces **/

	public ObservableList<Piece> getPieces() {
		return pieces;
	}
	
	public final ObjectProperty<Piece> getCurrentPieceProperty() {
		return currentPiece;
	}
	
	public final Piece getCurrentPiece() {
		return currentPiece.get();
	}
	
	public final void setCurrentPiece(Piece piece) {
		currentPiece.set(piece);
	}

	/** Setlists **/
	
	public ObservableList<Setlist> getSetlists() {
		return setlists;
	}
	
	public final ObjectProperty<Setlist> getCurrentSetlistProperty() {
		return currentSetlist;
	}
	
	public final Setlist getCurrentSetlist() {
		return currentSetlist.get();
	}	
	/**
	 * Creates a new empty setlist
	 * @param name The name of the setlist
	 * @return The setlist that we add
	 */
	public Setlist addSetlist(String name) {
		Setlist out = new Setlist(name);;
		this.setlists.add(out);
		// TODO: This needs to update the undo stack
		return out;
	}
	
	public final void setCurrentSetlist(Setlist setlist) {
		currentSetlist.set(setlist);
	}
	
	public final void deleteCurrentSetlist() {
		setlists.remove(this.getCurrentSetlist());
		currentSetlist.set(null);
	}
	
	/** Ensembles **/
	
	public ObservableList<Ensemble> getEnsembles() {
		return ensembles;
	}

	public final ObjectProperty<Ensemble> getCurrentEnsembleProperty() {
		return currentEnsemble;
	}
	
	public final Ensemble getCurrentEnsemble() {
		return currentEnsemble.get();
	}
	
	public final void setCurrentEnsemble(Ensemble ensemble) {
		currentEnsemble.set(ensemble);
	}
	
	public final void addNewEnsemble() {
		ensembles.add(new Ensemble("New Ensemble"));
	}
	
	public final void deleteCurrentEnsemble() {
		ensembles.remove(currentEnsemble.get());
	}
	
	/** Instruments **/
	
	public ObservableList<Instrument> getInstruments() {
		// TODO: At some point, we want all libraries to have their own instruments.
		return FXCollections.observableArrayList(Instrument.getInstruments());
	}

	
	
	
	/**
	 * Generates a library with fake testing data
	 * @return
	 */
	public static Library generateTestingLibrary() {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		for (Piece p : Piece.getAllFakePieces()) {
			pieces.add(p);
		}
		
		ArrayList<Setlist> setlists = new ArrayList<Setlist>(1);

		Setlist u = Setlist.generateTestUserSetlist();
		u.setName("Fall Concert Program 2019");
		setlists.add(u);
		u = Setlist.generateTestUserSetlist();
		u.setName("Spring Concert Program 2020");
		setlists.add(u);
		
		ArrayList<Ensemble> ensembles = new ArrayList<Ensemble>(1);
		
		for (int i = 0; i < 1; i++) {
			ensembles.add(Ensemble.generateTestEnsemble());
		}
		
		
		return new Library(pieces, setlists, ensembles);
	}

}
