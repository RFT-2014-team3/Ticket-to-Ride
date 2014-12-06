package shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kerekes Zoltán
 */
public enum TicketCard {
	DENVER__EL_PASO(		City.DENVER, City.EL_PASO, 4),
	KANSAS_CITY__HOUSTON(	City.KANSAS_CITY, City.HOUSTON, 5),
	NEW_YORK__ATLANTA(		City.NEW_YORK, City.ATLANTA, 6),
	CHICAGO__NEW_ORLEANS(	City.CHICAGO, City.NEW_ORLEANS, 7),
	CALGARY__SALT_LAKE_CITY(City.CALGARY, City.SALT_LAKE_CITY, 7),
	HELENA__LOS_ANGELES(	City.HELENA, City.LOS_ANGELES, 8),
	DULUTH__HOUSTON(		City.DULUTH, City.HOUSTON, 8),
	SAULT_ST_MARIE__NASHVILLE(City.SAULT_ST_MARIE, City.NASHVILLE, 8),
	MONTREAL__ATLANTA(		City.MONTREAL, City.ATLANTA, 9),
	SAULT_ST_MARIE__OKLAHOMA_CITY(City.SAULT_ST_MARIE, City.OKLAHOMA_CITY, 9),
	SEATTLE__LOS_ANGELES(	City.SEATTLE, City.LOS_ANGELES, 9),
	CHICAGO__SANTA_FE(		City.CHICAGO, City.SANTA_FE, 9),
	DULUTH__EL_PASO(		City.DULUTH, City.EL_PASO, 10),
	TORONTO__MIAMI(			City.TORONTO, City.MIAMI, 10),
	PORTLAND__PHOENIX(		City.PORTLAND, City.PHOENIX, 11),
	DALLAS__NEW_YORK(		City.DALLAS, City.NEW_YORK, 11),
	DENVER__PITTSBURGH(		City.DENVER, City.PITTSBURGH, 11),
	WINNIPEG__LITTLE_ROCK(	City.WINNIPEG, City.LITTLE_ROCK, 11),
	WINNIPEG__HOUSTON(		City.WINNIPEG, City.HOUSTON, 12),
	BOSTON__MIAMI(			City.BOSTON, City.MIAMI, 12),
	VANCOUVER__SANTA_FE(	City.VANCOUVER, City.SANTA_FE, 13),
	CALGARY__PHOENIX(		City.CALGARY, City.PHOENIX, 13),
	MONTREAL__NEW_ORLEANS(	City.MONTREAL, City.NEW_ORLEANS, 13),
	LOS_ANGELES__CHICAGO(	City.LOS_ANGELES, City.CHICAGO, 16),
	SAN_FRANCISCO__ATLANTA(	City.SAN_FRANCISCO, City.ATLANTA, 17),
	PORTLAND__NASHVILLE(	City.PORTLAND, City.NASHVILLE, 17),
	VANCOUVER__MONTREAL(	City.VANCOUVER, City.MONTREAL, 20),
	LOS_ANGELES__MIAMI(		City.LOS_ANGELES, City.MIAMI, 20),
	LOS_ANGELES__NEW_YORK(	City.LOS_ANGELES, City.NEW_YORK, 21),
	SEATTLE__NEW_YORK(		City.SEATTLE, City.NEW_YORK, 22);
	
	/** Score value. */
	private int value;
	/** Destination cities as an unmodifiable list. */
	private List<City> destinations;
	
	TicketCard(City destination1, City destination2, int value){
		City[] tempArray = new City[]{destination1, destination2};
		List<City> tempList = new ArrayList<>(Arrays.asList(tempArray));
		destinations = Collections.unmodifiableList(tempList);
		this.value = value;
	}
	
	/**
	 * @return Score value.
	 */
	int getValue(){
		return value;
	}
	
	/**
	 * @return Destination cities as an unmodifiable list.
	 */
	List<City> getDestinations(){
		return destinations;
	}
}
