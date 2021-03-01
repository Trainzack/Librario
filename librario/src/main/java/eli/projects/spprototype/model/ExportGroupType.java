package eli.projects.spprototype.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * This is part of the model that contains the information needed to properly group pieces upon export. 
 * @author Eli
 *
 */

public abstract class ExportGroupType {
	
	private BooleanProperty enabled;
	private IntegerProperty order;
	
	public ExportGroupType() {
		
		enabled = new SimpleBooleanProperty(false);
		order = new SimpleIntegerProperty(-1);
	}
	
	/**
	 * This boolean property controls whether this export group should be used to group the export. If false, this group will be ignored. 
	 * @return The enabled boolean property
	 */
	public BooleanProperty getEnabledProperty() {
		return enabled;
	}
	
	public boolean isEnabled() {
		return enabled.get();
	}
	
	/**
	 * This integer property represents the order in which this group is applied.
	 * 
	 * For instance, 1 means it is applied first, 2 means it is applied second, etc. -1 means it is disabled and therefore not applied. 
	 * @return
	 */
	public IntegerProperty getOrderProperty() {
		return order;
	}
	
	public int getOrder() {
		return order.get();
	}
	
	
	
	/**
	 * Get the human-readable name of the group
	 * @return A string containing the name of the group
	 */
	public abstract String getName();
	
	// TODO: The function to actually group things should be in here.
	
	/**
	 * Instantiates one of each type of export group.
	 * 
	 * @return An array containing one of each type of export group
	 */
	public static ExportGroupType[] getOneOfEachExportGroupType() {
		return new ExportGroupType[] {
			new PieceGroup(), 
			new SectionGroup(),
			new PartGroup(),
			new MusicianGroup(),
		};
	}

}

 class PieceGroup extends ExportGroupType {

	@Override
	public String getName() {
		return "piece";
	}
	
}

 class SectionGroup extends ExportGroupType {

	@Override
	public String getName() {
		return "section";
	}
	
}

 class PartGroup extends ExportGroupType {

	@Override
	public String getName() {
		return "part";
	}
	
}

 class MusicianGroup extends ExportGroupType {

	@Override
	public String getName() {
		return "musician";
	}
	
}
