package eli.projects.spprototype;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * https://kordamp.org/ikonli/cheat-sheet-entypo.html
 * @author Eli
 *
 */

public class App extends Application
{

	public static final String WINDOW_NAME = "Ossia";
	
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
	        
	        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/ossiaLogo.png")));
	        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/ossiaLogo32.png")));
	        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/ossiaLogo16.png")));
	        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/ossiaLogo48.png")));
	        
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
		alert.initOwner(primaryStage); // Should fix alerts taking up the fullscreen on MacOS, and should make sure that the window reliably pops up in front of the main window.

		alert.showAndWait();
	}
	
	public static void ShowError(String error, String reason) {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(error);
		alert.setContentText(reason);
		alert.initOwner(primaryStage);

		alert.showAndWait();
	}
	
	/**
	 * Show a confirmation dialog box.
	 * @param title The title of the dialog box
	 * @param content The text describing the confirmation
	 * @param actionName This is the string that is displayed on the button that does the action
	 * @return true if the user confirmed, false if not
	 */
	public static boolean showConfirmationDialog(String title, String content, String actionName) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.initOwner(primaryStage);

		ButtonType buttonTypeAction = new ButtonType(actionName);
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeAction, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		return (result.get() == buttonTypeAction);
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
