package eli.projects.spprototype.vista;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.controlsfx.control.tableview2.TableView2;

import eli.projects.spprototype.App;
import eli.projects.spprototype.Part;
import eli.projects.spprototype.Utility;
import eli.projects.spprototype.infrastructure.PieceService;
import eli.projects.spprototype.model.Ensemble;
import eli.projects.spprototype.model.Instrument;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Section;
import eli.projects.spprototype.model.Setlist;
import javafx.fxml.Initializable;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class PiecePresenter extends Vista implements Initializable {

	private FilteredList<Piece> filteredPieces;
	
	@Inject private Piece piece;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}

	@Override
	public ReadOnlyStringProperty getTitleProperty() {
		return piece.titleProperty();
	}

	@Override
	public ReadOnlyStringProperty getIconLiteralProperty() {
		// TODO Auto-generated method stub
		return new SimpleStringProperty("enty-music");
	}

}
