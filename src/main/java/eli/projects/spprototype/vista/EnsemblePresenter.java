package eli.projects.spprototype.vista;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import eli.projects.spprototype.App;
import eli.projects.spprototype.model.Ensemble;
import eli.projects.spprototype.model.Instrument;
import eli.projects.spprototype.model.Section;
import eli.projects.spprototype.model.Setlist;
import javafx.fxml.Initializable;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class EnsemblePresenter extends Vista implements Initializable {

	@FXML 
	private Label nameLabel;
	
	@FXML
	private TextField nameField;
	
	@FXML
	private Label membersLabel;
	
	@FXML
	private TableView<Section> sectionTableView;

	@FXML
	private TableColumn<Section, Instrument> sectionInstrumentColumn;
	@FXML
	private TableColumn<Section, Integer> sectionCountColumn;
	
	@Inject
	Ensemble ensemble;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		

		sectionInstrumentColumn.setCellValueFactory(new PropertyValueFactory<Section, Instrument>("instrumentName"));
		sectionCountColumn.setCellValueFactory(new PropertyValueFactory<Section, Integer>("count"));
		
		
		sectionTableView.setItems(ensemble.getSections());
		nameField.setText(ensemble.getName());
		nameLabel.setText(ensemble.getName());
		membersLabel.setText(ensemble.getNumberOfMembers() + " members");

		
		registerListener((Property) ensemble.getMemberCountProperty(), (obs, oldValue, newValue) -> {
			membersLabel.setText(ensemble.getNumberOfMembers() + " members");
		});
		
		// Update ensemble name when changing the name textfield
		nameField.textProperty().addListener((obs, oldText, newText) -> {
			ensemble.setName(nameField.textProperty().get());
		});

	}
	
	
	
	@FXML
	private void newSection() {
		// TODO Method Stub
	}
	
	@FXML
	private void deleteSection() {
		
		Section currentSection = sectionTableView.getSelectionModel().getSelectedItem();
		
		if (currentSection != null) {
			ensemble.deleteSection(currentSection);
		}
	}



	@Override
	public ReadOnlyStringProperty getTitleProperty() {
		return this.ensemble.getNameProperty();
	}

	@Override
	public ReadOnlyStringProperty getIconLiteralProperty() {
		// TODO Auto-generated method stub
		return new SimpleStringProperty("enty-list");
	}

}
