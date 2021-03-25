package eli.projects.spprototype.controller;

import eli.projects.spprototype.exporting.ExportGroupType;
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

public class ExportGroupListCell extends ReorderableListCell<ExportGroupType> {

	private CheckBox enabledBox;
	private HBox controlBox;
	private HBox subBox;
	private Label orderLabel;
	private Label controlLabel1;
	private Label controlLabel2;
	private ChoiceBox<String> groupTypeChoiceBox;
	
	private ExportGroupType group;
	
	private ChangeListener<? super Number> orderListener; 
	
	private static String[] numberStrings = {
			"","⓿", "❶", "❷", "❸", "❹", "❺", "❻", "❼", "❽", "❾", "❿",
			 "⓫", "⓬", "⓭", "⓮", "⓯", "⓰", "⓱", "⓲", "⓳", "⓴"	
	};
	
	public ExportGroupListCell() {
		super(false);
		
		Node grabGraphic = this.getGraphic();
		
		enabledBox = new CheckBox();
		
		orderLabel = new Label(numberStrings[0]);
		orderLabel.setMinWidth(20);
		
		
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
		// TODO: This really belongs as part of a subclass of listView. As it is now, each item adds its own redundant listener.
		this.listViewProperty().addListener((obs, oldList, newList) -> {
			if (oldList != null) {
				// I don't think we'll ever move a list cell from one listview to another, but if we do we would run into the problem of
				// having an invalid listener on the old listview.
				// Rather than fixing this (by keeping a reference to the listener we create and removing it when necessary), I will simply prevent it.
				throw new Error("Cannot change ExportGroupListCell listview");
			}
			newList.getItems().addListener((ListChangeListener<ExportGroupType>)(change) -> {
				updateListItemOrder();
			});
		});
		
		subBox.setDisable(true);
		
		controlBox = new HBox(grabGraphic, enabledBox, orderLabel, subBox);
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

		int enabledCount = 0;
		for (ExportGroupType g : this.getListView().getItems()) {
			if (g.isEnabled()) {
				g.getOrderProperty().set(++enabledCount);
			} else {
				g.getOrderProperty().set(-1);
			}
			
		}
	}
	
	@Override
	public void updateItem(ExportGroupType item, boolean empty) {
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
        	// TODO: When dragging the top item to the bottom, a bunch of boxes don't get their selected property properly bound to the model. Fix!
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
