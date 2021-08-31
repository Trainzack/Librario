package eli.projects.spprototype;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import com.airhacks.afterburner.injection.Injector;

import eli.projects.spprototype.infrastructure.EnsembleService;
import eli.projects.spprototype.infrastructure.LibraryService;
import eli.projects.spprototype.infrastructure.ListService;
import eli.projects.spprototype.infrastructure.PieceService;
import eli.projects.spprototype.model.Ensemble;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
import eli.projects.spprototype.vista.*;
import eli.projects.util.DataFormats;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DirectorySelectionPresenter implements Initializable {

	// The stage that contains this controller's views
	@Inject
	private Stage primaryStage;
	@Inject
	private File initialFile;
	
	
	@FXML
	private Button load;
	@FXML
	private TextField locationField;
	@FXML
	private CheckBox rememberThisDirectory;

	// TODO Find a way to get this programmatically
	static float SIDEBAR_LIST_CELL_HEIGHT = 24;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	void selectLocation() {
		//
	}
	
	@FXML
	void load() {
		this.primaryStage.close();
	}


}
