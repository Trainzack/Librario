package eli.projects.spprototype.controller;

import eli.projects.spprototype.App;
import eli.projects.spprototype.model.Library;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * Controls the Library
 * 
 * @author Eli
 *
 */
public class LibraryController {


	@FXML
	private Label setlistNameLabel;
	
	@FXML
	private TableView<Piece> setlistPieceTable;

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
	
	
	private Library library;
	
	/** Lists Section **/

	@FXML
	private ListView<Setlist> setlistView;
	@FXML
	private TextField setlistTitleField;
	@FXML
	private ListView<Piece> setlistPieceView;
	
	
	
	public void initModel(Library _library) {

		this.library = _library;
		
		// Initialize the listview on the left part of the screen that contains all of the setlists
		//setlistView = new ListView<AbstractSetlist>(library.getSetlists());
		assert setlistView != null;
		
		setlistView.setItems(library.getSetlists());

		setlistPieceTable.setItems(library.getPieces());
		
		
		setlistView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
			{ library.setCurrentSetlist(newSelection); });

		library.getCurrentSetlistProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				setlistPieceView.setItems(newSelection.getPieceList());
				setlistTitleField.setText(newSelection.toString());
			} else {
				setlistPieceView.setItems(null);
				setlistTitleField.setText("");
			}
		});
		
		
		// If the user types in the setlist name field, update the name of the setlist!
		
		
		setlistTitleField.textProperty().addListener((obs, oldText, newText) -> {
			if (library.getCurrentSetlist() != null) {
				library.getCurrentSetlist().setName(setlistTitleField.textProperty().get());
				setlistView.refresh();
			}
		});

		pieceTitleColumn.setCellValueFactory(new PropertyValueFactory<Piece, String>("title"));
		pieceComposerColumn.setCellValueFactory(new PropertyValueFactory<Piece, String>("composer"));
		pieceArrangerColumn.setCellValueFactory(new PropertyValueFactory<Piece, String>("arranger"));
		pieceYearColumn.setCellValueFactory(new PropertyValueFactory<Piece, Integer>("year"));
		pieceDurationColumn.setCellValueFactory(new PropertyValueFactory<Piece, Integer>("duration"));
		
		
	}
	
	@FXML
	private void newList() {
		System.out.println("TEST");
		library.addSetlist("New Setlist");
	}
	
	@FXML
	private void deleteList() {
		library.deleteCurrentSetlist();
		setlistPieceView.getSelectionModel().clearSelection();
		setlistView.getSelectionModel().clearSelection();
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
		App.ShowTempAlert("Action not yet implemented!");
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
	
}
