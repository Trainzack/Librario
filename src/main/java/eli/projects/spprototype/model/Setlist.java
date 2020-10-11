package eli.projects.spprototype.model;

import java.util.ArrayList;
import java.util.Optional;

import eli.projects.spprototype.App;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * This is a list of pieces that users can create, delete, edit, and manipulate in any way.
 * @author Eli
 *
 */
public class Setlist {
	
	private String name;
	
	// This variable contains the pieces in the tableview.
	private ObservableList<Piece> observableList;
	
	private TableView<Piece> setlistPieceTable;
	
	public Setlist(String name) {
		super();
		
		this.name = name;
		observableList = FXCollections.observableArrayList(new ArrayList<Piece>());
		
	}
	
	/**
	 * Generates a new UserSetlist filled with testing data.
	 * @return
	 */
	public static Setlist generateTestUserSetlist() {
		
		Setlist out = new Setlist("Test List");
		
		for (Piece p : Piece.generateFakePieces(12)) {
			out.add(p);
		}
		
		return out;
	}

	public ObservableList<Piece> getPieceList() {
		return observableList;
	}
	
	public void add(Piece newPiece) {
		observableList.add(newPiece);
	}

	public String getName() {
		return name;
	}
	
	public int getLength() {
		return observableList.size();
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isUserList() {
		return true;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
/*
	@Override
	public Node getControls() {
		
		TableColumn<Piece, String> nameColumn = new TableColumn<Piece, String>("Piece");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Piece, String>("title"));
		TableColumn<Piece, Integer> durationColumn = new TableColumn<Piece, Integer>("Duration");
		durationColumn.setCellValueFactory(new PropertyValueFactory<Piece, Integer>("duration"));
		
		/*
		TextField searchField = new TextField();
		Button searchButton = App.createTempButton( "🔎 Search", "Search");
		
		HBox pieceSearchControls = new HBox(searchField, searchButton);
		pieceSearchControls.setSpacing(5);
		*

		/**
		 * Set up the tableView
		 *
		setlistPieceTable = new TableView<Piece>(this.getPieceList());
		setlistPieceTable.setPrefWidth(800);
		setlistPieceTable.setMaxHeight(350);

		setlistPieceTable.getColumns().add(nameColumn);
		setlistPieceTable.getColumns().add(durationColumn);
		
		/**
		 * TODO
		 * Maybe we should make the entire details pane its own class heirarchy 
		 *
		
		Button tableRemove = new Button("➖ Remove Piece");
		
		tableRemove.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				
				Piece selectedPiece = setlistPieceTable.getSelectionModel().getSelectedItem();
				
				if (selectedPiece == null) {
					App.ShowError("Cannot Remove Piece", "No Piece Selected");
					return;
				}
				
				Alert confirmation = new Alert(AlertType.CONFIRMATION);
				confirmation.setContentText("Are you sure you want to remove \"" + selectedPiece.getTitle() + "\" from " + name + "?");
				confirmation.setTitle("Delete List");
				ButtonType buttonTypeDelete = new ButtonType("Remove \"" + selectedPiece.getTitle() + "\"");
				ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
				confirmation.getButtonTypes().setAll(buttonTypeDelete, buttonTypeCancel);

				Optional<ButtonType> result = confirmation.showAndWait();
				if (result.get() == buttonTypeDelete){
					observableList.remove(selectedPiece);
				}
				
			}
		});
		
		Button tableMoveUp = new Button("⬆ Move Up");
		
		tableMoveUp.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Piece selectedPiece = setlistPieceTable.getSelectionModel().getSelectedItem();
				if (selectedPiece == null) return;
				App.moveListItem(observableList, selectedPiece, -1);
				setlistPieceTable.getSelectionModel().selectAboveCell();
			}	
		});
		
		Button tableMoveDown = new Button("⬇ Move Down");
		
		tableMoveDown.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Piece selectedPiece = setlistPieceTable.getSelectionModel().getSelectedItem();
				if (selectedPiece == null) return;
				App.moveListItem(observableList, selectedPiece, 1);
				// I don't know why this needs to be done twice.
				setlistPieceTable.getSelectionModel().selectBelowCell();
				setlistPieceTable.getSelectionModel().selectBelowCell();
			}	
		});
		
		HBox tableModControls = new HBox(tableRemove, tableMoveUp, tableMoveDown);
		tableModControls.setSpacing(5);
		
		
		VBox piecesBox = new VBox(new Separator(), /*pieceSearchControls,*  setlistPieceTable, tableModControls, new Separator());
		piecesBox.setSpacing(10);
		piecesBox.setPadding(new Insets(20,0,20,0));
		piecesBox.setVgrow(setlistPieceTable, Priority.ALWAYS);
		
		return piecesBox;
	}*/

	
	
	
}
