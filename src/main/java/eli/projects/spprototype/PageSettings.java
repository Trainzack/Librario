package eli.projects.spprototype;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * An immutable set of page settings.
 * @author Eli
 *
 */
public class PageSettings {

	public final PDRectangle paperDimensions;
	public final boolean fitContentToPage;
	public final float margins;
	public final boolean witnessMarksAtEdges;
	
	
	
	public PageSettings(PDRectangle paperDimensions, boolean fitContentToPage, float margins,
			boolean witnessMarksAtEdges) {
		super();
		this.paperDimensions = paperDimensions;
		this.fitContentToPage = fitContentToPage;
		this.margins = margins;
		this.witnessMarksAtEdges = witnessMarksAtEdges;
	}

	public PageSettings(PDRectangle paperDimensions, boolean fitContentToPage, float margins) {
		super();
		this.paperDimensions = paperDimensions;
		this.fitContentToPage = fitContentToPage;
		this.margins = margins;
		this.witnessMarksAtEdges = false;
	}
	
	
}
