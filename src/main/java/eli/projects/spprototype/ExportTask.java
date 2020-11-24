package eli.projects.spprototype;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.util.Matrix;

import eli.projects.spprototype.model.PaperSize;
import eli.projects.spprototype.model.Piece;
import javafx.concurrent.Task;

public class ExportTask extends Task<Void> {
	
	private List<Part> parts;
	
	private File exportDestination;
	
	private OutputDocument document;
	
	private PageSettings imposedPageSettings;
	
	private boolean imposeOperation;
	
	/**
	 * Create a new export task, one page per
	 * 
	 * @param pageSettings The settings for the paper we are printing to
	 * @param parts The list of parts that we want to export, in the order they will appear in the document.
	 * @param exportDestination The destination we want to export those pieces
	 */
	public ExportTask(PageSettings pageSettings, List<Part> parts, File exportDestination) {
		super();
		this.parts = parts;
		this.exportDestination = exportDestination;
		
		this.imposeOperation = false;
		
		document = new OutputDocument(pageSettings);
		imposedPageSettings = null;
	}
	
	public ExportTask(PageSettings pageSettings, PageSettings cutPageSettings, List<Part> parts, File exportDestination) {
		super();
		this.parts = parts;
		this.exportDestination = exportDestination;
		
		this.imposeOperation = true;
		
		document = new OutputDocument(cutPageSettings);
		imposedPageSettings = pageSettings;
	}



	@Override
	protected Void call() throws IOException {
		

		
		// TODO: if doing multiple pages in one, adjust dimensions here?
		
		// TODO: Is there a cleaner way to get the number of pages for the progress bar?
		
		// Iterate over all of the pieces to find out the number of parts we need to add
		int numberOfPartsToAdd = 0;
		for (Part pa : parts) {
			numberOfPartsToAdd += 1;
		}
		
		int numberOfPartsAlreadyAdded = 0;

		
		// Iterate over all the parts to actually add them to the document
		for (Part pa : parts) {
            if (isCancelled()) {
                updateMessage("Cancelled");
                break;
            }
			updateMessage("Piece: " + pa.getPiece().getTitle() + " Part: " + pa.getDesignation().toString());
			
			// Add all of the DocumentSources from the part into the document.
			for (DocumentSource d : pa.getDocumentSources()) {
				document.addDocumentSource(d);
			}
			

			numberOfPartsAlreadyAdded += 1;
			updateProgress(numberOfPartsAlreadyAdded, numberOfPartsToAdd);
		}
		
		if (this.imposeOperation) {
			updateMessage("Imposing Output");
			updateProgress(-1, 0);
			OutputDocument imposedDocument = new OutputDocument(this.imposedPageSettings, document);
			document.close();
			document = imposedDocument;
		}
		
		
		if (exportDestination.isDirectory()) {
			exportDestination = new File(exportDestination, "ossia-test-output.pdf");
		}
		
		try {
			updateMessage("Saving to " + exportDestination.getPath());
			updateProgress(-1, numberOfPartsAlreadyAdded);
			document.save(exportDestination);
		} 
		
		
		finally {
			
			
			document.close();
			updateMessage("Export complete. Size: " + Utility.humanReadableByteCountSI(exportDestination.length()) + ". Parts: " + numberOfPartsAlreadyAdded + ". Pages: " + document.getNumberOfPages() + ".");
			updateProgress(1, 1);
		}
		
		
		return null;
	}

}
