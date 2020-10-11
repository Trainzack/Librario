package eli.projects.spprototype;

import java.io.IOException;

import eli.projects.spprototype.controller.EnsembleView;
import eli.projects.spprototype.controller.LibraryController;
import eli.projects.spprototype.controller.TabExport;
import eli.projects.spprototype.controller.TabLibrary;
import eli.projects.spprototype.controller.TabPieceEdit;
import eli.projects.spprototype.model.Library;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 
 * @author Eli
 *
 */
public class App extends Application
{

	static final String WINDOW_NAME = "Eli's Senior Project";
	
	// TODO: extract to css
	public static final String STYLE_LARGE_BUTTON = "-fx-font: 16 arial; -fx-padding: 10";
	public static final String STYLE_HEADER = "-fx-font: 32 arial; -fx-padding: 10";
	
	// TODO: Put this in some kind of datamodel?
	private static Library loadedLibrary;
	
    public static void main( String[] args )
    {
    	
    	launch();
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");

        // TODO Testing code, should be replaced later.
        loadedLibrary = Library.generateTestingLibrary();
        
        /*
        // TODO: Add icons to these (maybe https://kordamp.org/ikonli/#_javafx)
        Tab tabPieces = new TabLibrary();
        Tab tabEnsembles = new EnsembleView();
        Tab tabPieceEdit = new TabPieceEdit();
        Tab tabExport = new TabExport();
        
        TabPane mainPane = new TabPane(tabPieces, tabEnsembles, tabPieceEdit, tabExport);
        
        for (Tab t : mainPane.getTabs()) {
        	t.setClosable(false);
        	t.setStyle(STYLE_LARGE_BUTTON);
        }
        
        /**
         * Ensembles
         */
        
        
        
        
        /*
        // Make the menuBar
        //   TODO: Separate the menubar into its own class or something.
        
        MenuItem menuFileNew = new MenuItem("New Library");
        MenuItem menuFileOpen = new MenuItem("Open Library");
        
        Menu menuFile = new Menu("File", null, menuFileNew, menuFileOpen);
        
        MenuItem menuEditUndo = new MenuItem("Undo");
        MenuItem menuEditRedo = new MenuItem("Redo");
        
        Menu menuEdit = new Menu("Edit", null, menuEditUndo, menuEditRedo);
        

        MenuItem menuHelpAbout = new MenuItem("About");
        
        Menu menuHelp = new Menu("Help", null, menuHelpAbout);
        
        MenuBar menuBar = new MenuBar(menuFile, menuEdit, menuHelp);
        
        for (Menu m : menuBar.getMenus()) {
        	for (MenuItem i : m.getItems()) {
        		i.setDisable(true);
        	}
        }
        
        
        
        
        VBox OverallBox = new VBox(menuBar, mainPane);
        */
		try {
			FXMLLoader apploader = new FXMLLoader(getClass().getResource("/App.fxml"));
	        
			assert apploader.getLocation() != null;
			
			Parent root = apploader.load();
			
			LibraryController libraryController = apploader.getController();
			
	        libraryController.initModel(loadedLibrary);
	        
			
			Scene scene = new Scene(root, 1280, 800);
	        primaryStage.setScene(scene);
	        
	        primaryStage.setTitle(WINDOW_NAME);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        primaryStage.show();
		
	}
	
	public static void ShowTempAlert(String text) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Temporary Placeholder Alert");
		alert.setHeaderText(text);
		alert.setContentText("[This action is not yet implemented]");

		alert.showAndWait();
	}
	
	public static void ShowError(String error, String reason) {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(error);
		alert.setContentText(reason);

		alert.showAndWait();
	}
	
	
	public static Button createTempButton(String text, final String alertText) {
		Button b = new Button(text);
		
		b.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				App.ShowTempAlert(alertText);
			}
		});
		
		return b;
	}
	
	
	// TODO: This belongs in another class
	public static <E> void moveListItem(ObservableList<E> list, E item, int deltaIndex) {
		int oldIndex = list.indexOf(item);
		int desiredIndex = oldIndex + deltaIndex;
		
		// Keep the desired index in list bounds
		desiredIndex = desiredIndex > 0 ? desiredIndex : 0;
		desiredIndex = desiredIndex < list.size() ? desiredIndex : list.size() - 1;
		list.remove(oldIndex);
		list.add(desiredIndex, item);
		
	}
	
	
}
