package eli.projects.spprototype;

import java.io.File;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import eli.projects.spprototype.model.PaperSize;
import eli.projects.spprototype.model.Piece;
import javafx.concurrent.Task;

public class ExportTask extends Task<Void> {

	private PDRectangle paperDimensions;
	
	private List<Piece> pieces;
	
	private File exportDestination;
	
	
	/**
	 * 
	 * @param paperDimensions The size of the paper we are printing on
	 * @param pieces The list of pieces that we want to export, in order
	 * @param exportDestination The destination we want to export those pieces
	 */
	public ExportTask(PDRectangle paperDimensions, List<Piece> pieces, File exportDestination) {
		super();
		this.paperDimensions = paperDimensions;
		this.pieces = pieces;
		this.exportDestination = exportDestination;
	}



	@Override
	protected Void call() throws Exception {
		
		
		// TODO: Observe export grouping settings
		
		PDDocument document = new PDDocument();

		
		// TODO: if doing multiple pages in one, adjust dimensions here?
		
		// TODO: Is there a cleaner way to get the number of pages for the progress bar?
		
		// Iterate over all of the pieces to find out the number of parts we need to add
		int numberOfPagesToAdd = 0;
		for (Piece pi : pieces) {
			for (Part pa : pi.getParts()) {
				numberOfPagesToAdd += 1;
			}
		}
		
		int pagesComplete = 0;
		
		// Iterate over all the parts to actually add them to the document
		for (Piece pi : pieces) {
			for (Part pa : pi.getParts()) {
                if (isCancelled()) {
                    break;
                }
				updateMessage("Working on " + pa.toString());
				
				pa.appendPages(paperDimensions, document);
				pagesComplete += 1;
				updateProgress(pagesComplete, numberOfPagesToAdd);
			}
            if (isCancelled()) {
                updateMessage("Cancelled");
                break;
            }
		}
		
		
		if (exportDestination.isDirectory()) {
			exportDestination = new File(exportDestination, "ossia-test-output.pdf");
		}
		
		try {
			updateMessage("Saving to " + exportDestination.getPath());
			updateProgress(-1, pagesComplete);
			document.save(exportDestination);
		} 
		
		
		finally {
			
			updateMessage("Export complete. Size: " + Utility.humanReadableByteCountSI(exportDestination.length()));
			document.close();
			updateProgress(1, 1);
		}
		
		
		return null;
	}

}
