package eli.projects.spprototype.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents one section of a particular ensemble. The section is assumed to have a certain number of one instrument.
 * @author Eli
 *
 */
public class Section {
	

	// This ObjectProperty contains the instrument that this section is made of.
	private final ObjectProperty<Instrument> instrument = new SimpleObjectProperty<Instrument>(null);
	
	public final ObjectProperty<Instrument> getInstrumentProperty() {
		return instrument;
	}
	
	public final Instrument getInstrument() {
		return instrument.get();
	}
	
	public final void setInstrument(Instrument instrument) {
		this.instrument.set(instrument);
	}
	
	// This ObjectProperty contains the instrument that this section is made of.
	private final IntegerProperty count = new SimpleIntegerProperty();
	
	public final IntegerProperty getCountProperty() {
		return count;
	}
	
	public final int getCount() {
		return count.get();
	}
	
	public final void setCount(int count) {
		this.count.set(count);
	}

	public Section(Instrument instrument, int count) {
		super();

		this.instrument.set(instrument);
		this.count.set(count);
		
	}
	

	public final StringProperty instrumentNameProperty() {
		return new SimpleStringProperty(this.instrument.get().getName());
	}
	
	

}
