package eli.projects.spprototype.model;

import java.util.ArrayList;
import java.util.Optional;

import eli.projects.spprototype.App;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
	
	// This variable contains the pieces that are in this setlist.
	private ObservableList<Piece> observableList;
	
	
	private IntegerProperty numberOfPieces = new SimpleIntegerProperty(0);
	
	public Setlist(String name) {
		super();
		
		this.name = name;
		observableList = FXCollections.observableArrayList(new ArrayList<Piece>());
		
		observableList.addListener(new ListChangeListener<Piece>() {
			@Override
			public void onChanged(Change<? extends Piece> c) {
				numberOfPieces.set(observableList.size());
				
			}
		});
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
	
	public void remove(Piece piece) {
		observableList.remove(piece);
	}

	public String getName() {
		return name;
	}
	
	public IntegerProperty getLengthProperty() {
		return numberOfPieces;
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
	
}
