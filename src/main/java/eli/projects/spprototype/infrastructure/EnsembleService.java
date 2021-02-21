/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import eli.projects.spprototype.model.Ensemble;

/**
 * @author Eli
 * 
 * This service keeps track of a list of ensembles.
 * This is intended for use with the ensembles stored in the library.
 *
 */
public interface EnsembleService extends ObservableListService<Ensemble> {
	
}
