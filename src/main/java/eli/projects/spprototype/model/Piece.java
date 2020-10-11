package eli.projects.spprototype.model;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import eli.projects.spprototype.Part;
import eli.projects.spprototype.PartDesignation.InvalidPartDesignationException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
	
	private static final String[] SAMPLE_PIECE_NAMES = {"25 Miles", "All I do is Win ", "All Right Now", "Alma Mater, Cal Poly Pomona", "Angel is the Centerfold", "Apache", "Believer", "Better Now", "Cal Poly Pomona Fight Song", "Carry on Wayward Son", "Come Out and Play", "Crazy in Love", "Daft Punk Medley", "Dance to the Music", "Dream On", "Evil Ways", "Eye of the Tiger", "Final Countdown,The", "Flagpole Sitta", "Frankenstein", "Hand That Feeds, The", "Heartbreaker", "Hey! Baby!", "Higher Ground", "Holiday", "I'm Shipping up to Boston", "Impression That I Get, The", "In the Stone", "Land of a Thousand Dances", "Pretender, The", "Rage!", "Rock Lobster", "Doctor Who", "Get Down Tonight", "Hey Pachuco!", "Kids Aren't Alright, The", "Runaway Baby", "Sell Out", "Seven Nation Army", "Shout", "Shout it Out", "Stacy's Mom", "Star Spangled Banner", "Take On Me", "Uma Thurman", "William Tell Overture (Finale)", "You're Gonna Go Far Kid", "Word Up"};
	private static final String[] SAMPLE_COMPOSERS = {"Edwin Starr", "Christopher Bridges, Calvin Broadus, Johnny Mollings, Lenny Mollings & William Robers II", "Andy Fraser & Paul Rodgers", "Keith Weeks & Ron Evans", "Seth Justman", "Jerry Lordan", "Imagine Dragons", "Ed Roland & Dexter Green", "John Phillip Sousa", "Kerry Livgren", "Dexter Holland", "Beyoncé Knowles et. al.", "Daft Punk", "Sly Stone", "Steven Tyler", "Clarence Henry", "Frankie Sullivan & Jim Peterik", "Joey Tempest", "Sean Nelson, Jeff J. Lin, Aaron Huffman, Evan Sult", "Edgar Winter Group", "Trent Reznor", "Pat Benatar", "Margaret Cobb & Bruce Channel", "Stevie Wonder", "Billie Joe Armstrong, Mike Dirnt & Tré Cool", "Alexander Barr, Ken Casey, Woody Guthrie, & Matthew Kelly", "Dicky Barrett, Joe Gittleman", "Earth Wind & Fire", "Chris Kenner & Antonie Domino", "Dave Grohl, Taylor Hawkins, Nate Mendel & Chris Shiflett", "Rage Against the Machine", "Fred Schneider & Ricky Wilson", "Gary Glitter & Mike Leander", "Harry Watne Casey & Richard Finch", "Edwin Nichols, James Achor & David Dorame", "Dexter Holland", "Bruno Mars, Ari Levine, Philip Lawrence & Christopher Steven Brown", "Aaron Barrett & Scott Klopfenstein", "John Anthony White", "O'Kelly Isley, Jr, Rudolph Isley & Ronald Isley", "Dallas Austin, Jasper Cameron, Weldon Dean Parks, Hal Davis & Donald E. Fletcher", "Chris Collingwood & Adam Schlesinger", "Francis KeyScott & John Stafford Smith", "Magne Furuholmen, Morten Harket & Pål Waaktaar", "Andrew Hurley et. al.", "Gioachino Rossini", "Dexter Holland", "Larry Blackmon & Tomi Jenkins"};
	private static final String[] SAMPLE_ARRANGERS = {"Eli Zupke", "", "", "Eli Zupke", "Daniel Sandt", "Tim Waters", "Eli Zupke", "Tom Wallace & Tony McCutchen", "Daniel Sandt & Steven Corral", "Michael Sweeny", "Tony Fox", "Ralph Ford & Bryden Atwater", "Tom Wallace & Tony McCutchen", "Tony Fox", "Jay Bocook & Will Rapp", "Jay Dawson", "Charlie Harris", "John Higgins", "Daniel Sandt", "Tony Fox", "Tom Wallace & Tony McCutchen", "Tony Fox", "Tom Wallace", "Jay Dawson", "Jay Dawson", "Paul Murtha & Will Rapp", "Michael Sweeny", "Tony Fox & P. Brosché", "Tom Wallace & Tony McCutchen", "Tom Wallace & Tony McCutchen", "Jeremy Freer", "Michael Brown", "", "Vince Womack", "Tom Wallace & Tony McCutchen", "Tony Fox", "Tom Wallace & Tony McCutchen", "Jay Bocook", "Jay Dawson & Brian Mason", "", "Doug Adams", "Les Hicken & Jay Bocook", "John Philip Sousa", "Tim Waters", "Matt Conaway & Jack Holt", "Robert Longfield", "James Rodriguez", "Steven J. Corral"};
	private static final int[] SAMPLE_YEARS = {1969, 2010, 1970, 1968, 1981, 1960, 2017, 2004, 1918, 1976, 1994, 2003, 2014, 1968, 1973, 1969, 1982, 1986, 1998, 1972, 2005, 1979, 1961, 1973, 2005, 2005, 1997, 1965, 1962, 2007, 0, 1978, 1972, 1975, 1991, 1999, 2010, 1996, 2003, 1959, 2002, 2003, 1814, 1984, 2015, 1829, 2008, 1986};
	
	private static class PDFFileFilter implements FilenameFilter {

		public boolean accept(File dir, String name) {
			return name.endsWith(".pdf");
		}
		
	}

	// The name of the piece
	StringProperty compositionTitle = new SimpleStringProperty(this, "title");
	// The name of the composer(s)
	StringProperty composer = new SimpleStringProperty(this, "composer");
	// The name of the arranger(s)
	StringProperty arranger = new SimpleStringProperty(this, "arranger");
	// The estimated duration of the piece in seconds
	SimpleIntegerProperty year = new SimpleIntegerProperty(this, "year");
	// The estimated duration of the piece in seconds
	SimpleIntegerProperty duration = new SimpleIntegerProperty(this, "duration");
	
	
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
	
	private Part[] parts;

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

	public Piece(String compositionTitle, String composer, String arranger, int year,  int duration) {
		super();
		this.compositionTitle.set(compositionTitle);
		this.arranger.set(arranger);
		this.composer.set(composer);
		this.year.set(year);
		this.duration.set(duration);
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
				int year = SAMPLE_YEARS[i];
				int duration = r.nextInt(500) + 100;
				fake_pieces[i] = new Piece(name, composer, arranger, year, duration);
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
	
	public final IntegerProperty yearProperty() {
		return this.year;
	}
	
	public final IntegerProperty durationProperty() {
		return this.duration;
	}
	
	
	public String getTitle() {
		return this.compositionTitle.getValue();
	}


	@Override
	public String toString() {
		return this.getTitle();
	}
	
	
	
}
