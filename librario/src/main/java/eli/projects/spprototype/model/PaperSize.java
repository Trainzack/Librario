package eli.projects.spprototype.model;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

public enum PaperSize {
	
	LETTER("Letter", PDRectangle.LETTER),
	LEGAL("Legal", PDRectangle.LEGAL),
	FOLIO("Folio/C4", 228.6f, 304.8f),
	A0("A0", PDRectangle.A0),
	A1("A1", PDRectangle.A1),
	A2("A2", PDRectangle.A2),
	A3("A3", PDRectangle.A3),
	A4("A4", PDRectangle.A4),
	A5("A5", PDRectangle.A5),
	A6("A6", PDRectangle.A6),
	FLIP_FOLDER("Flip Folder", 480, 354),
	CUSTOM("Custom", null);
	
	
	
	private final String name;
	
	// Measurements stored as mm
	private final PDRectangle dimensions;
	
	PaperSize(String name, PDRectangle dimensions) {
		this.name = name;
		this.dimensions = dimensions;
	}
	
	PaperSize(String name, float width, float height) {
		this(name, new PDRectangle(width, height));
	}
	
	public String getName() {
		return this.name;
	}
	
	public PDRectangle getDimensions() {
		return this.dimensions;
	}
	
	public float getWidthmm() {
		
		if (this.dimensions == null) return -1;
		
		return Math.round(this.dimensions.getWidth()*25.4 / 72);
	}
	
	public float getHeightmm() {
		
		if (this.dimensions == null) return -1;
		
		return Math.round(this.dimensions.getHeight()*25.4 / 72);
	}
	
	public float getWidthPt() {
		
		if (this.dimensions == null) return -1;
		
		return this.dimensions.getWidth();
	}
	
	public float getHeightPt() {
		
		if (this.dimensions == null) return -1;
		
		return this.dimensions.getHeight();
	}
	
	public String toString() {
		if (this == CUSTOM) return "Custom";
		return this.name + " (" + this.getWidthmm() + "mm x " + this.getHeightmm() + "mm)";
	}
	
	
}
