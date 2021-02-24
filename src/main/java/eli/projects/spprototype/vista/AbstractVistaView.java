package eli.projects.spprototype.vista;

import java.util.function.Function;

import com.airhacks.afterburner.views.FXMLView;

/**
 * 
 * @author Eli
 *
 */
public abstract class AbstractVistaView extends FXMLView {

	public AbstractVistaView() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AbstractVistaView(Function<String, Object> injectionContext) {
		super(injectionContext);
		// TODO Auto-generated constructor stub
	}

}
