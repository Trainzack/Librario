package eli.projects.spprototype;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import com.airhacks.afterburner.injection.Injector;

import eli.projects.spprototype.infrastructure.InMemoryListService;
import eli.projects.spprototype.infrastructure.ListService;
import eli.projects.spprototype.model.Setlist;
import eli.projects.spprototype.vista.SetlistView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FrontpagePresenter implements Initializable {
	
	// The stage that contains this controller's views
	@Inject
	private Stage primaryStage;
	
	// This is the service that provides us the lists we display.
	@Inject
	private ListService listService;
	
	// This is the pane that will contain the other controllers we load in.
	@FXML
	private AnchorPane vistaPane;
	
	
	@FXML
	private ListView<Setlist> setlistSidebarListView;


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Request confirmation before closing window.
		// TODO: Does this belong here? I think not!
		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				boolean userWantsToQuit = App.showConfirmationDialog("Quit?", "Are you sure you want to quit Ossia?", "Quit");
				if (!userWantsToQuit) event.consume();
			}
		});
		
		
		//setlistSidebarListView.setCellFactory(param -> new );
		setlistSidebarListView.setItems(listService.getSetlists());
		
		// TODO: Surely there's a better way. Make listview buttons?
		setlistSidebarListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			
			if (newSelection != null) {
				
				// TODO wrap this in factory
				Map<Object, Object> context = new HashMap<>();
				context.put("primaryStage", primaryStage);
				context.put("list", newSelection);
				
				Injector.setConfigurationSource(context::get);
		        
		        SetlistView listView = new SetlistView();
				
				this.setVista(listView.getView());
			}
		});
		
	}
	
	
	/**
	 * Loads a new pane in the vista pane.
	 * 
	 * @param node The node to place inside the details pane
	 */
	public void setVista(Node node) {
		this.vistaPane.getChildren().clear();
		this.vistaPane.getChildren().addAll(node);
	     AnchorPane.setTopAnchor(node, 10.0);
	     AnchorPane.setBottomAnchor(node, 10.0);
	     AnchorPane.setLeftAnchor(node, 10.0);
	     AnchorPane.setRightAnchor(node, 10.0);
	}
	
	@FXML
	private void quitProgram() {
		//TODO: This and window close request should call the same function
		boolean userWantsToQuit = App.showConfirmationDialog("Quit?", "Are you sure you want to quit?", "Quit");
		// TODO: check to see if we have unsaved work
		
		if (userWantsToQuit) {
			this.primaryStage.close();	
		}
		
	}
	
	

}
