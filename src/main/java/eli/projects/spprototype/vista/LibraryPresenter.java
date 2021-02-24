package eli.projects.spprototype.vista;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import eli.projects.spprototype.controller.ReorderableListCell;
import eli.projects.spprototype.infrastructure.LibraryService;
import eli.projects.spprototype.model.Instrument;
import eli.projects.spprototype.model.Library;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class LibraryPresenter extends Vista implements Initializable {
	
	private StringProperty title = new SimpleStringProperty();
	
	@Inject private LibraryService libraryService;
	
	@FXML private TextField titleField;
	@FXML private Label pieceCountLabel;
	
	@FXML private ListView<Instrument> scoreOrderList;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		Library l = libraryService.getLibrary();
		
		title.bind(Bindings.concat(l.getTitleProperty(), " settings"));
		
		titleField.textProperty().set(l.getTitle());

		
		
		pieceCountLabel.textProperty().set(""+l.getPieces().size());
		
		
		// Update list name when changing the name textfield
		this.registerListener(titleField.textProperty(),
				(obs, oldText, newText) -> {
			l.setTitle(titleField.textProperty().get());
		});
		
		// Set up Properties Table TODO
		
		// Set up Score order list
		scoreOrderList.setItems(l.getInstruments());
		
		scoreOrderList.setCellFactory(list -> new ReorderableListCell<Instrument>() {

		    @Override
		    protected void updateItem(Instrument item, boolean empty) {
		        super.updateItem(item, empty);
		        if (empty || item == null || item == null) {
		            setText(null);
		        } else {
		            setText(item.getName());
		        }
		    }
		});
		
	}

	@Override
	public void remove() {
		super.remove();
		
		
	}

	@Override
	public ReadOnlyStringProperty getTitleProperty() {
		return title;
	}

	@Override
	public ReadOnlyStringProperty getIconLiteralProperty() {
		return new SimpleStringProperty("enty-cog");
	}
	
	// ------------------------------- SCORE ORDER LIST -------------------------------
	
	@FXML public void editSelectedInstrument() {
		// TODO Method stub
	}
	

	@FXML public void deleteSelectedInstrument() {
		// TODO Method stub
	}

}
