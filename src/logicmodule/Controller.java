package logicmodule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import netsubmodul.Client;
import netsubmodul.Server;
import shared.City;
import shared.PlayerColor;
import shared.TicketCard;
import shared.TrainColor;

// TODO [logic] Maybe need to stop server/client on exit.

/**
 * @author Kerekes Zoltán
 */
public class Controller implements GUIHandler {

	private enum State {
		CHOOSING,
		THROWING_TICKET,
		DRAWING_2ND_TRAIN,
	}
	
	private static final Controller instance = new Controller();
	
	private static final int SERVER_ID = Opcode.SERVER_ID;
	private OpcodeHandler oh = OpcodeHandler.getInstance();
	private static final int PORT = 9999;
	Server server;
	Client client;
	
	// server and client
	private Pathfinder pf;
	private Map<shared.Route, Route> routes = new HashMap<>();
	private TrainDeck trainDeck;
	private TicketDeck ticketDeck;
	private List<TicketCard> lastTicketCards = new ArrayList<>();
	private List<Player> players = new ArrayList<>();
	/** 0 means: not yet set; 1: this is the server; 2..5: this is a client. */
	private int playerID = 0; 
	private int currPlayerID = 0;
	private boolean isMyTurn;
	private State state = State.THROWING_TICKET;
	private boolean gameStarted = false;
	private boolean colorChosingTurn = false;
	private boolean initialDrawTurn = false;
	private boolean lastTurn = false;
	private int lastTurnPlayerID;
	private boolean gameFinished = false;
	
	// client specific
	private int clientCardsInTrainDeck = 120;
	private int clientCardsInTicketDeck = 30;
	private List<TrainColor> clientUpfaceTrainCards;
	
	private Controller() {}

	public static Controller getInstance() {
		return instance;
	}

	boolean isServer() {
		return playerID == 1;
	}
	
	private void nextPlayer() {
		clientUpdateYourTurnEnded(currPlayerID);
		if(currPlayerID == players.size())
			currPlayerID = 1;
		else
			currPlayerID++;
		state = State.CHOOSING;
		
		// init draw turn ended?
		if(currPlayerID == SERVER_ID && initialDrawTurn)
			for (int i = 1; i <= players.size(); i++) {
				clientUpdateInitTurnEnd(i);
			}
		
		clientUpdateYourTurnStarted(currPlayerID);
	}
	
	private void broadcastDeckStates() {
		List<TrainColor> upfaces = new ArrayList<>();
		for(int j = 0; j < 5; j++){
			if(trainDeck.upfaceCardIsExists(j))
				upfaces.add(trainDeck.getUpfaceCardColor(j));
			else
				upfaces.add(null);
		}
		List<Integer> scores = new ArrayList<>();
		for(int i = 0; i < players.size(); i++) {
			scores.add(players.get(i).getScore());
		}
		for(int i = 1; i <= players.size(); i++) {
			clientUpdateTicketDeck(i, ticketDeck.getCardsCount());
			clientUpdateTrainDeck(i, trainDeck.getDownfaceCardsCount());
			clientUpdateUpfaceTrainCards(i, upfaces);
			clientUpdateScores(i, scores);
		}
	}
	
	private boolean spendTrainCards(Rail rail) {
		int length = rail.getLength();
		TrainColor railColor = rail.getColor();
		Player player = players.get(currPlayerID - 1);
		List<TrainColor> nonGreys = new ArrayList<>();
		nonGreys.addAll(Arrays.asList(TrainColor.values()));
		nonGreys.remove(TrainColor.GREY);
		
		// non-grey rail
		// use non-locomotives + locomotives
		if(railColor != TrainColor.GREY) {
			if(player.getTrainCardCount(railColor) + player.getTrainCardCount(TrainColor.GREY) >= length) {
				// non-locomotives
				int discarded = Math.min(player.getTrainCardCount(railColor), length);
				List<TrainCard> cardsToDeck = player.removeTrainCards(railColor, discarded);
				clientUpdateLooseTrainCard(currPlayerID, railColor, discarded);
				for (TrainCard tc : cardsToDeck)
					trainDeck.discardCardIntoDeck(tc); 
				// locomotives
				if(discarded < length) {
					List<TrainCard> cardsToDeck2 = player.removeTrainCards(TrainColor.GREY, length - discarded);
					clientUpdateLooseTrainCard(currPlayerID, TrainColor.GREY, length - discarded);
					for (TrainCard tc : cardsToDeck2)
						trainDeck.discardCardIntoDeck(tc); 
				}
				return true;
			}
		}
		// grey rail
		else {
			// use only non-locomotives if can
			for (TrainColor c : nonGreys) {
				if(player.getTrainCardCount(c) >= length) {
					List<TrainCard> cardsToDeck = player.removeTrainCards(c, length);
					clientUpdateLooseTrainCard(currPlayerID, c, length);
					for (TrainCard tc : cardsToDeck)
						trainDeck.discardCardIntoDeck(tc);
					return true;
				}
			}
			// use non-locomotives + locomotives
			for (TrainColor c : nonGreys) {
				if(player.getTrainCardCount(c) + player.getTrainCardCount(TrainColor.GREY) >= length) {
					// non-locomotives
					int discarded = Math.min(player.getTrainCardCount(c), length);
					List<TrainCard> cardsToDeck = player.removeTrainCards(c, discarded);
					clientUpdateLooseTrainCard(currPlayerID, c, discarded);
					for (TrainCard tc : cardsToDeck)
						trainDeck.discardCardIntoDeck(tc); 
					// locomotives
					if(discarded < length) {
						List<TrainCard> cardsToDeck2 = player.removeTrainCards(TrainColor.GREY, length - discarded);
						clientUpdateLooseTrainCard(currPlayerID, TrainColor.GREY, length - discarded);
						for (TrainCard tc : cardsToDeck2)
							trainDeck.discardCardIntoDeck(tc); 
					}
					return true;
				}
			}
		}
		return false;
	}
	
	private void checkLastTurn() {
		for (int i = 0; i < players.size(); i++) {
			if(!lastTurn && players.get(i).getRemainingTrainsCount() <= 2) {
				lastTurn = true;
				lastTurnPlayerID = currPlayerID;
				return;
			}
		}
		if(lastTurn && lastTurnPlayerID == currPlayerID)
			finishGame();
	}
	
	private void finishGame() {
		for (Player p : pf.getLongestPathOwners()) {
			p.addScore(10);
		}
		for (Player p : players) {
			for (TicketCard card : p.getTicketCards()) {
				List<City> dests = card.getDestinations();
				City cityA = dests.get(0);
				City cityB = dests.get(1);
				if(pf.pathExistsBetween(cityA, cityB, p))
					p.addScore(card.getValue());
				else
					p.addScore(-card.getValue());
			}
		}
		for (int i = 1; i <= players.size(); i++) {
			clientUpdateGameFinished(i);
		}
	}
	
	
	// ========================== Requests from GUI. ==========================
	// <editor-fold desc="implements GUIHandler interface">

	@Override
	public boolean isGameStarted() {
		return gameStarted;
	}

	@Override
	public boolean isMyTurn() {
		return isMyTurn;
	}

	@Override
	public boolean isInitialDrawTurn() {
		return initialDrawTurn;
	}

	@Override
	public boolean isColorChosingTurn() {
		return colorChosingTurn;
	}
	
	@Override
	public boolean isGameFinished() {
		return gameFinished;
	}
	
	@Override
	public List<PlayerColor> getRouteOwnerColors(shared.Route route) {
		List<PlayerColor> colors = new ArrayList<>();
		for (Rail r : this.routes.get(route).getRails()) {
			if(r.getOwner() == null)
				colors.add(null);
			else
				colors.add(r.getOwner().getColor());
		}
		return colors;
	}

	@Override
	public int getNumberOfCardsInTrainDeck() {
		if(playerID == SERVER_ID) {
			return trainDeck.getDownfaceCardsCount();
		} else {
			return clientCardsInTrainDeck;
		}
	}

	@Override
	public int getNumberOfCardsInTicketDeck() {
		if(playerID == SERVER_ID) {
			return ticketDeck.getCardsCount();
		} else {
			return clientCardsInTicketDeck;
		}
	}

	@Override
	public List<TrainColor> getUpfaceTrainCards() {
		if(playerID == SERVER_ID) {
			List<TrainColor> cards = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				cards.add(trainDeck.getUpfaceCardColor(i));
			}
			return cards;
		} else {
			return new ArrayList<>(clientUpfaceTrainCards);
		}
	}

	@Override
	public List<TicketCard> getMyTicketCards() {
		return new ArrayList<>(players.get(playerID - 1).getTicketCards());
	}

	@Override
	public List<Integer> getMyTrainCards() {
		List<Integer> counts = new ArrayList<>();
		Player p = players.get(playerID - 1);
		for (TrainColor c : TrainColor.values()) {
			counts.add(p.getTrainCardCount(c));
		}
		return counts;
	}

	@Override
	public List<Integer> getPlayerScores() {
		List<Integer> scores = new ArrayList<>();
		for (Player p : players) {
			scores.add(p.getScore());
		}
		return scores;
	}

	@Override
	public List<PlayerColor> getPlayerColors() {
		List<PlayerColor> colors = new ArrayList<>();
		for (Player p : players) {
			colors.add(p.getColor());
		}
		return colors;
	}

	@Override
	public int getMyID() {
		return playerID;
	}
	
	@Override
	public List<TicketCard> getChoosableTicketCards() {
		return lastTicketCards;
	}

	// </editor-fold>
	
	
	
	// ======================= Input messages from GUI. =======================
	// <editor-fold desc="implements GUIHandler interface">
	
	@Override
	public String startNewServer() {
		server = Server.GetServer();
		if(server.isServerRunning())
			return server.GetServerAddress();
		
		playerID = 1;
		currPlayerID = 1;
		state = State.CHOOSING;
		clientUpdateYourTurnStarted(currPlayerID);
		
		for (shared.Route r : shared.Route.values()) {
			routes.put(r, Factory.newRoute(r));
		}
		pf = Factory.newPathfinder(new ArrayList<>(routes.values()));
		players.add(Factory.newPlayer());
		trainDeck = Factory.newTrainDeck();
		ticketDeck = Factory.newTicketDeck();
		
		server.setOpcodeHandler(oh);
		server.startServer(PORT);
		String ip = server.GetServerAddress();
		return ip;
	}
	
	@Override
	public boolean connectToServer(String ip) {
		client = new Client();
		client.setOpcodeHandler(oh);
		boolean success = client.startClient(ip, PORT);
		if(success) {
			float maxWaitSecs = 5;
			int i = 0;
			while(client.GetClientID() == -100 && i*0.1f < maxWaitSecs) {
				try {
					Thread.sleep(100);
					i++;
				} catch (InterruptedException ex) {}
			}
			int id = client.GetClientID();
			if(id == -100 || id > 5)
				return false;
			playerID = client.GetClientID();
			
			for (shared.Route r : shared.Route.values()) {
				routes.put(r, Factory.newRoute(r));
			}
			/*for (int j = 1; j <= playerID; j++) {
				players.add(Factory.newPlayer());
			}*/
			clientUpfaceTrainCards = new ArrayList<>();
			for (int j = 0; j < 5; j++) {
				clientUpfaceTrainCards.add(null);
			}
		}
		return success;
	}
	
	@Override
	public boolean startGame() {
		if(server.GetConnectedClients() > 0 && !gameStarted) {
			for (int i = 1; i <= players.size(); i++) {
				Player p = players.get(i - 1);
				// give trains
				p.setRemainingTrainsCount(45);
				// give train cards
				trainDeck.shuffleDownfaceCards();
				for (int j = 0; j < 4; j++) {
					TrainCard trCard = trainDeck.drawDownfaceCard();
					p.addTrainCards(trCard.getColor(), 1);
				}
				// give ticket cards
				ticketDeck.shuffleCards();
				List<TicketCard> tiCards = new ArrayList<>();
				for (int j = 0; j < 3; j++) {
					tiCards.add(ticketDeck.drawCard());
				}
				clientUpdateGainTicketCards(i, tiCards);
			}
			broadcastDeckStates();
			server.StartMatch();
			clientUpdateGameStarted();
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void chooseColor(PlayerColor color) {
		if(playerID != SERVER_ID)
			oh.sendTo(SERVER_ID, new Opcode(playerID, Opcode.Action.SELECT_COLOR,
					color.name()));
		else
			_chooseColor(playerID, color);
	}
	void _chooseColor(int senderID, PlayerColor color) {
		if(senderID != currPlayerID)
			return;
		for (Player p : players)
			if(p.getColor() != null && p.getColor() == color)
				return;
		
		players.get(currPlayerID - 1).setColor(color);
		clientUpdateColorChose(playerID, color);
		nextPlayer();
	}
	
	@Override
	public void drawTrainCard() {
		if(playerID != SERVER_ID)
			oh.sendTo(SERVER_ID, new Opcode(playerID, Opcode.Action.SELECT_TRAIN_DECK));
		else
			_drawTrainCard(playerID);
	}
	void _drawTrainCard(int senderID) {
		if(senderID != currPlayerID || trainDeck.getDownfaceCardsCount() == 0
				|| (state != State.CHOOSING && state != State.DRAWING_2ND_TRAIN)
				|| !gameStarted || colorChosingTurn || initialDrawTurn || gameFinished)
			return;
		
		TrainCard card = trainDeck.drawDownfaceCard();
		players.get(senderID - 1).addTrainCards(card.getColor(), 1);
		clientUpdateGainTrainCard(senderID, card.getColor());
		broadcastDeckStates();
		if(state == State.CHOOSING) {
			if(trainDeck.getDownfaceCardsCount() == 0 && !trainDeck.hasUpfaceNonLocomotiveCard())
				nextPlayer();
			else
				state = State.DRAWING_2ND_TRAIN;
		} else if(state == State.DRAWING_2ND_TRAIN) {
			nextPlayer();
		}
	}

	@Override
	public void drawTrainCard(int index) {
		if(playerID != SERVER_ID)
			oh.sendTo(SERVER_ID, new Opcode(playerID, Opcode.Action.SELECT_TRAIN_DECK,
					index));
		else
			_drawTrainCard(playerID, index);
	}
	void _drawTrainCard(int senderID, int index) {
		if(senderID != currPlayerID || !trainDeck.upfaceCardIsExists(index)
				|| (state != State.CHOOSING && state != State.DRAWING_2ND_TRAIN)
				|| !gameStarted || colorChosingTurn || initialDrawTurn || gameFinished)
			return;
		if(state == State.DRAWING_2ND_TRAIN && trainDeck.upfaceCardIsLocomotive(index))
			return;
		
		TrainCard card = trainDeck.drawUpfaceCard(index);
		players.get(senderID - 1).addTrainCards(card.getColor(), 1);
		clientUpdateGainTrainCard(senderID, card.getColor());
		broadcastDeckStates();
		if(state == State.CHOOSING) {
			if(trainDeck.upfaceCardIsLocomotive(index))
				nextPlayer();
			else
				state = State.DRAWING_2ND_TRAIN;
		} else {
			nextPlayer();
		}
	}

	@Override
	public void drawTicketCards() {
		if(playerID != SERVER_ID)
			oh.sendTo(SERVER_ID, new Opcode(playerID, Opcode.Action.SELECT_TICKET_DECK));
		else
			_drawTicketCards(playerID);
	}
	void _drawTicketCards(int senderID) {
		if(senderID != currPlayerID || ticketDeck.getCardsCount() == 0 
				|| state != State.CHOOSING || !gameStarted || colorChosingTurn || gameFinished)
			return;
		
		List<TicketCard> cards = new ArrayList<>();
		for(int i = 0; i < 3; i++){
			if(ticketDeck.getCardsCount() > 0)
				cards.add(ticketDeck.drawCard());
		}
		players.get(senderID - 1).getTicketCards().addAll(cards);
		clientUpdateGainTicketCards(senderID, cards);
		broadcastDeckStates();
		state = State.THROWING_TICKET;
	}

	@Override
	public void throwTicketCards(List<TicketCard> cards) {
		if(playerID != SERVER_ID)
			oh.sendTo(SERVER_ID, new Opcode(playerID, Opcode.Action.SELECT_THROW_TICKET_CARDS));
		else
			_throwTicketCards(playerID, cards);
	}
	void _throwTicketCards(int senderID, List<TicketCard> cards) {
		if(senderID != currPlayerID || state != State.THROWING_TICKET)
			return;
		
		for (TicketCard c : cards) {
			ticketDeck.discardCardIntoDeck(c);
			clientUpdateLooseTicketCard(senderID, c);
		}
		players.get(senderID - 1).getTicketCards().removeAll(cards);
		broadcastDeckStates();
		nextPlayer();
	}
	
	@Override
	public void claimRoute(shared.Route route) {
		if(playerID != SERVER_ID)
			oh.sendTo(SERVER_ID, new Opcode(playerID, Opcode.Action.SELECT_CLAIM_ROUTE,
					route.name()));
		else
			_claimRoute(playerID, route);
	}
	void _claimRoute(int senderID, shared.Route route) {
		if(senderID != currPlayerID || state != State.CHOOSING
				|| !gameStarted || colorChosingTurn || initialDrawTurn || gameFinished)
			return;
		
		Player player = players.get(currPlayerID - 1);
		Route r = routes.get(route);
		// have enough trains?
		if(r.getLength() > player.getRemainingTrainsCount())
			return;
		// already controlled by this player?
		for (Rail rail : r.getRails())
			if(rail.getOwner() == player)
				return;
		// somebody controlled one rail and only 2 or 3 players in the game?
		if(players.size() < 4)
			for (Rail rail : r.getRails())
				if(rail.getOwner() != null)
					return;
		for (Rail rail : r.getRails()) {
			// already controlled by somebody else
			if(rail.getOwner() != null)
				continue;
			// have enough and matching colored trains?
			if(!spendTrainCards(rail)) {
				continue;
			}
			// success
			player.setRemainingTrainsCount(player.getRemainingTrainsCount() - rail.getLength());
			player.addScore(r.getScoreValue());
			// notify GUIs to change the route's color
			for (int i = 1; i <= players.size(); i++) {
				Player owner1 = r.getRails().get(0).getOwner();
				PlayerColor c1 = owner1 == null ? null : owner1.getColor();
				Player owner2 = r.getRails().size() < 2 ? null : r.getRails().get(0).getOwner();
				PlayerColor c2 = owner2 == null ? null : owner2.getColor();
				clientUpdateRouteClaimed(i, route, c1, c2);
			}
			broadcastDeckStates();
			checkLastTurn();
			nextPlayer();
			return;
		}
	}
	
	// </editor-fold>
	
	
	
	// ===================== Update messages to client. =======================
	// <editor-fold desc="non-interface message methods">
	
	private void clientUpdateColorChose(int playerID, PlayerColor color) {
		for (int i = 1; i <= players.size(); i++) {
			oh.sendTo(i, new Opcode(SERVER_ID, Opcode.Action.UPDATE_COLOR_CHOSE,
					playerID, color.name()));
		}
		_clientUpdateColorChose(playerID, color);
	}
	void _clientUpdateColorChose(int playerID, PlayerColor color) {
		players.get(playerID - 1).setColor(color);
		
		// turn ended?
		for (Player p : players) {
			if(p.getColor() == null)
				return;
		}
		colorChosingTurn = false;
		initialDrawTurn = true;
	}
	
	void _clientUpdatePlayerCount(int n) {
		for (int i = players.size(); i < n; i++) {
			players.add(Factory.newPlayer());
		}
	}
	
	private void clientUpdateGameStarted() {
		for (int i = 1; i <= players.size(); i++) {
			oh.sendTo(i, new Opcode(SERVER_ID, Opcode.Action.UPDATE_GAME_STARTED));
		}
		_clientUpdateGameStarted();
	}
	void _clientUpdateGameStarted() {
		gameStarted = true;
		colorChosingTurn = true;
	}
	
	private void clientUpdateYourTurnStarted(int recipID) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_YOUR_TURN_STARTED));
		else
			_clientUpdateYourTurnStarted();
	}
	void _clientUpdateYourTurnStarted() {
		currPlayerID = playerID;
		isMyTurn = true;
	}
	
	private void clientUpdateYourTurnEnded(int recipID) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_YOUR_TURN_ENDED));
		else
			_clientUpdateYourTurnEnded();
	}
	void _clientUpdateYourTurnEnded() {
		isMyTurn = false;
	}
	
	private void clientUpdateRouteClaimed(int recipID, shared.Route route, PlayerColor color1, PlayerColor color2) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_ROUTE_CLAIMED, 
					route.name(), 
					color1 == null ? "" : color1.name(), 
					color2 == null ? "" : color2.name()));
		else
			_clientUpdateRouteClaimed(route, color1, color2);
	}
	void _clientUpdateRouteClaimed(shared.Route route, PlayerColor color1, PlayerColor color2) {
		List<Rail> rails = routes.get(route).getRails();
		// HACK
		for (Player p : players) {
			if(p.getColor() == color1) {
				rails.get(0).setOwner(p);
			}
			if(p.getColor() == color2 && color2 != null) {
				rails.get(1).setOwner(p);
			}
		}
	}
	
	private void clientUpdateTrainDeck(int recipID, int numberOfCards) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_TRAIN_DECK,
					numberOfCards));
		else
			_clientUpdateTrainDeck(numberOfCards);
	}
	void _clientUpdateTrainDeck(int numberOfCards) {
		clientCardsInTrainDeck = numberOfCards;
	}
	
	private void clientUpdateUpfaceTrainCards(int recipID, List<TrainColor> cards) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_UPFACE_TRAIN_CARDS,
					0, 0, 0, 0, 0,
					cards.get(0) == null ? "" : cards.get(0).name(), 
					cards.get(1) == null ? "" : cards.get(1).name(), 
					cards.get(2) == null ? "" : cards.get(2).name(), 
					cards.get(3) == null ? "" : cards.get(3).name(), 
					cards.get(4) == null ? "" : cards.get(4).name()));
		else
			_clientUpdateUpfaceTrainCards(cards);
	}
	void _clientUpdateUpfaceTrainCards(List<TrainColor> cards) {
		clientUpfaceTrainCards = cards;
	}
	
	private void clientUpdateTicketDeck(int recipID, int numberOfCards) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_TICKET_DECK,
					numberOfCards));
		else
			_clientUpdateTicketDeck(numberOfCards);
	}
	void _clientUpdateTicketDeck(int numberOfCards) {
		clientCardsInTicketDeck = numberOfCards;
	}
	
	private void clientUpdateGainTrainCard(int recipID, TrainColor card) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_GAIN_TRAIN_CARD,
					card.name()));
		else
			_clientUpdateGainTrainCard(card);
	}
	void _clientUpdateGainTrainCard(TrainColor card) {
		players.get(playerID - 1).addTrainCards(card, 1);
	}
	
	private void clientUpdateGainTicketCards(int recipID, List<TicketCard> cards) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_GAIN_TICKET_CARDS,
					cards.size() < 1 || cards.get(0) == null ? "" : cards.get(0).name(),
					cards.size() < 2 || cards.get(1) == null ? "" : cards.get(1).name(),
					cards.size() < 3 || cards.get(2) == null ? "" : cards.get(2).name() ));
		else
			_clientUpdateGainTicketCards(cards);
	}
	void _clientUpdateGainTicketCards(List<TicketCard> cards) {
		players.get(playerID - 1).getTicketCards().addAll(cards);
		lastTicketCards = cards;
	}
	
	private void clientUpdateLooseTrainCard(int recipID, TrainColor card, int n) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_LOOSE_TRAIN_CARD,
					n, card.name()));
		else
			_clientUpdateLooseTrainCard(card, n);
	}
	void _clientUpdateLooseTrainCard(TrainColor card, int n) {
		players.get(playerID - 1).removeTrainCards(card, n);
	}
	
	private void clientUpdateLooseTicketCard(int recipID, TicketCard card) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_LOOSE_TICKET_CARD,
					card.name()));
		else
			_clientUpdateLooseTicketCard(card);
	}
	void _clientUpdateLooseTicketCard(TicketCard card) {
		players.get(playerID - 1).getTicketCards().remove(card);
	}
	
	private void clientUpdateScores(int recipID, List<Integer> scores) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_SCORE,
					scores.size() < 1 || scores.get(0) == null ? 0 : scores.get(0),
					scores.size() < 2 || scores.get(1) == null ? 0 : scores.get(1),
					scores.size() < 3 || scores.get(2) == null ? 0 : scores.get(2),
					scores.size() < 4 || scores.get(3) == null ? 0 : scores.get(3),
					scores.size() < 5 || scores.get(4) == null ? 0 : scores.get(4),
					"", "", "", "", ""));
		else
			_clientUpdateScores(scores);
	}
	void _clientUpdateScores(List<Integer> scores) {
		for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			int currScore = p.getScore();
			p.addScore(scores.remove(0) - currScore);
		}
	}
	
	private void clientUpdateInitTurnEnd(int recipID) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_INIT_TURN_END));
		_clientUpdateInitTurnEnd();
	}
	void _clientUpdateInitTurnEnd() {
		initialDrawTurn = false;
	}
	
	private void clientUpdateGameFinished(int recipID) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_GAME_FINISHED));
		_clientUpdateGameFinished();
	}
	void _clientUpdateGameFinished() {
		gameFinished = true;
	}
	
	// </editor-fold>
	
	
	
}
