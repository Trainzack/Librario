/**
 * 
 */
package eli.projects.spprototype.infrastructure;

import java.util.ArrayList;
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
	
	/*
	 * Instantiates a list with made up pieces (not part of any library)
	 */
	public InMemoryEnsembleService(int ensembleCount) {
		
		ArrayList<Ensemble> fakeEnsembles = new ArrayList<Ensemble>();
		
		for (int i = 0; i < ensembleCount; i++) { 

			fakeEnsembles.add(Ensemble.generateTestEnsemble());
		}
		
		// Add all the properties that would make classes observing this list want to update what they're doing.
		Callback<Ensemble, Observable[]> extractor = new Callback<Ensemble, Observable[]>() {

			@Override
			public Observable[] call(Ensemble e) {
				return new Observable[] {e.getNameProperty(), e.getMemberCountProperty()};
			}
			
		};
		
		ensembles = FXCollections.observableArrayList(extractor);
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
	public void deleteItems(List<Ensemble> items) {
		ensembles.removeAll(items);
		
	}

	@Override
	public boolean save() {
		// We have no disk-representation, so we just fake this.
		return false;
	}

}
