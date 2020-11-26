package eli.projects.spprototype.controller;


import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

public class FloatMMSpinnerValueFactory extends SpinnerValueFactory<Float> {


	private float increment = 1.0f;
	
	
	public FloatMMSpinnerValueFactory() {
		super();
		this.setValue(0.0f);
		this.setConverter(new StringConverter<Float>() {
			
			@Override
			public String toString(Float object) {
				return object.toString() + " mm";
			}
			
			@Override
			public Float fromString(String string) {
				return Float.valueOf(("0" + string).replaceAll("[^\\d.]", ""));
			}
		});
	}

	@Override
	public void decrement(int steps) {
		if (!(this.getValue() == null) && this.getValue() > increment) {
			this.setValue(this.getValue() - increment);
		} else {
			this.setValue(0.0f);
		}
		
	}

	@Override
	public void increment(int steps) {
		if (this.getValue() == null) this.setValue(0.0f);
		this.setValue(this.getValue() + increment);
	}
	
}
