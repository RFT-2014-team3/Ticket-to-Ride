package logicmodule;

import java.util.List;

/**
 * Messages from GUI to logic module.
 * @author Kerekes Zoltán
 */
public interface GUIHandler {
	
	/**
	 * Draw a card from the train deck.
	 */
	void drawTrainCard();
	
	/**
	 * Draw a card from upface train cards.
	 * @param index within [0, 4] interval
	 */
	void drawTrainCard(int index);
	
	/**
	 * Draw 1-3 cards from the ticket card deck.
	 */
	void drawTicketCards();
	
	/**
	 * Throw 0-2 ticket cards from the last drawn ones.
	 */
	void throwTicketCards(List<shared.TicketCard> cards);
	
	/**
	 * Claim a route.
	 * @param route Which route should be claimed.
	 */
	void claimRoute(shared.Route route);
	
	/**
	 * Connect to the started server by the specified IP.
	 * @param ip The server's address.
	 */
	boolean connectToServer(String ip);
	
	/**
	 * Start the current program as server.
	 * @return The server's IP address.
	 */
	String startNewServer();
	
	/**
	 * Finish the connection phase. Start the game.
	 * @return False if not joined enough players.
	 */
	boolean startGame();
	
}
