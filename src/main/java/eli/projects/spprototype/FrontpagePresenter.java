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
	
	@Inject private Map<Object, Object> context;
	
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
		context.put("vistaManager", vistaManager);
		
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
				context.put("list", newSelection);

				Injector.setConfigurationSource(context::get);
				AbstractVistaView listView = new SetlistView();
		        vistaManager.setVista(listView);
		        
				ensembleSidebarListView.getSelectionModel().clearSelection();
			}
		});
		
		setlistSidebarListView.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				if (event.getGestureSource() != setlistSidebarListView) {
					Dragboard db = event.getDragboard();
					if (db.hasContent(DataFormats.PIECE_MIME_TYPE)) {
						event.acceptTransferModes(TransferMode.LINK);
					}
				}
				event.consume();
				
			}
			
		});
		
		// TODO: Add other sidebar drag events, as per https://docs.oracle.com/javafx/2/drag_drop/jfxpub-drag_drop.htm
		// Note: The PIECE reference probably won't work right due to unserialized properties, so may need to make an ID or hashing system
		
		
		// ---------------- All Sidebar ListViews ------------
		
		// Set all sidebars to be of the same height.
		for (ListView l : sidebars) {

			l.prefHeightProperty().bind(Bindings.size((ObservableList) l.itemsProperty().get()).multiply(SIDEBAR_LIST_CELL_HEIGHT).add(1));
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
		context.put("pieceService", pieceService);

		Injector.setConfigurationSource(context::get);
		AbstractVistaView piecesView = new PiecesView();
        vistaManager.setVista(piecesView);
	}
	
	
	@FXML
	private void openRandomPiece() {
		List<Piece> l = pieceService.getItems();
		if (l.size() < 1) return;
		Piece selectedPiece = l.get(App.randomGenerator.nextInt(l.size()));
		context.put("piece", selectedPiece);
		
		Injector.setConfigurationSource(context::get);
        PieceView pieceView = new PieceView();
        vistaManager.setVista(pieceView);
	}
	
	@FXML
	private void openLibrarySettings() {
		
		context.put("libraryService", libraryService);
		
		Injector.setConfigurationSource(context::get);
        AbstractVistaView newVista = new LibraryView();
        vistaManager.setVista(newVista);
	}
	
	@FXML
	private void search() {
		App.ShowTempAlert("You searched for " + searchField.getText() + "!");
	}
	
	@FXML
	private void createNewList() {
		listService.addItem(new Setlist("Untitled Setlist"));
	}
	
	@FXML
	private void createNewEnsemble() {
		ensembleService.addItem(new Ensemble("Untitled Ensemble"));
	}
	
	
	@FXML
	private void goToPreviousVista() {
		vistaManager.popVista();
	}

	
	

}
