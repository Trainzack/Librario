package eli.projects.spprototype.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class TabPieceEdit extends AbstractTab {

	public TabPieceEdit() {
		super("📜 Edit Piece");
		
		FileInputStream inputstream;
		try {
			inputstream = new FileInputStream("files/Placeholder.png");
			Image placeholder = new Image(inputstream); 
			
			ImageView iv = new ImageView(placeholder);
			iv.setFitHeight(600);
			
			TreeView pieceTree = new TreeView();
			
			TreeItem root = new TreeItem("Piece");
			pieceTree.setRoot(root);
			root.expandedProperty().set(true);
			pieceTree.setShowRoot(false);
			
			
			Random r = new Random();
			//TODO this is all mockup
			String[] instruments = {"Flute", "Clarinet", "Alto Saxophone", "Tenor Saxophone", "F. Horn", "Trumpet", "Trombone", "Tuba","Drums"};
			for (String i : instruments) {
				TreeItem instItem = new TreeItem(i);
				root.getChildren().add(instItem);
				int pageCount = r.nextInt(3) + 1;
				for (int x = 0; x < pageCount; x++) {
					TreeItem page = new TreeItem("Page " + (x + 1));
					instItem.getChildren().add(page);
				}
			}
			
			
			TextArea placeholderArea = new TextArea("This is placeholder for a whole bank of controls.");
			placeholderArea.setMaxWidth(250);
			VBox panelControls = new VBox(placeholderArea);

			VBox leftBox = new VBox(pieceTree, panelControls);
			leftBox.setSpacing(10);

			this.addChildren(leftBox, iv);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
