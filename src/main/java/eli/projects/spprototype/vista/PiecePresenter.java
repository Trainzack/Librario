package eli.projects.spprototype.vista;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.controlsfx.control.tableview2.TableView2;

import eli.projects.spprototype.App;
import eli.projects.spprototype.DocumentSource;
import eli.projects.spprototype.Part;
import eli.projects.spprototype.PartDesignation;
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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class PiecePresenter extends Vista implements Initializable {

	@Inject private Piece piece;
	
	@FXML private TableView2<Part> partTable;
	@FXML private TableColumn<Part, PartDesignation> partDesignationColumn;
	@FXML private TableColumn<Part, Integer> partPageCountColumn;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		partTable.setItems(piece.getParts());
		
		partDesignationColumn.setCellValueFactory(new PropertyValueFactory<Part, PartDesignation>("designation"));
		partPageCountColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("pages"));
		
		TableColumn<Part, String> documentSourceColum = new TableColumn<>();
		documentSourceColum.setText("PDF");
		partTable.getColumns().add(documentSourceColum);
		
		documentSourceColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Part, String> param) {
				// TODO Auto-generated method stub
				String out = "";
				for (DocumentSource d : param.getValue().getDocumentSources()) {
					out += d.toString();
				}
				
				return new SimpleStringProperty(out);
			}

		});
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
