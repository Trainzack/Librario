package eli.projects.spprototype.vista;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import eli.projects.spprototype.controller.ReorderableListCell;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class SetlistPresenter implements Initializable {

	@Inject
	Setlist list;
	
	@FXML
	Label titleLabel;
	
	@FXML
	TextField titleField;
	
	@FXML
	Label pieceCountLabel;
	
	@FXML
	ListView<Piece> pieceView;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Make interactive
		
		titleLabel.textProperty().set(list.getName());
		titleField.textProperty().set(list.getName());
		
		pieceCountLabel.textProperty().set(""+list.getLengthProperty().get());
		
		pieceView.setItems(list.getPieceList());
		
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
		
		
	}

}
