package eli.projects.spprototype.exporting;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import eli.projects.spprototype.Part;
import eli.projects.spprototype.exporting.PaperSettings.FinalPaperSettings;
import eli.projects.spprototype.model.Ensemble;
import eli.projects.spprototype.model.Instrument;
import eli.projects.spprototype.model.PaperSize;
import eli.projects.spprototype.model.Piece;
import eli.projects.spprototype.model.Setlist;


public class ExportSettings {
	
	/**
	 * Defines which target we are going for.
	 * @author Eli
	 *
	 */
	public enum TargetSelection {
		ALL,
		ENSEMBLE,
		INSTRUMENT;
	}
	
	private PaperSettings mainPaperSettings = new PaperSettings();


	private List<Piece> exportPieces;
	private String exportName;
	
	// This controls where we want to export things to.
	private ObjectProperty<File> exportDestination = new SimpleObjectProperty<File>(null);
	
	// This property is false iff the settings contained within this class are valid, and we are free to export.
	private BooleanProperty isInvalid = new SimpleBooleanProperty(true);
	
	
	private ObjectProperty<Ensemble> selectedExportEnsemble = new SimpleObjectProperty<Ensemble>(null);
	private ObjectProperty<Instrument> selectedExportInstrument = new SimpleObjectProperty<Instrument>(null);
	
	// These two control which type of source/target we are using
	private ObjectProperty<TargetSelection> selectedTarget = new SimpleObjectProperty<TargetSelection>();
	
	// Paper settings
	
	private BooleanProperty doublePages = new SimpleBooleanProperty(false);
	private BooleanProperty fitToPage = new SimpleBooleanProperty(false);
	
	/**This list contains any properties that, when changed, may change the validity of the settings. **/ 
	@SuppressWarnings("rawtypes")
	private final ObservableValue[] validityProperties =  {
			selectedExportEnsemble,
			selectedExportInstrument,
			selectedTarget,
		};
	
	/**
	 * 
	 * @param exportPieces A list containing all of the pieces that we want to export.
	 * @param exportName A string containing the name of this export, as displayed at the top of the page.
	 */
	@SuppressWarnings("unchecked")
	public ExportSettings(List<Piece> exportPieces, String exportName) {

			this.exportPieces = exportPieces;
			this.exportName = exportName;
		
		// Check validity after every setting change
		for (@SuppressWarnings("rawtypes") ObservableValue p : validityProperties ) {
			p.addListener((obs, oldValue, newValue) -> evaluateValidity());
		}
		
	}
	
	public BooleanProperty getIsInvalidProperty() {
		return this.isInvalid;
	}
	
	/**
	 * Determine whether or not the current settings of this object is valid to export, and update the isValid property to match.
	 */
	public void evaluateValidity() {
		
		boolean invalid = false;
		
		if (this.selectedTarget.get() == null) {
			invalid = true;
		} else {
			//TODO This section is messy AF. There must be a better way. Probably involving a hashmap.

			// Test to make sure that we have a selection for the target field
			Object fullProperty = null;
			switch (this.selectedTarget.get()) {
				case ALL:
					fullProperty = new Object();
					break;
				case ENSEMBLE:
					fullProperty = this.selectedExportEnsemble.get();
					break;
				case INSTRUMENT:
					fullProperty = this.selectedExportInstrument.get();
					break;
			}
			if (fullProperty == null) invalid = true;
			
		}
		
		//TODO Check paper size
		
		this.isInvalid.set(invalid);
	}
	
	/* Get Selected Objects
	 * 
	 * I'm not going to make individual getters and setters for each property any more, I don't think we need them. 
	 * 
	 */
	
	/** Target **/
	
	public ObjectProperty<Ensemble> getSelectedExportEnsembleProperty() {
		return selectedExportEnsemble;
	}
	
	public ObjectProperty<Instrument> getSelectedExportInstrumentProperty() {
		return selectedExportInstrument;
	}
	
	public final ObjectProperty<TargetSelection> getSelectedTargetProperty() {
		return selectedTarget;
	}

	/** Export Destination Folder **/

	public final ObjectProperty<File> getExportDestinationProperty() {
		return exportDestination;
	}
	
	public final File getExportDestination() {
		return exportDestination.get();
	}
	
	public final void setExportDestination(File destination) {
		exportDestination.set(destination);
	}
	

	/** Paper Properties **/

	
	public PaperSettings getMainPaperSettings() {
		return mainPaperSettings;
	}

	public void setMainPaperSettings(PaperSettings mainPaperSettings) {
		this.mainPaperSettings = mainPaperSettings;
	}
	
	
	
	public final BooleanProperty getDoublePagesProperty() {
		return doublePages;
	}

	public final boolean getDoublePages() {
		return doublePages.get();
	}
	
	public final void setDoublePages(boolean dub) {
		doublePages.set(dub);
	}
	

	public final BooleanProperty getFitToPageProperty() {
		return fitToPage;
	}
	
	public final boolean getFitToPage() {
		return fitToPage.get();
	}
	
	public final void setFitToPage(boolean fit) {
		fitToPage.set(fit);
	}
	
	public String getName() {
		return this.exportName;
	}
	
	
	
	public ExportTask getExportTask() {
		
		PDRectangle paperDimensions;
				
		ArrayList<Part> exportParts = new ArrayList<>();
		
		// TODO add grouping

		Ensemble exportEnsemble = this.selectedExportEnsemble.get();
		Instrument exportInstrument = this.selectedExportInstrument.get();
		
		for (Piece p : exportPieces) {
			
			// TODO Execute this switch statement once to create a filter.
			switch (this.selectedTarget.get()) {
			case ALL:
				exportParts.addAll(p.getParts());
				break;
			case ENSEMBLE:
				throw new InvalidParameterException("Ensemble export is unimplemented.");
				// break;
			case INSTRUMENT:
				for (Part pa : p.getParts()) {
					if (exportInstrument == pa.getDesignation().getInstrument()) {
						exportParts.add(pa);
					}
				}
				break;
			default:
				break;
			}
			
			// TODO: Filter parts
		}
		
		if (mainPaperSettings.getPaperSize() != PaperSize.CUSTOM) {
			paperDimensions = mainPaperSettings.getPaperSize().getDimensions();
		} else {
			paperDimensions = new PDRectangle(10, 10); // TODO: This needs to grab from paperWidth and paperHeight.
		}
		
		// float margins = this.mainPaperSettings.getPaperMargin();
		
		if (getDoublePages()) {

			PaperSettings cutPaperSettings = new PaperSettings();
			cutPaperSettings.setPaperSize(PaperSize.FLIP_FOLDER);
			
			
			FinalPaperSettings pageSettings = mainPaperSettings.getFinalSettings(false, false);
			FinalPaperSettings cutPageSettings = cutPaperSettings.getFinalSettings(getFitToPage(), true);
			
			return new ExportTask(pageSettings, cutPageSettings, exportParts, getExportDestination());
		} else {

			FinalPaperSettings finalPageSettings = mainPaperSettings.getFinalSettings(getFitToPage(), false);
			return new ExportTask(finalPageSettings, exportParts, getExportDestination());
		}
		
		
	}
	
}
