package eli.projects.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class StringUtils {
	
	public static String intToDuration(int duration) {
		return ((duration/60 %60 < 10) ? "0"  : "") + (duration / 60) + 
				":" + ((duration %60 < 10) ? "0"  : "")  +
				(duration % 60) + "";
	}
	
	/**
	 * From the most copied stackoverflow post in history
	 * @author aioobe & 
	 * @param bytes
	 * @return
	 */
	public static String humanReadableByteCountSI(long bytes) {
	    if (-1000 < bytes && bytes < 1000) {
	        return bytes + " B";
	    }
	    CharacterIterator ci = new StringCharacterIterator("kMGTPE");
	    while (bytes <= -999_950 || bytes >= 999_950) {
	        bytes /= 1000;
	        ci.next();
	    }
	    return String.format("%.1f %cB", bytes / 1000.0, ci.current());
	    
	}
	
	/**
	 * Adds 's' to the end of 'type' depending on count 
	 * @param type What kind of thing we're counting
	 * @param count How many of that thing there are
	 * @return A string containing the count, then the type properly pluralized.
	 */
	public static String pluralizer(String type, int count) {
		if (Math.abs(count) == 1) {
			return count + " " + type;
		} else {
			return count + " " + type + "s";
		}
	}
	
}

