package eli.projects.spprototype.model;

import java.util.Random;

public class Ensemble {

	private String name;
	
	private int numberOfMembers;

	// TODO: Currently this has only the minimum amount of info we need for the UI mockup.
	public Ensemble(String name, int numberOfMembers) {
		super();
		this.name = name;
		this.numberOfMembers = numberOfMembers;
	}
	
	/**
	 * Generates some random ensembles to test other code.
	 * 
	 * @param count The number of ensembles to generate
	 * @return An array containing all of the ensembles
	 */
	public static Ensemble generateTestEnsemble() {
		
		String[][] names = {
				{"Kazoo", "Trombone", "String", "Trumpet", "Cello", "Violin", "Clarinet", "Bassoon", "Pep", "Concert", "Symphonic", "Wind", "8AM", "Noon", "4PM", "6PM"},
				{"Ensemble", "Band", "Orchestra", "Quartet", "Choir"},
		};
		
		Random r = new Random();
		
		String name = names[0][r.nextInt(names[0].length)] + " " + names[1][r.nextInt(names[1].length)];
		int members = r.nextInt(130) + 20;
		Ensemble out = new Ensemble(name, members);
		
		return out;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfMembers() {
		return numberOfMembers;
	}

	public void setNumberOfMembers(int numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}
	
	public String toString() {
		return this.getName();
	}
	
}
