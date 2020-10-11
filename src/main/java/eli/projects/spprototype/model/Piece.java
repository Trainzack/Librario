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
	
	private static final String[] SAMPLE_PIECE_NAMES = {"25 Miles", "All I do is Win ", "All Right Now", "Angel is the Centerfold", "Apache", "Believer", "Better Now", "Cal Poly Pomona Fight Song", "Carry on Wayward Son", "Come Out and Play", "Crazy in Love", "Daft Punk Medley", "Dance to the Music", "Dream On", "Evil Ways", "Eye of the Tiger", "Flagpole Sitta", "Frankenstein", "Heartbreaker", "Hey! Baby!", "Higher Ground", "Holiday", "I'm Shipping up to Boston", "In the Stone", "Land of a Thousand Dances", "Rage!", "Rock Lobster", "Doctor Who", "Get Down Tonight", "Hey Pachuco!", "Runaway Baby", "Sell Out", "Seven Nation Army", "Shout", "Shout it Out", "Stacy's Mom", "Star Spangled Banner", "Take On Me", "Uma Thurman", "William Tell Overture (Finale)", "You're Gonna Go Far Kid", "Word Up", "Across Stars Warm Up", "Cadences", "Cal Chords Warm Up", "Crown Tuning Sequence", "Warm Ups", "Warmups", "Appalachian Spring Chorale", "20th Century Fox March", "25 or 6 to 4", "25 or 6 to 4", "A Pirate's Life For Me", "Africa", "Ain't No Other Man ", "Alfred Hitchcock", "All the Small Things", "Animal House", "Another One Bites the Dust", "Any Way You Want It", "Back in Black", "Band Cheers", "Bang Bang", "Bang the Drum All Day", "Barbara Ann", "Bend Me Shape Me", "Billie Jean", "Birdland", "Blasters from the Masters", "Blazing Saddles", "Blister in the Sun", "Born to be Wild", "Brick House", "Brick House", "Brooklyn", "Bumper to Bumper", "But It's Alright", "Can-Can", "Can't Hold Us", "Cantina Band", "Celebration", "Chameleon", "Children of Sanchez", "CHiPs Theme", "Come on Eileen", "Conga", "Crazy Train", "Cupid Shuffle", "Do Whatcha Wanna", "Don't Stop Believin", "Down in the Corner", "Dragnet", "Dude Looks Like A Lady", "Dynamite", "Brookyln", "Charges", "Enter Sandman", "Everybodys Everything", "Fantasy", "Feel Like Funkin' it Up", "Footloose", "Forget You", "Funkathustra", "Gangnam Style", "Get Ready for This", "Get Ready For This", "Gibbous", "Go Big Red", "Gonna Fly Now", "Gonna Fly Now", "Gonna Make You Sweat (Everybody Dance Now)", "Good Vibrations", "Gunsmoke March", "Hang On Sloopy", "Hang On Sloopy", "Happy", "Happy Birthday", "Harder Better Faster Stronger", "Hawaii Five 0 ", "Hip Hop Hooks", "Eli's Coming!", "Gimme Some Lovin'", "Get it On", "Hammerhead", "Hit Me With Your Best Shot", "House of the Rising Sun", "I Can't Turn You Loose", "I Don't Wanna Stop", "I Got You (I Feel Good)", "I Gotta Feeling", "I Just Can't Get Enough", "I Love Rock 'N Roll", "I Want You Back", "I'm a Believer", "I'm Too Sexy", "In the Middle", "In the Mood", "In the Mood", "In-A-Gadda-Da-Vida", "Iron Man", "It's a Small World", "It's Not Unusual", "James Bond Theme", "James Bond Theme", "James Brown Riffs", "Johnny B. Goode", "Joy to the World", "Jump", "Jumpin Jack Flash", "Kashmir", "Kernkraft 400", "Knock On Wood", "Kung Fu Fighting", "Let's Groove", "Light My Fire", "Livin' on a Prayer", "Locked Out of Heaven", "Louie Louie", "Louie Louie", "Low Rider", "Low Rider", "Low Rider", "Make Me Smile", "Mony Mony", "My Songs Know What You Did in the Dark (Light Em Up)", "Na Na Na Na Na Na Na Na Na", "Ode to Joy", "Olympic Fanfare and Theme", "On Broadway", "One Way Or Another (Teenage Kicks)", "Original Prankster", "Owner of a Lonely Heart", "Paradise City", "Monster", "Party in the USA", "Party Rock Anthem", "Peter Gunn", "Poker Face", "Power", "Pretty Fly for a White Guy", "Propane Nightmares", "Proud Many", "Pump It", "Push It", "Raise Your Glass", "Rawhide", "Rosanna", "Sanford & Son", "Saturday in the Park", "Savoy Truffle", "Scream and Shout", "September", "Shaft Theme", "Shaft Theme", "Shake it Off", "Sing a Song", "Sing a Song", "Smoke on the Water", "Smooth", "Yes", "Soak Up the Sun", "Soul Bossa Nova", "Spanish Flea", "Stadium Rock", "Stadium Rockers", "Star Spangled Banner", "Star Spangled Banner (Limited Edition)", "Star Wars", "Start Wearing Yellow", "Suffragette City", "Sunshine of Your Love", "Superman Theme", "Sweet Georgia Brown", "Sweet Georgia Brown (Globetrotter's Theme)", "Tear The Roof Off The Sucker (Give Up The Funk)", "Temptation", "Three Olympic Fanfares", "Three's Company", "Thrift Shop", "Thriller", "Ticket to Ride", "Tijuana Taxi", "Tongue Tied", "Tusk", "Talkin Out the Side of Your Neck", "Uptown Funk", "Superstar", "Vehicle", "Walking on Sunshine", "War", "Watermelon Man", "We're an American Band", "We're Not Gonna Take it", "What Now My Love", "Whip It", "Who Are You", "Wild Pack", "Wrecking Ball", "YMCA", "You Give Love a Bad Name", "You Oughta Know", "Zarathustra Fanfare", "Zip-a-dee-doo-dah", "What I've Done", "Can't Hold Us", "Can't Hold Us", "Funkytown", "Moskau", "Salvation is Created", "We Are Number One", "All I Want for Christmas is You", "Auld Lang Syne", "Carol of the Bells", "Jingle Bell Rock", "My Favorite Things", "The 12 Days of Christmas", "Winter Wonderland", "Handclap", "Hooked on a Feeling", "Irresistible", "Pompeii", "Sweet Caroline", "Katy Perry Closer (Dark Horse & Firework)", "Mr. Roboto", "Any Way You Want It", "25 or 6 to 4", "Conquistador", "Fame", "Let It Be Me", "Memory", "Na Na Hey Hey Kiss Him Goodbye", "Parada Del Sol", "Rock Around the Clock", "Goldfinger", "Sweet Dreams", "Walk This Way"};
	
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

	public Piece(String compositionTitle, int duration) {
		super();
		this.compositionTitle.set(compositionTitle);
		this.arranger.set("Tom Wallace");
		this.composer.set("Mozart");
		this.year.set(1997);
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
				int duration = r.nextInt(500) + 100;
				fake_pieces[i] = new Piece(name, duration);
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
