package eli.projects.spprototype.model;

import java.util.ArrayList;

/**
 * This is a quick and dirty representation of particular types of intstruments.
 * 
 * We copy directly from the Instruments.txt file I wrote.
 * 
 * TODO make this more robust, use a better file format
 * @author Eli
 *
 */

public class Instrument {
	
	public static class InstrumentDoesNotExistException extends Exception {

		private static final long serialVersionUID = 1L;
		
	}

	// This is the list of all instruments
	public static ArrayList<Instrument> instruments;
	
	// The id is the internal shorthand name.
	// TODO this is archaic and should be updated with the rest of this system.
	private String id;
	
	// The name is the display name
	private String name;
	
	// This is the list of other instrument IDs that this instrument can reasonably read
	// If the original part is not available, we use one of these instead, with the order of the array indicating priority
	// TODO this is archaic and should be updated with the rest of this system.
	private String[] suitableSubstitutions;
	
	// What category this instrument belongs in
	// TODO this should probably be its own object
	private String category;

	
	public Instrument(String id, String name, String[] suitableSubstitutions, String category) {
		super();
		this.name = name;
		this.id = id;
		this.suitableSubstitutions = suitableSubstitutions;
		this.category = category;
		
		if (instruments == null) {
			instruments = new ArrayList<Instrument>();
		}
		instruments.add(this);
	}
	
	/**
	 * Whether the instruments have already been initiated
	 */
	private static boolean instruments_init = false;
	
	/**
	 * We're doing a bad thing here and hardcoding all of our instrument definitions.
	 * But, that's a problem for FUTURE ELI to solve.
	 * TODO Read this from an external file (maybe use the same xml standard as MuseScore?)
	 */
	public static void initInstruments() {
		
		if (instruments_init) return;
		
		new Instrument("MELODICA", 	"Melodica", new String[]{"BELLS", "FLT", "PIC", "KEYBOARD", "OBOE"}, "Woodwind");
		new Instrument("FLT", 		"Flute", new String[]{"PIC", "MELODICA", "BELLS", "KEYBOARD", "OBOE"}, "Woodwind");
		new Instrument("CL", 		"Clarinet", new String[]{"TPT", "SOPSAX", "TENSAX"}, "Woodwind");
		new Instrument("ALTSAX", 	"Alto Sax", new String[]{"TBN", "BARSAX"}, "Woodwind");
		new Instrument("TENSAX", 	"Tenor Sax", new String[]{"BARTC", "BBHRN", "CL", "TPT"}, "Woodwind");
		new Instrument("BASCL", 	"Bass Clarinet", new String[]{"TENSAX", "BARTC", "CL", "TPT"}, "Woodwind");
		new Instrument("BARSAX", 	"Bari Sax", new String[]{"TUBA", "BASS", "TBN"}, "Woodwind");
		
		new Instrument("TPT", 		"Trumpet", new String[]{"CL", "BBHRN", "TENSAX"}, "Brass");
		new Instrument("HRN", 		"F Horn", new String[]{"HORN", "EBHRN", "EBCL"}, "Brass");
		new Instrument("BBHRN", 	"Bb Horn", new String[]{"TENSAX", "TPT"}, "Brass");
		new Instrument("TBN", 		"Trombone", new String[]{"BARBC", "BASSOON", "ALTSAX", "BARSAX"}, "Brass");
		new Instrument("BARTC", 	"Baritone (TC)", new String[]{"TENSAX", "BBHRN", "BASCL", "TPT"}, "Brass" );
		new Instrument("BARBC", 	"Baritone (BC)", new String[]{"TBN", "BASSOON"}, "Brass" );
		new Instrument("TUBA", 		"Tuba", new String[]{"BARSAX", "BASS", "TBN"},  "Brass");
		
		new Instrument("DMST", 		"Drumset", new String[]{"DRUMS", "TBN"}, "Percussion");
		new Instrument("BASDM", 	"Bass Drums", new String[]{"DRUMS", "TENDM", "DMST", "BASS", "TUBA", "BARSAX", "TBN"}, "Percussion");
		new Instrument("TENDM", 	"Tenor Drums", new String[]{"DRUMS", "TRITOM", "TENORDM", "SNARE", "DMST", "TBN"}, "Percussion");
		new Instrument("CYM", 		"Cymbals", new String[]{"DRUMS", "DMST", "SNARE", "TBN"}, "Percussion");
		new Instrument("PERC", 		"Auxiliary Percussion", new String[] {}, "Percussion");
		new Instrument("SNARE", 	"Snare", new String[]{"DRUMS", "TENDM", "DMST", "TBN"}, "Percussion" );

		new Instrument("BELLS", 	"Bells", new String[]{"FLT"}, "Woodwind");
		new Instrument("BASS", 		"Bass", new String[]{"TUBA", "BARSAX", "TBN"},  "Brass");
		
		
		instruments_init = true;
		
	}
	
	/**
	 * Return the instrument with the given ID, or throw an exception if no such instrument exists
	 * @param ID The ID of the instrument we want to find, of course
	 * @return The instrument that has that ID
	 * @throws InstrumentDoesNotExistException 
	 */
	public static Instrument GetInstrument(String ID) throws InstrumentDoesNotExistException {
		
		initInstruments();
		
		for (Instrument i : instruments) {
			if (i.getId().equals(ID)) return i;
		}
		
		throw new InstrumentDoesNotExistException();
	}
	

	public static ArrayList<Instrument> getInstruments() {
		initInstruments();
		
		return instruments;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String[] getSuitableSubstitutions() {
		return suitableSubstitutions;
	}

	public String getCategory() {
		return category;
	}
	
	@Override
	public String toString() {
		return getName();
		//return getId() + ":\t" + getName() + "\t" + getCategory(); 
	}
	
}
