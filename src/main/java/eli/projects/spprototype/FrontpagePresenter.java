package eli.projects.spprototype;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import com.airhacks.afterburner.injection.Injector;

import eli.projects.spprototype.infrastructure.EnsembleService;
import eli.projects.spprototype.infrastructure.InMemoryLibraryService;
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

public class FrontpagePresenter implements Initializable {

	private VistaManager vistaManager;

	// The stage that contains this controller's views
	@Inject
	private Stage primaryStage;

	
	private LibraryService libraryService;
	private EnsembleService ensembleService;
	private ListService listService;
	private PieceService pieceService;

	// Version information
	@Inject
	private String javaVersion;
	@Inject
	private String javafxVersion;
	@Inject
	private String appVersion;

	@Inject
	private Map<Object, Object> context;

	// This is the pane that will contain the other controllers we load in.
	@FXML
	private AnchorPane vistaPane;

	@FXML
	private TextField searchField;
	@FXML
	private Button searchButton;

	// @FXML private ListView<Piece> pieceSidebarListView;
	@FXML
	private ListView<Ensemble> ensembleSidebarListView;
	@FXML
	private ListView<Setlist> setlistSidebarListView;

	@FXML
	private Button backButton;
	@FXML
	private HBox vistaStackHbox;

	@FXML
	private Label leftStatus;
	@FXML
	private Label rightStatus;

	// TODO Find a way to get this programmatically
	static float SIDEBAR_LIST_CELL_HEIGHT = 24;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if (App.getAppSettings().getLibraryLocation() != null) {
			File libraryLocation = new File(App.getAppSettings().getLibraryLocation());
			if (libraryLocation.exists()) {
				this.setLibraryService(new InMemoryLibraryService(libraryLocation));
			}
			
		}
		
		leftStatus.setText("Version " + appVersion);
		rightStatus.setText("Java " + javaVersion + ", JavaFX " + javafxVersion);

		// Change name of library label
		// controlLibrary.textProperty().bind(libraryService.getLibrary().getTitleProperty());

		// ------------- Vista Manager ----------------------

		vistaManager = new VistaManager(vistaPane, backButton, vistaStackHbox);
		context.put("vistaManager", vistaManager);

		// -------------- Sidebars --------------------------

		@SuppressWarnings("rawtypes")
		ListView[] sidebars = { ensembleSidebarListView, setlistSidebarListView, };

		// ------------- Ensemble Sidebar -------------------


		ensembleSidebarListView.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {

					if (newSelection != null) {
						setEnsembleVista(newSelection);
						setlistSidebarListView.getSelectionModel().clearSelection();
					}
				});

		// ------------- Setlist Sidebar -------------------


		// TODO: Surely there's a better way. Make listview buttons?
		setlistSidebarListView.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {

					if (newSelection != null) {
						setListVista(newSelection);
						ensembleSidebarListView.getSelectionModel().clearSelection();
					}
				});

		// Allow Pieces to be drag and dropped into the list.
		setlistSidebarListView.setCellFactory(new setlistListCellFactory());

		// TODO: Add other sidebar drag events, as per
		// https://docs.oracle.com/javafx/2/drag_drop/jfxpub-drag_drop.htm
		// Note: The PIECE reference probably won't work right due to unserialized
		// properties, so may need to make an ID or hashing system

		// ---------------- All Sidebar ListViews ------------

		// Set all sidebars to be of the same height.
		for (ListView l : sidebars) {

			l.prefHeightProperty().bind(
					Bindings.size((ObservableList) l.itemsProperty().get()).multiply(SIDEBAR_LIST_CELL_HEIGHT).add(1));
		}

	}
	
	private void setLibraryService(LibraryService s) {
		this.libraryService = s;
		
		this.ensembleService = libraryService.getLibrary().getEnsembleService();
		this.listService = libraryService.getLibrary().getListService();
		this.pieceService = libraryService.getLibrary().getPieceService();

		ensembleSidebarListView.setItems(ensembleService.getItems());
		setlistSidebarListView.setItems(listService.getItems());

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
	private void openLibrary() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		
		if (App.getAppSettings().getLibraryLocation() != null) {
			File def = new File(App.getAppSettings().getLibraryLocation());
			
			if (def.exists() && def.isDirectory()) {
				directoryChooser.setInitialDirectory(def);
			}
			
		}
		
		directoryChooser.setTitle("Please select a library.");
		File selected = directoryChooser.showDialog(primaryStage);
		
		if (selected != null) {
			this.setLibraryService(new InMemoryLibraryService(selected));
			App.getAppSettings().setLibraryLocation(selected.getAbsolutePath());
			this.openPieces();
		}
		
	}

	@FXML
	private void openPieces() {
		if (pieceService == null) return;
		context.put("pieceService", pieceService);

		Injector.setConfigurationSource(context::get);
		AbstractVistaView piecesView = new PiecesView();
		vistaManager.setVista(piecesView);
	}

	@FXML
	private void openRandomPiece() {
		if (pieceService == null) return;
		List<Piece> l = pieceService.getItems();
		if (l.size() < 1)
			return;
		Piece selectedPiece = l.get(App.randomGenerator.nextInt(l.size()));
		setPieceVista(selectedPiece);
	}

	@FXML
	private void openLibrarySettings() {
		if (libraryService == null) return;

		context.put("libraryService", libraryService);

		Injector.setConfigurationSource(context::get);
		AbstractVistaView newVista = new LibraryView();
		vistaManager.setVista(newVista);
	}

	@FXML
	private void search() {
		App.showTempAlert("You searched for " + searchField.getText() + "!");
	}

	@FXML
	private void createNewList() {
		Setlist l = new Setlist("Untitled Setlist");
		listService.addItem(l);
		setListVista(l);
	}

	@FXML
	private void createNewEnsemble() {
		Ensemble e = new Ensemble("Untitled Ensemble");
		ensembleService.addItem(e);
		setEnsembleVista(e);
	}

	@FXML
	private void goToPreviousVista() {
		vistaManager.popVista();
	}

	/**
	 * Sets the vista to a new vista with the given piece.
	 * 
	 * @param p The piece to put in the vista
	 */
	private void setPieceVista(Piece p) {
		context.put("piece", p);
		Injector.setConfigurationSource(context::get);
		PieceView pieceView = new PieceView();
		vistaManager.setVista(pieceView);
	}

	/**
	 * Sets the vista to a new vista containing the given list.
	 * 
	 * @param l The list to put in the vista
	 */
	private void setListVista(Setlist l) {
		context.put("list", l);

		Injector.setConfigurationSource(context::get);
		AbstractVistaView listView = new SetlistView();
		vistaManager.setVista(listView);
	}

	/**
	 * Sets the vista to a new vista containing the given ensemble.
	 * 
	 * @param l The ensemble to put in the vista
	 */
	private void setEnsembleVista(Ensemble e) {
		context.put("ensemble", e);

		Injector.setConfigurationSource(context::get);
		AbstractVistaView ensembleView = new EnsembleView();
		vistaManager.setVista(ensembleView);
	}

}
