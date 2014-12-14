package logicmodule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import netsubmodul.Client;
import netsubmodul.IClient;
import netsubmodul.IServer;
import netsubmodul.Server;
import shared.PlayerColor;
import shared.TicketCard;
import shared.TrainColor;
import view.MainFrame;

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
	
	// TODO [logic] GUIUpdate
	//private GUIUpdate gui = MainFrame.getInstance();
	
	private static final int SERVER_ID = Opcode.SERVER_ID;
	private OpcodeHandler oh = OpcodeHandler.getInstance();
	private static final int PORT = 9999;
	IServer server;
	IClient client;
	
	private Pathfinder pf;
	private Map<shared.Route, Route> routes;
	private TrainDeck trainDeck;
	private TicketDeck ticketDeck;
	private List<Player> players;
	/** 0 means: not yet set; 1: this is the server; 2..5: this is a client. */
	private int playerID = 0;
	private int currPlayerID = 0;
	private State state = State.THROWING_TICKET;
	
	private Controller() {}

	public static Controller getInstance() {
		return instance;
	}

	boolean isServer() {
		return playerID == 1;
	}
	
	private void nextPlayer() {
		guiUpdateYourTurnEnded(currPlayerID);
		if(currPlayerID == players.size())
			currPlayerID = 1;
		else
			currPlayerID++;
		state = State.CHOOSING;
		guiUpdateYourTurnStarted(currPlayerID);
	}
	
	private void broadcastDeckStates() {
		List<TrainColor> upfaces = new ArrayList<>();
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
			guiUpdateScore(i, players.get(i).getScore());
		}
	}
	
	private boolean spendTrainCards(Rail rail) {
		int length = rail.getLength();
		TrainColor railColor = rail.getColor();
		Player player = players.get(currPlayerID);
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
				guiUpdateLooseTrainCard(currPlayerID, railColor, discarded);
				for (TrainCard tc : cardsToDeck)
					trainDeck.discardCardIntoDeck(tc); 
				// locomotives
				if(discarded < length) {
					List<TrainCard> cardsToDeck2 = player.removeTrainCards(TrainColor.GREY, length - discarded);
					guiUpdateLooseTrainCard(currPlayerID, TrainColor.GREY, length - discarded);
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
					guiUpdateLooseTrainCard(currPlayerID, c, length);
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
					guiUpdateLooseTrainCard(currPlayerID, c, discarded);
					for (TrainCard tc : cardsToDeck)
						trainDeck.discardCardIntoDeck(tc); 
					// locomotives
					if(discarded < length) {
						List<TrainCard> cardsToDeck2 = player.removeTrainCards(TrainColor.GREY, length - discarded);
						guiUpdateLooseTrainCard(currPlayerID, TrainColor.GREY, length - discarded);
						for (TrainCard tc : cardsToDeck2)
							trainDeck.discardCardIntoDeck(tc); 
					}
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	// ======================= Input messages from GUI. =======================
	// <editor-fold desc="implements GUIHandler interface">
	
	@Override
	public String startNewServer() {
		playerID = 1;
		currPlayerID = 1;
		routes = new HashMap<>();
		for (shared.Route r : shared.Route.values()) {
			routes.put(r, Factory.newRoute(r));
		}
		pf = Factory.newPathfinder(new ArrayList<>(routes.values()));
		players = new ArrayList<>();
		players.add(Factory.newPlayer());
		trainDeck = Factory.newTrainDeck();
		ticketDeck = Factory.newTicketDeck();
		
		server = Server.GetServer();
		server.startServer(PORT);
		String ip = server.GetServerAddress();
		return ip;
	}
	
	@Override
	public boolean connectToServer(String ip) {
		client = new Client();
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
			if(client.GetClientID() == -100)
				return false;
			playerID = client.GetClientID();
		}
		return success;
	}
	
	@Override
	public boolean startGame() {
		if(server.GetConnectedClients() > 0){
			server.StartMatch();
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void drawTrainCard() {
		if(playerID != SERVER_ID)
			oh.sendTo(SERVER_ID, new Opcode(playerID, Opcode.Action.SELECT_TRAIN_DECK));
		else
			_drawTrainCard(playerID);
	}
	void _drawTrainCard(int senderIndex) {
		if(senderIndex != currPlayerID || trainDeck.getDownfaceCardsCount() == 0
				|| (state != State.CHOOSING && state != State.DRAWING_2ND_TRAIN))
			return;
		
		TrainCard card = trainDeck.drawDownfaceCard();
		players.get(senderIndex).addTrainCards(card.getColor(), 1);
		guiUpdateGainTrainCard(senderIndex, card.getColor());
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
	void _drawTrainCard(int senderIndex, int index) {
		if(senderIndex != currPlayerID || !trainDeck.upfaceCardIsExists(index)
				|| (state != State.CHOOSING && state != State.DRAWING_2ND_TRAIN))
			return;
		if(state == State.DRAWING_2ND_TRAIN && trainDeck.upfaceCardIsLocomotive(index))
			return;
		
		TrainCard card = trainDeck.drawUpfaceCard(index);
		players.get(senderIndex).addTrainCards(card.getColor(), 1);
		guiUpdateGainTrainCard(senderIndex, card.getColor());
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
	void _drawTicketCards(int senderIndex) {
		if(senderIndex != currPlayerID || ticketDeck.getCardsCount() == 0 
				|| state != State.CHOOSING)
			return;
		
		List<TicketCard> cards = new ArrayList<>();
		for(int i = 0; i < 3; i++){
			if(ticketDeck.getCardsCount() > 0)
				cards.add(ticketDeck.drawCard());
		}
		players.get(senderIndex).getTicketCards().addAll(cards);
		guiUpdateGainTicketCards(senderIndex, cards);
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
	void _throwTicketCards(int senderIndex, List<TicketCard> cards) {
		if(senderIndex != currPlayerID || state != State.THROWING_TICKET)
			return;
		
		for (TicketCard c : cards) {
			ticketDeck.discardCardIntoDeck(c);
			guiUpdateLooseTicketCard(senderIndex, c);
		}
		players.get(senderIndex).getTicketCards().removeAll(cards);
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
	void _claimRoute(int senderIndex, shared.Route route) {
		if(senderIndex != currPlayerID || state != State.CHOOSING)
			return;
		
		Player player = players.get(currPlayerID);
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
			for (int i = 0; i < players.size(); i++) {
				Player owner1 = r.getRails().get(0).getOwner();
				PlayerColor c1 = owner1 == null ? null : owner1.getColor();
				Player owner2 = r.getRails().size() < 2 ? null : r.getRails().get(0).getOwner();
				PlayerColor c2 = owner2 == null ? null : owner2.getColor();
				guiUpdateRouteClaimed(i, route, c1, c2);
			}
			broadcastDeckStates();
			nextPlayer();
			return;
		}
	}
	
	// </editor-fold>
	
	
	
	// ======================= Update messages to GUI. =======================
	// <editor-fold desc="to GUIUpdate interface">
	
	private void guiUpdateYourTurnStarted(int recipID) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_YOUR_TURN_STARTED));
		else
			_guiUpdateYourTurnStarted();
	}
	void _guiUpdateYourTurnStarted() {
		gui.updateYourTurnStarted();
	}
	
	private void guiUpdateYourTurnEnded(int recipID) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_YOUR_TURN_ENDED));
		else
			_guiUpdateYourTurnEnded();
	}
	void _guiUpdateYourTurnEnded() {
		gui.updateYourTurnEnded();
	}
	
	private void guiUpdateRouteClaimed(int recipID, shared.Route route, PlayerColor color1, PlayerColor color2) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_ROUTE_CLAIMED, 
					route.name(), 
					color1 == null ? "" : color1.name(), 
					color2 == null ? "" : color2.name()));
		else
			_guiUpdateRouteClaimed(route, color1, color2);
	}
	void _guiUpdateRouteClaimed(shared.Route route, PlayerColor color1, PlayerColor color2) {
		List<shared.PlayerColor> colors = new ArrayList<>();
		colors.add(color1);
		colors.add(color2);
		gui.updateRouteClaimed(route, colors);
	}
	
	private void guiUpdateTrainDeck(int recipID, int numberOfCards) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_TRAIN_DECK,
					numberOfCards));
		else
			_guiUpdateTrainDeck(numberOfCards);
	}
	void _guiUpdateTrainDeck(int numberOfCards) {
		gui.updateTrainDeck(numberOfCards);
	}
	
	private void guiUpdateUpfaceTrainCards(int recipID, List<TrainColor> cards) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_UPFACE_TRAIN_CARDS,
					0, 0, 0, 0,
					cards.get(0) == null ? "" : cards.get(0).name(), 
					cards.get(1) == null ? "" : cards.get(1).name(), 
					cards.get(2) == null ? "" : cards.get(2).name(), 
					cards.get(3) == null ? "" : cards.get(3).name(), 
					cards.get(4) == null ? "" : cards.get(4).name()));
		else
			_guiUpdateUpfaceTrainCards(cards);
	}
	void _guiUpdateUpfaceTrainCards(List<TrainColor> cards) {
		gui.updateUpfaceTrainCards(cards);
	}
	
	private void guiUpdateTicketDeck(int recipID, int numberOfCards) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_TICKET_DECK,
					numberOfCards));
		else
			_guiUpdateTicketDeck(numberOfCards);
	}
	void _guiUpdateTicketDeck(int numberOfCards) {
		gui.updateTicketDeck(numberOfCards);
	}
	
	private void guiUpdateGainTrainCard(int recipID, TrainColor card) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_GAIN_TRAIN_CARD,
					card.name()));
		else
			_guiUpdateGainTrainCard(card);
	}
	void _guiUpdateGainTrainCard(TrainColor card) {
		gui.gainTrainCard(card);
	}
	
	private void guiUpdateGainTicketCards(int recipID, List<TicketCard> cards) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_GAIN_TICKET_CARDS,
					cards.get(0) == null ? "" : cards.get(0).name(),
					cards.get(1) == null ? "" : cards.get(1).name(),
					cards.get(2) == null ? "" : cards.get(2).name() ));
		else
			_guiUpdateGainTicketCards(cards);
	}
	void _guiUpdateGainTicketCards(List<TicketCard> cards) {
		gui.gainTicketCards(cards);
	}
	
	private void guiUpdateLooseTrainCard(int recipID, TrainColor card, int n) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_LOOSE_TRAIN_CARD,
					n, card.name()));
		else
			_guiUpdateLooseTrainCard(card, n);
	}
	void _guiUpdateLooseTrainCard(TrainColor card, int n) {
		gui.looseTrainCard(card, n);
	}
	
	private void guiUpdateLooseTicketCard(int recipID, TicketCard card) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_LOOSE_TICKET_CARD,
					card.name()));
		else
			_guiUpdateLooseTicketCard(card);
	}
	void _guiUpdateLooseTicketCard(TicketCard card) {
		gui.looseTicketCard(card);
	}
	
	private void guiUpdateScore(int recipID, int score) {
		if(this.playerID != recipID)
			oh.sendTo(recipID, new Opcode(SERVER_ID, Opcode.Action.UPDATE_SCORE,
					score));
		else
			_guiUpdateScore(score);
	}
	void _guiUpdateScore(int score) {
		gui.updateScore(score);
	}
	
	// </editor-fold>
	
	
	
}
