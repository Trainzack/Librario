package eli.projects.spprototype;

import java.net.URL;
import java.util.HashMap;
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
	
	
	
	// This is the service that provides us the lists we display.
	@Inject private LibraryService libraryService;
	
	private EnsembleService ensembleService;
	private ListService listService;
	private PieceService pieceService;
	
	// Version information
	@Inject private String javaVersion;
	@Inject private String javafxVersion;
	@Inject private String appVersion;
	
	// This is the pane that will contain the other controllers we load in.
	@FXML private AnchorPane vistaPane;
	
	@FXML private Button controlLibrary;
	
	@FXML private TextField searchField;
	@FXML private Button searchButton;
	
	//@FXML private ListView<Piece> pieceSidebarListView;
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
		
		ensembleService = libraryService.getLibrary().getEnsembleService();
		listService = libraryService.getLibrary().getListService();
		pieceService = libraryService.getLibrary().getPieceService();
		
		leftStatus.setText("Version " + appVersion);
		rightStatus.setText("Java " + javaVersion + ", JavaFX " + javafxVersion);

		controlLibrary.textProperty().bind(libraryService.getLibrary().getTitleProperty());
		
		
		// ------------- Vista Manager ----------------------
		
		vistaManager = new VistaManager(vistaPane, backButton, vistaStackHbox);
		
		// -------------- Sidebars --------------------------
		
		@SuppressWarnings("rawtypes")
		ListView[] sidebars = {
				ensembleSidebarListView,
				setlistSidebarListView,
		};
		
		// ------------- Ensemble Sidebar -------------------		
		
		ensembleSidebarListView.setItems(ensembleService.getItems());
		
		
		
		ensembleSidebarListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			
			if (newSelection != null) {
				
				// TODO wrap this in factory
				Map<Object, Object> context = new HashMap<>();
				context.put("primaryStage", primaryStage);
				context.put("ensemble", newSelection);
				
				Injector.setConfigurationSource(context::get);
		        
				AbstractVistaView ensembleView = new EnsembleView();
				
		        vistaManager.setVista(ensembleView);
		        setlistSidebarListView.getSelectionModel().clearSelection();
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
		        
				AbstractVistaView listView = new SetlistView();
				
		        
		        vistaManager.setVista(listView);
				ensembleSidebarListView.getSelectionModel().clearSelection();
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
		context.put("vistaManager", vistaManager);
		
		Injector.setConfigurationSource(context::get);
        
		AbstractVistaView piecesView = new PiecesView();
		
        vistaManager.setVista(piecesView);
	}
	
	
	@FXML
	private void openRandomPiece() {
		List<Piece> l = pieceService.getItems();
		if (l.size() < 1) return;
		Piece selectedPiece = l.get(App.randomGenerator.nextInt(l.size()));
		Map<Object, Object> context = new HashMap<>();
		context.put("piece", selectedPiece);
		
		Injector.setConfigurationSource(context::get);
        PieceView pieceView = new PieceView();
		
        vistaManager.setVista(pieceView);
	}
	
	@FXML
	private void openLibrarySettings() {
		Map<Object, Object> context = new HashMap<>();
		context.put("primaryStage", primaryStage);
		context.put("libraryService", libraryService);
		context.put("vistaManager", vistaManager);
		
		Injector.setConfigurationSource(context::get);
        
        AbstractVistaView newVista = new LibraryView();
		
        vistaManager.setVista(newVista);
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
