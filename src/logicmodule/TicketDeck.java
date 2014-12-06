package logicmodule;

import shared.TicketCard;

/**
 * @author Kerekes Zoltán
 */
interface TicketDeck {
	void discardCardIntoDeck(TicketCard card);
	int getCardsCount();
	TicketCard drawCard();
}
