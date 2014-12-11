package logicmodule;

import java.util.List;

/**
 * Messages from logic module to GUI.
 * @author Kerekes Zoltán
 */
public interface GUIUpdate {

	/**
	 * Inform the GUI to start turn specific interaction.
	 */
	void updateYourTurnStarted();
	
	/**
	 * Inform the GUI to stop turn specific interaction.
	 */
	void updateYourTurnEnded();
	
	/**
	 * Send the lastly claimed route's information.
	 * @param route Which route changed.
	 * @param colors Lastly changed route's rails' color.
	 */
	void updateRouteClaimed(shared.Route route, List<shared.PlayerColor> colors);
	
	/**
	 * Send the number of remaining cards in the train deck.
	 * Upface cards not counted.
	 * @param numberOfCards Remaining cards in the train deck.
	 */
	void updateTrainDeck(int numberOfCards);
	
	/**
	 * Send upface cards's color. 
	 * Length of the list was always 5, so some elements maybe null.
	 * @param cards upface cards's color
	 */
	void updateUpfaceTrainCards(List<shared.TrainColor> cards);
	
	/**
	 * Send the number of remaining cards in the ticket deck.
	 * @param numberOfCards Remaining cards in the ticket deck.
	 */
	void updateTicketDeck(int numberOfCards);
	
	/**
	 * The player gained a train card.
	 * @param card The card's color.
	 */
	void gainTrainCard(shared.TrainColor card);
	
	/**
	 * The player gained a ticket card.
	 * @param card The card.
	 */
	void gainTicketCards(List<shared.TicketCard> cards);
	
	/**
	 * The player used up n train card.
	 * @param card Card's color.
	 * @param n Number of cards used up.
	 */
	void looseTrainCard(shared.TrainColor card, int n);
	
	/**
	 * The player thrown a ticket card.
	 * @param card The card.
	 */
	void looseTicketCard(shared.TicketCard card);
	
	/**
	 * Send the player's score.
	 * @param score New score.
	 */
	void updateScore(int score);
	
}
