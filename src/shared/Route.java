package shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kerekes Zoltán
 */
public enum Route {
	VANCOUVER__CALGARY(City.VANCOUVER, City.CALGARY, 3, TrainColor.GREY),
	VANCOUVER__SEATTLE(City.VANCOUVER, City.SEATTLE, 1, TrainColor.GREY, TrainColor.GREY),
	SEATTLE__CALGARY(City.SEATTLE, City.CALGARY, 4, TrainColor.GREY),
	SEATTLE__PORTLAND(City.SEATTLE, City.PORTLAND, 1, TrainColor.GREY, TrainColor.GREY),
	SEATTLE__HELENA(City.SEATTLE, City.HELENA, 6, TrainColor.YELLOW),
	PORTLAND__SALT_LAKE_CITY(City.PORTLAND, City.SALT_LAKE_CITY, 6, TrainColor.BLUE),
	PORTLAND__SAN_FRANCISCO(City.PORTLAND, City.SAN_FRANCISCO, 5, TrainColor.GREEN, TrainColor.PURPLE),
	SALT_LAKE_CITY__SAN_FRANCISCO(City.SALT_LAKE_CITY, City.SAN_FRANCISCO, 5, TrainColor.ORANGE, TrainColor.WHITE),
	SAN_FRANCISCO__LOS_ANGELES(City.SAN_FRANCISCO, City.LOS_ANGELES, 3, TrainColor.YELLOW, TrainColor.PURPLE),
	LAS_VEGAS__LOS_ANGELES(City.LAS_VEGAS, City.LOS_ANGELES, 2, TrainColor.GREY),
	LOS_ANGELES__PHOENIX(City.LOS_ANGELES, City.PHOENIX, 3, TrainColor.GREY),
	LOS_ANGELES__EL_PASO(City.LOS_ANGELES, City.EL_PASO, 6, TrainColor.BLACK),
	PHOENIX__EL_PASO(City.PHOENIX, City.EL_PASO, 3, TrainColor.GREY),
	PHOENIX__SANTA_FE(City.PHOENIX, City.SANTA_FE, 3, TrainColor.GREY),
	PHOENIX__DENVER(City.PHOENIX, City.DENVER, 5, TrainColor.WHITE),
	SALT_LAKE_CITY__LAS_VEGAS(City.SALT_LAKE_CITY, City.LAS_VEGAS, 3, TrainColor.ORANGE),
	SALT_LAKE_CITY__DENVER(City.SALT_LAKE_CITY, City.DENVER, 3, TrainColor.RED, TrainColor.YELLOW),
	HELENA__SALT_LAKE_CITY(City.HELENA, City.SALT_LAKE_CITY, 3, TrainColor.PURPLE),
	CALGARY__HELENA(City.CALGARY, City.HELENA, 4, TrainColor.GREY),
	HELENA__DENVER(City.HELENA, City.DENVER, 4, TrainColor.GREEN),
	DENVER__SANTA_FE(City.DENVER, City.SANTA_FE, 2, TrainColor.GREY),
	EL_PASO__SANTA_FE(City.EL_PASO, City.SANTA_FE, 2, TrainColor.GREY),
	EL_PASO__HOUSTON(City.EL_PASO, City.HOUSTON, 6, TrainColor.GREEN),
	EL_PASO__DALLAS(City.EL_PASO, City.DALLAS, 4, TrainColor.RED),
	EL_PASO__OKLAHOMA_CITY(City.EL_PASO, City.OKLAHOMA_CITY, 5, TrainColor.YELLOW),
	SANTA_FE__OKLAHOMA_CITY(City.SANTA_FE, City.OKLAHOMA_CITY, 3, TrainColor.BLUE),
	DENVER__OKLAHOMA_CITY(City.DENVER, City.OKLAHOMA_CITY, 4, TrainColor.RED),
	DENVER__KANSAS_CITY(City.DENVER, City.KANSAS_CITY, 4, TrainColor.BLACK, TrainColor.ORANGE),
	OMAHA__DENVER(City.OMAHA, City.DENVER, 4, TrainColor.PURPLE),
	HELENA__OMAHA(City.HELENA, City.OMAHA, 5, TrainColor.RED),
	HELENA__DULUTH(City.HELENA, City.DULUTH, 6, TrainColor.ORANGE),
	HELENA__WINNIPEG(City.HELENA, City.WINNIPEG, 4, TrainColor.BLUE),
	CALGARY__WINNIPEG(City.CALGARY, City.WINNIPEG, 6, TrainColor.WHITE),
	WINNIPEG__DULUTH(City.WINNIPEG, City.DULUTH, 4, TrainColor.BLACK),
	DULUTH__OMAHA(City.DULUTH, City.OMAHA, 2, TrainColor.GREY, TrainColor.GREY),
	OMAHA__KANSAS_CITY(City.OMAHA, City.KANSAS_CITY, 1, TrainColor.GREY, TrainColor.GREY),
	KANSAS_CITY__OKLAHOMA_CITY(City.KANSAS_CITY, City.OKLAHOMA_CITY, 2, TrainColor.GREY, TrainColor.GREY),
	DALLAS__OKLAHOMA_CITY(City.DALLAS, City.OKLAHOMA_CITY, 2, TrainColor.GREY, TrainColor.GREY),
	DALLAS__HOUSTON(City.DALLAS, City.HOUSTON, 1, TrainColor.GREY, TrainColor.GREY),
	HOUSTON__NEW_ORLEANS(City.HOUSTON, City.NEW_ORLEANS, 2, TrainColor.GREY),
	DALLAS__LITTLE_ROCK(City.DALLAS, City.LITTLE_ROCK, 2, TrainColor.GREY),
	OKLAHOMA_CITY__LITTLE_ROCK(City.OKLAHOMA_CITY, City.LITTLE_ROCK, 2, TrainColor.GREY),
	KANSAS_CITY__SAINT_LOUIS(City.KANSAS_CITY, City.SAINT_LOUIS, 2, TrainColor.BLUE, TrainColor.PURPLE),
	OMAHA__CHICAGO(City.OMAHA, City.CHICAGO, 4, TrainColor.BLUE),
	DULUTH__CHICAGO(City.DULUTH, City.CHICAGO, 3, TrainColor.RED),
	DULUTH__TORONTO(City.DULUTH, City.TORONTO, 6, TrainColor.PURPLE),
	DULUTH__SAULT_ST_MARIE(City.DULUTH, City.SAULT_ST_MARIE, 3, TrainColor.GREY),
	WINNIPEG__SAULT_ST_MARIE(City.WINNIPEG, City.SAULT_ST_MARIE, 6, TrainColor.GREY),
	SAULT_ST_MARIE__MONTREAL(City.SAULT_ST_MARIE, City.MONTREAL, 5, TrainColor.BLACK),
	SAULT_ST_MARIE__TORONTO(City.SAULT_ST_MARIE, City.TORONTO, 2, TrainColor.GREY),
	TORONTO__MONTREAL(City.TORONTO, City.MONTREAL, 3, TrainColor.GREY),
	CHICAGO__TORONTO(City.CHICAGO, City.TORONTO, 4, TrainColor.WHITE),
	CHICAGO__SAINT_LOUIS(City.CHICAGO, City.SAINT_LOUIS, 2, TrainColor.GREEN, TrainColor.WHITE),
	SAINT_LOUIS__LITTLE_ROCK(City.SAINT_LOUIS, City.LITTLE_ROCK, 2, TrainColor.GREY),
	LITTLE_ROCK__NEW_ORLEANS(City.LITTLE_ROCK, City.NEW_ORLEANS, 3, TrainColor.GREEN),
	NEW_ORLEANS__MIAMI(City.NEW_ORLEANS, City.MIAMI, 6, TrainColor.RED),
	NEW_ORLEANS__ATLANTA(City.NEW_ORLEANS, City.ATLANTA, 4, TrainColor.YELLOW, TrainColor.ORANGE),
	LITTLE_ROCK__NASHVILLE(City.LITTLE_ROCK, City.NASHVILLE, 3, TrainColor.WHITE),
	SAINT_LOUIS__NASHVILLE(City.SAINT_LOUIS, City.NASHVILLE, 2, TrainColor.GREY),
	SAINT_LOUIS__PITTSBURGH(City.SAINT_LOUIS, City.PITTSBURGH, 5, TrainColor.GREEN),
	CHICAGO__PITTSBURGH(City.CHICAGO, City.PITTSBURGH, 3, TrainColor.ORANGE, TrainColor.BLACK),
	PITTSBURGH__TORONTO(City.PITTSBURGH, City.TORONTO, 2, TrainColor.GREY),
	MONTREAL__NEW_YORK(City.MONTREAL, City.NEW_YORK, 3, TrainColor.BLUE),
	MONTREAL__BOSTON(City.MONTREAL, City.BOSTON, 2, TrainColor.GREY, TrainColor.GREY),
	BOSTON__NEW_YORK(City.BOSTON, City.NEW_YORK, 2, TrainColor.YELLOW, TrainColor.RED),
	PITTSBURGH__NEW_YORK(City.PITTSBURGH, City.NEW_YORK, 2, TrainColor.WHITE, TrainColor.GREEN),
	NASHVILLE__PITTSBURGH(City.NASHVILLE, City.PITTSBURGH, 4, TrainColor.YELLOW),
	NASHVILLE__ATLANTA(City.NASHVILLE, City.ATLANTA, 1, TrainColor.GREY),
	NASHVILLE__RALEIGH(City.NASHVILLE, City.RALEIGH, 3, TrainColor.BLACK),
	PITTSBURGH__RALEIGH(City.PITTSBURGH, City.RALEIGH, 2, TrainColor.GREY),
	PITTSBURGH__WASHINGTON(City.PITTSBURGH, City.WASHINGTON, 2, TrainColor.GREY),
	WASHINGTON__NEW_YORK(City.WASHINGTON, City.NEW_YORK, 2, TrainColor.ORANGE, TrainColor.BLACK),
	RALEIGH__WASHINGTON(City.RALEIGH, City.WASHINGTON, 2, TrainColor.GREY, TrainColor.GREY),
	ATLANTA__RALEIGH(City.ATLANTA, City.RALEIGH, 2, TrainColor.GREY, TrainColor.GREY),
	CHARLESTON__RALEIGH(City.CHARLESTON, City.RALEIGH, 2, TrainColor.GREY),
	ATLANTA__CHARLESTON(City.ATLANTA, City.CHARLESTON, 2, TrainColor.GREY),
	ATLANTA__MIAMI(City.ATLANTA, City.MIAMI, 5, TrainColor.BLUE),
	MIAMI__CHARLESTON(City.MIAMI, City.CHARLESTON, 4, TrainColor.PURPLE);
	
	private int length;
	/** Destination cities as an unmodifiable list. */
	private List<City> destinations;
	/** Rails' colors as an unmodifiable list. */
	private List<TrainColor> railColors;
	
	Route(City destination1, City destination2, int length, TrainColor railColor1){
		City[] tempColorArray = new City[]{destination1, destination2};
		List<City> tempDestList = new ArrayList<>(Arrays.asList(tempColorArray));
		destinations = Collections.unmodifiableList(tempDestList);
		
		this.length = length;
		
		List<TrainColor> tempColorList = new ArrayList<>();
		tempColorList.add(railColor1);
		railColors = Collections.unmodifiableList(tempColorList);
	}
	
	Route(City destination1, City destination2, int length, TrainColor railColor1, TrainColor railColor2){
		City[] tempArray = new City[]{destination1, destination2};
		List<City> tempDestList = new ArrayList<>(Arrays.asList(tempArray));
		destinations = Collections.unmodifiableList(tempDestList);
		
		this.length = length;
		
		TrainColor[] tempColorArray = new TrainColor[]{railColor1, railColor2};
		List<TrainColor> tempColorList = new ArrayList<>(Arrays.asList(tempColorArray));
		railColors = Collections.unmodifiableList(tempColorList);
	}
	
	public int getLength(){
		return length;
	}
	
	/**
	 * @return Destination cities as an unmodifiable list.
	 */
	public List<City> getDestinations(){
		return destinations;
	}
	
	/**
	 * @return Rails' colors as an unmodifiable list.
	 */
	public List<TrainColor> getRailColors(){
		return railColors;
	}
}