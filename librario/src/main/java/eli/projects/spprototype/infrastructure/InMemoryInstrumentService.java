package eli.projects.spprototype.infrastructure;

import java.util.Collection;
import java.util.List;

import eli.projects.spprototype.model.Instrument;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InMemoryInstrumentService implements InstrumentService {

	private ObservableList<Instrument> instruments = FXCollections.observableArrayList(Instrument.getInstruments());
	
	@Override
	public ObservableList<Instrument> getItems() {
		return instruments;
	}

	@Override
	public void deleteItem(Instrument item) {
		instruments.remove(item);

	}

	@Override
	public void deleteItems(Collection<Instrument> items) {
		instruments.removeAll(items);

	}

	@Override
	public void addItem(Instrument item) {
		instruments.add(item);
		
	}

	@Override
	public void addItems(Collection<Instrument> items) {
		instruments.addAll(items);
		
	}

	@Override
	public boolean save() {
		return false;
	}

}
