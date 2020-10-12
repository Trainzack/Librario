package eli.projects.spprototype.model;

public enum PaperSize {
	
	LETTER("Letter", 215.9, 279.4),
	FOLIO("Folio/C4", 228.6, 304.8),
	A4("A4", 210, 297),
	A5("A5", 148, 210),
	B4("B4", 240, 353),
	B5("B5", 176, 250),
	B6("B6", 125, 176),
	CUSTOM("Custom", -1, -1);
	
	
	
	private final String name;
	
	// Measurements stored as mm
	private final double width;
	private final double height;
	
	PaperSize(String name, double width, double height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getWidthmm() {
		return this.width;
	}
	
	public double getHeightmm() {
		return this.height;
	}
	
	public double getWidthInch() {
		return this.width * 25.4;
	}
	
	public double getHeightInch() {
		return this.height * 25.4;
	}
	
	public double getWidthPt() {
		return this.width * 2.834;
	}
	
	public double getHeightPt() {
		return this.height * 2.834;
	}
	
	public String toString() {
		if (this == CUSTOM) return "Custom";
		return this.name + " (" + this.getWidthmm() + "mm x " + this.getHeightmm() + "mm)";
	}
	
	
}
