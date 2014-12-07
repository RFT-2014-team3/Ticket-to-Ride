package logicmodule;

import java.util.List;
import shared.City;

/**
 * @author Kerekes Zoltán
 */
interface Pathfinder {
	List<Player> getLongestPathOwners();
	boolean pathExistsBetween(City cityA, City cityB, Player player);
}
