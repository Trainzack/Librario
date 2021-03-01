/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import eli.projects.spprototype.model.Instrument;

/**
 * @author Eli
 * 
 * This service keeps track of a list of instruments.
 * This is intended for use with the instruments that the library understands.
 *
 */
public interface InstrumentService extends ObservableListService<Instrument> {
	
}
