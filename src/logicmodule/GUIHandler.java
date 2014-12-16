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
	
	/**
	 * Player selected a color.
	 * @param color the selected color.
	 */
	void chooseColor(shared.PlayerColor color);
	
	/**
	 * Enough players joined and game started at the server.
	 * @return True if game started.
	 */
	boolean isGameStarted();
	
	/**
	 * @return True if you are the player who currently do something.
	 */
	boolean isMyTurn();
	
	/**
	 * @return True if players now choosing first ticket cards.
	 */
	boolean isInitialDrawTurn();
	
	/**
	 * @return True if players now choosing color.
	 */
	//boolean isColorChosingTurn();
	
	/**
	 * @return True if game finished and all score summarized.
	 */
	boolean isGameFinished();
	
	/**
	 * Returns the route's owners' color.
	 * @param route Whick route's color fetched.
	 * @return Always a list with two element, but the second element is null 
	 * if route is a single route. Both elements can be null if that route not 
	 * owned by any player.
	 */
	List<shared.PlayerColor> getRouteOwnerColors(shared.Route route);
	
	/**
	 * Returns the number of remaining cards in the train deck.
	 * Upface cards not counted.
	 * @return Remaining cards in the train deck.
	 */
	int getNumberOfCardsInTrainDeck();
	
	/**
	 * Returns the number of remaining cards in the ticket deck.
	 * @return Remaining cards in the ticket deck.
	 */
	int getNumberOfCardsInTicketDeck();
	
	/**
	 * Send upface cards's color. 
	 * Length of the list was always 5, so some elements maybe null.
	 * @return Upface cards's color.
	 */
	List<shared.TrainColor> getUpfaceTrainCards();
	
	/**
	 * @return The payer's ticket cards.
	 */
	List<shared.TicketCard> getMyTicketCards();
	
	/**
	 * The player's card counts. 9 integer values. N'th value shows how many 
	 * cards of n'th type of train cards.
	 * @return The player's train cards.
	 */
	List<Integer> getMyTrainCards();
	
	/**
	 * 2..5 integer values.
	 * @return The player's score in a list.
	 */
	List<Integer> getPlayerScores();
	
	/**
	 * All players' color in a list. So length equals joined players count.
	 * Can be used cunjunction with {@link #getMyID()} to extract current 
	 * player's color.
	 * @return All players' color.
	 */
	List<shared.PlayerColor> getPlayerColors();
	
	/**
	 * @return This player's ID. A number between 1 and 5.
	 */
	int getMyID();
	
	/**
	 * Lastly gained ticket cards. Can discard 1 or 2. 
	 * @return Choosable/discardable ticket cards.
	 */
	List<shared.TicketCard> getChoosableTicketCards();
	
}
