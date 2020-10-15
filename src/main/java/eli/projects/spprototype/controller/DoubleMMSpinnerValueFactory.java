package eli.projects.spprototype.controller;


import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

public class DoubleMMSpinnerValueFactory extends SpinnerValueFactory<Double> {


	private double increment = 1.0;
	
	
	
	public DoubleMMSpinnerValueFactory() {
		super();
		this.setValue(0.0);
		this.setConverter(new StringConverter<Double>() {
			
			@Override
			public String toString(Double object) {
				return object.toString() + " mm";
			}
			
			@Override
			public Double fromString(String string) {
				return Double.valueOf(("0" + string).replaceAll("[^\\d.]", ""));
			}
		});
	}

	@Override
	public void decrement(int steps) {
		if (!(this.getValue() == null) && this.getValue() > increment) {
			this.setValue(this.getValue() - increment);
		} else {
			this.setValue(0.0);
		}
		
	}

	@Override
	public void increment(int steps) {
		if (this.getValue() == null) this.setValue(0.0);
		this.setValue(this.getValue() + increment);
	}
	
}
