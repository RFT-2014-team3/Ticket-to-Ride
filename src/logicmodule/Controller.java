package logicmodule;

import java.util.ArrayList;
import java.util.List;
import shared.TicketCard;

/**
 * @author Kerekes Zoltán
 */
public class Controller implements GUIHandler {

	private enum ActionState {
		CHOOSING_ACTION,
		THROWING_TICKET_CARDS,
		DRAWING_SECOND_TRAIN_CARD,
	}
	
	private static final Controller instance = new Controller();
	private Pathfinder pf;
	private OpcodeHandler oh = OpcodeHandler.getInstance();
	private List<Player> players;
	//private List<City> TODO [logic] del or create
	private List<Route> routes;
	private TrainDeck trainDeck;
	private TicketDeck ticketDeck;
	/** 0 means: not yet set; 1: this is the server; 2..5: this is a client. */
	private int playerIndex = 0;
	private int currentPlayerIndex = 0;
	private ActionState actionState = ActionState.THROWING_TICKET_CARDS;
	
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
	
	// TODO: [logic] startGame()
	
	private void nextPlayer() {
		guiUpdateYourTurnEnded(currentPlayerIndex);
		if(currentPlayerIndex == players.size())
			currentPlayerIndex = 1;
		else
			currentPlayerIndex++;
		actionState = ActionState.CHOOSING_ACTION;
		guiUpdateYourTurnStarted(currentPlayerIndex);
	}
	
	private void broadcastDeckStates() {
		List<shared.TrainColor> upfaces = new ArrayList<>();
		for(int j = 0; j < 5; j++){
			if(trainDeck.upfaceCardIsExists(j))
				upfaces.add(trainDeck.getUpfaceCardColor(j));
			else
				upfaces.add(null);
		}
		for(int i = 0; i < players.size(); i++) {
			guiUpdateTicketDeck(i, ticketDeck.getCardsCount());
			guiUpdateTrainDeck(i, trainDeck.getDownfaceCardsCount());
			guiUpdateUpfaceTrainCards(i, upfaces);
		}
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
		if(playerIndex != this.currentPlayerIndex || trainDeck.getDownfaceCardsCount() == 0)
			return;
		
		TrainCard card = trainDeck.drawDownfaceCard();
		players.get(playerIndex).getTrainCards().add(card);
		guiUpdateGainTrainCard(playerIndex, card.getColor());
		trainDeck.useDiscardedCardsIfDownfacesEmpty();
		broadcastDeckStates();
		if(actionState == ActionState.CHOOSING_ACTION) {
			if(trainDeck.getDownfaceCardsCount() == 0 && !trainDeck.hasUpfaceNonLocomotiveCard())
				nextPlayer();
			else
				actionState = ActionState.DRAWING_SECOND_TRAIN_CARD;
		} else if(actionState == ActionState.DRAWING_SECOND_TRAIN_CARD) {
			nextPlayer();
		}
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
		if(playerIndex != this.currentPlayerIndex || !trainDeck.upfaceCardIsExists(index))
			return;
		if(actionState == ActionState.DRAWING_SECOND_TRAIN_CARD 
				&& trainDeck.upfaceCardIsLocomotive(index))
			return;
		
		TrainCard card = trainDeck.drawUpfaceCard(index);
		players.get(playerIndex).getTrainCards().add(card);
		guiUpdateGainTrainCard(playerIndex, card.getColor());
		broadcastDeckStates();
		if(actionState == ActionState.CHOOSING_ACTION) {
			if(trainDeck.upfaceCardIsLocomotive(index))
				nextPlayer();
			else
				actionState = ActionState.DRAWING_SECOND_TRAIN_CARD;
		} else {
			nextPlayer();
		}
	}

	@Override
	public void drawTicketCards() {
		if(playerIndex != Opcode.SERVER_INDEX)
			oh.sendOpcodeTo(Opcode.SERVER_INDEX, new Opcode(
					Opcode.Sender.GUI, playerIndex, Opcode.Action.SELECT_TICKET_DECK));
		else
			_drawTicketCards(playerIndex);
	}
	void _drawTicketCards(int playerIndex) {
		if(playerIndex != this.currentPlayerIndex || ticketDeck.getCardsCount() == 0)
			return;
		
		List<shared.TicketCard> cards = new ArrayList<>();
		for(int i = 0; i < 3; i++){
			if(ticketDeck.getCardsCount() > 0)
				cards.add(ticketDeck.drawCard());
		}
		players.get(playerIndex).getTicketCards().addAll(cards);
		guiUpdateGainTicketCards(playerIndex, cards);
		broadcastDeckStates();
		actionState = ActionState.THROWING_TICKET_CARDS;
	}

	@Override
	public void throwTicketCards(List<TicketCard> cards) {
		if(playerIndex != Opcode.SERVER_INDEX)
			oh.sendOpcodeTo(Opcode.SERVER_INDEX, new Opcode(
					Opcode.Sender.GUI, playerIndex, Opcode.Action.SELECT_THROW_TICKET_CARDS));
		else
			_throwTicketCards(playerIndex, cards);
	}
	void _throwTicketCards(int playerIndex, List<TicketCard> cards) {
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

	
	
	
	
	private void logicUpdatePlayerIndex(int playerIndex) {
		oh.sendOpcodeTo(playerIndex, new Opcode(
				Opcode.Sender.LOGIC, Opcode.SERVER_INDEX, Opcode.Action.UPDATE_PLAYER_INDEX,
				playerIndex));
	}
	void _updatePlayerIndex(int playerIndex) {
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
	
	private void guiUpdateGainTrainCard(int playerIndex, shared.TrainColor card) {
		if(this.playerIndex != playerIndex)
			oh.sendOpcodeTo(playerIndex, new Opcode(
					Opcode.Sender.LOGIC, Opcode.SERVER_INDEX, Opcode.Action.UPDATE_GAIN_TRAIN_CARD,
					card.name()));
		else
			_guiUpdateGainTrainCard(card);
	}
	void _guiUpdateGainTrainCard(shared.TrainColor card) {
		// TODO [logic]
	}
	
	private void guiUpdateGainTicketCards(int playerIndex, List<shared.TicketCard> cards) {
		if(this.playerIndex != playerIndex)
			oh.sendOpcodeTo(playerIndex, new Opcode(
					Opcode.Sender.LOGIC, Opcode.SERVER_INDEX, Opcode.Action.UPDATE_GAIN_TICKET_CARDS,
					cards.get(0) == null ? "" : cards.get(0).name(),
					cards.get(1) == null ? "" : cards.get(1).name(),
					cards.get(2) == null ? "" : cards.get(2).name() ));
		else
			_guiUpdateGainTicketCards(cards);
	}
	void _guiUpdateGainTicketCards(List<shared.TicketCard> cards) {
		// TODO [logic]
	}
	
	private void guiUpdateLooseTrainCard(int playerIndex, shared.TrainColor card, int n) {
		if(this.playerIndex != playerIndex)
			oh.sendOpcodeTo(playerIndex, new Opcode(
					Opcode.Sender.LOGIC, Opcode.SERVER_INDEX, Opcode.Action.UPDATE_LOOSE_TRAIN_CARD,
					n, card.name()));
		else
			_guiUpdateLooseTrainCard(card, n);
	}
	void _guiUpdateLooseTrainCard(shared.TrainColor card, int n) {
		// TODO [logic]
	}
	
	private void guiUpdateLooseTicketCard(int playerIndex, shared.TicketCard card) {
		if(this.playerIndex != playerIndex)
			oh.sendOpcodeTo(playerIndex, new Opcode(
					Opcode.Sender.LOGIC, Opcode.SERVER_INDEX, Opcode.Action.UPDATE_LOOSE_TICKET_CARD,
					card.name()));
		else
			_guiUpdateLooseTicketCard(card);
	}
	void _guiUpdateLooseTicketCard(shared.TicketCard card) {
		// TODO [logic]
	}
	
	private void guiUpdateScore(int playerIndex, int score) {
		if(this.playerIndex != playerIndex)
			oh.sendOpcodeTo(playerIndex, new Opcode(
					Opcode.Sender.LOGIC, Opcode.SERVER_INDEX, Opcode.Action.UPDATE_SCORE,
					score));
		else
			_guiUpdateScore(score);
	}
	void _guiUpdateScore(int score) {
		// TODO [logic]
	}
}
