package eli.projects.spprototype.vista;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import eli.projects.spprototype.App;
import eli.projects.spprototype.controller.ReorderableListCell;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SetlistPresenter extends Vista implements Initializable {
	
	@Inject private Setlist list;
	
	@FXML private TextField titleField;
	@FXML private Label pieceCountLabel;
	@FXML private ListView<Piece> pieceView;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		// TODO Make interactive
		
		
		titleField.textProperty().set(list.getName());
		
		pieceCountLabel.textProperty().set(""+list.getLengthProperty().get());
		
		pieceView.setItems(list.getPieceList());
		
		// Allow the cells to be reordered.
		pieceView.setCellFactory(list -> new ReorderableListCell<Piece>() {

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
		
		// Update list name when changing the name textfield
		this.registerListener(titleField.textProperty(),
				(obs, oldText, newText) -> {
			list.setName(titleField.textProperty().get());
		});
		
	}
	
	@FXML
	public void deleteList() {
		// TODO Method stub
		App.ShowTempAlert("Not yet implemented!");
	}
	
	@FXML
	public void exportList() {
		// TODO Method stub
		App.ShowTempAlert("Not yet implemented!");
	}

	@Override
	public ReadOnlyStringProperty getTitleProperty() {
		// TODO Auto-generated method stub
		return list.getNameProperty();
	}

	@Override
	public ReadOnlyStringProperty getIconLiteralProperty() {
		// TODO Auto-generated method stub
		return new SimpleStringProperty("enty-list");
	}

}
