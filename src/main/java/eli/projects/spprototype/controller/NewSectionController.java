package eli.projects.spprototype.controller;

import eli.projects.spprototype.model.Ensemble;
import eli.projects.spprototype.model.Instrument;
import eli.projects.spprototype.model.Library;
import eli.projects.spprototype.model.Section;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

/**
 * @deprecated
 * @author Eli
 *
 */
public class NewSectionController {

	@FXML
	private ComboBox<Instrument> instrumentComboBox;
	
	@FXML
	private Spinner<Integer> memberCountSpinner;
	
	private Library library;
	private Stage stage;
	private Ensemble ensemble;
	
	public void initModel(Ensemble _ensemble, Library _library, Stage _stage) {

		this.library = _library;
		
		// This stage belongs to this window!
		this.stage = _stage;
		
		this.ensemble = _ensemble;
		
		instrumentComboBox.setItems(library.getInstruments());
		instrumentComboBox.setValue(instrumentComboBox.getItems().get(0));
		
		memberCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));
	}
	
	
	/** Close this window and actually create the section! **/
	@FXML
	private void finishCreate() {
		
		// Force the spinner to commit the value that has been typed upon focus loss.
		this.memberCountSpinner.increment(0);
		
		this.ensemble.addSection(new Section(
				instrumentComboBox.getValue(),
				memberCountSpinner.getValue()));
		
		stage.close();
	}
	
	/** Close this window and do nothing. **/
	@FXML
	private void finishCancel() {
		// TODO
		stage.close();
	}
}
