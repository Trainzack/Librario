package eli.projects.spprototype.controller;

import eli.projects.spprototype.App;
import eli.projects.spprototype.model.Library;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
	private TableColumn<Piece, Integer> pieceDurationColumn;
	
	private Library library;
	
	/** Lists Section **/

	@FXML
	private ListView<Setlist> setlistView;
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
			} else {
				setlistPieceView.setItems(null);
			}
		});
		
		pieceTitleColumn.setCellValueFactory(new PropertyValueFactory<Piece, String>("title"));
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
