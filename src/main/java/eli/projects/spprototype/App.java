package eli.projects.spprototype;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

import com.airhacks.afterburner.injection.Injector;

import eli.projects.spprototype.infrastructure.InMemoryLibraryService;
import eli.projects.spprototype.model.Library;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * https://kordamp.org/ikonli/cheat-sheet-entypo.html
 * https://www.wimdeblauwe.com/blog/2015/2015-03-24-introduction-to-using-javafx-with-afterburner.fx/
 * @author Eli
 *
 */

public class App extends Application
{
	

	public static final Random randomGenerator = new Random();
	
	public static final String WINDOW_NAME = "Librario";
	
	// TODO: Put this in some kind of datamodel?
	private static Library loadedLibrary;
	
	private static Stage primaryStage;

	private static Settings appSettings;
	
    public static void main( String[] args )
    {
    	
    	launch();
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// This lets us pop up a box whenever we throw an exception.
		Thread.setDefaultUncaughtExceptionHandler(App::handleError);
		
		// Request confirmation before closing window.
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				boolean userWantsToQuit = App.confirmQuit();
				if (!userWantsToQuit) event.consume();
			}
		});
		
		// Load settings
		appSettings = Settings.loadSettings();
		
		// Set up the context for depedancy injection
		Map<Object, Object> context = new HashMap<>();
		context.put("context", context);
		context.put("primaryStage", primaryStage);
		
		//context.put("libraryService", 	new InMemoryLibraryService(-1)); // Make up test library (no PDFS)

		
		
		context.put("javaVersion",  	System.getProperty("java.version"));
		context.put("javafxVersion",  	System.getProperty("javafx.version"));
		

		final Properties properties = new Properties();
		properties.load(getClass().getResourceAsStream("/project.properties"));
		
		assert !properties.isEmpty();
		context.put("appVersion",	  	properties.getProperty("version"));
		
		this.primaryStage = primaryStage;
		
        
        
        // TODO Testing code, should be replaced later.
        //loadedLibrary = Library.loadOldLibrary(); //Library.generateTestingLibrary(10);

        
        Injector.setConfigurationSource(context::get);
        
        FrontpageView mainView = new FrontpageView();
 
		Scene scene = new Scene(mainView.getView(), 1280, 800);
        primaryStage.setScene(scene);  
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/ossiaLogo.png")));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/ossiaLogo32.png")));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/ossiaLogo16.png")));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/ossiaLogo48.png")));
        
        primaryStage.setTitle(WINDOW_NAME);
        
        primaryStage.show();

	}
	
	public static Settings getAppSettings() {
		return appSettings;
	}

	private static void handleError(Thread t, Throwable e) {
        if (Platform.isFxApplicationThread()) {
        	showException(e, "An exception has occured.");
        } else {
            System.err.println("An unexpected error occurred in "+t);

        }
	}
	
	// TODO: method names not following proper capitalization.
	public static void showTempAlert(String text) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Temporary Placeholder Alert");
		alert.setHeaderText("This action is not yet implemented");
		alert.setContentText(text);
		alert.initOwner(primaryStage); // Should fix alerts taking up the fullscreen on MacOS, and should make sure that the window reliably pops up in front of the main window.

		alert.showAndWait();
	}
	
	public static void showError(String error, String reason) {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(error);
		alert.setContentText(reason);
		alert.initOwner(primaryStage);

		alert.showAndWait();
	}
	
	/**
	 * Show a dialog box with the stack-trace of an exception inside.
	 * @param e AThe exception to display in this box.
	 * @param description A human-readable description of the problem.
	 */
	public static void showException(Throwable throwable, String description) {
		showException(new Throwable[] {throwable}, description);
	}
	
	
	/**
	 * Show a dialog box with the stack-trace of an exception inside.
	 * @param e An array containing exceptions to display in this box.
	 * @param description A human-readable description of the problem.
	 */
	public static void showException(Throwable throwables[], String description) {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("An Exception Has Occured"); //This may not always give good results, testing is needed.
		
		if (throwables.length > 1) {
			alert.setHeaderText(throwables.length + " Exceptions Have Occured");
		} else {
			alert.setHeaderText("An Exception Has Occured");
		}
		alert.setContentText(description);
		
		StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        for (Throwable e : throwables) {e.printStackTrace(printWriter);};
        String stackTrace = stringWriter.toString();
        
        TextArea stackTraceArea = new TextArea(stackTrace);
        stackTraceArea.setEditable(false);
        stackTraceArea.setWrapText(true);
        
        stackTraceArea.setMaxWidth(Double.MAX_VALUE);
        stackTraceArea.setMinWidth(600);
        stackTraceArea.setMaxHeight(Double.MAX_VALUE);
        
        alert.getDialogPane().setExpandableContent(stackTraceArea);
        
        try {
			stringWriter.close();
			printWriter.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        
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
	
	/**
	 * Show the dialogue box asking if the user wants to quit.
	 * @return True if they want to quit, false otherwise
	 */
	public static boolean confirmQuit() {
		// TODO: Check if unsaved work?
		return App.showConfirmationDialog("Quit?", "Are you sure you want to quit?", "Quit");
	}
	
	
	public static Button createTempButton(String text, final String alertText) {
		Button b = new Button(text);
		
		b.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				App.showTempAlert(alertText);
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
