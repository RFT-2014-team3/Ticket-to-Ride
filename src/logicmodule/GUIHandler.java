package logicmodule;

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
	 * Draw cards from the ticket card deck.
	 */
	void drawTicketCard();
	
	/**
	 * Claim a route.
	 * @param route Which route should be claimed.
	 */
	void claimRoute(shared.Route route);
	
	/**
	 * Connect to the started server by the specified IP.
	 * @param ip The server's address.
	 */
	void connectToServer(String ip);
	
	/**
	 * Start the current program as server.
	 */
	void startNewServer();
	
}
