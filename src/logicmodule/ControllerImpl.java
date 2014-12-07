package logicmodule;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kerekes Zoltán
 */
class ControllerImpl implements Controller {

	public ControllerImpl() {
		List<Route> routes = new ArrayList<>();
		for (shared.Route route : shared.Route.values()) {
			routes.add(Factory.newRoute(route));
		}
		Pathfinder pf = Factory.newPathfinder(routes);
		// TODO: [logic] ControllerImpl
	}

}
