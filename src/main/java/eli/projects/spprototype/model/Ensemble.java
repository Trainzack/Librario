package eli.projects.spprototype.model;

import java.util.ArrayList;
import java.util.Random;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Ensemble {

	private String name;
	
	// This observablelist contains all of the sections in this ensemble
	private ObservableList<Section> sections;
	// This ObjectProperty contains the currently selected section
	private final ObjectProperty<Section> currentSection = new SimpleObjectProperty<Section>(null);
	
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

	// TODO: Currently this has only the minimum amount of info we need for the UI mockup.
	public Ensemble(String name) {
		super();
		this.name = name;
		
		ArrayList<Section> tempTestingList = new ArrayList<>();
		
		for (Instrument i : Instrument.getInstruments()) {

			tempTestingList.add(new Section(i, 2));
		}
		
		
		this.sections = FXCollections.observableArrayList(tempTestingList);
	}
	
	
	
	
	
	/**
	 * Generates some random ensembles to test other code.
	 * 
	 * @param count The number of ensembles to generate
	 * @return An array containing all of the ensembles
	 */
	public static Ensemble generateTestEnsemble() {
		
		Random r = new Random();
		
		String name = "Concert Band";
		Ensemble out = new Ensemble(name);
		
		return out;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfMembers() {
		int count = 0;
		for (Section s : this.sections) count += s.getCount();
		return count;
	}
	
	public String toString() {
		return this.getName();
	}
	
}
