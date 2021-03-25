package eli.projects.spprototype.vista;

import java.util.HashMap;
import java.util.Map;

import eli.projects.spprototype.model.PaperSize;
import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;

public abstract class Vista {
	
	// We keep track of all listeners so that we can remove them all before we delete the vista
	// This way we can ensure garbage collection picks up the listeners.
	@SuppressWarnings("rawtypes")
	Map<ChangeListener, ReadOnlyProperty> changeListeners = new HashMap<>();
	@SuppressWarnings("rawtypes")
	Map<InvalidationListener, ReadOnlyProperty> invalidationListeners = new HashMap<>();
	
	/**
	 * Called once when the vista is no longer being shown.
	 * This method will remove all listners.
	 */
	public final void remove() {
		for (@SuppressWarnings("rawtypes") ChangeListener l : changeListeners.keySet()) {
			ReadOnlyProperty p = changeListeners.get(l);
			p.removeListener(l);
		}
		for (@SuppressWarnings("rawtypes") InvalidationListener l : invalidationListeners.keySet()) {
			ReadOnlyProperty p = invalidationListeners.get(l);
			p.removeListener(l);
		}
	}
	
	/**
	 * This method returns the ReadOnlyStringProperty that contains the title of this vista, for use in the breadcrumb bar.
	 * @return The ReadOnlyStringProperty
	 */
	public abstract ReadOnlyStringProperty getTitleProperty();
	
	/**
	 * This method returns the ReadOnlyStringProperty that contains the icon literal of this vista, for use in the breadcrumb bar.
	 * @return The ReadOnlyStringProperty
	 */
	public abstract ReadOnlyStringProperty getIconLiteralProperty();
	
	/**
	 * Registers the given listener to the specified property.
	 * 
	 * This does two things:
	 * 1) it adds the listener to the property.
	 * 2) it adds the listener to the list that will get removed when .remove() is called on the vista.
	 * 
	 * @param p The property to register the listener to.
	 * @param l The ChangeListener to register
	 */
	@SuppressWarnings("unchecked")
	protected void registerListener(ReadOnlyProperty p, ChangeListener l) {
		p.addListener(l);
		changeListeners.put(l, p);
	}
	
	
	/**
	 * Registers the given listener to the specified property.
	 * 
	 * This does two things:
	 * 1) it adds the listener to the property.
	 * 2) it adds the listener to the list that will get removed when .remove() is called on the vista.
	 * 
	 * @param p The property to register the listener to.
	 * @param l The InvalidationListener to register
	 */
	@SuppressWarnings("unchecked")
	protected void registerListener(ReadOnlyProperty p, InvalidationListener l) {
		p.addListener(l);
		invalidationListeners.put(l, p);
	}

}
