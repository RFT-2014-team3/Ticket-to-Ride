package logicmodule;

import java.util.List;
import shared.TrainColor;

/**
 * @author Kerekes Zoltán
 */
class Factory {
	static Player newPlayer(){
		return new PlayerImpl();
	}
	static Route newRoute(shared.Route route){
		return new RouteImpl(route);
	}
	static TicketDeck newTicketDeck(){
		return new TicketDeckImpl();
	}
	static TrainCard newTrainCard(TrainColor color){
		return new TrainCardImpl(color);
	}
	static LocomotiveCard newLocomotiveCard(){
		return new LocomotiveCardImpl();
	}
	static TrainDeck newTrainDeck(){
		return new TrainDeckImpl();
	}
	static Pathfinder newPathfinder(List<Route> routes){
		return new PathfinderImpl(routes);
	}
}
