package eli.projects.spprototype;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import com.airhacks.afterburner.injection.Injector;

import eli.projects.spprototype.infrastructure.EnsembleService;
import eli.projects.spprototype.infrastructure.ListService;
import eli.projects.spprototype.infrastructure.PieceService;
import eli.projects.spprototype.model.Ensemble;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;
import eli.projects.spprototype.vista.*;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class FrontpagePresenter implements Initializable {
	
	private VistaManager vistaManager;
	
	// The stage that contains this controller's views
	@Inject private Stage primaryStage;
	
	
	@Inject private EnsembleService ensembleService;
	
	// This is the service that provides us the lists we display.
	@Inject private ListService listService;
	@Inject private PieceService pieceService;
	
	// Version information
	@Inject private String javaVersion;
	@Inject private String javafxVersion;
	@Inject private String appVersion;
	
	// This is the pane that will contain the other controllers we load in.
	@FXML private AnchorPane vistaPane;
	

	@FXML private Button buttonPieces;
	
	@FXML private TextField searchField;
	@FXML private Button searchButton;
	
	@FXML private ListView<Piece> pieceSidebarListView;
	@FXML private ListView<Ensemble> ensembleSidebarListView;
	@FXML private ListView<Setlist> setlistSidebarListView;
	
	@FXML private Button backButton;
	@FXML private HBox vistaStackHbox;
	
	@FXML private Label leftStatus;
	@FXML private Label rightStatus;
	
	// TODO Find a way to get this programmatically
	static float SIDEBAR_LIST_CELL_HEIGHT = 24;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		leftStatus.setText("Version " + appVersion);
		rightStatus.setText("Java " + javaVersion + ", JavaFX " + javafxVersion);
		
		vistaManager = new VistaManager(vistaPane, backButton, vistaStackHbox);
		ListView[] sidebars = {
				pieceSidebarListView,
				ensembleSidebarListView,
				setlistSidebarListView,
		};
		
		// ------------- Vista Manager ----------------------
		
		
		
		// ------------- Pieces Sidebar ---------------------
		

		
		// ------------- Ensemble Sidebar -------------------		
		
		ensembleSidebarListView.setItems(ensembleService.getItems());
		
		ensembleSidebarListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			
			if (newSelection != null) {
				
				// TODO wrap this in factory
				Map<Object, Object> context = new HashMap<>();
				context.put("primaryStage", primaryStage);
				context.put("ensemble", newSelection);
				
				Injector.setConfigurationSource(context::get);
		        
		        EnsembleView ensembleView = new EnsembleView();
				
		        vistaManager.setVista(ensembleView);
			}
		});		
		
		// ------------- Setlist Sidebar -------------------
		
		setlistSidebarListView.setItems(listService.getItems());
		
		// TODO: Surely there's a better way. Make listview buttons?
		setlistSidebarListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			
			if (newSelection != null) {
				
				// TODO wrap this in factory
				Map<Object, Object> context = new HashMap<>();
				context.put("primaryStage", primaryStage);
				context.put("list", newSelection);
				
				Injector.setConfigurationSource(context::get);
		        
		        SetlistView listView = new SetlistView();
				
		        
		        vistaManager.setVista(listView);
			}
		});
		
		
		// Set all sidebars to be of the same height.
		for (ListView l : sidebars) {

			l.prefHeightProperty().bind(Bindings.size((ObservableList) l.itemsProperty().get()).multiply(SIDEBAR_LIST_CELL_HEIGHT));
		}
		
	}

	
	@FXML
	private void quitProgram() {
		boolean userWantsToQuit = App.confirmQuit();
		// TODO: check to see if we have unsaved work
		
		if (userWantsToQuit) {
			this.primaryStage.close();	
		}
		
	}
	
	@FXML
	private void openPieces() {
		Map<Object, Object> context = new HashMap<>();
		context.put("primaryStage", primaryStage);
		context.put("pieceService", pieceService);
		
		Injector.setConfigurationSource(context::get);
        
        PiecesView piecesView = new PiecesView();
		
        vistaManager.setVista(piecesView);
	}
	
	@FXML
	private void search() {
		App.ShowTempAlert("You searched for " + searchField.getText() + "!");
	}
	
	@FXML
	private void goToPreviousVista() {
		vistaManager.popVista();
	}
	
	

}