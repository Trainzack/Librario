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
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.controlsfx.control.SearchableComboBox;

/**
 * Controls the Library
 * 
 * @author Eli
 *
 */
public class MainController {


	
	private Stage stage;
	
	
	/** Data Model **/
	private Library library;
	private ExportSettings exportSettings;
	
	
	// private Node exportNode;

	@FXML
	private Label rightStatus;
	
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
	private Node pieceButtonBar;
	
	
	
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
	
	@FXML
	private Node listEditBox;
	
	
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
	
	@FXML
	private Node ensembleEditBox;

	
	
	
	public void initModel(Library _library, Stage _stage) throws IOException {

		this.library = _library;
		this.stage = _stage;
		
		
		
		this.exportSettings = new ExportSettings();
		
		
		// Initialize the listview on the left part of the screen that contains all of the setlists
		//setlistView = new ListView<AbstractSetlist>(library.getSetlists());
		assert setlistView != null;
		
		/** Piece Table **/
		
		filteredPieces = new FilteredList<>(library.getPieces());
		
		libraryPieceTable.setItems(filteredPieces);
		
		libraryPieceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
			{ 
				library.setCurrentPiece(newSelection);
				pieceButtonBar.setDisable(newSelection == null);
			});

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
			{
				library.setCurrentSetlist(newSelection);
				listEditBox.disableProperty().set(newSelection == null);
			});

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
			{
				library.setCurrentEnsemble(newSelection);
				ensembleEditBox.disableProperty().set(newSelection == null);
			});


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
	private void listRemovePiece() {
		App.ShowTempAlert("Action not yet implemented!");
	}
	
	// TODO: Create new exportsettings page!
	@FXML
	private void exportList() {
		if (library.getCurrentSetlist() == null) {
			App.ShowError("Cannot export list.", "No list is selected. Please select a list and try again.");
		} else {
			exportSettings.getSelectedExportSetlistProperty().set(library.getCurrentSetlist());
			//exportPieceSource.selectToggle(exportSetlistToggle);
			// TODO: Make sure we select the "using piece" property
			openExportPopup();
		}
	}
	
	@FXML
	private void exportSelectedPiece() {
		if (library.getCurrentPiece() == null) {
			App.ShowError("Cannot export piece.", "No piece is selected. Please select a piece and try again.");
		} else {
			exportSettings.getSelectedExportPieceProperty().set(library.getCurrentPiece());
			//exportPieceSource.selectToggle(exportPieceToggle);
			// TODO: Make sure we select the "using piece" property
			openExportPopup();
		}
	}
	
	@FXML
	private void openExportPopup() {
		FXMLLoader exportLoader = new FXMLLoader(getClass().getResource("/controller/ExportController.fxml"));
        
		assert exportLoader.getLocation() != null;
		
		try {
			Parent exportNode = exportLoader.load();
			
		    Scene exportScene = new Scene(exportNode);
		    Stage popup = new Stage();
		    popup.setTitle("Export");
		    popup.setScene(exportScene);
		    popup.initModality(Modality.WINDOW_MODAL);
		    popup.resizableProperty().set(false);
		    
		    // TODO is this the best way of getting the current window? Maybe we should pass a reference in to this class earlier.
		    popup.initOwner(rightStatus.getScene().getWindow());

			ExportController exportController = exportLoader.getController();

			exportController.initModel(exportSettings, library, popup);
			
			popup.show();

		} catch (IOException e) {
			App.ShowError("Cannot Export", "Could not load Export page. This is a bug.");
			e.printStackTrace();
		}
	}

	@FXML
	private void newEnsemble() {
		library.addNewEnsemble();
	}
	@FXML
	private void deleteEnsemble() {
		library.deleteCurrentEnsemble();
	}

}
