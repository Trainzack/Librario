package eli.projects.spprototype.model;

import java.io.IOException;

import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;

/**
 * This interface is anything that we can add to the document.
 * @author Eli
 *
 */
public interface FormContentSource {

	public PDFormXObject[] getDocumentForms(LayerUtility layerUtility) throws IOException;
	
}
