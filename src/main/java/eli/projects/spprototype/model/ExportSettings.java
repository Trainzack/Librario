package eli.projects.spprototype.model;

import java.io.File;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ExportSettings {


	// This controls where we want to export things to.
	private ObjectProperty<File> exportDestination = new SimpleObjectProperty<File>(null);
	
	// This property is true whenever the settings contained within this class are valid, and we are free to export.
	private BooleanProperty isValid = new SimpleBooleanProperty(false);
	
	// These three control what objects the user has selected for exporting. 
	private ObjectProperty<Setlist> selectedExportSetlist = new SimpleObjectProperty<Setlist>(null);
	private ObjectProperty<Piece> selectedExportPiece = new SimpleObjectProperty<Piece>(null);
	private ObjectProperty<Ensemble> selectedExportEnsemble = new SimpleObjectProperty<Ensemble>(null);
	private ObjectProperty<Instrument> selectedExportInstrument = new SimpleObjectProperty<Instrument>(null);
	
	private ObjectProperty<PaperSize> paperSize = new SimpleObjectProperty<PaperSize>(null);
	private DoubleProperty paperWidth = new SimpleDoubleProperty();
	
	public ExportSettings() {

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
		
	}
	
	public BooleanProperty getIsValidProperty() {
		return this.isValid;
	}
	
	/* Get Selected Objects
	 * 
	 * I'm not going to make individual getters and setters for each property any more, I don't think we need them. 
	 * 
	 */
	
	public final ObjectProperty<Setlist> getSelectedExportSetlistProperty() {
		return selectedExportSetlist;
	}

	public ObjectProperty<Piece> getSelectedExportPieceProperty() {
		return selectedExportPiece;
	}
	
	public ObjectProperty<Ensemble> getSelectedExportEnsembleProperty() {
		return selectedExportEnsemble;
	}
	
	public ObjectProperty<Instrument> getSelectedExportInstrumentProperty() {
		return selectedExportInstrument;
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

}
