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
	 * @param cards upface cards's color
	 */
	void updateUpfaceTrainCards(List<shared.TrainColor> cards);
	
	/**
	 * Send the number of remaining cards in the ticket deck.
	 * @param numberOfCards Remaining cards in the ticket deck.
	 */
	void updateTicketDeck(int numberOfCards);
	
}
