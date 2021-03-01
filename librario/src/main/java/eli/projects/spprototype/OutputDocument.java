package eli.projects.spprototype;

import java.io.IOException;
import java.security.InvalidParameterException;

import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.util.Matrix;

import eli.projects.spprototype.model.PaperSettings;
import eli.projects.spprototype.model.PaperSettings.FinalPaperSettings;

public class OutputDocument extends PDDocument {
	
	private FinalPaperSettings pageSettings;
	private LayerUtility layerUtility;

	/**
	 * Creates a new blank document using the given PageSettings.
	 * 
	 * Use this if you want to call addDocumentSource
	 * @param context The settings for every page in the document.
	 */
	public OutputDocument(FinalPaperSettings pageSettings) {
		super();
		this.pageSettings = pageSettings;
		
		this.layerUtility = new LayerUtility(this);
	}
	
	/**
	 * Creates a new document imposed from the given source: Each pair of pages from the old source will be placed in one page in the new document.
	 * @param context The settings for every page in the new document.
	 * @param source The document that we will take pages from and place them in our own.
	 * @throws IOException 
	 */
	public OutputDocument(FinalPaperSettings pageSettings, OutputDocument source) throws IOException {
		super();
		this.pageSettings = pageSettings;
		
		
		this.layerUtility = new LayerUtility(this);
		
		// TODO: Add support for an arbitrary number of rows and columns.
		
		for (int i = 0; i < source.getNumberOfPages(); i+= 2) {

			PDPage page = new PDPage(pageSettings.paperDimensions);
			this.addPage(page);
			
			for (int r = 0; r < 2; r++) {
				if (i + r >= source.getNumberOfPages()) {
					continue;
				}
				
				addFormAtGridLocation(layerUtility.importPageAsForm(source, i + r), r, 0);

			}

		}
	}
	
	public void addDocumentSource(DocumentSource d) throws IOException {
		
		PDPage page = new PDPage(pageSettings.paperDimensions);
		this.addPage(page);
		
        try (PDPageContentStream contents = new PDPageContentStream(this, page, PDPageContentStream.AppendMode.APPEND, true))
        {
        	
            if (this.pageSettings.witnessMarksAtEdges) {
            	
            	contents.setLineWidth(1);
            	
            	float lLX = pageSettings.paperDimensions.getLowerLeftX();
            	float lLY = pageSettings.paperDimensions.getLowerLeftY();
            	float uRX = pageSettings.paperDimensions.getUpperRightX();
            	float uRY = pageSettings.paperDimensions.getUpperRightY();
            	
            	float[][] points = {
            			{lLX, lLY},
            			{lLX, uRY},
            			{uRX, uRY},
            			{uRX, lLY}
            	};
            	
            	contents.moveTo(uRX, lLY);
            	for (float[] point : points) {
            		contents.lineTo(point[0], point[1]);
            		// contents.moveTo(point[0], point[1]);
            	}
            	contents.stroke();
            	
            }
            
            PDFormXObject form = d.getDocumentForm(layerUtility);

        	float scale = 1.0f;
        	
            // If we're fitting to page, then change the scale of the form to fit the page
            if (pageSettings.fitContentToPage) {
            	float inputAspectRatio = form.getBBox().getWidth() / form.getBBox().getHeight();
            	float cutAspectRatio = pageSettings.paperDimensions.getWidth() / pageSettings.paperDimensions.getHeight();
            	
            	
            	if (inputAspectRatio > cutAspectRatio) {
            		// Width is the deciding dimension
            		scale = (pageSettings.paperDimensions.getWidth() - 2 * pageSettings.margins) / form.getBBox().getWidth();
            	} else {
            		// Height is the deciding dimension
            		scale = (pageSettings.paperDimensions.getHeight() - 2 * pageSettings.margins) / form.getBBox().getHeight();
            	}
            }
        	
            Matrix scaleMatrix = Matrix.getScaleInstance(scale, scale);
            
            Matrix translateMatrix = Matrix.getScaleInstance(1.0f, 1.0f);
            
            translateMatrix.translate(0.0f, pageSettings.paperDimensions.getUpperRightY());
            translateMatrix.translate(0.0f,  -form.getBBox().getHeight() * scale); // TODO: it would be nice if we could compartmentalize scaling and transform operations to avoid needing the scaling factor in the transformation operations.
            
            translateMatrix.translate(pageSettings.margins, -pageSettings.margins);
            
            contents.transform(translateMatrix);
            contents.transform(scaleMatrix);
            contents.drawForm(form);
            
        }
	}
	
	/**
	 * Adds the given form object to the document's last page in a grid-based location.
	 * 
	 * This function assumes that all the other documents have the same size, as it assumes the size of the grid by the size of the document you're adding right now.
	 * 
	 * @param form The form to add
	 * @param row The row (starting at the top)
	 * @param column (starting at the left)
	 * @throws IOException 
	 */
	public void addFormAtGridLocation(PDFormXObject form, int row, int column) throws IOException {
		
		
		
        try (PDPageContentStream contents = new PDPageContentStream(this, this.getPage(getNumberOfPages()-1), PDPageContentStream.AppendMode.APPEND, true))
        {
		
			Matrix translateMatrix = Matrix.getScaleInstance(1.0f, 1.0f);
            
            translateMatrix.translate(0.0f, pageSettings.paperDimensions.getUpperRightY());
            translateMatrix.translate(0.0f,  -form.getBBox().getHeight() * (1 + row));
            translateMatrix.translate(form.getBBox().getWidth() * (column), 0.0f);
            translateMatrix.translate(pageSettings.margins, -pageSettings.margins);
            
            contents.transform(translateMatrix);
            contents.drawForm(form);

    		this.layerUtility.wrapInSaveRestore(this.getPage(getNumberOfPages()-1));
        }
	}
	
	
	
}
