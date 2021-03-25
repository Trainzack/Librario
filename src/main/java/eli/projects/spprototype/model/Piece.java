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
	private static final String[] SAMPLE_PIECE_NAMES = {"Emblems", "Variations on a Shaker Melody", "Dream", "Repercussions", "Armenian Dances (Part I)", "Armenian Dances (Part II)", "Russian Christmas Music", "Hymn Variants (based on Lasst uns erfreuen [1623])", "Blue and White Dance", "Athenian Festival", "Serenade in D Minor", "Sinfonia Drammatica", "Theme and Variations, Op. 43a", "Suite in D Major, Op. 29", "Kappa Kappa Psi March", "Glass Menagerie", "Home Again", "Orient et Occident, Op. 25", "The Universal Judgement", "Riverwalk", "Symphony No. 1 for Band", "Affirmation and Credo", "Symphonic Suite", "Fanfare and Allegro", "Symphonic Dance No. 3: Fiesta", "Symphonic Essays", "Variations on a Maine Theme", "Desert Winds", "Unusual Behavior in Ceremonies Involving Drums", "Colors Aloft", "Suite Française", "Mansions of Glory", "In The Spring, at the Time When Kings Go Off to War", "Symphonic Canticle", "A Child's Garden of Dreams", "Symphony No. 4 (West Point)", "In Memoriam", "A Tuning Piece: Songs of Fall and Winter", "Traveler", "Purple Heart", "Dark Dreams of a Circus Bandstand", "Ballet for Band", "Cloudless Day, Bitter Sky", "Come, memory ...", "Tau Beta Sigma March", "Celestial Dancers", "A Little Learning is a Dangerous Thing", "Cloudburst", "Equus", "Ghost Train Triptych", "Godzilla Eats Las Vegas!", "October", "Prelude and Double Fugue", "Blue Shades", "Vesuvius ", "Angels in The Architecture", "Nitro", "An American Elegy", "Gaian Visions", "An American Elegy", "A Fraternal Prelude", "Loud Sunsets", "Scherzo", "Carolina Fantasy", "An Original Suite", "William Byrd Suite", "Study in Textures", "Hammersmith: Prelude and Scherzo, Op. 52", "First Suite in E-flat Major, Op. 28/1", "Second Suite in F Major, Op. 28/2", "La Fiesta Mexicana", "Ferris Fantasy", "Grande symphonie funèbre et triomphale, Op. 15", "Americans We", "The Footlifter", "His Honor", "Chorale and Alleluia", "Concerto for Piano and Wind Instruments", "Symphonies of Wind Instruments", "Saxophone Concerto", "Sinfonietta", "Kappa Kappa Psi March", "Movements for Wind Ensemble", "Gavorkna Fanfare", "Four Maryland Songs", "Bandancing", "Century Tower", "Chorale Prelude on a German Folk Tune", "Pagan Dances", "Variants on an Ancient Air", "Daystar: Symphonic Variations for Wind and Percussion", "Concerto for Piano and Wind Ensemble", "Neologue", "Chronolog", "Cantique and Festival", "A Gathering of Angels", "From the Mountains", "The Imperceptible Voices Cloaked in Wind", "Block M", "Overture Alfresco", "Fugue with Drums", "Elegy", "Incantation and Dance", "Symphony No. 2", "Variations on a Korean Folk Song", "Sinfonia for Winds and Percussion", "Infinite Horizons", "(Redacted)", "Semper Fidelis", "Stars and Stripes Forever", "The Washington Post", "The Thunderer", "But God's Own Descent", "Dance Variations", "Rondo Cappricio", "Rondo Jubiloso", "The Gates of the Wonder-World Open", "The March of Kappa Kappa Psi", "And the mountains rising nowhere", "Concerto for Piano and Wind Orchestra: Solar Traveller", "Space Symphony", "Culloden", "Hands of Mercy", "Husaria Cavalry Overture", "HardDrive", "Of Blood and Stone", "The Florentiner March", "Music for Prague", "Apotheosis of This Earth", "Concerto for Trumpet and Wind Orchestra", "Into the Solitude", "Masque", "A Century of Opportunity Celebration", "Two American Canvases", "Let Us Now Praise Famous Men", "LUX: Legend of Sankta Lucia", "Cycles of Moons and Tides", "Niagara Falls", "Dance Scene", "Back to Old Fairview", "King Ubu", "Dakota", "American Salute", "Symphony No. 4 (West Point)", "Fantasia for Band", "Songs of Abelard", "Always We Begin Again", "Prelude and Dance", "Symphony in B-flat", "Jacob's Ladder", "Fanatic Fanfare", "Irish Tune from County Derry", "Lincolnshire Posy", "Country Gardens", "Canzona", "Morning Song", "The Seasons", "Catcher of Shadows", "English Folk Song Suite", "Flourish for Wind Band", "Toccata Marziale", "Sea Songs", "High Adventure", "Caccia", "The Two Rivers", "National Intercollegiate Band March", "Sonatina No 1 in F major (Aus der Werkstatt eines Invaliden)", "Reflections On An Old Hymn Tune", "Fuse", "Crest of Allegiance", "Overture: Memory of a Friend", "Stars and Stripes Variations", "Propagula", "Suite of Old American Dances", "Symphonic Songs for Band", "Symphonic Songs for Band", "Danse Celestiale", "To the Summit! (Strive for the Highest)", "Seven Deadly Sins", "Chamarita!", "From This Wilderness...", "Rocky Point Holiday", "Commando March", "Dances of Nahawand", "Vigor", "Ancient Irish Hymn", "March in B-flat Major, Op. 99", "Variations and Fugue on How Firm a Foundation", "Ebullience", "Revenge of the Darkseekers!", "Hajj", "Idyll and Whirlwind", "Sinfonia XV \"Ursa Major\"", "Mourning Dances", "Capitan Majesty", "Symphonic Requiem", "Divertimento, Op. 42", "Psalm for Band, Op. 53", "Symphony No. 6, Op. 69", "Masque", "Divergents", "The Seventh Seal", "Caccia for Band", "Tunbridge Fair", "Dilemmae", "George Washington Bridge", "Someday", "The Gold Bug", "Play!", "Balkanya", "Symphony in 3 Scenes", "At the Crossroads", "Le Nozze di Figaro", "Sells-Floto Triumphal", "The River", "Prelude and Fugue in D minor, BWV 539", "Sinfonia in F major", "Suíte popular", "Trittico Botticelliano", "Modulationes variae", "Symphony in D major"};
	private static final String[] SAMPLE_COMPOSERS_PEP = {"Edwin Starr", "Christopher Bridges, Calvin Broadus, Johnny Mollings, Lenny Mollings & William Robers II", "Andy Fraser & Paul Rodgers", "Keith Weeks & Ron Evans", "Seth Justman", "Jerry Lordan", "Imagine Dragons", "Ed Roland & Dexter Green", "John Phillip Sousa", "Kerry Livgren", "Dexter Holland", "Beyoncé Knowles et. al.", "Daft Punk", "Sly Stone", "Steven Tyler", "Clarence Henry", "Frankie Sullivan & Jim Peterik", "Joey Tempest", "Sean Nelson, Jeff J. Lin, Aaron Huffman, Evan Sult", "Edgar Winter Group", "Trent Reznor", "Pat Benatar", "Margaret Cobb & Bruce Channel", "Stevie Wonder", "Billie Joe Armstrong, Mike Dirnt & Tré Cool", "Alexander Barr, Ken Casey, Woody Guthrie, & Matthew Kelly", "Dicky Barrett, Joe Gittleman", "Earth Wind & Fire", "Chris Kenner & Antonie Domino", "Dave Grohl, Taylor Hawkins, Nate Mendel & Chris Shiflett", "Rage Against the Machine", "Fred Schneider & Ricky Wilson", "Gary Glitter & Mike Leander", "Harry Watne Casey & Richard Finch", "Edwin Nichols, James Achor & David Dorame", "Dexter Holland", "Bruno Mars, Ari Levine, Philip Lawrence & Christopher Steven Brown", "Aaron Barrett & Scott Klopfenstein", "John Anthony White", "O'Kelly Isley, Jr, Rudolph Isley & Ronald Isley", "Dallas Austin, Jasper Cameron, Weldon Dean Parks, Hal Davis & Donald E. Fletcher", "Chris Collingwood & Adam Schlesinger", "Francis KeyScott & John Stafford Smith", "Magne Furuholmen, Morten Harket & Pål Waaktaar", "Andrew Hurley et. al.", "Gioachino Rossini", "Dexter Holland", "Larry Blackmon & Tomi Jenkins"};
	private static final String[] SAMPLE_COMPOSERS = {"Aaron Copland", "Aaron Copland", "Adam F. Brennan", "Adam Gorb", "Alfred Reed", "Alfred Reed", "Alfred Reed", "Alfred Reed", "Andrew Boysen Jr.", "Anne McGinty", "Antonín Dvořák", "Arnold Franchetti", "Arnold Schoenberg", "Arthur Bird", "Bohumil Makovsky", "Brant Karrick", "Brian Biddle", "Camille Saint-Saëns", "Camillo de Nardis", "Carl Johnson", "Claude T. Smith", "Claude T. Smith", "Clifton Williams", "Clifton Williams", "Clifton Williams", "Clifton Williams", "Craig Skeffington", "Cynthia Folio", "Daniel Bukvich", "Daniel Godfrey", "Darius Milhaud", "David Gillingham", "David Holsinger", "David Holsinger", "David Maslanka", "David Maslanka", "David Maslanka", "David Maslanka", "David Maslanka", "David Shaffer", "David Williams", "Don Gillis", "Donald Grantham", "Donald Grantham", "Donald I. Moore", "Eric Ewazen", "Eric Honour", "Eric Whitacre", "Eric Whitacre", "Eric Whitacre", "Eric Whitacre", "Eric Whitacre", "Fisher Tull", "Frank Ticheli", "Frank Ticheli", "Frank Ticheli", "Frank Ticheli", "Frank Ticheli", "Frank Ticheli", "Frank Ticheli", "Gary Powell Nash", "George Lam", "Gioachino Rossini", "Gordon \"Dick\" Goodwin", "Gordon Jacob", "Gordon Jacob", "Gunther Schuller", "Gustav Holst", "Gustav Holst", "Gustav Holst", "H. Owen Reed", "Harry Dempsey", "Hector Berlioz", "Henry Fillmore", "Henry Fillmore", "Henry Fillmore", "Howard Hanson", "Igor Stravinsky", "Igor Stravinsky", "Ingolf Dahl", "Ingolf Dahl", "J. DeForest Cline", "Jack Hughes", "Jack Stamp", "Jack Stamp", "Jack Stamp", "James Barnes", "James Barnes", "James Barnes", "James Curnow", "James Curnow", "James Woodward", "Jared Spears", "Jared Spears", "Jared Spears", "Jared Spears", "Jay Chattaway", "Jerome P. Miskell", "Jerry Bilik", "Jerry Bilik", "Joe Nelson", "John Barnes Chance", "John Barnes Chance", "John Barnes Chance", "John Barnes Chance", "John Boda", "John Cheetham", "John Mackey", "John P. Sousa", "John P. Sousa", "John P. Sousa", "John P. Sousa", "John White", "John Zdechlik", "John Zdechlik", "John Zdechlik", "Joseph C. Phillips", "Joseph O. DeLuca", "Joseph Schwantner", "Judith Lang Zaimont", "Julie Giroux", "Julie Giroux", "Julie Giroux", "Julie Giroux", "Julie Giroux", "Julie Giroux", "Julius Fučík", "Karel Husa", "Karel Husa", "Karel Husa", "Katherine Murdock", "Kenneth Hesketh", "Lester Pack", "Mark Camphouse", "Martin Mailman", "Mary Jeanne van Appledorn", "Mary Jeanne van Appledorn", "Michael Daugherty", "Michael Hennagin", "Michael Leckrone", "Michael Schelle", "MJ Cotton", "Morton Gould", "Morton Gould", "Nolan Schmit", "Norman Dello Joio", "Patrick Burns", "Paul Creston", "Paul Hindemith", "Paul Richards", "Paul Richards", "Percy Grainger", "Percy Grainger", "Percy Grainger", "Peter Mennin", "Philip Sparke", "Philip Sparke", "Philip Wilby", "Ralph Vaughan Williams", "Ralph Vaughan Williams", "Ralph Vaughan Williams", "Ralph Vaughan Williams", "Randol Bass", "Reber Clark", "Richard D. Hall", "Richard Franko Goldman", "Richard Strauss", "Richard Willis", "Rob Smith", "Robert Foster", "Robert Jager", "Robert Jager", "Robert Linn", "Robert Russell Bennett", "Robert Russell Bennett", "Robert Russell Bennett", "Robert Sheldon", "Robert W. Smith", "Robert Xavier Rodriguez", "Roger Nixon", "Roland Barrett", "Ron Nelson", "Samuel Baber", "Scott Meister", "Sean O'Loughlin", "Sean O'Loughlin", "Sergei Prokofiev", "Stephen Emmons", "Stephen Lias", "Stephen Melillo", "Stephen Melillo", "Steven Bryant", "Timothy Broege", "Timothy Mahr", "Timothy Rhea", "Václav Nelhýbel", "Vincent Persichetti", "Vincent Persichetti", "Vincent Persichetti", "W. Francis McBeth", "W. Francis McBeth", "W. Francis McBeth", "W. Francis McBeth", "Walter Piston", "William Latham", "William Schuman", "Zachary J. Friedland", "Victor Herbert", "Carl Holmquist", "Jan Van der Roost", "Yasuhide Ito", "Robert W Smith", "Wolfgang Amadeus Mozart", "Karl King", "Stephen Hill", "Mordechai Rechtman", "Christoph Graupner", "José Staneck", "Ottorino Respighi", "Ole Olsen", "Adolph Carl Kunzen"};
	private static final String[] SAMPLE_ARRANGERS_PEP = {"Eli Zupke", "", "", "Eli Zupke", "Daniel Sandt", "Tim Waters", "Eli Zupke", "Tom Wallace & Tony McCutchen", "Daniel Sandt & Steven Corral", "Michael Sweeny", "Tony Fox", "Ralph Ford & Bryden Atwater", "Tom Wallace & Tony McCutchen", "Tony Fox", "Jay Bocook & Will Rapp", "Jay Dawson", "Charlie Harris", "John Higgins", "Daniel Sandt", "Tony Fox", "Tom Wallace & Tony McCutchen", "Tony Fox", "Tom Wallace", "Jay Dawson", "Jay Dawson", "Paul Murtha & Will Rapp", "Michael Sweeny", "Tony Fox & P. Brosché", "Tom Wallace & Tony McCutchen", "Tom Wallace & Tony McCutchen", "Jeremy Freer", "Michael Brown", "", "Vince Womack", "Tom Wallace & Tony McCutchen", "Tony Fox", "Tom Wallace & Tony McCutchen", "Jay Bocook", "Jay Dawson & Brian Mason", "", "Doug Adams", "Les Hicken & Jay Bocook", "John Philip Sousa", "Tim Waters", "Matt Conaway & Jack Holt", "Robert Longfield", "James Rodriguez", "Steven J. Corral"};
	private static final String[] SAMPLE_ARRANGERS = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "Katherine Peter", "Andrew Glover", "", "", "Bob Boblin", "Bob Boblin", "Bob Boblin", "Bob Boblin", "Bob Boblin"};
	private static final int[] SAMPLE_YEARS_PEP = {1969, 2010, 1970, 1968, 1981, 1960, 2017, 2004, 1918, 1976, 1994, 2003, 2014, 1968, 1973, 1969, 1982, 1986, 1998, 1972, 2005, 1979, 1961, 1973, 2005, 2005, 1997, 1965, 1962, 2007, 0, 1978, 1972, 1975, 1991, 1999, 2010, 1996, 2003, 1959, 2002, 2003, 1814, 1984, 2015, 1829, 2008, 1986};
	private static final String[] SAMPLE_YEARS = {"1964", "1958", "2007", "2011", "1972", "1976", "1944", "1991", "2012", "1989", "1878", "1985", "1943", "1889", "1934", "2010", "2006", "1869", "1878", "1989", "1977", "1978", "1957", "1956", "1967", "1963", "2007", "1997", "1999", "2003", "1944", "2007", "1988", "1989", "1981", "1993", "1989", "1995", "2003", "2007", "1998", "1953", "2002", "2002", "1971", "2007", "2009", "2002", "2000", "1994", "1996", "2000", "1979", "1996", "1997", "2009", "2006", "2000", "1991", "2000", "1997", "2008", "1863", "2010", "1928", "1923", "1967", "1930", "1909", "1911", "1949", "1982", "1840", "1929", "1935", "1933", "1954", "1924", "1947", "1948", "1961", "1931", "2011", "1991", "2000", "2003", "1983", "1985", "1987", "1987", "1993", "2008", "1970", "1973", "1981", "1996", "2001", "2001", "1955", "1987", "2008", "1972", "1960", "1972", "1966", "1985", "1991", "2013", "1888", "1896", "1889", "1889", "1992", "1976", "1979", "1997", "2009", "1933", "1977", "2009", "1991", "1999", "2001", "2006", "2010", "2015", "1907", "1968", "1971", "1973", "2002", "1987", "2009", "2009", "1975", "1981", "1995", "1997", "1979", "2010", "1980", "2002", "1943", "1952", "2005", "1969", "2010", "1959", "1951", "2002", "2005", "1918", "1937", "1928", "1951", "2003", "2005", "1988", "1923", "1939", "1924", "1923", "2002", "2003", "2013", "1961", "1943", "1976", "2008", "1992", "1973", "1983", "1970", "1949", "1957", "1957", "1989", "2001", "1984", "1981", "1999", "1966", "1943", "2002", "2003", "2006", "1944", "2007", "2003", "1997", "2000", "2013", "1987", "2001", "1997", "1965", "1950", "1953", "1956", "1968", "1969", "1971", "1980", "1950", "1973", "1950", "2011", "1896", "2008", "1998", "1998", "1994", "1786", "1914", "2018", "1722", "1770", "1929", "1927", "1909", ""};

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
			fake_pieces = new Piece[SAMPLE_PIECE_NAMES.length];
			
			for (int i = 0; i < fake_pieces.length; i++) {
				String name = SAMPLE_PIECE_NAMES[i];
				String composer = SAMPLE_COMPOSERS[i];
				String arranger = SAMPLE_ARRANGERS[i];
				String year = SAMPLE_YEARS[i];
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
