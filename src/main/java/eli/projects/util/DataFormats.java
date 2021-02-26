package eli.projects.util;

import javafx.scene.input.DataFormat;

/**
 * This class contains all of the MIME types of data that we might want to put onto clipboards
 * @author Eli
 * 
 * See also: https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types
 *
 */
public class DataFormats {


	/**
	 * For serialized objects.
	 */
	public static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
	

	/**
	 * For serialized objects.
	 */
	public static final DataFormat PIECE_MIME_TYPE = new DataFormat("application/x-java-piece-reference");
	
}
