package eli.projects.spprototype.controller;

import eli.projects.spprototype.App;
import eli.projects.spprototype.Utility;
import eli.projects.spprototype.model.*;
import eli.projects.spprototype.model.ExportSettings.SourceSelection;
import javafx.beans.value.ChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

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
	
	
	/** Lists Section **/

	@FXML
	private ListView<Setlist> setlistView;
	@FXML
	private TextField setlistTitleField;
	@FXML
	private Label setlistPieceCountLabel;
	@FXML
	private ListView<Piece> setlistPieceView;
	
	@FXML
	private Node setlistButtonBar;
	@FXML
	private Node listEditBox;
	
	@FXML
	private Node setlistPieceButtonBar;
	
	
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
		
		// Request confirmation before closing window.
		this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				boolean userWantsToQuit = App.showConfirmationDialog("Quit?", "Are you sure you want to quit?", "Quit");
				if (!userWantsToQuit) event.consume();
			}
		});
		
		
		
		this.exportSettings = new ExportSettings();
		
		/** Piece Table **/
		
		filteredPieces = new FilteredList<>(library.getPieces());
		
		SortedList<Piece> sortedPieces = new SortedList<>(filteredPieces);
		
		sortedPieces.comparatorProperty().bind(libraryPieceTable.comparatorProperty());
		
		libraryPieceTable.setItems(sortedPieces);
		
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

		// This is the listener that listens for changes to the count
		ChangeListener<Number> setlistCountLabelChangeListener = (obs2, oldValue, newValue) -> {
			setlistPieceCountLabel.setText("" + newValue);
		};
		

		// TODO: can this be abstracted into its own class?
		library.getCurrentSetlistProperty().addListener((obs, oldSelection, newSelection) -> {

			if (oldSelection != null) oldSelection.getLengthProperty().removeListener(setlistCountLabelChangeListener);
			
			if (newSelection != null) {
				
				
				setlistPieceView.setItems(newSelection.getPieceList());
				setlistTitleField.setText(newSelection.getName());
				//TODO: We really should do something with binding and properties for the length.
				setlistPieceCountLabel.setText("" + newSelection.getLengthProperty().get());
				newSelection.getLengthProperty().addListener(setlistCountLabelChangeListener);
				setlistButtonBar.setDisable(false);
			} else {
				setlistPieceView.setItems(null);
				setlistTitleField.setText("");
				setlistPieceCountLabel.setText("0");
				setlistButtonBar.setDisable(true);
			}
		});
		
		setlistPieceView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			setlistPieceButtonBar.setDisable(newSelection == null);
			
			// When we select a piece on the right, select it in the main tableview.
			if (newSelection != null) {
				library.getCurrentPieceProperty().set(newSelection);
			}
		});
		
		// TODO: push into own class
		setlistPieceView.setCellFactory(list -> new ReorderablePieceListCell() {

		    {
		    	// Nothing here I guess?
		    }

		    @Override
		    protected void updateItem(Piece item, boolean empty) {
		        super.updateItem(item, empty);
		        if (empty || item == null || item == null) {
		            setText(null);
		        } else {
		            setText(item.getTitle());
		        }
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
				ensembleMembersLabel.setText(newSelection.getNumberOfMembers() + " members");
			} else {
				ensembleSectionTableView.setItems(null);
				ensembleNameField.setText("");
				ensembleMembersLabel.setText("0 members");
			}
		});

		// Update ensemble name when changing the name listview
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
		Setlist list = library.addSetlist("New List"); 
		setlistView.getSelectionModel().select(list);
	}
	
	@FXML
	private void deleteList() {

		Setlist selectedSetlist = library.getCurrentSetlist();
		
		if (selectedSetlist == null) {
			// App.ShowError("Cannot delete list.", "No list is selected. Please select a list and try again.");
		} else {
			boolean userWantsToDelete = App.showConfirmationDialog("Are you sure?", 
					"Are you sure you want to delete " + selectedSetlist.getName() + "?", 
					"Delete " + selectedSetlist.getName());
			if (userWantsToDelete) {
				int index = setlistView.getSelectionModel().getSelectedIndex();
				library.deleteCurrentSetlist();
				setlistPieceView.getSelectionModel().clearSelection();
				// TODO: Keep current index selected after delete?
				setlistView.getSelectionModel().clearSelection();
			}
		}
	}
	



	@FXML
	private void addPieceToList() {
		if (library.getCurrentSetlist() == null) {
			App.ShowError("Cannot add to list.", "No list is selected. Please select a list and try again.");
		} else {
			if (library.getCurrentPiece() == null) {
				// App.ShowError("Cannot add to list.", "No piece is selected. Please select a piece and try again.");
			} else {
				library.getCurrentSetlist().add(library.getCurrentPiece());	
			}
		}
	}

	@FXML
	private void editSelectedPiece() {
		if (library.getCurrentPiece() == null) {
			// App.ShowError("Cannot edit piece.", "No piece is selected. Please select a piece and try again.");
		} else {
			App.ShowTempAlert("Action not yet implemented!");
		}
	}

	@FXML
	private void deleteSelectedPiece() {
		if (library.getCurrentPiece() == null) {
			// App.ShowError("Cannot delete piece.", "No piece is selected. Please select a piece and try again.");
		} else {
			Piece selectedPiece = library.getCurrentPiece();
			
			boolean userWantsToDelete = App.showConfirmationDialog("Are you sure?", 
					"Are you sure you want to delete " + selectedPiece.getTitle() + " from the library?", 
					"Delete " + selectedPiece.getTitle());
			if (userWantsToDelete) {
				library.deletePiece(selectedPiece);
			}
		}
		

	}
	
	@FXML
	private void listRemovePiece() {
		int index = setlistPieceView.getSelectionModel().getSelectedIndex();
		Setlist list = library.getCurrentSetlist();
		list.remove(setlistPieceView.getSelectionModel().getSelectedItem());
		setlistPieceView.getSelectionModel().clearSelection();
		// TODO: Keep current index selected after delete?
		setlistPieceView.getSelectionModel().clearAndSelect(index);
		
	}
	
	// TODO: Create new exportsettings page!
	@FXML
	private void exportList() {
		if (library.getCurrentSetlist() == null) {
			App.ShowError("Cannot export list.", "No list is selected. Please select a list and try again.");
		} else {
			exportSettings.getSelectedExportSetlistProperty().set(library.getCurrentSetlist());
			exportSettings.getSelectedSourceProperty().set(SourceSelection.LIST);
			
			openExportPopup();
		}
	}
	
	@FXML
	private void exportSelectedPiece() {
		if (library.getCurrentPiece() == null) {
			App.ShowError("Cannot export piece.", "No piece is selected. Please select a piece and try again.");
		} else {
			exportSettings.getSelectedExportPieceProperty().set(library.getCurrentPiece());
			exportSettings.getSelectedSourceProperty().set(SourceSelection.PIECE);
			
			openExportPopup();
		}
	}
	
	@FXML
	private void openExportPopup() {
		//TODO this should probably go in the export controller class
		FXMLLoader exportLoader = new FXMLLoader(getClass().getResource("/controller/ExportController.fxml"));
        
		assert exportLoader.getLocation() != null;
		
		try {
			Parent exportNode = exportLoader.load();
			
		    Scene exportScene = new Scene(exportNode);
		    Stage popup = new Stage();
		    popup.setTitle("Export: " + App.WINDOW_NAME);
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
	private void newSection() {
		
		FXMLLoader exportLoader = new FXMLLoader(getClass().getResource("/controller/NewSectionController.fxml"));
        
		assert exportLoader.getLocation() != null;
		
		try {
			Parent exportNode = exportLoader.load();
			
		    Scene exportScene = new Scene(exportNode);
		    Stage popup = new Stage();
		    popup.setTitle("Export: " + App.WINDOW_NAME);
		    popup.setScene(exportScene);
		    popup.initModality(Modality.WINDOW_MODAL);
		    popup.resizableProperty().set(false);
		    
		    // TODO is this the best way of getting the current window? Maybe we should pass a reference in to this class earlier.
		    popup.initOwner(rightStatus.getScene().getWindow());

			NewSectionController newSectionController = exportLoader.getController();

			newSectionController.initModel(library.getCurrentEnsemble(), library, popup);
			
			popup.show();
			

		} catch (IOException e) {
			App.ShowError("Cannot Export", "Could not load Export page. This is a bug.");
			e.printStackTrace();
		}
	}
	
	@FXML
	private void deleteSection() {
		
		Section currentSection = ensembleSectionTableView.getSelectionModel().getSelectedItem();
		
		if (currentSection != null) {
			library.getCurrentEnsemble().deleteSection(currentSection);
		}
		
	}
	
	@FXML
	private void deleteEnsemble() {
		
		Ensemble selectedEnsemble = library.getCurrentEnsemble();
		
		if (selectedEnsemble == null) {
			// App.ShowError("Cannot delete ensemble.", "No ensemble is selected. Please select an ensemble and try again.");
		} else {
			boolean userWantsToDelete = App.showConfirmationDialog("Are you sure?", 
					"Are you sure you want to delete " + selectedEnsemble.getName() + "?", 
					"Delete " + selectedEnsemble.getName());
			if (userWantsToDelete) {
				library.deleteCurrentEnsemble();
			}
		}
	}

	@FXML
	private void quitProgram() {
		//TODO: This and window close request should call the same function
		boolean userWantsToQuit = App.showConfirmationDialog("Quit?", "Are you sure you want to quit?", "Quit");
		// TODO: check to see if we have unsaved work
		this.stage.close();
	}
	
}
