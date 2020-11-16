package eli.projects.spprototype;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.util.Matrix;

import eli.projects.spprototype.model.DocumentAppendable;
import eli.projects.spprototype.model.Piece;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;

/**
 * This class is one part of a specific piece.
 * 
 * This class contains a reference to the associated piece, instrument, and numeral.
 * @author Eli
 *
 */

public class Part implements DocumentAppendable {
	
	// The designation that applies to this piece
	SimpleObjectProperty<PartDesignation> designation = new SimpleObjectProperty<>(this, "designation");
	
	// Which piece this part belongs to
	private Piece piece;

	SimpleIntegerProperty pages = new SimpleIntegerProperty(this, "pages");
	
	// TODO: how to represent connection to PDF file?

	/**
	 * Creates a new part with the given designation and belonging to the given piece.
	 * @param designations The designation of this piece 
	 * @param piece The piece that this part belongs to.
	 */
	public Part(PartDesignation designation, Piece piece) {
		super();
		this.designation.set(designation);
		this.piece = piece;
		
		this.pages.set(1); //TODO
	}
	
	// TODO: A method to assign certain pages from a PDF to this part.


	/**
	public Part(Piece piece, String path) throws InvalidPartDesignationException {
		//TODO WE shouldn't be throwing this partdesignationexcption, we should replace it with something better.
		super();
		this.piece = piece;
		
		// Get the name of the file from the path
		String filename = new File(path).getName();
		// remove the extension
		filename = filename.split("\\.")[0];
		
		
		this.designations = new PartDesignation[] {PartDesignation.ParseDesignation(filename)};
	}**/

	public PartDesignation getDesignation() {
		return designation.get();
	}

	public Piece getPiece() {
		return piece;
	}
	
	public IntegerProperty getPageCount() {
		return pages;
	}

	public void appendPages(PDRectangle pageSize, PDDocument target) throws IOException {
		
		Random r = new Random();
		
		r.nextFloat();
		
		File samplePage = new File("files/Test/" + r.nextInt(10) + ".pdf");
		
		for (int i = 0; i < this.getPageCount().get(); i++) {
			
			PDDocument source = PDDocument.load(samplePage);

			PDPage page = new PDPage(pageSize);
			
			target.addPage(page);
			
            try (PDPageContentStream contents = new PDPageContentStream(target, page))
            {
                contents.beginText();
                contents.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contents.newLineAtOffset(2, PDRectangle.LETTER.getHeight() - 12);
                contents.showText("Piece: " + this.getPiece().getTitle());
                contents.endText();
                
                // Create a Form XObject from the source document using LayerUtility
                LayerUtility layerUtility = new LayerUtility(target);
                PDFormXObject form = layerUtility.importPageAsForm(source, 0);
                
                Matrix matrix = Matrix.getScaleInstance(0.5f, 0.5f);
                contents.transform(matrix);
                contents.drawForm(form);
            }
            
            source.close();
			
			/*
			PDPageContentStream contentStream = new PDPageContentStream(target, page1);

			contentStream.setLeading(14.5f);
			
			contentStream.moveTo(100, pageSize.getUpperRightY() - 100);
			contentStream.beginText();
			contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.TIMES_ROMAN, 16);
			contentStream.newLineAtOffset(100, pageSize.getUpperRightY() - 100);
			contentStream.showText("Placeholder!");
			contentStream.newLine();
			contentStream.showText("Piece: " + this.getPiece().getTitle());
			contentStream.newLine();
			contentStream.showText("Part: " + this.designation.get().toString());
			contentStream.newLine();
			contentStream.showText("Page: " + (i+1) + "/" + this.getPageCount().get());
			contentStream.endText();
			
			contentStream.close();
			
			source.close();
			*/
		}
		
	}
	

	/*
	public PDDocument getPdfDocument() throws InvalidPasswordException, IOException {
		return PDDocument.load(new File(documentPath));
	}*/
	
	public final ObjectProperty<PartDesignation> designationProperty() {
		return this.designation;
	}
	
	public final IntegerProperty pagesProperty() {
		return this.pages;
	}
	
	@Override
	public String toString() {
		return this.piece.getTitle() + ": " + this.designation.get().toString();
	}
	
	
	
	
}
