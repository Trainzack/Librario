package eli.projects.spprototype;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class Utility {
	
	public static String intToDuration(int duration) {
		return "" + (duration / 60) + 
				"'" + ((duration %60 < 10) ? "0"  : "")  +
				(duration % 60) + "\"";
	}
}

