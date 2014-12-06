package logicmodule;

/**
 * @author Kerekes Zoltán
 */
interface TrainDeck {
	int getDownfaceCardsCount();
	TrainCard drawDownfaceCard();
	TrainCard drawUpfaceCard(int index);
	boolean upfaceCardIsLocomotive(int index);
	boolean upfaceCardIsExists(int index);
	void discardUpfaceCards();
	void shuffleDiscardedCards();
	void useDiscardedCards();
	void discardCardIntoDeck(TrainCard card);
}
