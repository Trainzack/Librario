package eli.projects.spprototype.infrastructure;

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
	public void deleteItems(List<Instrument> items) {
		instruments.removeAll(items);

	}

	@Override
	public boolean save() {
		return false;
	}

}
