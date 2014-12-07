package logicmodule;

import java.util.List;
import shared.TrainColor;

/**
 * @author Kerekes Zoltán
 */
class Factory {
	static Controller newController(){
		return new ControllerImpl();
	}
	static Player newPlayer(ScoreMarker scoreMarker){
		return new PlayerImpl(scoreMarker);
	}
	static Route newRoute(shared.Route route){
		return new RouteImpl(route);
	}
	static ScoreMarker newScoreMarker(Player owner){
		return new ScoreMarkerImpl(owner);
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
