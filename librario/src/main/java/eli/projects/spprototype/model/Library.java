package eli.projects.spprototype.model;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.pdfbox.pdmodel.PDDocument;

import eli.projects.spprototype.App;
import eli.projects.spprototype.Part;
import eli.projects.spprototype.PartDesignation;
import eli.projects.spprototype.PartDesignation.InvalidPartDesignationException;
import eli.projects.spprototype.SimpleDocumentSource;
import eli.projects.spprototype.infrastructure.EnsembleService;
import eli.projects.spprototype.infrastructure.InMemoryEnsembleService;
import eli.projects.spprototype.infrastructure.InMemoryInstrumentService;
import eli.projects.spprototype.infrastructure.InMemoryListService;
import eli.projects.spprototype.infrastructure.InMemoryPieceService;
import eli.projects.spprototype.infrastructure.InstrumentService;
import eli.projects.spprototype.infrastructure.ListService;
import eli.projects.spprototype.infrastructure.PieceService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
	
	
	private PieceService pieceService;
	private ListService listService;
	private EnsembleService ensembleService;
	private InstrumentService instrumentService;
	
	private StringProperty title = new SimpleStringProperty("Untitled Library");
	
	
	public Library(PieceService pieceService, ListService listService, EnsembleService ensembleService,
			InstrumentService instrumentService) {
		super();
		this.pieceService = pieceService;
		this.listService = listService;
		this.ensembleService = ensembleService;
		this.instrumentService = instrumentService;
	}

	/** Pieces **/
	
	public PieceService getPieceService() {
		return this.pieceService;
	}

	public ObservableList<Piece> getPieces() {
		return this.pieceService.getItems();
	}

	/** Setlists **/
	
	public ObservableList<Setlist> getSetlists() {
		return this.listService.getItems();
	}
	
	public ListService getListService() {
		return this.listService;
	}
	
	/**
	 * Creates a new empty setlist
	 * @param name The name of the setlist
	 * @return The setlist that we add
	 */
	public Setlist addSetlist(String name) {
		Setlist out = new Setlist(name);
		this.listService.getItems().add(out);
		return out;
	}
	
	/** Ensembles **/
	
	public ObservableList<Ensemble> getEnsembles() {
		return this.ensembleService.getItems();
	}

	public EnsembleService getEnsembleService() {
		return this.ensembleService;
	}
	
	public final void addNewEnsemble() {
		this.ensembleService.getItems().add(new Ensemble("New Ensemble"));
	}
	
	public final void deleteEnsemble(Ensemble e) {
		this.ensembleService.deleteItem(e);
	}
	
	/** Title **/
	
	public String getTitle() {
		return this.title.get();
	}
	
	public StringProperty getTitleProperty() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title.set(title);
	}
	
	/** Instruments **/
	
	public ObservableList<Instrument> getInstruments() {
		return this.instrumentService.getItems();
	}
	
	public InstrumentService getInstrumentService() {
		return this.instrumentService;
	}

	
	
	
	/**
	 * Generates a library with fake testing data
	 * @return A library with made-up testing data
	 * @param limit The maximum number of pieces allowed in this testing library (-1 for no limit)
	 */
	public static Library generateTestingLibrary(int limit) {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		
		int j = 0;
		for (Piece p : Piece.getAllFakePieces()) {
			if (limit > 0 && ++j > limit) break;
			pieces.add(p);
			
		}
		
		ArrayList<Setlist> setlists = new ArrayList<Setlist>(1);

		for (String s : new String[] {"Fall Concert Program 2019", "Spring Concert Program 2020", "Fall Concert Program 2020"} ) {
			Setlist u = new Setlist(s);
			setlists.add(u);
			for (int i = 0; i < 4; i ++) {
				u.add(pieces.get(App.randomGenerator.nextInt(pieces.size())));
			}
		}

		
		ArrayList<Ensemble> ensembles = new ArrayList<Ensemble>(1);
		
		for (int i = 0; i < 5; i++) {
			ensembles.add(Ensemble.generateTestEnsemble());
		}
		
		return new Library(new InMemoryPieceService(pieces), new InMemoryListService(setlists), new InMemoryEnsembleService(ensembles), new InMemoryInstrumentService());
	}
	
	public static Library loadOldLibrary(File path) {
		
		
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
		
		Library output = new Library(
				new InMemoryPieceService(pieces),
				new InMemoryListService(setlists),
				new InMemoryEnsembleService(ensembles),
				new InMemoryInstrumentService()
			); 
		output.setTitle("BPB Library");
		return  output;
		
	}

}
