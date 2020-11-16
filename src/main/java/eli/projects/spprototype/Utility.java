package eli.projects.spprototype;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class Utility {
	
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
	
}

