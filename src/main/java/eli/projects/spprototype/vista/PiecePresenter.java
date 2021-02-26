package eli.projects.spprototype.vista;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import com.airhacks.afterburner.injection.Injector;

import eli.projects.spprototype.DocumentSource;
import eli.projects.spprototype.Part;
import eli.projects.spprototype.PartDesignation;
import eli.projects.spprototype.infrastructure.LibraryService;
import eli.projects.spprototype.model.ExportSettings;
import eli.projects.spprototype.model.ExportSettings.SourceSelection;
import eli.projects.spprototype.model.Piece;
import javafx.fxml.Initializable;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class PiecePresenter extends Vista implements Initializable {

	@Inject private Piece piece;
	@Inject private Stage primaryStage;
	@Inject private LibraryService libraryService;
	@Inject private Map<Object, Object> context;
	@Inject private VistaManager vistaManager;
	
	@FXML private TableView<Part> partTable;
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
	
	@FXML
	public void exportPiece() {
		// TODO wrap this in factory
		
		ExportSettings es = new ExportSettings(this.libraryService.getLibrary());
		es.getSelectedSourceProperty().set(SourceSelection.PIECE);
		es.getSelectedExportPieceProperty().set(piece);
		
		context.put("primaryStage", primaryStage);
		context.put("exportSettings", es);
		
		Injector.setConfigurationSource(context::get);
		
        
		AbstractVistaView exportView = new ExportView();
		
        vistaManager.pushVista(exportView);
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
