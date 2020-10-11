package eli.projects.spprototype.controller;

import eli.projects.spprototype.App;
import eli.projects.spprototype.model.Ensemble;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
public class EnsembleView extends AbstractTab {

	private ListView<Ensemble> ensembleList;
	
	// TODO: Better way to keep track of ensembles? ObservableList?
	private Ensemble[] ensembles;
	
	private Ensemble selectedEnsemble;
	
	private Label ensembleNameLabel;
	private Label ensembleContentLabel;
	private TableView<String> ensembleInstrumentTable;
	
	public EnsembleView() {
		super("🎺 Ensembles");
		
		ensembleList = new ListView<Ensemble>(App.loadedLibrary.getEnsembles());
		
		// This is the code that allows the details on the right to change when the user clicks on an ensemble on the left.
		ensembleList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ensemble>()
		{
			public void changed(ObservableValue<? extends Ensemble> observable, Ensemble oldValue, Ensemble newValue) {
				selectedEnsemble = newValue;
				updateDetails();
			}
			
		});
		
		ensembleNameLabel = new Label("--");
		ensembleNameLabel.setStyle(App.STYLE_HEADER);
		ensembleContentLabel = new Label("Members: 0");
		
		// TODO: Link up the table to the instruments
		ensembleInstrumentTable = new TableView<String>();

		TableColumn<String, String> nameColumn = new TableColumn<String, String>("Instrument");
		TableColumn<String, Integer> countColumn = new TableColumn<String, Integer>("Number");
		
		
		ensembleInstrumentTable = new TableView();

		ensembleInstrumentTable.getColumns().add(nameColumn);
		ensembleInstrumentTable.getColumns().add(countColumn);
		
		ensembleInstrumentTable.getItems().add("Test");
		
		
		
		VBox detailsBox = new VBox(ensembleNameLabel, ensembleContentLabel, ensembleInstrumentTable);
		detailsBox.setSpacing(10);
		
		Button ensembleAdd = App.createTempButton("➕ New Ensemble", "This will create a new blank ensemble");
		Button ensembleRemove = App.createTempButton("➖ Delete Ensemble", "Are you sure you want to delete the currently selected ensemble?");
		HBox ensembleModControls = new HBox(ensembleAdd, ensembleRemove);
		ensembleModControls.setSpacing(10);
		
		VBox leftBox = new VBox(ensembleList, ensembleModControls);
		leftBox.setSpacing(10);
		
		this.addChildren(leftBox, detailsBox);
		
		
	}
	
	/**
	 * Update the details pane.
	 *
	private void updateDetails() {
		ensembleNameLabel.setText(selectedEnsemble.getName());
		ensembleContentLabel.setText("Members: " + selectedEnsemble.getNumberOfMembers());
	}
	
}
*/