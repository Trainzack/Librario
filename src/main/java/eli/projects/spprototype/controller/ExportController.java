package eli.projects.spprototype.controller;

import java.io.File;

import org.controlsfx.control.SearchableComboBox;

import eli.projects.spprototype.App;
import eli.projects.spprototype.model.Ensemble;
import eli.projects.spprototype.model.ExportSettings;
import eli.projects.spprototype.model.Instrument;
import eli.projects.spprototype.model.Library;
import eli.projects.spprototype.model.PaperSize;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * This class controls the export screen.
 * 
 * It is part of the view of the MainController.
 * 
 * @author Eli
 *
 */

public class ExportController {
	

	// These are the models we rely on
	private ExportSettings exportSettings;
	private Library library;
	
	private Stage stage;
	
	/** Export Section **/
	
	/* Source Settings */
	
	@FXML
	private ToggleGroup exportPieceSource;
	
	@FXML
	private ToggleButton exportSetlistToggle;
	@FXML
	private SearchableComboBox<Setlist> exportSetlistComboBox;
	
	@FXML
	private ToggleButton exportPieceToggle;
	@FXML
	private SearchableComboBox<Piece> exportPieceComboBox;
	
	/* Target Settings */
	

	@FXML
	private ToggleGroup exportPieceTarget;
	
	@FXML
	private ToggleButton exportTargetAllPartsToggle;
	
	@FXML
	private ToggleButton exportTargetEnsembleToggle;
	@FXML
	private SearchableComboBox<Ensemble> exportTargetEnsembleComboBox;	
	@FXML
	private ToggleButton exportTargetInstrumentToggle;
	@FXML
	private SearchableComboBox<Instrument> exportTargetInstrumentComboBox;
	
	
	@FXML
	private ComboBox<PaperSize> exportPaperSize;
	@FXML
	private Spinner<Double> exportPageWidthSpinner;
	
	
	@FXML
	private ToggleGroup exportPageOrientation;
	@FXML
	private ToggleGroup exportFolderGrouping;
	
	@FXML
	private TextField exportDirectoryTextField;
	
	
	
	@FXML
	private Button exportButton;
	

	
	public void initModel(ExportSettings _exportSettings, Library _library, Stage _stage) {

		this.exportSettings = _exportSettings;
		this.library = _library;
		
		// This stage belongs to this window!
		this.stage = _stage;
		
		
		exportPieceSource.selectedToggleProperty(); //TODO: Figure out how to link this to the model.
		
		/* Source Settings */
		
		exportSetlistComboBox.setItems(library.getSetlists());
		exportSetlistComboBox.valueProperty().bindBidirectional(exportSettings.getSelectedExportSetlistProperty());
		exportSetlistToggle.selectedProperty().addListener((obs, oldValue, newValue) -> {
			exportSetlistComboBox.disableProperty().set(!newValue);
		});
		
		//TODO: Figure out exception that occurs whenever lists are deleted while the combobox is active.
		exportPieceComboBox.setItems(library.getPieces());
		exportPieceComboBox.valueProperty().bindBidirectional(exportSettings.getSelectedExportPieceProperty());
		exportPieceToggle.selectedProperty().addListener((obs, oldValue, newValue) -> {
			exportPieceComboBox.disableProperty().set(!newValue);
		});
		
		exportTargetEnsembleComboBox.setItems(library.getEnsembles());
		exportTargetEnsembleComboBox.valueProperty().bindBidirectional(exportSettings.getSelectedExportEnsembleProperty());
		exportTargetEnsembleToggle.selectedProperty().addListener((obs, oldValue, newValue) -> {
			exportTargetEnsembleComboBox.disableProperty().set(!newValue);
		});
		
		exportTargetInstrumentComboBox.setItems(library.getInstruments());
		exportTargetInstrumentComboBox.valueProperty().bindBidirectional(exportSettings.getSelectedExportInstrumentProperty());
		exportTargetInstrumentToggle.selectedProperty().addListener((obs, oldValue, newValue) -> {
			exportTargetInstrumentComboBox.disableProperty().set(!newValue);
		});
		
		/* Page Settings */
		
		exportPaperSize.setItems(FXCollections.observableArrayList(PaperSize.values()));
		
		
		/* Export Folder Grouping */
		
		/* Destination */
		
		exportSettings.getExportDestinationProperty().addListener((obs, oldValue, newValue) -> {
			exportDirectoryTextField.setText((newValue == null) ? "" : newValue.getPath());
		});
		
		
	}


	// Todo: we might want to use something else here.
	@FXML
	private void exportChooseDirectory() {

        DirectoryChooser dir = new DirectoryChooser();
        dir.setTitle("Choose a destination to export");
        // If the user has already selected a destination before, use that one!
        if (exportSettings.getExportDestination() != null) dir.setInitialDirectory(exportSettings.getExportDestination());
        File destination = dir.showDialog(this.stage);
        // Don't change the destination if the user didn't select anything
        if (destination != null) exportSettings.setExportDestination(destination);
        
	}
	
	/** Close this window and actually do the export! **/
	@FXML
	private void finishExport() {
		// TODO
		stage.close();
		App.ShowTempAlert("Export Successful.");
	}
	
	/** Close this window and do nothing. **/
	@FXML
	private void finishCancel() {
		// TODO
		stage.close();
	}

}
