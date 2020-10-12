package eli.projects.spprototype.controller;

import eli.projects.spprototype.App;
import eli.projects.spprototype.Utility;
import eli.projects.spprototype.model.Ensemble;
import eli.projects.spprototype.model.ExportSettings;
import eli.projects.spprototype.model.Instrument;
import eli.projects.spprototype.model.Library;
import eli.projects.spprototype.model.PaperSize;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Section;
import eli.projects.spprototype.model.Setlist;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

import org.controlsfx.control.SearchableComboBox;

/**
 * Controls the Library
 * 
 * @author Eli
 *
 */
public class LibraryController {


	private Library library;
	
	private Stage stage;

	
	/** Search **/
	
	@FXML
	private TextField searchFilterField;
	@FXML
	private Button searchFilterButton;
	
	private FilteredList<Piece> filteredPieces;
	
	/** Piece Table **/
	@FXML
	private TableView<Piece> libraryPieceTable;

	@FXML
	private TableColumn<Piece, String> pieceTitleColumn;
	@FXML
	private TableColumn<Piece, String> pieceComposerColumn;
	@FXML
	private TableColumn<Piece, String> pieceArrangerColumn;
	@FXML
	private TableColumn<Piece, Integer> pieceYearColumn;
	@FXML
	private TableColumn<Piece, Integer> pieceDurationColumn;
	
	
	
	@FXML
	private TabPane tabPane;
	
	@FXML
	private Tab tabLists;
	@FXML
	private Tab tabEnsembles;
	@FXML
	private Tab tabExport;
	
	
	/** Lists Section **/

	@FXML
	private ListView<Setlist> setlistView;
	@FXML
	private TextField setlistTitleField;
	@FXML
	private ListView<Piece> setlistPieceView;
	
	
	/** Ensembles Section **/
	@FXML
	private ListView<Ensemble> ensembleView;
	@FXML
	private TextField ensembleNameField;
	@FXML
	private TableView<Section> ensembleSectionTableView;

	@FXML
	private TableColumn<Section, Instrument> sectionInstrumentColumn;
	@FXML
	private TableColumn<Section, Integer> sectionCountColumn;
	@FXML
	private Label ensembleMembersLabel;
	
	/** Export Section **/
	
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
	
	@FXML
	private CheckBox exportEnsembleCheckBox;
	@FXML
	private SearchableComboBox<Ensemble> exportEnsembleComboBox;
	
	
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
	
	
	
	public void initModel(Library _library, Stage _stage) {

		this.library = _library;
		this.stage = _stage;
		
		// Initialize the listview on the left part of the screen that contains all of the setlists
		//setlistView = new ListView<AbstractSetlist>(library.getSetlists());
		assert setlistView != null;
		
		filteredPieces = new FilteredList<>(library.getPieces());
		
		libraryPieceTable.setItems(filteredPieces);
		
		libraryPieceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
		{ library.setCurrentPiece(newSelection); });

		// Table Columns
		pieceTitleColumn.setCellValueFactory(new PropertyValueFactory<Piece, String>("title"));
		pieceComposerColumn.setCellValueFactory(new PropertyValueFactory<Piece, String>("composer"));
		pieceArrangerColumn.setCellValueFactory(new PropertyValueFactory<Piece, String>("arranger"));
		pieceYearColumn.setCellValueFactory(new PropertyValueFactory<Piece, Integer>("year"));
		pieceDurationColumn.setCellValueFactory(new PropertyValueFactory<Piece, Integer>("duration"));
		pieceDurationColumn.setCellFactory(p -> new TableCell<Piece, Integer>() {
			@Override
			protected void updateItem(Integer item, boolean empty) {
				if (empty || item == null) {
					setText(null);
				} else {
					setText(Utility.intToDuration(item));
				}
				
			}
		});
		
		/** Setlists **/
		
		setlistView.setItems(library.getSetlists());
		
		setlistView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
			{ library.setCurrentSetlist(newSelection); });

		library.getCurrentSetlistProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				setlistPieceView.setItems(newSelection.getPieceList());
				setlistTitleField.setText(newSelection.getName());
			} else {
				setlistPieceView.setItems(null);
				setlistTitleField.setText("");
			}
		});

		// Update setlist name when changing the name listview
		setlistTitleField.textProperty().addListener((obs, oldText, newText) -> {
			if (library.getCurrentSetlist() != null) {
				library.getCurrentSetlist().setName(setlistTitleField.textProperty().get());
				setlistView.refresh();
			}
		});
		
		/** Ensembles **/
		
		ensembleView.setItems(library.getEnsembles());
		
		ensembleView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
			{ library.setCurrentEnsemble(newSelection); });


		sectionInstrumentColumn.setCellValueFactory(new PropertyValueFactory<Section, Instrument>("instrumentName"));
		sectionCountColumn.setCellValueFactory(new PropertyValueFactory<Section, Integer>("count"));
		
		library.getCurrentEnsembleProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				ensembleSectionTableView.setItems(newSelection.getSections());
				ensembleNameField.setText(newSelection.getName());
				ensembleMembersLabel.setText("Members: " + newSelection.getNumberOfMembers());
			} else {
				ensembleSectionTableView.setItems(null);
				ensembleNameField.setText("");
				ensembleMembersLabel.setText("Members: 0");
			}
		});

		// Update setlist name when changing the name listview
		ensembleNameField.textProperty().addListener((obs, oldText, newText) -> {
			if (library.getCurrentEnsemble() != null) {
				library.getCurrentEnsemble().setName(ensembleNameField.textProperty().get());
				ensembleView.refresh();
			}
		});
		
		/** Export **/
		
		/* Source Settings */
		
		exportSetlistComboBox.setItems(library.getSetlists());
		exportSetlistToggle.selectedProperty().addListener((obs, oldValue, newValue) -> {
			exportSetlistComboBox.disableProperty().set(!newValue);
		});
		
		//TODO: Figure out exception that occurs whenever lists are deleted while the combobox is active.
		exportPieceComboBox.setItems(library.getPieces());
		exportPieceToggle.selectedProperty().addListener((obs, oldValue, newValue) -> {
			exportPieceComboBox.disableProperty().set(!newValue);
		});
		
		exportEnsembleComboBox.setItems(library.getEnsembles());
		exportEnsembleCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			exportEnsembleComboBox.disableProperty().set(!newValue);
		});
		
		/* Page Settings */
		
		exportPaperSize.setItems(FXCollections.observableArrayList(PaperSize.values()));
		
		
		/* Export Folder Grouping */
		
		/* Destination */
		
		library.exportSettings.getExportDestinationProperty().addListener((obs, oldValue, newValue) -> {
			exportDirectoryTextField.setText((newValue == null) ? "" : newValue.getPath());
		});
		
	}
	
	@FXML
	private void filterPieceTable() {

		String searchString = searchFilterField.getText().toLowerCase();
		if (searchString.isEmpty()) {
			filteredPieces.setPredicate(piece -> true);
		} else {
			filteredPieces.setPredicate(piece -> {
				if (piece.titleProperty().get().toLowerCase().contains(searchString) ||
						piece.arrangerProperty().get().toLowerCase().contains(searchString) ||
						piece.composerProperty().get().toLowerCase().contains(searchString)) {
					return true;
				}
				return false;
			});
		}
		
		
	}
	
	@FXML
	private void newList() {
		library.addSetlist("New Setlist");
	}
	
	@FXML
	private void deleteList() {
		int index = setlistView.getSelectionModel().getSelectedIndex();
		library.deleteCurrentSetlist();
		setlistPieceView.getSelectionModel().clearSelection();
		// TODO: Keep current index selected after delete?
		setlistView.getSelectionModel().clearAndSelect(index);
	}

	@FXML
	private void exportList() {
		App.ShowTempAlert("Action not yet implemented!");
	}
	

	@FXML
	private void removeSelectedPiece() {
		App.ShowTempAlert("Action not yet implemented!");
	}


	@FXML
	private void addPieceToList() {
		tabPane.getSelectionModel().select(tabLists);
		if (library.getCurrentSetlist() == null) {
			App.ShowError("Cannot add to list.", "No list is selected. Please select a list and try again.");
		} else {
			if (library.getCurrentPiece() == null) {
				App.ShowError("Cannot add to list.", "No piece is selected. Please select a piece and try again.");
			} else {
				library.getCurrentSetlist().add(library.getCurrentPiece());	
			}
		}
	}

	@FXML
	private void editSelectedPiece() {
		App.ShowTempAlert("Action not yet implemented!");
	}

	@FXML
	private void deleteSelectedPiece() {
		App.ShowTempAlert("Action not yet implemented!");
	}

	@FXML
	private void exportSelectedPiece() {
		App.ShowTempAlert("Action not yet implemented!");
	}

	@FXML
	private void newEnsemble() {
		App.ShowTempAlert("Action not yet implemented!");
	}
	@FXML
	private void deleteEnsemble() {
		App.ShowTempAlert("Action not yet implemented!");
	}

	@FXML
	private void exportChooseDirectory() {
		ExportSettings es = library.exportSettings;

        DirectoryChooser dir = new DirectoryChooser();
        dir.setTitle("Choose a destination to export");
        // If the user has already selected a destination before, use that one!
        if (es.getExportDestination() != null) dir.setInitialDirectory(es.getExportDestination());
        File destination = dir.showDialog(this.stage);
        // Don't change the destination if the user didn't select anything
        if (destination != null) es.setExportDestination(destination);
        
	}
}
