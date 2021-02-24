package eli.projects.spprototype.vista;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.controlsfx.control.tableview2.TableView2;

import com.airhacks.afterburner.injection.Injector;

import eli.projects.spprototype.App;
import eli.projects.spprototype.Part;
import eli.projects.spprototype.Utility;
import eli.projects.spprototype.infrastructure.PieceService;
import eli.projects.spprototype.model.Ensemble;
import eli.projects.spprototype.model.Instrument;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Section;
import eli.projects.spprototype.model.Setlist;
import javafx.fxml.Initializable;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class PiecesPresenter extends Vista implements Initializable {

	private FilteredList<Piece> filteredPieces;
	
	@Inject private PieceService pieceService;
	@Inject private VistaManager vistaManager;
	
	@FXML private TextField searchFilterField;
	
	@FXML private TableView2<Piece> pieceTable;

	@FXML private TableColumn<Piece, String> pieceTitleColumn;
	@FXML private TableColumn<Piece, String> pieceComposerColumn;
	@FXML private TableColumn<Piece, String> pieceArrangerColumn;
	@FXML private TableColumn<Piece, Integer> pieceYearColumn;
	@FXML private TableColumn<Piece, Integer> pieceDurationColumn;
	
	@FXML private ToolBar pieceButtonBar;
	
	@FXML private Label selectionCountLabel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		/** Piece Table **/
		
		// When we type in the search box, update right away.
		searchFilterField.textProperty().addListener((obs, oldValue, newValue) -> {
			filterPieceTable();
		});
		
		filteredPieces = new FilteredList<>(pieceService.getItems());
		
		SortedList<Piece> sortedPieces = new SortedList<>(filteredPieces);
		
		sortedPieces.comparatorProperty().bind(pieceTable.comparatorProperty());
		
		pieceTable.setItems(sortedPieces);
		
		pieceTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		pieceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldPiece, newPiece) -> {
			pieceButtonBar.setDisable(newPiece == null);
			pieceTable.getSelectionModel().select(newPiece); // Why does this line exist?
			
			selectionCountLabel.setText(Utility.pluralizer("piece", pieceTable.getSelectionModel().getSelectedCells().size()) + " selected");
			
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
		pieceTable.getColumns().add(piecePartCountColum);
		piecePartCountColum.setVisible(false);

	}
	
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
	private void editSelectedPiece() {
		
		ObservableList<Piece> selection = pieceTable.getSelectionModel().getSelectedItems();
		if (selection.size() <= 0) {
			
			
			
		} else {
			Piece selectedPiece = selection.get(0);
			Map<Object, Object> context = new HashMap<>();
			context.put("piece", selectedPiece);
			
			Injector.setConfigurationSource(context::get);
	        PieceView pieceView = new PieceView();
			
	        vistaManager.pushVista(pieceView);
		}
	}

	@FXML
	private void deleteSelectedPiece() {
		
		ObservableList<Piece> selection = pieceTable.getSelectionModel().getSelectedItems();
		
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
				pieceService.deleteItems(selection);
			}
		}

	}
	
	@FXML
	private void exportSelectedPiece() {
		// TODO Method stub
	}
	
	@FXML
	private void addPieceToList() {
		// TODO Method stub
	}

	@Override
	public ReadOnlyStringProperty getTitleProperty() {
		return new SimpleStringProperty("Pieces");
	}

	@Override
	public ReadOnlyStringProperty getIconLiteralProperty() {
		// TODO Auto-generated method stub
		return new SimpleStringProperty("enty-beamed-note");
	}

}
