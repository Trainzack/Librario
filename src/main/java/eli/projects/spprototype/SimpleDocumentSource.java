package eli.projects.spprototype;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;

import eli.projects.spprototype.model.FormContentSource;

/**
 * This represents a source that we can pull a single page of sheet music from.
 * 
 * @author Eli
 *
 */

public class SimpleDocumentSource implements DocumentSource {

	private File fileLocation;
	
	private int page;
	
	
	public SimpleDocumentSource(File fileLocation, int page) {
		super();
		this.fileLocation = fileLocation;
		this.page = page;
	}
	
	/**
	 * Returns a PDFormXObject that contains the contents of this page of sheet music. 
	 * @param layerUtility The layerutility we want to use this PDFOrmXObject for.
	 * @return The form object that represents this document
	 * @throws IOException
	 */
	@Override
	public PDFormXObject getDocumentForm(LayerUtility layerUtility) throws IOException {

		PDDocument source = null;

		source = PDDocument.load(fileLocation);

		PDFormXObject form = layerUtility.importPageAsForm(source, page);
		
		source.close();
		
		return form;
		
	}
	
	@Override
	public String toString() {
		return this.fileLocation.getPath() + " (" + (this.page + 1) + ")";
	}
	
	/**
	 * Returns whether this document source already has a margin around the edges.
	 * @return True if there is a margin, false otherwise.
	 */
	@Override
	public boolean includesMargin() {
		// TODO method stub
		return false;
	}
	
	
}
