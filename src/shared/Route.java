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
	SEATTLE__CALGARY(City.SEATTLE, City.CALGARY, 4, TrainColor.GREY);
	// soon...


	
	private int length;
	/** Destination cities as an unmodifiable list. */
	private List<City> destinations;
	/** Trails' colors as an unmodifiable list. */
	private List<TrainColor> trailColors;
	
	Route(City destination1, City destination2, int length, TrainColor railColor1){
		City[] tempColorArray = new City[]{destination1, destination2};
		List<City> tempDestList = new ArrayList<>(Arrays.asList(tempColorArray));
		destinations = Collections.unmodifiableList(tempDestList);
		
		this.length = length;
		
		List<TrainColor> tempColorList = new ArrayList<>();
		tempColorList.add(railColor1);
		trailColors = Collections.unmodifiableList(tempColorList);
	}
	
	Route(City destination1, City destination2, int length, TrainColor railColor1, TrainColor railColor2){
		City[] tempArray = new City[]{destination1, destination2};
		List<City> tempDestList = new ArrayList<>(Arrays.asList(tempArray));
		destinations = Collections.unmodifiableList(tempDestList);
		
		this.length = length;
		
		TrainColor[] tempColorArray = new TrainColor[]{railColor1, railColor2};
		List<TrainColor> tempColorList = new ArrayList<>(Arrays.asList(tempColorArray));
		trailColors = Collections.unmodifiableList(tempColorList);
	}
	
	int getLength(){
		return length;
	}
	
	/**
	 * @return Destination cities as an unmodifiable list.
	 */
	List<City> getDestinations(){
		return destinations;
	}
	
	/**
	 * @return trails' colors as an unmodifiable list.
	 */
	List<City> getTrailColors(){
		return destinations;
	}
}