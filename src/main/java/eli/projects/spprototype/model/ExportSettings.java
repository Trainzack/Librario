package eli.projects.spprototype.model;

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

import eli.projects.spprototype.ExportTask;
import eli.projects.spprototype.PageSettings;
import eli.projects.spprototype.Part;


public class ExportSettings {
	
	private static final PDRectangle flipFolderPaperSize = new PDRectangle(480, 354); // Almost B6 paper, but a little smaller
	
	/**
	 * Defines which source we are pulling pieces from
	 * @author Eli
	 *
	 */
	public enum SourceSelection {
		LIST,
		PIECE,
	}
	
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
	
	private Library library;


	// This controls where we want to export things to.
	private ObjectProperty<File> exportDestination = new SimpleObjectProperty<File>(null);
	
	// This property is false iff the settings contained within this class are valid, and we are free to export.
	private BooleanProperty isInvalid = new SimpleBooleanProperty(true);
	
	// These four control the selected target/source objects 
	private ObjectProperty<Setlist> selectedExportSetlist = new SimpleObjectProperty<Setlist>(null);
	private ObjectProperty<Piece> selectedExportPiece = new SimpleObjectProperty<Piece>(null);
	private ObjectProperty<Ensemble> selectedExportEnsemble = new SimpleObjectProperty<Ensemble>(null);
	private ObjectProperty<Instrument> selectedExportInstrument = new SimpleObjectProperty<Instrument>(null);
	
	// These two control which type of source/target we are using
	private ObjectProperty<SourceSelection> selectedSource = new SimpleObjectProperty<SourceSelection>();
	private ObjectProperty<TargetSelection> selectedTarget = new SimpleObjectProperty<TargetSelection>();
	
	// Paper settings
	private ObjectProperty<PaperSize> paperSize = new SimpleObjectProperty<PaperSize>(null);
	private DoubleProperty paperWidth = new SimpleDoubleProperty();
	
	private FloatProperty paperMargin = new SimpleFloatProperty(10);
	
	private BooleanProperty doublePages = new SimpleBooleanProperty(false);
	private BooleanProperty fitToPage = new SimpleBooleanProperty(false);
	
	// Export Grouping
	private ObservableList<ExportGroupType> exportGroups;
	
	/**This list contains any properties that, when changed, may change the validity of the settings. **/ 
	@SuppressWarnings("rawtypes")
	private final ObservableValue[] validityProperties =  {
			selectedExportSetlist,
			selectedExportPiece,
			selectedExportEnsemble,
			selectedExportInstrument,
			selectedSource,
			selectedTarget,
			paperSize,
			paperWidth,
		};
	
	@SuppressWarnings("unchecked")
	public ExportSettings(Library l) {

		this.library = l; //TODO: Is it a good idea to couple these like this?
		
		this.paperSize.set(PaperSize.LETTER);
		
		paperSize.addListener((obs, oldValue, newValue) -> {
			if (newValue != PaperSize.CUSTOM) {
				this.paperWidth.set(newValue.getWidthmm());
				// TODO change height
			}
		});
		
		// If we change the paper size value away from what's specified, we are now using a custom paper size
		paperWidth.addListener((obs, oldValue, newValue) ->{
			if ((double)newValue != this.getPaperSize().getWidthmm()) this.paperSize.set(PaperSize.CUSTOM);
		});
		
		//TODO do the same for paper height
		//TODO paper size width change not done: Need to update spinners
		
		exportGroups = FXCollections.observableArrayList(ExportGroupType.getOneOfEachExportGroupType());
		
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
		
		if (this.selectedSource.get() == null || this.selectedTarget.get() == null) {
			invalid = true;
		} else {
			//TODO This section is messy AF. There must be a better way. Probably involving a hashmap.
			// Test to make sure that we have a selection for the source field
			Object fullProperty = null;
			switch (this.selectedSource.get()) {
				case LIST:
					fullProperty = this.selectedExportSetlist.get();
					break;
				case PIECE:
					fullProperty = this.selectedExportPiece.get();
					break;
			}
			if (fullProperty == null) invalid = true;

			// Test to make sure that we have a selection for the target field
			fullProperty = null;
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
	
	/** Source **/
	
	public final ObjectProperty<Setlist> getSelectedExportSetlistProperty() {
		return selectedExportSetlist;
	}

	public ObjectProperty<Piece> getSelectedExportPieceProperty() {
		return selectedExportPiece;
	}
	
	public final ObjectProperty<SourceSelection> getSelectedSourceProperty() {
		return selectedSource;
	}
	
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
	
	/** Paper Size **/
	
	public final ObjectProperty<PaperSize> getPaperSizeProperty() {
		return paperSize;
	}
	
	public final PaperSize getPaperSize() {
		return paperSize.get();
	}
	
	public final void setPaperSize(PaperSize p) {
		paperSize.set(p);
	}
	
	/** Paper Width **/

	public final DoubleProperty getPaperWidthProperty() {
		return paperWidth;
	}
	
	public final double getPaperWidth() {
		return paperWidth.get();
	}
	
	public final void setPaperWidth(double w) {
		paperWidth.set(w);
	}
	
	public final FloatProperty getPaperMarginProperty() {
		return paperMargin;
	}
	
	public final float getPaperMargin() {
		return paperMargin.get();
	}
	
	public final void setPaperMargin(float w) {
		paperMargin.set(w);
	}
	

	/** Paper Properties **/ 
	
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
	
	
	
	public ExportTask getExportTask() {
		
		PDRectangle paperDimensions;
		
		
		// TODO: We should build these lists in real time, as they are updated
		// And tie in the validity to them. If the list is empty, for example, it should become invalid.
		
		
		List<Piece> exportPieces;
		
		
		switch (this.selectedSource.get()) {
		case LIST:
			exportPieces = this.selectedExportSetlist.get().getPieceList();
			break;
		case PIECE:
			exportPieces = new ArrayList<>(library.getPieces().size());
			exportPieces.add(this.selectedExportPiece.get());
			break;
		default:
			exportPieces = new ArrayList<>(library.getPieces().size());
			break;
			
		}
		
		ArrayList<Part> exportParts = new ArrayList<>(exportPieces.size() * 5);
		
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
		
		if (getPaperSize() != PaperSize.CUSTOM) {
			paperDimensions = getPaperSize().getDimensions();
		} else {
			paperDimensions = new PDRectangle(10, 10); // TODO: This needs to grab from paperWidth and paperHeight.
		}
		
		float margins = this.getPaperMargin();
		
		if (getDoublePages()) {


			PageSettings pageSettings = new PageSettings(paperDimensions, false, margins / 2);
			PageSettings cutPageSettings = new PageSettings(flipFolderPaperSize, getFitToPage(), margins / 2, true); // TODO double check margins is proper.
			
			return new ExportTask(pageSettings, cutPageSettings, exportParts, getExportDestination());
		} else {

			PageSettings pageSettings = new PageSettings(paperDimensions, getFitToPage(), margins);
			return new ExportTask(pageSettings, exportParts, getExportDestination());
		}
		
		
	}
	
}
