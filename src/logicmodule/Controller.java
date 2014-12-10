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
	//private List<City> TODO: [logic] del or create
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
	public void drawTrainCard() {
		if(playerIndex != Opcode.SERVER_INDEX)
			oh.sendOpcodeTo(Opcode.SERVER_INDEX, new Opcode(
					Opcode.Sender.GUI, playerIndex, Opcode.Action.SELECT_TRAIN_DECK));
		else
			_drawTrainCard(playerIndex);
	}
	public void _drawTrainCard(int playerIndex) {
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
	public void _drawTrainCard(int playerIndex, int index) {
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
	public void _drawTicketCard(int playerIndex) {
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
	public void _claimRoute(int playerIndex, shared.Route route) {
		// TODO [logic]
	}

	@Override
	public void connectToServer(String ip) {
		// TODO [logic]
	}

}
