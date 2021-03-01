package eli.projects.spprototype;

/**
 * This class is a wrapper for the App class, as making a JavaFX Application class run directly from Maven causes problems.
 * (This solution is as per https://stackoverflow.com/questions/52653836/maven-shade-javafx-runtime-components-are-missing )
 * 
 * This feels like a kludgey workaround, but it makes the program function.
 * @author Eli
 *
 */
public class Main {

	public static void main(String[] args) {
		App.main(args);
	}

}
