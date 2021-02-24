package eli.projects.spprototype.vista;

import java.util.Stack;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.*;
import javafx.util.Callback;

/**
 * This class manages the vistas.
 * This might be a better match for the design paradigm if this is a controller, instead of being part of the frontpage controller.
 * @author Eli
 *
 */
public class VistaManager {
	
	private static final double INSET_AMOUNT = 10.0;
	
	private static final String DELIMINATOR_ICON_LITERAL = "enty-chevron-right";

	private AnchorPane vistaPane;
	private Button backButton;
	private HBox vistaStackHbox;
	
	private Stack<AbstractVistaView> vistaStack;
	private int selectedIndex;
	
	public VistaManager(AnchorPane vistaPane, Button backButton, HBox vistaStackHbox) {
		super();
		this.vistaPane = vistaPane;
		this.backButton = backButton;
		this.vistaStackHbox = vistaStackHbox;
		
		
		vistaStack = new Stack<>();
		clearVistaStack();
		
	}
	
	/**
	 * Loads a new vista in the vista pane, overriding everything else in the BreadCrumBar
	 * 
	 * @param vista The vista to load
	 * @param Node The vista's root node, to place inside the details pane
	 */
	public void setVista(AbstractVistaView vista) {
		clearVistaStack();
		pushVista(vista);
	}
	
	/**
	 * Loads a new vista in the vista pane, appending it to the end of the BreadCrumBar
	 * @param vista
	 * @param node
	 */
	public void pushVista(AbstractVistaView vista) {
		vistaStack.add(vista);
		Parent node = vista.getView();
		selectedIndex = vistaStack.size() - 1;
		this.vistaPane.getChildren().clear();
		this.vistaPane.getChildren().addAll(node);
		AnchorPane.setTopAnchor(	node, INSET_AMOUNT);
		AnchorPane.setBottomAnchor(node, INSET_AMOUNT);
		AnchorPane.setLeftAnchor(	node, INSET_AMOUNT);
		AnchorPane.setRightAnchor(	node, INSET_AMOUNT);
		updateBCB();
	}
	
	/**
	 * Pops and deletes the current vista.
	 */
	public void popVista() {
		AbstractVistaView old = vistaStack.pop();
		((Vista)old.getPresenter()).remove();
		selectedIndex -= 1;
		this.vistaPane.getChildren().clear();
		if (!this.vistaStack.isEmpty()) {
			this.vistaPane.getChildren().add(this.vistaStack.lastElement().getView());
		}
		
		updateBCB();
	}
	
	/**
	 * Removes the entire vista stack, and deletes the listeners the vistas have created.
	 */
	public void clearVistaStack() {
		
		for (AbstractVistaView v : vistaStack) {((Vista) v.getPresenter()).remove();}
		vistaStack.clear();
		this.vistaPane.getChildren().clear();
		selectedIndex = -1;
		updateBCB();
	}
	
	/**
	 * Rebuilds the BreadCrumbBar
	 */
	private void updateBCB() {
		
		vistaStackHbox.getChildren().clear();
		
		if (vistaStack.size() > 0) {
			
			for(int i = 0; i < vistaStack.size(); i++) {
				Vista v = (Vista) vistaStack.get(i).getPresenter();
				String title = "";
				if (i == vistaStack.size() - 1) {
					title = v.getTitleProperty().get();
				}
				vistaStackHbox.getChildren().add(new Label(
						title,
						new FontIcon(v.getIconLiteralProperty().get())
					)
						);
				if (i < vistaStack.size() - 1) {
					vistaStackHbox.getChildren().add(new FontIcon(DELIMINATOR_ICON_LITERAL));
				}
			}
			
			backButton.setDisable(false);
		} else {

			backButton.setDisable(true);
		}


	}

	
	
	
}
