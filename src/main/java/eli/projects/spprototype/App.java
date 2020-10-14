package eli.projects.spprototype;

import java.io.IOException;
import java.net.URL;

import eli.projects.spprototype.controller.MainController;
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
import javafx.stage.Stage;

/**
 * https://kordamp.org/ikonli/cheat-sheet-entypo.html
 * @author Eli
 *
 */

public class App extends Application
{

	static final String WINDOW_NAME = "Eli's Senior Project";
	
	// TODO: Put this in some kind of datamodel?
	private static Library loadedLibrary;
	
	private static Stage primaryStage;
	
    public static void main( String[] args )
    {
    	
    	launch();
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		this.primaryStage = primaryStage;
		
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");

        // TODO Testing code, should be replaced later.
        loadedLibrary = Library.generateTestingLibrary();

		try {
			
			URL fxmlLocation = getClass().getResource("/controller/MainController.fxml");
			
			System.out.println(fxmlLocation);
					
			FXMLLoader apploader = new FXMLLoader(fxmlLocation);
	        
			assert apploader.getLocation() != null;
			
			Parent root = apploader.load();
			
			MainController libraryController = apploader.getController();
			
	        libraryController.initModel(loadedLibrary, primaryStage);
	        
			
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
