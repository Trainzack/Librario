/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.management.AttributeList;

import eli.projects.spprototype.model.Ensemble;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

/**
 * @author Eli
 *
 */
public class InMemoryEnsembleService implements EnsembleService {
	

	private ObservableList<Ensemble> ensembles;
	
	
	/**
	 * Instantiates an empty in-memory service.
	 */
	public InMemoryEnsembleService() {
		// Add all the properties that would make classes observing this list want to update what they're doing.
		Callback<Ensemble, Observable[]> extractor = new Callback<Ensemble, Observable[]>() {

			@Override
			public Observable[] call(Ensemble e) {
				return new Observable[] {e.getNameProperty(), e.getMemberCountProperty()};
			}
			
		};
		
		ensembles = FXCollections.observableArrayList(extractor);
	}
	
	/**
	 * Instantiates an in-memory service containing the given ensembles.
	 * @param ensembles The ensembles to include in this service.
	 */
	public InMemoryEnsembleService(Collection<Ensemble> ensembles) {
		this();
		this.ensembles.addAll(FXCollections.observableArrayList(ensembles));
	}
	
	
	/**
	 * Instantiates a list with made up ensembles (not part of any library)
	 * @param ensembleCount The number of ensembles to invent
	 */
	public InMemoryEnsembleService(int ensembleCount) {
		this();
		
		ArrayList<Ensemble> fakeEnsembles = new ArrayList<Ensemble>();
		
		for (int i = 0; i < ensembleCount; i++) { 

			fakeEnsembles.add(Ensemble.generateTestEnsemble());
		}
		

		ensembles.addAll(fakeEnsembles);
		
	} 
	
	@Override
	public ObservableList<Ensemble> getItems() {
		
		return ensembles;
	}

	@Override
	public void deleteItem(Ensemble item) {
		ensembles.remove(item);
		
	}

	@Override
	public void deleteItems(Collection<Ensemble> items) {
		ensembles.removeAll(items);
		
	}
	

	@Override
	public void addItem(Ensemble item) {
		ensembles.add(item);
		
	}

	@Override
	public void addItems(Collection<Ensemble> items) {
		ensembles.addAll(items);
	}

	@Override
	public boolean save() {
		// We have no disk-representation, so we just fake this.
		return false;
	}



}
