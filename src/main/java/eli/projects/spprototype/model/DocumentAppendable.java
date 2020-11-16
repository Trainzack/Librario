package eli.projects.spprototype.model;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * This interface is anything that we can add to the document.
 * @author Eli
 *
 */
public interface DocumentAppendable {

	public void appendPages(PDRectangle pageSize, PDDocument doc) throws IOException;
	
}
