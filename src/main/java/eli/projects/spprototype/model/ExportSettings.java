package eli.projects.spprototype.model;

import java.io.File;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ExportSettings {


	// This controls where we want to export things to.
	private ObjectProperty<File> exportDestination = new SimpleObjectProperty<File>(null);
	
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
		
	}
	

	public final ObjectProperty<File> getExportDestinationProperty() {
		return exportDestination;
	}
	
	public final File getExportDestination() {
		return exportDestination.get();
	}
	
	public final void setExportDestination(File destination) {
		exportDestination.set(destination);
	}
	
	
	public final ObjectProperty<PaperSize> getPaperSizeProperty() {
		return paperSize;
	}
	
	public final PaperSize getPaperSize() {
		return paperSize.get();
	}
	
	public final void setPaperSize(PaperSize p) {
		paperSize.set(p);
	}

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
