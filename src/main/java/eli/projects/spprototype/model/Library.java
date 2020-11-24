package eli.projects.spprototype.model;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.apache.pdfbox.pdmodel.PDDocument;

import eli.projects.spprototype.SimpleDocumentSource;
import eli.projects.spprototype.Part;
import eli.projects.spprototype.PartDesignation;
import eli.projects.spprototype.PartDesignation.InvalidPartDesignationException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
	
	public final void deletePiece(Piece piece) {
		// TODO: remove this piece from all setlists!
		this.pieces.remove(piece);
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
	public static Library generateTestingLibrary(int limit) {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		
		int j = 0;
		for (Piece p : Piece.getAllFakePieces()) {
			if (++j > limit) break;
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
	
	public static Library loadOldLibrary() {
		
		File path = new File("D:/Sheet Music/Pep Band");
		
		ArrayList<Piece> pieces = new ArrayList<Piece>();

		for (File pieceFolder : path.listFiles()) {
			if (!pieceFolder.isDirectory()) continue;
			
			String name = pieceFolder.getName();
			
			Piece piece = new Piece(name, "Composer", "Arranger", "1998", 200);
			
			pieces.add(piece);
			
			for (File partFile : pieceFolder.listFiles()) {
				if (partFile.isDirectory() || !partFile.getPath().toLowerCase().endsWith(".pdf")) continue;
				
				String partName = partFile.getName().toUpperCase().replaceAll(".PDF", "");
				
				int pageCount = 1;
				
				try {
					PDDocument partDoc = PDDocument.load(partFile);
					pageCount = partDoc.getNumberOfPages();
					partDoc.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					continue;
				}
				
				Part part;
				try {
					part = new Part(PartDesignation.ParseDesignation(partName), piece);
					
					//if (part.getDesignation().getInstrument().getId() != "TBN") continue; //TODO Temp filter
					piece.getParts().add(part);
					for (int i = 0; i < pageCount; i++) part.getDocumentSources().add(new SimpleDocumentSource(partFile, i));
				} catch (InvalidPartDesignationException e) {
					// TODO Auto-generated catch block
					System.out.println("Invalid part! " + partName + ", at " + partFile.getPath());
				}
				

				
				
			}
			
		}
		
		Random r = new Random();
		
		ArrayList<Setlist> setlists = new ArrayList<Setlist>(1);
		Setlist u = new Setlist("Books");
		for (Piece p : pieces) {
			if (r.nextBoolean()) u.add(p);
		}
		setlists.add(u);
		
		ArrayList<Ensemble> ensembles = new ArrayList<Ensemble>(1);
		
		for (int i = 0; i < 1; i++) {
			ensembles.add(Ensemble.generateTestEnsemble());
		}
		
		
		return new Library(pieces, setlists, ensembles);
		
	}

}
