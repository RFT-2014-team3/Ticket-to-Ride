package logicmodule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import shared.TrainColor;

/**
 * @author Kerekes Zoltán
 */
class TrainDeckImpl implements TrainDeck {

	private TrainCard[] upfaceCards;
	private List<TrainCard> downfaceCards = new ArrayList<>();
	private List<TrainCard> discardedCards = new ArrayList<>();
	
	public TrainDeckImpl() {
		List<TrainColor> mainColors = new ArrayList<>(Arrays.asList(TrainColor.values()));
		mainColors.remove(TrainColor.GREY);
		for (TrainColor c : mainColors) {
			for(int i = 0; i < 12; i++)
				downfaceCards.add(Factory.newTrainCard(c));
		}
		for(int i = 0; i < 14; i++)
			downfaceCards.add(Factory.newLocomotiveCard());
		upfaceCards = new TrainCard[5];
	}

	@Override
	public int getDownfaceCardsCount() {
		return downfaceCards.size();
	}

	@Override
	public TrainCard drawDownfaceCard() {
		useDiscardedCardsIfDownfacesEmpty();
		return downfaceCards.remove(0);
	}

	@Override
	public TrainCard drawUpfaceCard(int index) {
		TrainCard temp = upfaceCards[index];
		upfaceCards[index] = null;
		fillUpfaceCards();
		return temp;
	}
	
	private void fillUpfaceCards() {
		if(getDownfaceCardsCount() == 0)
			return;
		
		for (int i = 0; i < 5; i++) {
			if(!upfaceCardIsExists(i)){
				useDiscardedCardsIfDownfacesEmpty();
				if(getDownfaceCardsCount() == 0)
					break;
				upfaceCards[i] = drawDownfaceCard();
			}
		}
		checkLocomotiveCount();
	}
	
	private void checkLocomotiveCount() {
		for(int repeats = 0; repeats < 10; repeats++) {
			int count = 0;
			for (int i = 0; i < 5; i++) {
				if(upfaceCardIsExists(i) && upfaceCardIsLocomotive(i))
					count++;
			}
			if(count < 3)
				return;
			discardUpfaceCards();
			if(getDownfaceCardsCount() < 5) {
				shuffleDiscardedCards();
				useDiscardedCards();
			}
			if(getDownfaceCardsCount() < 5)
				return;
			for (int i = 0; i < 5; i++)
				upfaceCards[i] = drawDownfaceCard();
		}
	}

	@Override
	public boolean upfaceCardIsLocomotive(int index) {
		return upfaceCards[index] instanceof LocomotiveCard;
	}

	@Override
	public boolean upfaceCardIsExists(int index) {
		return upfaceCards != null;
	}

	@Override
	public void discardUpfaceCards() {
		for(int i = 0; i < 5; i++){
			if(upfaceCards[i] != null){
				discardedCards.add(upfaceCards[i]);
				upfaceCards[i] = null;
			}
		}
	}

	@Override
	public void shuffleDownfaceCards() {
		Collections.shuffle(downfaceCards);
	}

	@Override
	public void shuffleDiscardedCards() {
		Collections.shuffle(discardedCards);
	}

	@Override
	public void useDiscardedCards() {
		downfaceCards.addAll(discardedCards);
		discardedCards.clear();
	}

	@Override
	public void discardCardIntoDeck(TrainCard card) {
		discardedCards.add(card);
	}

	@Override
	public TrainColor getUpfaceCardColor(int index) {
		return upfaceCards[index].getColor();
	}

	@Override
	public boolean hasUpfaceNonLocomotiveCard() {
		for (TrainCard c : upfaceCards) {
			if(c != null && !(c instanceof LocomotiveCard))
				return true;
		}
		return false;
	}

	private void useDiscardedCardsIfDownfacesEmpty() {
		if(getDownfaceCardsCount() == 0) {
			shuffleDiscardedCards();
			useDiscardedCards();
		}
	}
	
}
