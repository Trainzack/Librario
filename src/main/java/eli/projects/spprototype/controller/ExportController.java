package eli.projects.spprototype.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.controlsfx.control.SearchableComboBox;

import eli.projects.spprototype.App;
import eli.projects.spprototype.ExportTask;
import eli.projects.spprototype.model.Ensemble;
import eli.projects.spprototype.model.ExportGroupType;
import eli.projects.spprototype.model.ExportSettings;
import eli.projects.spprototype.model.Instrument;
import eli.projects.spprototype.model.Library;
import eli.projects.spprototype.model.PaperSize;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
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

	private static final PaperSize DEFAULT_PAPER_SIZE = PaperSize.LETTER;
	

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
	
	/* Page Size */
	
	@FXML
	private ComboBox<PaperSize> exportPaperSize;
	@FXML
	private Spinner<Double> exportPageWidthSpinner;
	@FXML
	private Spinner<Double> exportPageHeightSpinner;
	@FXML
	private Spinner<Double> exportPageMarginSpinner;
	
	
	@FXML
	private ToggleGroup exportPageOrientation;

	@FXML
	private ListView<ExportGroupType> exportGroupingListView;
	
	
	
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
		
		exportPieceComboBox.setItems(library.getPieces());
		exportPieceComboBox.valueProperty().bindBidirectional(exportSettings.getSelectedExportPieceProperty());
		exportPieceToggle.selectedProperty().addListener((obs, oldValue, newValue) -> {
			exportPieceComboBox.disableProperty().set(!newValue);
		});
		
		// Whenever we make selections with the togglebox, change the corresponding setting in ExportSettings
		// TODO: Maybe we can encapsulate more of this in the model?
		exportPieceSource.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
			
			ExportSettings.SourceSelection value = null;
			if (newValue == exportSetlistToggle) {
				value = ExportSettings.SourceSelection.LIST;
			} else if (newValue == exportPieceToggle) {
				value = ExportSettings.SourceSelection.PIECE;
			}
			exportSettings.getSelectedSourceProperty().set(value);
		});
		
		// Set these toggles to their correct initial values
		if (exportSettings.getSelectedSourceProperty().get() != null) {
			switch (exportSettings.getSelectedSourceProperty().get()) {
			case LIST:
				exportSetlistToggle.setSelected(true);
				break;
			case PIECE:
				exportPieceToggle.setSelected(true);
				break;
			}
		}

		
		/* Target Settings */
		
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
		
		// Whenever we make selections with the togglebox, change the corresponding setting in ExportSettings
		// TODO: Maybe we can encapsulate more of this in the model?
		exportPieceTarget.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
			
			ExportSettings.TargetSelection value = null;
			if (newValue == exportTargetEnsembleToggle) {
				value = ExportSettings.TargetSelection.ENSEMBLE;
			} else if (newValue == exportTargetInstrumentToggle) {
				value = ExportSettings.TargetSelection.INSTRUMENT;
			} else if (newValue == exportTargetAllPartsToggle) {
				value = ExportSettings.TargetSelection.ALL;
			}
			
			exportSettings.getSelectedTargetProperty().set(value);
		});
		
		// Set these toggles to their correct initial values
		if (exportSettings.getSelectedTargetProperty().get() != null) {
			switch (exportSettings.getSelectedTargetProperty().get()) {
			case ENSEMBLE:
				exportTargetEnsembleToggle.setSelected(true);
				break;
			case INSTRUMENT:
				exportTargetInstrumentToggle.setSelected(true);
				break;
			case ALL:
				exportTargetAllPartsToggle.setSelected(true);
			}
		}

		
		/* Page Settings */
		
		exportPaperSize.setItems(FXCollections.observableArrayList(PaperSize.values()));
		
		exportPageMarginSpinner.setValueFactory(new DoubleMMSpinnerValueFactory());
		exportPageWidthSpinner.setValueFactory(new DoubleMMSpinnerValueFactory());
		exportPageHeightSpinner.setValueFactory(new DoubleMMSpinnerValueFactory());
		

		// TODO: This is wrong. Export paper size should update the model, which should update the width and height settings in the model, which should then update the spinners.
		exportPaperSize.valueProperty().addListener((obs, oldValue, newValue) -> {
			exportPageWidthSpinner.getValueFactory().setValue(newValue.getWidthmm());
		});
		
		exportPaperSize.valueProperty().addListener((obs, oldValue, newValue) -> {
			exportPageHeightSpinner.getValueFactory().setValue(newValue.getHeightmm());
		});
		
		exportPaperSize.setValue(DEFAULT_PAPER_SIZE);
		// TODO: This is unconnected to the model.
		exportPageMarginSpinner.getValueFactory().setValue(10.0);
		
		/* Export Folder Grouping */
		exportGroupingListView.setCellFactory(list -> new ExportGroupListCell());
		
		exportGroupingListView.setItems(exportSettings.getExportGroups());
		
		
		/** Buttons **/
		
		// Whenever the export settings are invalid, disallow exporting.
		exportButton.disableProperty().bind(exportSettings.getIsInvalidProperty());
		
		exportSettings.evaluateValidity();
		
	}

	
	/** Close this window and actually do the export! **/
	@FXML
	private void finishExport() {
		
		
		exportSettings.evaluateValidity();
		if (exportSettings.getIsInvalidProperty().get()) {
			//This is bad
			App.ShowError("Export Failed", "The settings you chose for export were invalid. Additionally, the program should have prevented this.");
		}
		
		DirectoryChooser dir = new DirectoryChooser();
        dir.setTitle("Choose an export directory");
        // If the user has already selected a destination before, use that one!
        if (exportSettings.getExportDestination() != null) dir.setInitialDirectory(exportSettings.getExportDestination());
        File destination = dir.showDialog(this.stage);

        if (destination == null) {
        	// If they don't select a destination, then cancel the export! 
        } else {
        	exportSettings.setExportDestination(destination);
        	
        	
        	stage.close();
        	
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Exporting");
    		alert.setHeaderText("Currently exporting, please wait.");
    		alert.initOwner(stage);
    		
    		Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
    		Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
    		
    		okButton.setDisable(true);
    		
    		ProgressBar exportProgress = new ProgressBar(0.5);
    		
    		alert.setGraphic(exportProgress);
            
    		PDRectangle paperDimensions;
    		
    		if (exportSettings.getPaperSize() != PaperSize.CUSTOM) {
    			paperDimensions = exportSettings.getPaperSize().getDimensions();
    		} else {
    			paperDimensions = new PDRectangle(10, 10); // TODO: This needs to grab from paperWidth and paperHeight.
    		}
    		
    		ExportTask exportTask = new ExportTask(paperDimensions, library.getPieces(), exportSettings.getExportDestination());
    		
    		exportTask.messageProperty().addListener((obs, oldValue, newValue) -> {
    			alert.setContentText(newValue);
    		});

    		exportProgress.progressProperty().bind(exportTask.progressProperty());
    		exportTask.stateProperty().addListener((obs, oldState, currentState) -> {
    			if (currentState == Worker.State.SUCCEEDED || currentState == Worker.State.FAILED) {
    				okButton.setDisable(false);
    				cancelButton.setDisable(true);
    			}
    		});
    		
    		Thread exportThread = new Thread(exportTask);
    		exportThread.setDaemon(true);
    		exportThread.start();
    		

    		
    		
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		
    		if (result.get() == ButtonType.CANCEL) {
    			exportTask.cancel();
    		}
    		
    		
        }
		
	}
	
	/** Close this window and do nothing. **/
	@FXML
	private void finishCancel() {
		// TODO
		stage.close();
	}

}
