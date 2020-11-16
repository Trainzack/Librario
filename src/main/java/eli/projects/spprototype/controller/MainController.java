package eli.projects.spprototype.controller;

import eli.projects.spprototype.App;
import eli.projects.spprototype.DocumentSource;
import eli.projects.spprototype.Part;
import eli.projects.spprototype.PartDesignation;
import eli.projects.spprototype.Utility;
import eli.projects.spprototype.model.*;
import eli.projects.spprototype.model.ExportSettings.SourceSelection;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

import org.controlsfx.control.tableview2.TableView2;

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
	private TableView2<Piece> libraryPieceTable;

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
	
	/** Piece Details **/
	
	@FXML
	private TableView2<Part> piecePartTable;
	
	@FXML
	private TableColumn<Part, PartDesignation> piecePartDesignationColumn;
	@FXML
	private TableColumn<Part, Integer> piecePartPageCountColumn;
	
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
				boolean userWantsToQuit = App.showConfirmationDialog("Quit?", "Are you sure you want to quit Ossia?", "Quit");
				if (!userWantsToQuit) event.consume();
			}
		});
		
		
		
		this.exportSettings = new ExportSettings(this.library);
		
		/** Piece Table **/
		
		// When we type in the search box, update right away.
		searchFilterField.textProperty().addListener((obs, oldValue, newValue) -> {
			filterPieceTable();
		});
		
		filteredPieces = new FilteredList<>(library.getPieces());
		
		SortedList<Piece> sortedPieces = new SortedList<>(filteredPieces);
		
		sortedPieces.comparatorProperty().bind(libraryPieceTable.comparatorProperty());
		
		libraryPieceTable.setItems(sortedPieces);
		
		libraryPieceTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		libraryPieceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldPiece, newPiece) -> {
			pieceButtonBar.setDisable(newPiece == null);
			libraryPieceTable.getSelectionModel().select(newPiece); // Why does this line exist?
			
			if (newPiece != null) {
				String parts = "Parts: ";
				
				for (Part p : newPiece.getParts()) {
					parts += p.getDesignation().toString() + ", ";
				}
			}
			
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
		
		TableColumn<Piece, Integer> piecePartCountColum = new TableColumn<Piece, Integer>("Parts");
		piecePartCountColum.setCellValueFactory(new PropertyValueFactory<Piece, Integer>("partCount"));
		libraryPieceTable.getColumns().add(piecePartCountColum);
		piecePartCountColum.setVisible(false);
		
		/** Piece Details **/

		libraryPieceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldPiece, newPiece) -> {
			
			if (newPiece != null) {
				piecePartTable.setItems(newPiece.getParts());
			} else {
				piecePartTable.setItems(null);
			}
			
		});
		
		piecePartDesignationColumn.setCellValueFactory(new PropertyValueFactory<Part, PartDesignation>("designation"));
		piecePartPageCountColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("pages"));
		
		TableColumn<Part, String> documentSourceColum = new TableColumn<>();
		documentSourceColum.setText("PDF");
		piecePartTable.getColumns().add(documentSourceColum);
		
		documentSourceColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Part, String> param) {
				// TODO Auto-generated method stub
				String out = "";
				for (DocumentSource d : param.getValue().getDocumentSources()) {
					out += d.toString();
				}
				
				return new SimpleStringProperty(out);
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
				libraryPieceTable.getSelectionModel().clearSelection();
				if (sortedPieces.contains(newSelection)) {
					libraryPieceTable.getSelectionModel().select(newSelection);
					libraryPieceTable.scrollTo(newSelection);
				}
				
			}
		});
		
		// TODO: push into own class
		setlistPieceView.setCellFactory(list -> new ReorderableListCell<Piece>() {

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
		
		String searchString = searchFilterField.getText().toLowerCase().trim();
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
			
			ObservableList<Piece> selection = libraryPieceTable.getSelectionModel().getSelectedItems();
			
			if (selection.size() <= 0) {
				// App.ShowError("Cannot add to list.", "No piece is selected. Please select a piece and try again.");
			} else {
				for (Piece p : selection) {
					library.getCurrentSetlist().add(p);	
				}
			}
		}
	}

	@FXML
	private void editSelectedPiece() {
		
		ObservableList<Piece> selection = libraryPieceTable.getSelectionModel().getSelectedItems();
		if (selection.size() <= 0) {
			// App.ShowError("Cannot edit piece.", "No piece is selected. Please select a piece and try again.");
		} else {
			App.ShowTempAlert("Action not yet implemented!");
		}
	}

	@FXML
	private void deleteSelectedPiece() {
		ObservableList<Piece> selection = libraryPieceTable.getSelectionModel().getSelectedItems();
		
		if (selection.size() <= 0) {
			// App.ShowError("Cannot delete piece.", "No piece is selected. Please select a piece and try again.");
		} else {
			
			String pieceCount = "" + selection.size() + " pieces";
			
			if (selection.size() == 1) {
				pieceCount = selection.get(0).getTitle();
			}
			
			boolean userWantsToDelete = App.showConfirmationDialog("Are you sure?", 
					"Are you sure you want to delete " + pieceCount + " from the library?", 
					"Delete " + pieceCount);
			// TODO: List how many setlists this piece is in, and warn that it will be removed from all of them.
			if (userWantsToDelete) {
				// We need to take the items out of the observable list before we start iterating over it, otherwise it skip some when we delete.
				for (Piece p : selection.toArray(new Piece[selection.size()])) {
					library.deletePiece(p);
				}
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
		ObservableList<Piece> selection = libraryPieceTable.getSelectionModel().getSelectedItems();
		if (selection.size() <= 0) {
			App.ShowError("Cannot export piece.", "No piece is selected. Please select a piece and try again.");
		} else {
			exportSettings.getSelectedExportPieceProperty().set(selection.get(0));
			// TODO Undefined behavior here when multiple pieces are selected. Create list? Hmmm...
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
			App.ShowException(e, "Could not load Export page. This is a bug.");
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
			App.ShowException(e, "Could not load new section page. This is a bug.");
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
		
		if (userWantsToQuit) {
			this.stage.close();	
		}
		
	}
	
}
