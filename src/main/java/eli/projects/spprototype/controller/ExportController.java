package eli.projects.spprototype.controller;

import java.io.File;
import java.util.Optional;

import org.controlsfx.control.SearchableComboBox;

import eli.projects.spprototype.App;
import eli.projects.spprototype.ExportTask;
import eli.projects.spprototype.model.Ensemble;
import eli.projects.spprototype.model.ExportSettings;
import eli.projects.spprototype.model.Instrument;
import eli.projects.spprototype.model.Library;
import eli.projects.spprototype.model.PaperSettings;
import eli.projects.spprototype.model.PaperSize;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * This class controls the export screen.
 * 
 * It is part of the view of the MainController.
 * @deprecated
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
	@FXML private GridPane mainPageSettings;
	@FXML private PageSizeController mainPageSettingsController;
	
	
	@FXML
	private CheckBox exportPageDoublePages;
	@FXML
	private CheckBox exportPageFitToPage;
	
	
	@FXML
	private Button exportButton;
	

	
	
	
	public void initModel(ExportSettings _exportSettings, Library _library, Stage _stage) {

		this.exportSettings = _exportSettings;
		this.library = _library;
		
		// This stage belongs to this window!
		this.stage = _stage;

		// Init sub models
		mainPageSettingsController.initModel(exportSettings.getMainPaperSettings(), PaperSize.LETTER);
		
		
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
		

		
		exportSettings.getFitToPageProperty().addListener((obs, oldValue, newValue) -> {
			exportPageFitToPage.selectedProperty().set(newValue);
		});

		exportPageFitToPage.selectedProperty().addListener((obs, oldValue, newValue) -> {
			exportSettings.getFitToPageProperty().set(newValue);
		});
		

		exportPageFitToPage.setSelected(exportSettings.getFitToPage()); // Set initial value
		
		exportSettings.getDoublePagesProperty().addListener((obs, oldValue, newValue) -> {
			exportPageDoublePages.selectedProperty().set(newValue);
		});

		exportPageDoublePages.selectedProperty().addListener((obs, oldValue, newValue) -> {
			exportSettings.getDoublePagesProperty().set(newValue);
		});

		exportPageDoublePages.setSelected(exportSettings.getDoublePages()); // Set initial value
		
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
		
		//DirectoryChooser dir = new DirectoryChooser();
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        // If the user has already selected a destination before, use that one!
        if (exportSettings.getExportDestination() != null) { 
        	fileChooser.setInitialDirectory(exportSettings.getExportDestination().getParentFile());
        	fileChooser.setInitialFileName(exportSettings.getExportDestination().getName());
        }
        File destination = fileChooser.showSaveDialog(this.stage);

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
            
    		ExportTask exportTask = exportSettings.getExportTask();
    		
    		exportTask.messageProperty().addListener((obs, oldValue, newValue) -> {
    			alert.setContentText(newValue);
    		});

    		exportProgress.progressProperty().bind(exportTask.progressProperty());
    		exportTask.stateProperty().addListener((obs, oldState, currentState) -> {
    			if (currentState == Worker.State.SUCCEEDED || currentState == Worker.State.FAILED) {
    				okButton.setDisable(false);
    				cancelButton.setDisable(true);
    				
    			}
    			
    			if (currentState == Worker.State.SUCCEEDED) {

    	    		okButton.setText("Finish");
    	    		// TODO: Add "View Document" button.
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
