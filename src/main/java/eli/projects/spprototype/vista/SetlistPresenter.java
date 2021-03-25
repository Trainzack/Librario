package eli.projects.spprototype.vista;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import com.airhacks.afterburner.injection.Injector;

import eli.projects.spprototype.App;
import eli.projects.spprototype.controller.ReorderableListCell;
import eli.projects.spprototype.exporting.ExportSettings;
import eli.projects.spprototype.infrastructure.LibraryService;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SetlistPresenter extends Vista implements Initializable {
	
	@Inject private Setlist list;
	@Inject private Stage primaryStage;
	@Inject private Map<Object, Object> context;
	@Inject private VistaManager vistaManager;
	
	@Inject private LibraryService libraryService;
	
	@FXML private TextField titleField;
	@FXML private Label pieceCountLabel;
	@FXML private ListView<Piece> pieceView;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		// TODO Make interactive
		
		
		titleField.textProperty().set(list.getName());
		
		pieceCountLabel.textProperty().set(""+list.getLengthProperty().get());
		
		pieceView.setItems(list.getPieceList());
		
		pieceView.setPlaceholder(new Label("There are no pieces in this list! You can add pieces to this list in the Pieces tab, by dragging them from the table to the name of this list."));
		
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
		
		if (App.showConfirmationDialog("List Deletion", "Are you sure you want to delete '" + this.list.getName() +"'?", "Delete")) {
			vistaManager.clearVistaStack();
			libraryService.getLibrary().getListService().deleteItem(this.list);
		}
		
	}
	
	@FXML
	public void exportList() {
		
		ExportSettings es = new ExportSettings(this.list.getPieceList(), this.list.getName());
		
		context.put("primaryStage", primaryStage);
		context.put("exportSettings", es);
		
		Injector.setConfigurationSource(context::get);
		
		AbstractVistaView exportView = new ExportView();
		
        vistaManager.pushVista(exportView);
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
