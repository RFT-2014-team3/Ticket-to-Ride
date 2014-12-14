package logicmodule;

/**
 * @author Kerekes Zoltán
 */
interface TrainDeck {
	int getDownfaceCardsCount();
	shared.TrainColor getUpfaceCardColor(int index);
	TrainCard drawDownfaceCard();
	TrainCard drawUpfaceCard(int index);
	boolean upfaceCardIsLocomotive(int index);
	boolean upfaceCardIsExists(int index);
	void discardUpfaceCards();
	void shuffleDownfaceCards();
	void shuffleDiscardedCards();
	void useDiscardedCards();
	void discardCardIntoDeck(TrainCard card);
	boolean hasUpfaceNonLocomotiveCard();
}
