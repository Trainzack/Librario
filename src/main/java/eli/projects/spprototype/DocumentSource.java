package eli.projects.spprototype;

import java.io.IOException;

import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;

public interface DocumentSource {

	/**
	 * Returns a PDFormXObject that contains the contents of this page of sheet music. 
	 * @param layerUtility The layerutility we want to use this PDFOrmXObject for.
	 * @return The form object that represents this document
	 * @throws IOException
	 */
	PDFormXObject getDocumentForm(LayerUtility layerUtility) throws IOException;

	/**
	 * Returns whether this document source already has a margin around the edges.
	 * @return True if there is a margin, false otherwise.
	 */
	boolean includesMargin();

}