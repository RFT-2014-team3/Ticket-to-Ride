package logicmodule;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kerekes Zoltán
 */
public class Controller implements GUIHandler {
	
	private static final Controller instance = new Controller();
	private Pathfinder pf;
	private OpcodeHandler oh = OpcodeHandler.getInstance();
	/** 0 means: not yet set; 1: this is the server; 2..5: this is a client. */
	private int playerIndex = 0;
	private int currentPlayerIndex = 0;
	private List<Player> players;
	//private List<City> TODO [logic] del or create
	private List<Route> routes;
	private TrainDeck trainDeck;
	private TicketDeck ticketDeck;
	
	private Controller() {}

	public static Controller getInstance() {
		return instance;
	}

	@Override
	public void startNewServer() {
		playerIndex = 1;
		currentPlayerIndex = 1;
		routes = new ArrayList<>();
		for (shared.Route r : shared.Route.values()) {
			routes.add(Factory.newRoute(r));
		}
		pf = Factory.newPathfinder(routes);
		players = new ArrayList<>();
		players.add(Factory.newPlayer());
		trainDeck = Factory.newTrainDeck();
		ticketDeck = Factory.newTicketDeck();
	}
	
	
	
	
	
	@Override
	public void connectToServer(String ip) {
		// TODO [logic]
	}

	@Override
	public void drawTrainCard() {
		if(playerIndex != Opcode.SERVER_INDEX)
			oh.sendOpcodeTo(Opcode.SERVER_INDEX, new Opcode(
					Opcode.Sender.GUI, playerIndex, Opcode.Action.SELECT_TRAIN_DECK));
		else
			_drawTrainCard(playerIndex);
	}
	void _drawTrainCard(int playerIndex) {
		// TODO [logic]
	}

	@Override
	public void drawTrainCard(int index) {
		if(playerIndex != Opcode.SERVER_INDEX)
			oh.sendOpcodeTo(Opcode.SERVER_INDEX, new Opcode(
					Opcode.Sender.GUI, playerIndex, Opcode.Action.SELECT_TRAIN_DECK, index));
		else
			_drawTrainCard(playerIndex, index);
	}
	void _drawTrainCard(int playerIndex, int index) {
		// TODO [logic]
	}

	@Override
	public void drawTicketCard() {
		if(playerIndex != Opcode.SERVER_INDEX)
			oh.sendOpcodeTo(Opcode.SERVER_INDEX, new Opcode(
					Opcode.Sender.GUI, playerIndex, Opcode.Action.SELECT_TICKET_DECK));
		else
			_drawTicketCard(playerIndex);
	}
	void _drawTicketCard(int playerIndex) {
		// TODO [logic]
	}

	@Override
	public void claimRoute(shared.Route route) {
		if(playerIndex != Opcode.SERVER_INDEX)
			oh.sendOpcodeTo(Opcode.SERVER_INDEX, new Opcode(
					Opcode.Sender.GUI, playerIndex, Opcode.Action.SELECT_CLAIM_ROUTE, route.name()));
		else
			_claimRoute(playerIndex, route);
	}
	void _claimRoute(int playerIndex, shared.Route route) {
		// TODO [logic]
	}

	
	
	
	
	private void guiUpdateYourTurnStarted(int playerIndex) {
		if(this.playerIndex != playerIndex)
			oh.sendOpcodeTo(playerIndex, new Opcode(
					Opcode.Sender.LOGIC, Opcode.SERVER_INDEX, Opcode.Action.UPDATE_YOUR_TURN_STARTED));
		else
			_guiUpdateYourTurnStarted();
	}
	void _guiUpdateYourTurnStarted() {
		// TODO [logic]
	}
	
	private void guiUpdateYourTurnEnded(int playerIndex) {
		if(this.playerIndex != playerIndex)
			oh.sendOpcodeTo(playerIndex, new Opcode(
					Opcode.Sender.LOGIC, Opcode.SERVER_INDEX, Opcode.Action.UPDATE_YOUR_TURN_ENDED));
		else
			_guiUpdateYourTurnEnded();
	}
	void _guiUpdateYourTurnEnded() {
		// TODO [logic]
	}
	
	private void guiUpdateRouteClaimed(int playerIndex, shared.Route route, shared.PlayerColor color1, shared.PlayerColor color2) {
		if(this.playerIndex != playerIndex)
			oh.sendOpcodeTo(playerIndex, new Opcode(
					Opcode.Sender.LOGIC, Opcode.SERVER_INDEX, Opcode.Action.UPDATE_ROUTE_CLAIMED, 
					route.name(), color1.name(), color2.name()));
		else
			_guiUpdateRouteClaimed(route, color1, color2);
	}
	void _guiUpdateRouteClaimed(shared.Route route, shared.PlayerColor color1, shared.PlayerColor color2) {
		// TODO [logic]
	}
	
	private void guiUpdateTrainDeck(int playerIndex, int numberOfCards) {
		if(this.playerIndex != playerIndex)
			oh.sendOpcodeTo(playerIndex, new Opcode(
					Opcode.Sender.LOGIC, Opcode.SERVER_INDEX, Opcode.Action.UPDATE_TRAIN_DECK,
					numberOfCards));
		else
			_guiUpdateTrainDeck(numberOfCards);
	}
	void _guiUpdateTrainDeck(int numberOfCards) {
		// TODO [logic]
	}
	
	private void guiUpdateUpfaceTrainCards(int playerIndex, List<shared.TrainColor> cards) {
		if(this.playerIndex != playerIndex)
			oh.sendOpcodeTo(playerIndex, new Opcode(
					Opcode.Sender.LOGIC, Opcode.SERVER_INDEX, Opcode.Action.UPDATE_UPFACE_TRAIN_CARDS,
					0, 0, 0, 0,
					cards.get(0) == null ? "" : cards.get(0).name(), 
					cards.get(1) == null ? "" : cards.get(1).name(), 
					cards.get(2) == null ? "" : cards.get(2).name(), 
					cards.get(3) == null ? "" : cards.get(3).name(), 
					cards.get(4) == null ? "" : cards.get(4).name()));
		else
			_guiUpdateUpfaceTrainCards(cards);
	}
	void _guiUpdateUpfaceTrainCards(List<shared.TrainColor> cards) {
		// TODO [logic]
	}
	
	private void guiUpdateTicketDeck(int playerIndex, int numberOfCards) {
		if(this.playerIndex != playerIndex)
			oh.sendOpcodeTo(playerIndex, new Opcode(
					Opcode.Sender.LOGIC, Opcode.SERVER_INDEX, Opcode.Action.UPDATE_TICKET_DECK,
					numberOfCards));
		else
			_guiUpdateTicketDeck(numberOfCards);
	}
	void _guiUpdateTicketDeck(int numberOfCards) {
		// TODO [logic]
	}
	
	
	
	
	
	private void logicUpdatePlayerIndex(int playerIndex) {
		oh.sendOpcodeTo(playerIndex, new Opcode(
				Opcode.Sender.LOGIC, Opcode.SERVER_INDEX, Opcode.Action.UPDATE_PLAYER_INDEX,
				playerIndex));
	}
	void _updatePlayerIndex(int playerIndex) {
		// TODO [logic]
	}
	
}
