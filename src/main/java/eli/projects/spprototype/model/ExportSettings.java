package eli.projects.spprototype.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import eli.projects.spprototype.Part;


public class ExportSettings {
	
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
	
	private ObjectProperty<PaperSize> paperSize = new SimpleObjectProperty<PaperSize>(null);
	private DoubleProperty paperWidth = new SimpleDoubleProperty();
	
	
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
	
	/**
	 * This method executes the export that the settings found in this class control.
	 * @throws IOException 
	 */
	public void export() throws IOException {
		
		// TODO: Observe export grouping settings
		
		PDDocument document = new PDDocument();
		
		PDRectangle paperDimensions;
		
		if (this.paperSize.get() != PaperSize.CUSTOM) {
			paperDimensions = this.paperSize.get().getDimensions();
		} else {
			paperDimensions = new PDRectangle(10, 10); // TODO: This needs to grab from paperWidth and paperHeight.
		}
		
		// TODO: if doing multiple pages in one, adjust dimensions here?
		
		for (Piece pi : this.library.getPieces()) {
			for (Part pa : pi.getParts()) {
				pa.appendPages(paperDimensions, document);
			}
		}
		
		
		File destination = this.getExportDestination();
		
		if (destination.isDirectory()) {
			destination = new File(destination, "ossia-test-output.pdf");
		}
		
		try {
			document.save(destination);	
		} 
		
		
		finally {
			document.close();
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

	/** Export Groups **/
	
	public final ObservableList<ExportGroupType> getExportGroups() {
		return exportGroups;
	}
	
}
