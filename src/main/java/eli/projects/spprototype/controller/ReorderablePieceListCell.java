package eli.projects.spprototype.controller;

import eli.projects.spprototype.model.Piece;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.input.*;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Adapted from 
 * https://stackoverflow.com/questions/28603224/sort-tableview-with-drag-and-drop-rows/28606524
 * @author Eli
 */
public class ReorderablePieceListCell extends ListCell<Piece> {

	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
	
	public ReorderablePieceListCell() {
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
				Piece draggedPiece = getListView().getItems().remove(draggedIndex);

				int dropIndex; 

				if (this.isEmpty()) {
					dropIndex = getListView().getItems().size() ;
				} else {
					dropIndex = this.getIndex();
				}

				getListView().getItems().add(dropIndex, draggedPiece);

				event.setDropCompleted(true);
				getListView().getSelectionModel().select(dropIndex);
				event.consume();
			}
		});
		
		setOnKeyPressed(event -> {
			System.out.println(event.getCode());
		});
	}
}