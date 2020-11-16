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
	 * @param paperDimensions
	 * @param pieces
	 * @param exportDestination
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
		
		int numberOfPagesToAdd = 0;
		
		for (Piece pi : pieces) {
			for (Part pa : pi.getParts()) {
				numberOfPagesToAdd += 1;
			}
		}
		
		int pagesComplete = 0;
		
		for (Piece pi : pieces) {
			for (Part pa : pi.getParts()) {
				updateMessage("Working on " + pi.getTitle() + " : " + pa.toString());
				
				pa.appendPages(paperDimensions, document);
				pagesComplete += 1;
				updateProgress(pagesComplete / (0.0f + numberOfPagesToAdd), 1.1);
			}
		}
		
		
		if (exportDestination.isDirectory()) {
			exportDestination = new File(exportDestination, "ossia-test-output.pdf");
		}
		
		try {
			updateMessage("Saving to " + exportDestination.getPath());
			updateProgress(1.0, 1.1);
			document.save(exportDestination);
		} 
		
		
		finally {
			updateMessage("Export complete");
			document.close();
			updateProgress(1.0, 1.0);
		}
		
		
		return null;
	}

}
