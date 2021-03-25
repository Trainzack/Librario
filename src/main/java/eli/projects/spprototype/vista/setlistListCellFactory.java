package eli.projects.spprototype.vista;

import eli.projects.spprototype.model.Setlist;
import eli.projects.util.DataFormats;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

/**
 * This class creates ListCells that contain setlists. Each cell will accept a drag and drop transfer of a piece, which will add it to the end of the setlist.
 * @author Eli
 *
 */
public class setlistListCellFactory implements Callback<ListView<Setlist>, ListCell<Setlist>> {

	@Override
	public ListCell<Setlist> call(ListView<Setlist> param) {

		/**
		 * Set the text of this cell to the name of this setlist.
		 */
		ListCell<Setlist> l = new ListCell<Setlist>() {
			@Override
			protected void updateItem(Setlist item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null) {
					this.setText(item.getName());
				} else {
					this.setText("");
				}
			}
		};

		l.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				if (event.getGestureSource() != param) {
					Dragboard db = event.getDragboard();
					if (db.hasContent(DataFormats.PIECE_ARRAY_MIME_TYPE)) {
						event.acceptTransferModes(TransferMode.LINK);
					}
				}
				event.consume();

			}

		});

		l.setOnDragEntered(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				if (event.getGestureSource() != param) {
					Dragboard db = event.getDragboard();
					if (db.hasContent(DataFormats.PIECE_ARRAY_MIME_TYPE)) {
						if (!l.getStyleClass().contains("dragover")) l.getStyleClass().add("dragover");
						System.out.println(l.getStyleClass());
					}
				}

			}

		});

		l.setOnDragExited(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				l.getStyleClass().remove("dragover");

			}
		});

		l.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasContent(DataFormats.PIECE_ARRAY_MIME_TYPE)) {
					int[] IDs = (int[]) db.getContent(DataFormats.PIECE_ARRAY_MIME_TYPE);
					
					for (int i = 0; i < IDs.length; i++) {
						// If even one gets through, we count this as a success. I'm not sure if this is the desired behaviour.
						if (l.getItem().addFromID(IDs[i])) {
							success = true;
						}
					}
				}
				// Let the source know whether the IDs were successfully transferred
				event.setDropCompleted(success);
				event.consume();

			}

		});

		l.setOnDragDone(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				// I don't think anything needs to happen here? The original stays as it is.
				event.consume();
			}
			
		});

		return l;
	}
}
