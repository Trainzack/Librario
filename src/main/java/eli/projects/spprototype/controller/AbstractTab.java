package eli.projects.spprototype.controller;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

abstract class AbstractTab extends Tab {

	private Pane contentPane;
	private HBox boxSetup;
	private Pane detailsPane;
	private Pane leftPane;
	
	public AbstractTab(String name) {
		super(name);
    	this.setClosable(false);
    	
    	 detailsPane = new Pane();
    	 leftPane = new Pane();
    	
    	HBox boxSetup = new HBox(leftPane, detailsPane);
    	
   	 	//detailsPane.prefWidthProperty().bind(boxSetup.widthProperty());
    	//detailsPane.prefHeightProperty().bind(boxSetup.heightProperty());
    	
    	contentPane = new Pane(boxSetup);
    	boxSetup.setPadding(new Insets(15));
    	boxSetup.setSpacing(30);
    	this.setContent(contentPane);
    	
		// TODO Auto-generated constructor stub
	}
	
	public void addChildren(Node left, Node right) {
		leftPane.getChildren().add(left);
		detailsPane.getChildren().add(right);
	}

}
