package logicmodule;

import shared.City;
import java.util.List;

/**
 * @author Kerekes Zoltán
 */
interface Route {
	int getLength();
	List<City> getDestinations();
	List<Rail> getRails();
}
