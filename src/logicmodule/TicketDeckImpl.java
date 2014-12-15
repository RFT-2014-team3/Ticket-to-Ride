package logicmodule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import shared.TicketCard;

/**
 * @author Kerekes Zoltán
 */
class TicketDeckImpl implements TicketDeck {

	private List<TicketCard> cards;
	
	public TicketDeckImpl() {
		cards = new ArrayList<>(Arrays.asList(TicketCard.values()));
	}

	@Override
	public void discardCardIntoDeck(TicketCard card) {
		cards.add(card);
	}

	@Override
	public int getCardsCount() {
		return cards.size();
	}

	@Override
	public TicketCard drawCard() {
		return cards.remove(0);
	}

	@Override
	public void shuffleCards() {
		Collections.shuffle(cards);
	}

}
