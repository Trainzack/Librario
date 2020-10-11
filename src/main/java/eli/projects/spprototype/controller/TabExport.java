package eli.projects.spprototype.controller;

import java.util.ArrayList;

import eli.projects.spprototype.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class TabExport extends AbstractTab {

	public TabExport() {
		super("➡ Export");
		
		GridPane exportControls = new GridPane();
		
		ObservableList<String> ensembleList = FXCollections.observableArrayList(new String[] {"Ensemble"});
		ComboBox ensemble = new ComboBox(ensembleList);
		ensemble.getSelectionModel().selectFirst();
		exportControls.add(ensemble, 0, 0);
		
		ObservableList<String> listList = FXCollections.observableArrayList(new String[] {"List / Piece"});
		ComboBox list = new ComboBox(listList);
		list.getSelectionModel().selectFirst();
		exportControls.add(list, 0, 1);

		ObservableList<String> paperSizeList = FXCollections.observableArrayList(new String[] {"Paper Size"});
		ComboBox paperSize = new ComboBox(paperSizeList);
		paperSize.getSelectionModel().selectFirst();
		exportControls.add(paperSize, 0, 2);
		
		CheckBox doublePapers = new CheckBox("Double Pages per Paper");
		exportControls.add(doublePapers, 0, 3);
		
		CheckBox doubleSidedOrder = new CheckBox("Double-sided order");
		exportControls.add(doubleSidedOrder, 0, 4);
		
		CheckBox createFoldersSong = new CheckBox("Create Folders per Song");
		exportControls.add(createFoldersSong, 0, 5);
		
		CheckBox createFoldersInstrument = new CheckBox("Create Folders per Instrument");
		exportControls.add(createFoldersInstrument, 0, 6);
		

		Button export = App.createTempButton("➡ Export", "This will start the exporting action");
		export.setStyle(App.STYLE_LARGE_BUTTON);
		exportControls.add(export, 0, 7);
		
		Label preview = new Label("PREVIEW");
		preview.setStyle(App.STYLE_HEADER);

		this.addChildren(exportControls, preview);
	}

}
