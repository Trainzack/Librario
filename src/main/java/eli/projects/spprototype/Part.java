package eli.projects.spprototype;

import java.io.File;
import java.io.IOException;

import eli.projects.spprototype.PartDesignation.InvalidPartDesignationException;
import eli.projects.spprototype.model.Piece;

/**
 * This class is one part of a specific piece.
 * 
 * This class contains a reference to the associated piece, instrument, and numeral.
 * @author Eli
 *
 */

public class Part {
	
	// All of the part designations that apply to this part.
	// (This will probably only be one, but that is not always the case).
	private PartDesignation[] designations;
	
	// Which piece this part belongs to
	private Piece piece;
	
	// The path to the PDF document that contains the sheet music for this part
	private String documentPath;

	public Part(PartDesignation[] designations, Piece piece, String documentPath) {
		super();
		this.designations = designations;
		this.piece = piece;
		this.documentPath = documentPath;
	}
	
	public Part(Piece piece, String path) throws InvalidPartDesignationException {
		//TODO WE shouldn't be throwing this partdesignationexcption, we should replace it with something better.
		super();
		this.piece = piece;
		this.documentPath = path;
		
		// Get the name of the file from the path
		String filename = new File(path).getName();
		// remove the extension
		filename = filename.split("\\.")[0];
		
		
		this.designations = new PartDesignation[] {PartDesignation.ParseDesignation(filename)};
	}

	public PartDesignation[] getDesignations() {
		return designations;
	}

	public Piece getPiece() {
		return piece;
	}

	/*
	public PDDocument getPdfDocument() throws InvalidPasswordException, IOException {
		return PDDocument.load(new File(documentPath));
	}*/
	
	
	
	
}
