package eli.projects.spprototype.controller;

import eli.projects.spprototype.model.ExportGroup;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ExportGroupListCell extends ReorderableListCell<ExportGroup> {

	private CheckBox enabledBox;
	private HBox controlBox;
	private HBox subBox;
	private Label orderLabel;
	private Label controlLabel1;
	private Label controlLabel2;
	private ChoiceBox<String> groupTypeChoiceBox;
	
	private ExportGroup group;
	
	private ChangeListener<? super Number> orderListener; 
	
	private static String[] numberStrings = {
			"","⓿", "❶", "❷", "❸", "❹", "❺", "❻", "❼", "❽", "❾", "❿",
			 "⓫", "⓬", "⓭", "⓮", "⓯", "⓰", "⓱", "⓲", "⓳", "⓴"	
	};
	
	public ExportGroupListCell() {
		super(false);
		
		Node grabGraphic = this.getGraphic();
		
		orderLabel = new Label(numberStrings[0]);
		orderLabel.setMinWidth(20);
		
		enabledBox = new CheckBox();
		
		controlLabel1 = new Label();
		controlLabel2 = new Label();
		
		// TODO: make this have a real data type.
		groupTypeChoiceBox = new ChoiceBox<>();
		
		groupTypeChoiceBox.setItems(FXCollections.observableArrayList(new String[] {"Folders", "PDF Files", "Order"}));
		
		subBox = new HBox(controlLabel1, groupTypeChoiceBox, controlLabel2);
		
		// TODO: maybe this should be bound to the group's property instead?
		enabledBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			subBox.setDisable(!newValue);
			updateListItemOrder();
		});
		
		// When the order of this list changes, we need to update the item order text.
		this.listViewProperty().addListener((obs, oldList, newList) -> {
			if (oldList != null) {
				// I don't think we'll ever move a list cell from one listview to another, but if we do we would run into the problem of
				// having an invalid listener on the old listview.
				// Rather than fixing this (by keeping a reference to the listener we create and removing it when necessary), I will simply prevent it.
				throw new Error("Cannot change ExportGroupListCell listview");
			}
			newList.getItems().addListener((ListChangeListener<ExportGroup>)(change) -> {
				updateListItemOrder();
			});
		});
		
		subBox.setDisable(true);
		
		controlBox = new HBox(grabGraphic, orderLabel, enabledBox, subBox);
		controlBox.setSpacing(10);
		
		// Make sure everything's vertically centered.
		subBox.setAlignment(Pos.CENTER_LEFT);
		controlBox.setAlignment(Pos.CENTER_LEFT);
		
		
		orderListener = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				updateItemOrderLabel(newValue.intValue());
			}};
		
		
	}
	
	
	private void updateItemOrderLabel(int number) {

		// TODO: add safety if this new value is bigger than the array.
		orderLabel.setText(numberStrings[number+1]);
	}
	
	/**
	 * Updates the OrderProperty on all of the ExportGroups contained in the same ListView as this one
	 * 
	 * TODO: This method probably belongs somewhere else.
	 */
	private void updateListItemOrder() {

		System.out.println("Update!");
		int enabledCount = 0;
		for (ExportGroup g : this.getListView().getItems()) {
			if (g.isEnabled()) {
				g.getOrderProperty().set(++enabledCount);
			} else {
				g.getOrderProperty().set(-1);
			}
			
		}
	}
	
	@Override
	public void updateItem(ExportGroup item, boolean empty) {
        super.updateItem(item, empty);
        setEditable(false);
        
        if (group != null) {
        	// Unbind everything from the previous group
        	group.getEnabledProperty().unbind();
        	group.getOrderProperty().removeListener(orderListener);
        }
        
        if (item != null) {        
        	group = item;
        	controlLabel1.setText("Group ");
        	controlLabel2.setText(" by " + item.getName());
        	
        	this.enabledBox.setSelected(item.isEnabled());
        	item.getEnabledProperty().bind(this.enabledBox.selectedProperty());
        	item.getOrderProperty().addListener(orderListener);
        	updateItemOrderLabel(item.getOrder());
        	
            setGraphic(controlBox);
        } else {
            setGraphic(null);
            group = null;
        }
    }  
	
	
	
}
