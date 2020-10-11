package eli.projects.spprototype.controller;


import java.io.IOException;
import java.util.Optional;

import eli.projects.spprototype.App;
import eli.projects.spprototype.model.AbstractSetlist;
import eli.projects.spprototype.model.Library;
import eli.projects.spprototype.model.UserSetlist;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class TabLibrary extends AbstractTab {

	private ListView<AbstractSetlist> setlistView;
	
	private Label setlistNameLabel;
	private Label setlistContentLabel;
	
	private Pane pieceViewControlNode;
	
	private Library library;
	/*
	public TabLibrary(Library _library) {
		super("🎼 Library");
		
		this.library = _library;
		

		
		//TODO: EW
		this.addChildren(new Pane(), root);
		
		
		/*
		
		// Initialize the listview on the left part of the screen that contains all of the setlists
		setlistView = new ListView<AbstractSetlist>(library.getSetlists());
		
		// This is the code that allows the details on the right to change when the user clicks on a setlist on the left.
		setlistView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
			{ library.setCurrentSetlist(newSelection); });
		
		library.getCurrentSetlistProperty().addListener((obs, oldSetlist, newSetlist) -> {
			if (newSetlist == null) {
				throw new Error("You never got around to making this work for null selections. Fixit!");
				// TODO
			} else {
				// Replace the controls with this node's controls.
				pieceViewControlNode.getChildren().setAll(newSetlist.getControls());
				updateDetails();
				
			}
		});
		
		
		setlistNameLabel = new Label("--");
		setlistNameLabel.setStyle(App.STYLE_HEADER);
		setlistContentLabel = new Label("Pieces: 0");

		pieceViewControlNode = new Pane();
		pieceViewControlNode.setMaxHeight(480);
		pieceViewControlNode.setPrefHeight(500);
		
		
		Button exportList = App.createTempButton("➡ Export List", "This will export the list");
		exportList.setStyle(App.STYLE_LARGE_BUTTON);
		Button exportPiece = App.createTempButton("➡ Export Selected Piece", "This will export the piece you have selected");
		exportPiece.setStyle(App.STYLE_LARGE_BUTTON);
		
		HBox exportBox = new HBox(exportList, exportPiece);
		exportBox.setSpacing(10);
		
		VBox detailsBox = new VBox(setlistNameLabel, setlistContentLabel, pieceViewControlNode, exportBox);
		detailsBox.setSpacing(10);
		detailsBox.setPadding(new Insets(10));
		

		Button listAdd = new Button("➕ New List");

		
		listAdd.setOnAction(new EventHandler<ActionEvent>() {
			
			// TODO: Set setlist name?
			public void handle(ActionEvent event) {
				library.addSetlist("New Setlist");
			}
		});
		
		Button listRemove = new Button("➖ Delete List");
		
		listRemove.setOnAction(() - > {
			library.deleteCurrentSetlist();
		});
		
		listRemove.setOnAction(new EventHandler<ActionEvent>() {
			
			this.library
			
			// TODO: Set setlist name?
			public void handle(ActionEvent event) {
				
				AbstractSetlist selectedSetlist = library.getCurrentSetlist();
				
				if (library.getCurrentSetlist() == null) {
					App.ShowError("Cannot Delete List", "No List Selected");
					return;
				} else if (!library.getCurrentSetlist().isUserList()) {
					App.ShowError("Cannot Delete List", "You cannot delete \"" + selectedSetlist.getName() + "\".");
					return;
				}
				
				Alert confirmation = new Alert(AlertType.CONFIRMATION);
				confirmation.setContentText("Are you sure you want to delete \"" + selectedSetlist.getName() + "\"?");
				confirmation.setTitle("Delete List");
				ButtonType buttonTypeDelete = new ButtonType("Delete \"" + selectedSetlist.getName() + "\"");
				ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
				confirmation.getButtonTypes().setAll(buttonTypeDelete, buttonTypeCancel);

				Optional<ButtonType> result = confirmation.showAndWait();
				if (result.get() == buttonTypeDelete){
					App.loadedLibrary.getSetlists().remove(selectedSetlist);
				}
				
			}
		});

 	
		HBox listModControls = new HBox(listAdd, listRemove);
		listModControls.setSpacing(10);
		
		VBox leftBox = new VBox(setlistView, listModControls);
		leftBox.setSpacing(10);
		
		
		Pane detailsPane = new Pane(detailsBox);

		this.addChildren(leftBox, detailsPane);

		detailsPane.prefWidthProperty().bind(((Region) detailsPane.getParent()).widthProperty());
		
		
		*/
	}
	
	/**
	 * Update the details pane.
	 *
	private void updateDetails() {
		setlistNameLabel.setText(selectedSetlist.getName());
		setlistContentLabel.setText("Pieces: " + selectedSetlist.getLength());
		
	}*/
	
}

