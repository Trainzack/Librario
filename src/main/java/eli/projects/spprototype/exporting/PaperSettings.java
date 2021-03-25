package eli.projects.spprototype.exporting;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import eli.projects.spprototype.model.PaperSize;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyFloatProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * An immutable set of page settings.
 * @author Eli
 *
 */
public class PaperSettings {
	
	private ObjectProperty<PaperSize> paperSize = new SimpleObjectProperty<PaperSize>();
	
	private FloatProperty paperWidth = new SimpleFloatProperty();
	private FloatProperty paperHeight = new SimpleFloatProperty();
	
	private FloatProperty paperMargin = new SimpleFloatProperty();

	
	public PaperSettings() {
		this(PaperSize.LETTER, 10);
	}
	
	public PaperSettings(PaperSize paperSize, float paperMargin) {
		super();
		this.setPaperSize(paperSize);
		this.paperMargin.set(paperMargin);
	}
	
	/** Paper Size **/
	
	public final ReadOnlyObjectProperty<PaperSize> getPaperSizeProperty() {
		return paperSize;
	}

	public final PaperSize getPaperSize() {
		return paperSize.get();
	}
	
	public final void setPaperSize(PaperSize p) {
		paperSize.set(p);
		if (p != PaperSize.CUSTOM) {
			paperWidth.set(p.getWidthPt());
			paperHeight.set(p.getHeightPt());
		}
	}
	
	/** Paper Width **/

	public final ReadOnlyFloatProperty getPaperWidthProperty() {
		return paperWidth;
	}
	
	public final float getPaperWidth() {
		return paperWidth.get();
	}
	
	public final void setPaperWidth(float w) {
		paperWidth.set(w);
		paperSize.set(PaperSize.CUSTOM);
	}
	
	/** Paper Height **/
	
	public final ReadOnlyFloatProperty getPaperHeightProperty() {
		return paperHeight;
	}
	
	public final float getPaperHeight() {
		return paperHeight.get();
	}
	
	public final void setPaperHeight(float h) {
		paperHeight.set(h);
		paperSize.set(PaperSize.CUSTOM);
	}
	
	/** Paper Margins **/
	
	public final FloatProperty getPaperMarginProperty() {
		return paperMargin;
	}
	
	public final float getPaperMargin() {
		return paperMargin.get();
	}
	
	public final void setPaperMargin(float w) {
		paperMargin.set(w);
	}
	
	/**
	 * This class contains all the relevant information from the model, but in an immutable
	 * state. This class is used when sending page size information to the exporting tasks.
	 * 
	 * We use an immutable object here because we don't want the page size changing mid-task.
	 * 
	 * @author Eli
	 *
	 */
	public class FinalPaperSettings {

		public final PDRectangle paperDimensions;
		public final float margins;
		
		public final boolean fitContentToPage;
		public final boolean witnessMarksAtEdges;
		
		private FinalPaperSettings(PaperSettings ps, boolean fit, boolean witness) {
			super();
			
			this.paperDimensions = new PDRectangle(ps.getPaperWidth(), ps.getPaperHeight());
			this.margins = ps.getPaperMargin();
			
			this.fitContentToPage = fit;
			this.witnessMarksAtEdges = witness;
			
		}
		
		@Override
		public String toString() {
			String out = "Dimensions: " + this.paperDimensions.toString() + "; Margins: " + this.margins + "; ";
			
			if (this.fitContentToPage) out += " Fit to page; ";
			if (this.witnessMarksAtEdges) out += " Witness marks on; ";
			return out;
			
		}
		
	}
	
	//TODO Move fit and witness out of this method call, and into this object as proper properties.
	
	public FinalPaperSettings getFinalSettings(boolean fit, boolean witness) { 
		return new FinalPaperSettings(this, fit, witness);
	}
	
}
