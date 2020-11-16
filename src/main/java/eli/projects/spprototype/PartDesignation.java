package eli.projects.spprototype;

import eli.projects.spprototype.model.Instrument;
import eli.projects.spprototype.model.Instrument.InstrumentDoesNotExistException;

/**
 * A simple container class for all of the general information about a part.
 * 
 * Specifically, we want the instrument and the numeral.
 * 
 * Instances of this class are immutable.
 * 
 * @author Eli
 *
 */

public class PartDesignation {
	
	public static class InvalidPartDesignationException extends Exception {

		private static final long serialVersionUID = 5102791532886168435L;

		public InvalidPartDesignationException() {
			super();
			// TODO Auto-generated constructor stub
		}

		public InvalidPartDesignationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
			super(arg0, arg1, arg2, arg3);
			// TODO Auto-generated constructor stub
		}

		public InvalidPartDesignationException(String arg0, Throwable arg1) {
			super(arg0, arg1);
			// TODO Auto-generated constructor stub
		}

		public InvalidPartDesignationException(String arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}

		public InvalidPartDesignationException(Throwable cause) {
			super(cause);
			// TODO Auto-generated constructor stub
		}

	}

	// The '1' part of 'Trombone 1'
	private int numeral;
	
	// The 'Trombone' part of 'Trombone 1'
	private Instrument instrument;

	public int getNumeral() {
		return numeral;
	}

	public PartDesignation(int numeral, Instrument instrument) {
		super();
		this.numeral = numeral;
		this.instrument = instrument;
	}
	
	public static PartDesignation ParseDesignation(String designation) throws InvalidPartDesignationException {
		String[] elements = designation.split("_");
		
		try {
			Instrument i = Instrument.GetInstrument(elements[0]);
			
			int n = 0;
			
			if (elements.length > 1) { //TODO: This method of parsing filenames is very fallible.
				// However, we should replace our entire file format solution before fixing this.
				try {
					n = Integer.parseInt(elements[1]);
				} catch (NumberFormatException e) {}
			}
			
			return new PartDesignation(n, i);
			
		} catch (InstrumentDoesNotExistException e) {
			throw new InvalidPartDesignationException("Invalid instrument '" + elements[0] + "'");
		}
	}
	
	public Instrument getInstrument() {
		return instrument;
	}
	
	@Override
	public String toString() {
		return this.getInstrument().getName() + " " + this.getNumeral();
	}
	
	
	
}
