package eli.projects.spprototype.controller;

import eli.projects.spprototype.model.PaperSettings;
import eli.projects.spprototype.model.PaperSize;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;



public class PageSizeController {
	
	private PaperSettings paperSettings;

	@FXML
	private ComboBox<PaperSize> paperSizeCombo;
	
	@FXML
	private Spinner<Float> widthSpinner;
	@FXML
	private Spinner<Float> heightSpinner;
	@FXML
	private Spinner<Float> marginSpinner;
	
	@FXML
	private RadioButton portraitRadio;
	@FXML
	private RadioButton landscapeRadio;
	@FXML
	private ToggleGroup pageOrientation;
	
	/**
	 * Initiate the model. Call after all the windows are done being set up.
	 * @param paperSettings An object containing the page settings that this controller is to control.
	 */
	public void initModel(PaperSettings paperSettings, PaperSize defaultPaperSize) {
		
		this.paperSettings = paperSettings;

		
		paperSizeCombo.setItems(FXCollections.observableArrayList(PaperSize.values()));
		
		marginSpinner.setValueFactory(new FloatMMSpinnerValueFactory());
		widthSpinner.setValueFactory(new FloatMMSpinnerValueFactory());
		heightSpinner.setValueFactory(new FloatMMSpinnerValueFactory());
		


		paperSizeCombo.valueProperty().addListener((obs, oldValue, newValue) -> {
			paperSettings.setPaperSize(newValue);
		});
		

		paperSettings.getPaperSizeProperty().addListener((obs, oldValue, newValue) -> {
			paperSizeCombo.valueProperty().set(newValue);
			
			if (newValue == PaperSize.CUSTOM) {
				
			}
		});
		
		paperSizeCombo.setValue(PaperSize.CUSTOM); // Change it so that we update all the change listeners.
		paperSizeCombo.setValue(defaultPaperSize);
		
		// Page width
		
		paperSettings.getPaperWidthProperty().addListener((obs, oldValue, newValue) -> {
			widthSpinner.getValueFactory().setValue(newValue.floatValue());
		});
		
		// TODO replace listeners with bindings: Learn how they work!
		widthSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
			paperSettings.setPaperWidth(newValue.floatValue());
		});
		
		widthSpinner.getValueFactory().setValue(paperSettings.getPaperWidth());
		

		// Page height
		
		paperSettings.getPaperHeightProperty().addListener((obs, oldValue, newValue) -> {
			heightSpinner.getValueFactory().setValue(newValue.floatValue());
		});
		
		heightSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
			paperSettings.setPaperHeight(newValue.floatValue());
		});
		
		heightSpinner.getValueFactory().setValue(paperSettings.getPaperHeight());
		
		// Margin
		
		paperSettings.getPaperMarginProperty().addListener((obs, oldValue, newValue) -> {
			marginSpinner.getValueFactory().setValue(newValue.floatValue());	
		});
		
		marginSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
			paperSettings.setPaperMargin(newValue.floatValue());
		});
		
		marginSpinner.getValueFactory().setValue(paperSettings.getPaperMargin());
		
		
	}
	
}
