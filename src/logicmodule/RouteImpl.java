package logicmodule;

import java.util.ArrayList;
import java.util.List;
import shared.City;
import shared.TrainColor;

/**
 * @author Kerekes Zoltán
 */
class RouteImpl implements Route {
	
	/**
	 * @author Kerekes Zoltán
	 */
	class RailImpl implements Rail {
		
		Player owner = null;
		TrainColor color;

		public RailImpl(TrainColor color) {
			this.color = color;
		}

		@Override
		public TrainColor getColor() {
			return color;
		}

		@Override
		public Player getOwner() {
			return owner;
		}

		@Override
		public void setOwner(Player owner) {
			this.owner = owner;
		}
	}
	
	
	private shared.Route route;
	private List<Rail> rails;
	
	public RouteImpl(shared.Route route) {
		this.route = route;
		rails = new ArrayList<>();
		for (TrainColor railColor : route.getRailColors()) {
			rails.add(new RailImpl(railColor));
		}
	}

	@Override
	public int getLength() {
		return route.getLength();
	}

	@Override
	public List<City> getDestinations() {
		return route.getDestinations();
	}

	@Override
	public List<Rail> getRails() {
		return rails;
	}

}
