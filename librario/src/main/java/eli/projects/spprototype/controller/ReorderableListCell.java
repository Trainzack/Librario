package eli.projects.spprototype.controller;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.text.FontSmoothingType;

/**
 * Adapted from 
 * https://stackoverflow.com/questions/28603224/sort-tableview-with-drag-and-drop-rows/28606524
 * @author Eli
 */
public class ReorderableListCell<T> extends ListCell<T> {

	// This type belongs here, as opposed to util.DataFormats because it is specific to this class
	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object-reorderable-list-cell-index");
	
	
	public ReorderableListCell() {
		this(false);
	}
	
	public ReorderableListCell(boolean deleteable) {
		super();
		
		FontIcon dragGraphic = new FontIcon("enty-menu"); // "mdi-drag"
		dragGraphic.setIconSize(10);
		dragGraphic.setFontSmoothingType(FontSmoothingType.LCD);
		dragGraphic.setVisible(false);
		setGraphic(dragGraphic);
		
		// The drag graphic should only be shown when this item has something in it! 
		this.emptyProperty().addListener((obs, oldValue, newValue) -> {
			dragGraphic.setVisible(!newValue);
		});
		
		
		setOnDragDetected(event -> {
			if (! this.isEmpty()) {
				Integer index = this.getIndex();
				Dragboard dragBoard = this.startDragAndDrop(TransferMode.MOVE);
				dragBoard.setDragView(this.snapshot(null, null));
				ClipboardContent cc = new ClipboardContent();
				cc.put(SERIALIZED_MIME_TYPE, index);
				dragBoard.setContent(cc);
				event.consume();
			}
		});

		setOnDragOver(event -> {
			Dragboard dragBoard = event.getDragboard();
			if (dragBoard.hasContent(SERIALIZED_MIME_TYPE)) {
				if (this.getIndex() != ((Integer)dragBoard.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
					event.consume();
				}
			}
		});

		setOnDragDropped(event -> {
			Dragboard dragBoard = event.getDragboard();
			if (dragBoard.hasContent(SERIALIZED_MIME_TYPE)) {
				int draggedIndex = (Integer) dragBoard.getContent(SERIALIZED_MIME_TYPE);
				T draggedItem = getListView().getItems().remove(draggedIndex);

				int dropIndex; 

				if (this.isEmpty()) {
					dropIndex = getListView().getItems().size() ;
				} else {
					dropIndex = this.getIndex();
				}

				getListView().getItems().add(dropIndex, draggedItem);

				event.setDropCompleted(true);
				getListView().getSelectionModel().select(dropIndex);
				event.consume();
			}
		});
		
		// TODO: If deleteable, let us delete items with a key press.
		setOnKeyPressed(event -> {
			System.out.println(event.getCode());
		});
	}
}