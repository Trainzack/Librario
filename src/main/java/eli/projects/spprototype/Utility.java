package eli.projects.spprototype;

public class Utility {
	
	public static String intToDuration(int duration) {
		return ((duration/60 %60 < 10) ? "0"  : "") + (duration / 60) + 
				":" + ((duration %60 < 10) ? "0"  : "")  +
				(duration % 60) + "";
	}
	
	
}

