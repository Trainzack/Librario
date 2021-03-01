package eli.projects.spprototype.model;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public class Ensemble {

	private StringProperty name = new SimpleStringProperty();
	
	private SimpleIntegerProperty memberCount = new SimpleIntegerProperty();
	
	// This observablelist contains all of the sections in this ensemble
	private ObservableList<Section> sections;
	// This ObjectProperty contains the currently selected section
	private final ObjectProperty<Section> currentSection = new SimpleObjectProperty<Section>(null);


	public Ensemble(String name) {
		super();
		this.name.set(name);
		
		this.sections = FXCollections.observableArrayList(new Callback<Section, Observable[]>() {

			@Override
			public Observable[] call(Section s) {
				return new Observable[] {s.getCountProperty(), s.getInstrumentProperty()};
			}
		
			
		});
		
		// Update the Member Count property every time that we change the sections list.
		this.sections.addListener(new ListChangeListener<Section>() {

			@Override
			public void onChanged(Change<? extends Section> c) {
				memberCount.set(getNumberOfMembers());
			}
			
		});
		
	}
	
	
	public ObservableList<Section> getSections() {
		return sections;
	}
	
	public final ObjectProperty<Section> getCurrentSectionProperty() {
		return currentSection;
	}
	
	public final Section getCurrentSection() {
		return currentSection.get();
	}
	
	public final void setCurrentSection(Section section) {
		currentSection.set(section);
	}
	
	public void addSection(Section section) {
		sections.add(section);
	}
	
	public void deleteSection(Section section) {
		sections.remove(section);
	}

	
	/**
	 * Generates a random ensemble to test other code.
	 * 
	 * @return A single random ensemble
	 */
	public static Ensemble generateTestEnsemble() {
		
		String name = "Concert Band";
		Ensemble out = new Ensemble(name);
		
		for (Instrument i : Instrument.getInstruments()) {

			out.addSection(new Section(i, 2));
		}
		
		return out;
	}
	

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}
	
	public StringProperty getNameProperty() {
		return this.name;
	}

	public int getNumberOfMembers() {
		int count = 0;
		for (Section s : this.sections) count += s.getCount();
		return count;
	}
	
	public ReadOnlyIntegerProperty getMemberCountProperty() {
		return this.memberCount;
	}
	
	public String toString() {
		return this.getName();
	}
	
}
