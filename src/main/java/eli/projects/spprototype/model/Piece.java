package eli.projects.spprototype.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;

import eli.projects.spprototype.SimpleDocumentSource;
import eli.projects.spprototype.Part;
import eli.projects.spprototype.PartDesignation;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * This class represents one single piece of music.
 * 
 * It should contain within references to all of the associated PDFS
 * 
 * as well as any metadata (title, composers, key, etc.)
 * 
 * @author Eli
 *
 */
public class Piece {
	
	// This keeps track of IDs that we assign. This ID is *not* saved, but recreated in memory every time.
	// This ID may be different across different sessions.
	private static int cur_id = 0;
	
	// This keeps track of all pieces so that we can drag and drop them
	// I get the feeling that this is an unwise design decision.
	//	-- In particular, this means that pieces will never be garbage collected, even if we load a new library
	//  -- It may be smarter to put this in the library class.
	private static Dictionary<Integer, Piece> all_pieces = new Hashtable<>();
	
	private static final String[] SAMPLE_PIECE_NAMES_PEP = {"25 Miles", "All I do is Win ", "All Right Now", "Alma Mater, Cal Poly Pomona", "Angel is the Centerfold", "Apache", "Believer", "Better Now", "Cal Poly Pomona Fight Song", "Carry on Wayward Son", "Come Out and Play", "Crazy in Love", "Daft Punk Medley", "Dance to the Music", "Dream On", "Evil Ways", "Eye of the Tiger", "Final Countdown,The", "Flagpole Sitta", "Frankenstein", "Hand That Feeds, The", "Heartbreaker", "Hey! Baby!", "Higher Ground", "Holiday", "I'm Shipping up to Boston", "Impression That I Get, The", "In the Stone", "Land of a Thousand Dances", "Pretender, The", "Rage!", "Rock Lobster", "Doctor Who", "Get Down Tonight", "Hey Pachuco!", "Kids Aren't Alright, The", "Runaway Baby", "Sell Out", "Seven Nation Army", "Shout", "Shout it Out", "Stacy's Mom", "Star Spangled Banner", "Take On Me", "Uma Thurman", "William Tell Overture (Finale)", "You're Gonna Go Far Kid", "Word Up"};
	private static final String[] SAMPLE_COMPOSERS_PEP = {"Edwin Starr", "Christopher Bridges, Calvin Broadus, Johnny Mollings, Lenny Mollings & William Robers II", "Andy Fraser & Paul Rodgers", "Keith Weeks & Ron Evans", "Seth Justman", "Jerry Lordan", "Imagine Dragons", "Ed Roland & Dexter Green", "John Phillip Sousa", "Kerry Livgren", "Dexter Holland", "Beyoncé Knowles et. al.", "Daft Punk", "Sly Stone", "Steven Tyler", "Clarence Henry", "Frankie Sullivan & Jim Peterik", "Joey Tempest", "Sean Nelson, Jeff J. Lin, Aaron Huffman, Evan Sult", "Edgar Winter Group", "Trent Reznor", "Pat Benatar", "Margaret Cobb & Bruce Channel", "Stevie Wonder", "Billie Joe Armstrong, Mike Dirnt & Tré Cool", "Alexander Barr, Ken Casey, Woody Guthrie, & Matthew Kelly", "Dicky Barrett, Joe Gittleman", "Earth Wind & Fire", "Chris Kenner & Antonie Domino", "Dave Grohl, Taylor Hawkins, Nate Mendel & Chris Shiflett", "Rage Against the Machine", "Fred Schneider & Ricky Wilson", "Gary Glitter & Mike Leander", "Harry Watne Casey & Richard Finch", "Edwin Nichols, James Achor & David Dorame", "Dexter Holland", "Bruno Mars, Ari Levine, Philip Lawrence & Christopher Steven Brown", "Aaron Barrett & Scott Klopfenstein", "John Anthony White", "O'Kelly Isley, Jr, Rudolph Isley & Ronald Isley", "Dallas Austin, Jasper Cameron, Weldon Dean Parks, Hal Davis & Donald E. Fletcher", "Chris Collingwood & Adam Schlesinger", "Francis KeyScott & John Stafford Smith", "Magne Furuholmen, Morten Harket & Pål Waaktaar", "Andrew Hurley et. al.", "Gioachino Rossini", "Dexter Holland", "Larry Blackmon & Tomi Jenkins"};
	private static final String[] SAMPLE_ARRANGERS_PEP = {"Eli Zupke", "", "", "Eli Zupke", "Daniel Sandt", "Tim Waters", "Eli Zupke", "Tom Wallace & Tony McCutchen", "Daniel Sandt & Steven Corral", "Michael Sweeny", "Tony Fox", "Ralph Ford & Bryden Atwater", "Tom Wallace & Tony McCutchen", "Tony Fox", "Jay Bocook & Will Rapp", "Jay Dawson", "Charlie Harris", "John Higgins", "Daniel Sandt", "Tony Fox", "Tom Wallace & Tony McCutchen", "Tony Fox", "Tom Wallace", "Jay Dawson", "Jay Dawson", "Paul Murtha & Will Rapp", "Michael Sweeny", "Tony Fox & P. Brosché", "Tom Wallace & Tony McCutchen", "Tom Wallace & Tony McCutchen", "Jeremy Freer", "Michael Brown", "", "Vince Womack", "Tom Wallace & Tony McCutchen", "Tony Fox", "Tom Wallace & Tony McCutchen", "Jay Bocook", "Jay Dawson & Brian Mason", "", "Doug Adams", "Les Hicken & Jay Bocook", "John Philip Sousa", "Tim Waters", "Matt Conaway & Jack Holt", "Robert Longfield", "James Rodriguez", "Steven J. Corral"};
	private static final int[] SAMPLE_YEARS_PEP = {1969, 2010, 1970, 1968, 1981, 1960, 2017, 2004, 1918, 1976, 1994, 2003, 2014, 1968, 1973, 1969, 1982, 1986, 1998, 1972, 2005, 1979, 1961, 1973, 2005, 2005, 1997, 1965, 1962, 2007, 0, 1978, 1972, 1975, 1991, 1999, 2010, 1996, 2003, 1959, 2002, 2003, 1814, 1984, 2015, 1829, 2008, 1986};
	
	// The name of the piece
	StringProperty compositionTitle = new SimpleStringProperty(this, "title");
	// The name of the composer(s)
	StringProperty composer = new SimpleStringProperty(this, "composer");
	// The name of the arranger(s)
	StringProperty arranger = new SimpleStringProperty(this, "arranger");
	// The estimated duration of the piece in seconds
	StringProperty year = new SimpleStringProperty(this, "year");
	// The estimated duration of the piece in seconds
	SimpleIntegerProperty duration = new SimpleIntegerProperty(this, "duration");
	// The number of parts this piece has.
	SimpleIntegerProperty partCount = new SimpleIntegerProperty(this, "partCount");
	
	
	//TODO: Figure out what kind of metadata is desirable and how to store it here
	// Title
	// Composer
	// Year of composition
	// Catalog number
	// Language
	// Piece Style
	// Instrumentation
	// Number of Movements
	// Length of piece
	// Key
	
	private ObservableList<Part> parts;
	
	private int id;
	/**
	 * This constructor creates a piece from a folder full of PDF files (like the kind I used to make)
	 * 
	 * @param inputPath The path of the folder that contains all of the parts for this piece
	 *//*
	public Piece(String inputPath) {
		super();
		
		File directory = new File(inputPath);
		
		String[] paths = directory.list(new PDFFileFilter());
		
		//TODO: If a part fails, then this array won't get filled in correctly
		Part[] parts = new Part[paths.length];
		
		for (int i = 0; i < parts.length; i++) {
			try {
				parts[i] = new Part(this, paths[i]);
			} catch (InvalidPartDesignationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				parts[i] = null;
			}
		}
		
		
		
	}*/

	public Piece(String compositionTitle, String composer, String arranger, String year2,  int duration) {
		super();
		this.compositionTitle.set(compositionTitle);
		this.arranger.set(arranger);
		this.composer.set(composer);
		this.year.set(year2);
		this.duration.set(duration);
		
		this.parts = FXCollections.observableArrayList();
		
		// Ensure that every instantiated piece has a unique id, and is in the all_pieces map.
		this.id = Piece.cur_id++;
		Piece.all_pieces.put(this.id, this);
		
		// When we change the number of parts, update the part count property.
		this.parts.addListener((ListChangeListener<? super Part>)(Change) -> {
			this.partCount.set(parts.size());
		});
		
		
	}
	
	private static Piece[] fake_pieces;
	
	/**
	 * TODO: This is real rough
	 */
	public static Piece[] generateFakePieces(int count) {
		
		Random r = new Random();
		
		initFakePieces();
		
		Piece[] out = new Piece[count];
		
		for (int i = 0; i < count; i++) {
			out[i] = fake_pieces[r.nextInt(fake_pieces.length)];
		}
		
		return out;
	}
	
	public static Piece[] getAllFakePieces() {
		initFakePieces();
		return fake_pieces;
	}
	
	private static void initFakePieces() {

		Random r = new Random();
		
		if (fake_pieces == null) {
			fake_pieces = new Piece[SAMPLE_PIECE_NAMES_PEP.length];
			
			for (int i = 0; i < fake_pieces.length; i++) {
				String name = SAMPLE_PIECE_NAMES_PEP[i];
				String composer = SAMPLE_COMPOSERS_PEP[i];
				String arranger = SAMPLE_ARRANGERS_PEP[i];
				String year = "" + SAMPLE_YEARS_PEP[i];
				int duration = r.nextInt(800) + 100;
				Piece nextPiece = new Piece(name, composer, arranger, year, duration);
				fake_pieces[i] = nextPiece;
				
				for (Instrument inst : Instrument.getInstruments()) {
					// Create a random number of parts [0-3] for each instrument.
					for (int j = 0; j < r.nextInt(4); j++) {
						Part p = new Part(new PartDesignation(j + 1, inst), nextPiece);

						nextPiece.parts.add(p);
						
					}
				}
				
			}
		}
	}
	
	
	/*
	 * This is for UI javafx table workings.
	 */
	public final StringProperty titleProperty() {
		return this.compositionTitle;
	}
	
	public final StringProperty composerProperty() {
		return this.composer;
	}
	
	public final StringProperty arrangerProperty() {
		return this.arranger;
	}
	
	public final StringProperty yearProperty() {
		return this.year;
	}
	
	public final IntegerProperty durationProperty() {
		return this.duration;
	}
	
	public final ObservableList<Part> getParts() {
		return this.parts;
	}
	
	public final IntegerProperty partCountProperty() {
		return this.partCount;
	}
	
	public String getTitle() {
		return this.compositionTitle.getValue();
	}


	/**
	 * Returns an ID number that currently represents this piece.
	 * The ID numbers are not saved with the pieces, but instead are recreated each time.
	 * This number should not be saved or stored anywhere. 
	 * @return The ID number for this piece in this session.
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * Returns the piece with the given ID number. This is used for drag and drop.
	 * @param ID The ID number of the piece
	 * @return The piece
	 */
	public static Piece getPiece(int ID) {
		return Piece.all_pieces.get(ID);
	}
	
	@Override
	public String toString() {
		return this.getTitle();
	}
	
	
	
}
